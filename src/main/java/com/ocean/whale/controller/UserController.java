package com.ocean.whale.controller;

import com.ocean.whale.api.GetBatchUserPublicDataResponse;
import com.ocean.whale.model.UserPublicData;
import com.ocean.whale.service.auth.AuthService;
import com.ocean.whale.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/getBatchUserPublicData")
    public GetBatchUserPublicDataResponse getBatchUserPublicData(@RequestHeader String accessToken, @RequestParam List<String> userIds) {
        // Verify the token and fetch the userDetailedData UID
        String requesterUserId = authService.verifyAndFetchUid(accessToken);
        // Get recommendations based on the UID
        GetBatchUserPublicDataResponse response = new GetBatchUserPublicDataResponse();

        List<UserPublicData> userPublicData = userIds.stream().map(userId -> {
            System.out.println(userId);
            return userService.getUserPublicData(userId);
        }).toList();

        response.setUserPublicDataList(userPublicData);

        return response;
    }


}