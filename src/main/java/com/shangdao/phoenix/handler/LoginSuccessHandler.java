package com.shangdao.phoenix.handler;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.shangdao.phoenix.entity.user.User;
import com.shangdao.phoenix.entity.user.UserRepository;
import com.shangdao.phoenix.util.UserDetailsImpl;

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{
    
    @Autowired
    UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetailsImpl.getUser();
        user.setLastLoginAt(new Date());
        System.out.println("设置系统登录时间");
        userRepository.save(user);
        super.onAuthenticationSuccess(request, response, authentication);
    }
    
    

}
