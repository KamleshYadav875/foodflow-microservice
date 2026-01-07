package com.foodflow.identity_service.util;

import com.foodflow.identity_service.exceptions.UnauthenticatedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils(){}

    public static Long getCurrentUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication ==null || !authentication.isAuthenticated()){
            throw new UnauthenticatedException("User is not authenticated");
        }

        return (Long) authentication.getPrincipal();
    }

}
