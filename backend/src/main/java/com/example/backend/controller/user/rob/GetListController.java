package com.example.backend.controller.user.rob;

import com.example.backend.pojo.Rob;
import com.example.backend.service.user.rob.GetListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetListController {
    @Autowired
    private GetListService getListService;

    @GetMapping("/user/rob/get_list/")
    public List<Rob> getList() {
        return getListService.getList();
    }
}
