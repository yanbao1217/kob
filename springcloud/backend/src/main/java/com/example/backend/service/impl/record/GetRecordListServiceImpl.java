package com.example.backend.service.impl.record;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.mapper.RecordMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.pojo.Record;
import com.example.backend.pojo.User;
import com.example.backend.service.record.GetRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class GetRecordListServiceImpl implements GetRecordListService {
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer page) {
        if (page == null || page < 1) page = 1;

        IPage<Record> recordIPage = new Page<>(page, 10);
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");

        IPage<Record> resultPage = recordMapper.selectPage(recordIPage, queryWrapper);
        List<Record> records = resultPage.getRecords();

        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();

        for (Record record : records) {
            User userA = userMapper.selectById(record.getAId());
            User userB = userMapper.selectById(record.getBId());

            JSONObject item = new JSONObject();
            item.put("a_photo", userA.getPhoto());
            item.put("a_username", userA.getUsername());
            item.put("b_photo", userB.getPhoto());
            item.put("b_username", userB.getUsername());

            String result = "平局";
            if ("A".equals(record.getLoser())) result = "B胜";
            else if ("B".equals(record.getLoser())) result = "A胜";

            item.put("result", result);
            item.put("record", record);
            items.add(item);
        }

        resp.put("records", items);
        resp.put("records_count", resultPage.getTotal());   // 总记录数
        resp.put("current_page", resultPage.getCurrent()); // 当前页
        resp.put("page_size", resultPage.getSize());       // 每页条数
        resp.put("total_page", resultPage.getPages());     // 总页数

        return resp;
    }
}