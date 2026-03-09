package com.example.backend.service.impl.user.rob;

import com.example.backend.mapper.RobMapper;
import com.example.backend.pojo.Rob;
import com.example.backend.pojo.User;
import com.example.backend.service.impl.utils.UserDetailsImpl;
import com.example.backend.service.user.rob.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RemoveServiceImpl implements RemoveService {
    @Autowired
    private RobMapper robMapper;

    @Override
    public Map<String, String> remove(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        int rob_id = Integer.parseInt(data.get("rob_id"));

        Rob rob = robMapper.selectById(rob_id);
        Map<String, String> map = new HashMap<>();

        if (rob == null) {
            map.put("error_message", "Rob不存在或已被删除");
            return map;
        }

        if (!rob.getUserId().equals(user.getId())) {
            map.put("error_message", "没有权限删除该Rob");
            return map;
        }

        robMapper.deleteById(rob_id);

        map.put("error_message", "success");

        return map;
    }
}
