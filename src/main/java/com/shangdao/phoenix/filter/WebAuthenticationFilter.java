package com.shangdao.phoenix.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangdao.phoenix.entity.user.User;
import com.shangdao.phoenix.entity.user.UserRepository;
import com.shangdao.phoenix.service.GetMethodService;
import com.shangdao.phoenix.util.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;

public class WebAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    GetMethodService getMethodService;
    @Autowired
    UserRepository userRepository;

    private RememberMeServices rememberMeServices = new NullRememberMeServices();
	
	@Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//application/json;charset=utf-8
        if(request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_UTF8_VALUE)
                ||request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)){

            //use jackson to deserialize json
            ObjectMapper mapper = new ObjectMapper();
            UsernamePasswordAuthenticationToken authRequest = null;
            try (InputStream is = request.getInputStream()){
                AuthenticationBean authenticationBean = mapper.readValue(is,AuthenticationBean.class);
                authRequest = new UsernamePasswordAuthenticationToken(
                        authenticationBean.getUsername(), authenticationBean.getPassword());
            }catch (IOException e) {
                e.printStackTrace();
                authRequest = new UsernamePasswordAuthenticationToken(
                        "", "");
            }finally {
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            }
        }
        else {
            return super.attemptAuthentication(request, response);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String type = request.getHeader("dataType");
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authResult.getPrincipal();
        String ip = request.getRemoteAddr();
        User user = userDetailsImpl.getUser();
        user.setLastLoginAt(new Date());
        user.setLastLoginIp(ip);
        userRepository.save(user);
        if ("json".equals(type)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Authentication success. Updating SecurityContextHolder to contain: "
                        + authResult);
            }

            SecurityContextHolder.getContext().setAuthentication(authResult);

            rememberMeServices.loginSuccess(request, response, authResult);

            // Fire event
            if (this.eventPublisher != null) {
                eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(
                        authResult, this.getClass()));
            }

            Object myinfo = getMethodService.myInfo();
            response.setStatus(200);
            ObjectMapper mapper = new ObjectMapper();
            String body = mapper.writeValueAsString(myinfo);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = null;
            try {
                out = response.getWriter();
                out.append(body);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        } else {
            super.successfulAuthentication(request, response, chain, authResult);
        }

    }
	
	public static class AuthenticationBean{
		private String username;
		private String password;
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		
	}
}
