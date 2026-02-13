package com.andromeda.artemisa.utils;

import org.springframework.security.core.context.SecurityContextHolder;

import com.andromeda.artemisa.security.entities.CustomUserDetails;
import org.springframework.security.core.Authentication;


public class Function {
    
    public Function() {
    }

    public static CustomUserDetails authentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return user;
    }
}
