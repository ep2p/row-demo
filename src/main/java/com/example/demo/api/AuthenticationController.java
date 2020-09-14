package com.example.demo.api;

import lab.idioglossia.row.annotations.RowController;
import lab.idioglossia.row.context.RowContextHolder;
import lab.idioglossia.row.context.RowUser;
import lab.idioglossia.row.repository.RowSessionRegistry;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@RowController
public class AuthenticationController {
    private final RowSessionRegistry sessionRegistry;

    public AuthenticationController(RowSessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @PostMapping("/login")
    public @ResponseBody LoginResponseDto login(@RequestBody LoginDto loginDto){
        if(loginDto.getUsername().equals("sepehr") && loginDto.getPassword().equals("12345")){
            RowUser rowUser = RowContextHolder.getContext().getRowUser();
            List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
            grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
            sessionRegistry.getSession(rowUser.getUserId(), rowUser.getSessionId()).setExtra(new UsernamePasswordAuthenticationToken("admin", "admin", grantedAuthorityList));
            return new LoginResponseDto("Success");
        }
        return new LoginResponseDto("failed");
    }

    @GetMapping("/checkAccess")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public @ResponseBody SampleDto checkAccess(){
        return new SampleDto("you have access.");
    }
}
