package com.example.backend.service.impl.user.account;

import com.example.backend.pojo.User;
import com.example.backend.service.impl.utils.UserDetailsImpl;
import com.example.backend.service.user.account.LoginService;
import com.example.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> getToken(String username, String password) {

        Map<String, String> map = new HashMap<>();

        if (username == null || username.isEmpty()) {
            map.put("error_message", "用户名不能为空");
            return map;
        } else if (password == null || password.isEmpty()) {
            map.put("error_message", "密码不能为空");
            return map;
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);


        try {
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            // 登录失败，会自动处理

            UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
            User user = loginUser.getUser();
            String jwt = JwtUtil.createJWT(user.getId().toString());
            String userId = user.getId().toString();

            map.put("error_message", "success");
            map.put("user_id", userId);
            map.put("token", jwt);

            return map;
        } catch (AuthenticationException e) {
            map.put("error_message", "用户名或密码错误");
            return map;
        }

    }
}
