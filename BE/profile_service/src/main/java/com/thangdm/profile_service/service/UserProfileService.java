package com.thangdm.profile_service.service;

import com.thangdm.profile_service.dto.request.UserProfileRequestDto;
import com.thangdm.profile_service.dto.response.UserProfileResponseDto;
import com.thangdm.profile_service.entity.UserProfile;
import com.thangdm.profile_service.mapper.UserProfileMapper;
import com.thangdm.profile_service.repository.UserProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper mapper;
    public UserProfileResponseDto createUserProfile(UserProfileRequestDto requestDto){
        UserProfile userProfile = mapper.toUserProfile(requestDto);
        return mapper.toUserProfileResponseDto(userProfileRepository.save(userProfile));
    }

    public UserProfileResponseDto getProfile(String profileId){
        return mapper.toUserProfileResponseDto(userProfileRepository.findById(profileId).orElse(null));
    }
}
