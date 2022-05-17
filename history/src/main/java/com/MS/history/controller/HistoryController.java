package com.MS.history.controller;

import com.MS.history.model.DTO.HistoryDTO;
import com.MS.history.model.History;
import com.MS.history.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/historic/user")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping("/{user_id}")
    private HistoryDTO getById(@PathVariable long user_id){
        return this.historyService.getById(user_id);
    }
}
