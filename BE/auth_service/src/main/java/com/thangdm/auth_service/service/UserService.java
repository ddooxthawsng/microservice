package com.thangdm.auth_service.service;

import com.thangdm.auth_service.constants.PredefinedRole;
import com.thangdm.auth_service.dto.request.UserCreationRequest;
import com.thangdm.auth_service.dto.request.UserUpdateRequest;
import com.thangdm.auth_service.dto.response.UserResponse;
import com.thangdm.auth_service.entity.Role;
import com.thangdm.auth_service.entity.User;
import com.thangdm.auth_service.exception.ErrorCode;
import com.thangdm.auth_service.exception.UserException;
import com.thangdm.auth_service.mapper.RoleMapper;
import com.thangdm.auth_service.mapper.UserMapper;
import com.thangdm.auth_service.repository.RoleRepository;
import com.thangdm.auth_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper mapper;
    RoleMapper roleMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) {
        //Check kiểu này bị issue concurrent => để cho db check ( add unique field vào trường cần check )
//        if (userRepository.existsByUsername(request.getUsername()))
//            throw new UserException(ErrorCode.USER_EXISTED);
        User user = mapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);

        try { //Để db check unique
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new UserException(ErrorCode.USER_EXISTED);
        }


        return mapper.toUserResponse(user);
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserException(ErrorCode.USER_NOTFOUND));
        mapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        List<Role> roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return mapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(mapper::toUserResponse).toList();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String id) {
        return mapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserResponse login(String username, String password) {
        // Tìm user theo username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(ErrorCode.INVALID_CREDENTIALS));

        // Kiểm tra password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserException(ErrorCode.INVALID_CREDENTIALS);
        }

        // Trả về user response (không bao gồm password)
        return mapper.toUserResponse(user);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        return mapper.toUserResponse(userRepository.findByUsername(username).orElseThrow(() -> new UserException(ErrorCode.USER_NOTFOUND)));
    }
}