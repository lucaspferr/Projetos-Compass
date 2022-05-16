package com.MS.history.controller;

import com.MS.history.model.History;
import com.MS.history.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("teste")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping("/{history_id}")
    private History getAll(@PathVariable long history_id){
        return this.historyService.getAll(history_id);
    }
}
