package com.cyber.quitsmokingsimulator.repository;

import com.cyber.quitsmokingsimulator.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long> {
    
    // 根据sessionId查找用户数据
    Optional<UserData> findBySessionId(String sessionId);
    
    // 根据sessionId删除用户数据
    void deleteBySessionId(String sessionId);
    
    // 检查sessionId是否存在
    boolean existsBySessionId(String sessionId);
} 