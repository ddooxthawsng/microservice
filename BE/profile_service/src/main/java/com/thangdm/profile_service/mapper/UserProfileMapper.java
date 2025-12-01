package com.thangdm.profile_service.mapper;

import com.thangdm.profile_service.dto.request.UserProfileRequestDto;
import com.thangdm.profile_service.dto.response.UserProfileResponseDto;
import com.thangdm.profile_service.entity.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileResponseDto toUserProfileResponseDto(UserProfile userProfile);

    UserProfile toUserProfile(UserProfileRequestDto request);
}
