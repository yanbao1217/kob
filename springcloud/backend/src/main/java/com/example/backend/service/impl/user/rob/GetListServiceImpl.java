package com.example.backend.service.impl.user.rob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.mapper.RobMapper;
import com.example.backend.pojo.Rob;
import com.example.backend.pojo.User;
import com.example.backend.service.impl.utils.UserDetailsImpl;
import com.example.backend.service.user.rob.GetListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetListServiceImpl implements GetListService {
    @Autowired
    private RobMapper robMapper;

    @Override
    public List<Rob> getList() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        QueryWrapper<Rob> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());

        return robMapper.selectList(queryWrapper);
    }
}
