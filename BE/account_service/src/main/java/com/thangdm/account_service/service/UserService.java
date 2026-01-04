package com.thangdm.account_service.service;

import com.thangdm.account_service.constants.PredefinedRole;
import com.thangdm.account_service.dto.request.UserCreationRequest;
import com.thangdm.account_service.dto.request.UserUpdateRequest;
import com.thangdm.account_service.dto.response.UserResponse;
import com.thangdm.account_service.entity.Role;
import com.thangdm.account_service.entity.User;
import com.thangdm.account_service.exception.AccountException;
import com.thangdm.account_service.exception.ErrorCodeAccountService;
import com.thangdm.account_service.mapper.UserMapper;
import com.thangdm.account_service.repository.RoleRepository;
import com.thangdm.account_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
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
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) {
        User user = mapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AccountException(ErrorCodeAccountService.USER_EXISTED);
        }

        return mapper.toUserResponse(user);
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AccountException(ErrorCodeAccountService.USER_NOTFOUND));
        
        mapper.updateUser(user, request);
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getRoles() != null) {
            List<Role> roles = roleRepository.findAllById(request.getRoles());
            user.setRoles(new HashSet<>(roles));
        }

        return mapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(mapper::toUserResponse)
                .toList();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String id) {
        return mapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AccountException(ErrorCodeAccountService.USER_NOTFOUND)));
    }

    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AccountException(ErrorCodeAccountService.USER_NOTFOUND));
        return mapper.toUserResponse(user);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AccountException(ErrorCodeAccountService.USER_NOTFOUND));

        return mapper.toUserResponse(user);
    }
}
