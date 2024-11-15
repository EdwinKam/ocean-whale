package com.ocean.whale.controller;

import com.ocean.whale.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  // TODO change to post mapping, add param for accessToken
  @GetMapping("/create")
  public String createUser() throws Exception {
    userService.createUser("fsf", "sgs");
    return "haha";
  }
}