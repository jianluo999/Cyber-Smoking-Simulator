package com.cyber.quitsmokingsimulator.service;

import com.cyber.quitsmokingsimulator.model.SmokingRecord;
import com.cyber.quitsmokingsimulator.repository.SmokingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SmokingService {
    
    @Autowired
    private SmokingRecordRepository smokingRecordRepository;
    
    public SmokingRecord recordSmoking(SmokingRecord record) {
        return smokingRecordRepository.save(record);
    }
    
    public List<SmokingRecord> getAllRecords() {
        return smokingRecordRepository.findAll();
    }
    
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        List<SmokingRecord> records = smokingRecordRepository.findAll();
        
        stats.put("totalSmokes", records.size());
        stats.put("todaySmokes", records.stream()
            .filter(r -> r.getSmokingTime().toLocalDate().equals(java.time.LocalDate.now()))
            .count());
        
        return stats;
    }
} 