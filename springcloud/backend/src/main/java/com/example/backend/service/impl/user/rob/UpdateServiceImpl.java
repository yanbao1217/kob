package com.example.backend.service.impl.user.rob;

import com.example.backend.mapper.RobMapper;
import com.example.backend.pojo.Rob;
import com.example.backend.pojo.User;
import com.example.backend.service.impl.utils.UserDetailsImpl;
import com.example.backend.service.user.rob.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateServiceImpl implements UpdateService {
    @Autowired
    private RobMapper robMapper;

    @Override
    public Map<String, String> update(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        int rob_id = Integer.parseInt(data.get("rob_id"));

        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        Map<String, String> map = new HashMap<>();

        if (title == null || title.isEmpty()) {
            map.put("error_message", "标题不能为空");
            return map;
        }
        if (title.length() > 100) {
            map.put("error_message", "标题长度不能大于100");
            return map;
        }
        if (description != null && description.length() > 300) {
            map.put("error_message", "Bot的长度不能大于300");
            return map;
        }
        if (description == null || description.isEmpty()) {
            description = "这个用户很懒，什么都没有写";
        }
        if (content == null || content.isEmpty()) {
            map.put("error_message", "代码不能为空");
            return map;
        }
        if (content.length() > 10000) {
            map.put("error_message", "代码长度不能大于10000");
        }

        Rob rob = robMapper.selectById(rob_id);

        if (rob == null) {
            map.put("error_message", "Rob不存在或已被删除");
            return map;
        }

        if (!rob.getUserId().equals(user.getId())) {
            map.put("error_message", "没有权限修改该Rob");
            return map;
        }

        Rob new_rob = new Rob(
                rob.getId(),
                user.getId(),
                title,
                description,
                content,
                rob.getCreatetime(),
                new Date()
        );

        robMapper.updateById(new_rob);

        map.put("error_message", "success");

        return map;
    }
}
