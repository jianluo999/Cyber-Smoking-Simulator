package com.cyber.quitsmokingsimulator.controller;

import com.cyber.quitsmokingsimulator.model.SmokingRecord;
import com.cyber.quitsmokingsimulator.service.SmokingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/smoking")
@CrossOrigin(origins = "http://localhost:3000")
public class SmokingController {
    
    @Autowired
    private SmokingService smokingService;
    
    @PostMapping("/smoke")
    public SmokingRecord recordSmoking(@RequestBody SmokingRecord record) {
        return smokingService.recordSmoking(record);
    }
    
    @GetMapping("/records")
    public List<SmokingRecord> getAllRecords() {
        return smokingService.getAllRecords();
    }
    
    @GetMapping("/stats")
    public Object getStats() {
        return smokingService.getStats();
    }
} 