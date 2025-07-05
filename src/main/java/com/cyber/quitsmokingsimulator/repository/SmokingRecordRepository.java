package com.cyber.quitsmokingsimulator.repository;

import com.cyber.quitsmokingsimulator.model.SmokingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmokingRecordRepository extends JpaRepository<SmokingRecord, Long> {
} 