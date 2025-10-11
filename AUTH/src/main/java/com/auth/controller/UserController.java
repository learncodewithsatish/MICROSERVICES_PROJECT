package com.auth.controller;

import com.auth.exception.BadRequestException;
import com.auth.model.JwtTokenResponse;
import com.auth.model.LoginRequest;
import com.auth.model.User;
import com.auth.model.UserDto;
import com.auth.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register-user")
    public UserDto registerUser(@RequestBody User user){
        UserDto userDto = userService.saveUser(user);
        return userDto;
    }

    @PostMapping("/generate-token")
    public JwtTokenResponse generateToken(@RequestBody LoginRequest loginRequest){

        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            if(authentication.isAuthenticated()){
                return userService.generateToken(loginRequest.getUsername());
            }else{
                throw new BadRequestException("Invalid Credentials");
            }

        }catch (Exception e){
            throw new BadRequestException("Invalid Credentials");
        }
    }



}
