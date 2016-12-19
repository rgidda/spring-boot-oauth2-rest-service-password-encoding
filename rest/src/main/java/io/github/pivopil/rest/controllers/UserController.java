package io.github.pivopil.rest.controllers;

import io.github.pivopil.rest.constants.REST_API;
import io.github.pivopil.rest.services.CustomUserDetailsService;
import io.github.pivopil.share.viewmodels.UserViewModel;
import io.github.pivopil.share.entities.impl.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(REST_API.USERS)
public class UserController {

    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public UserController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping(REST_API.ME)
    public UserDetails me(@AuthenticationPrincipal User user) {
        return customUserDetailsService.loadUserByUsername(user.getLogin());
    }

    @GetMapping
    public Iterable<User> getUsers() {
        return customUserDetailsService.findAll();
    }

    @GetMapping(REST_API.ID_PATH_VARIABLE)
    public UserDetails getSingle(@PathVariable("id") Long id) {
        return customUserDetailsService.getSingle(id);
    }

    @PostMapping
    public UserViewModel createUser(@RequestBody UserViewModel userViewModel) {
        return customUserDetailsService.createNewUser(userViewModel);
    }

    @PutMapping(REST_API.ID_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@RequestBody User user, @PathVariable("id") Long id) {
    }

    @DeleteMapping(REST_API.ID_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable("id") Long id) {
    }

}
