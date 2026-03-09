package com.example.backend.controller.user.rob;

import com.example.backend.service.user.rob.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UpdateController {
    @Autowired
    private UpdateService updateService;

    @PostMapping("/user/rob/update/")
    public Map<String, String> update(@RequestBody Map<String, String> data) {
        return updateService.update(data);
    }
}
