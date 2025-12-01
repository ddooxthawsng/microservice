package com.thangdm.profile_service.controller;

import com.thangdm.profile_service.dto.request.UserProfileRequestDto;
import com.thangdm.profile_service.dto.response.UserProfileResponseDto;
import com.thangdm.profile_service.service.UserProfileService;
import lombok.AccessLevel;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserProfileController {
    UserProfileService userProfileService;

    @PostMapping("/")
    UserProfileResponseDto create(@RequestBody UserProfileRequestDto dto){
        return userProfileService.createUserProfile(dto);
    }

    @GetMapping("/{profileId}")
    UserProfileResponseDto findById(@PathVariable String profileId){
        return userProfileService.getProfile(profileId);
    }
}
