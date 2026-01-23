package com.system.servicebooking.service_booking.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Helper {
    public String getCurrentUserId(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal().toString();
    }

    public boolean isAdmin(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(role->role.equals("ROLE_ADMIN"));
    }
}
