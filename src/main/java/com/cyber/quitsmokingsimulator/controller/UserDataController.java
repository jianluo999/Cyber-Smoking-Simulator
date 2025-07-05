package com.cyber.quitsmokingsimulator.controller;

import com.cyber.quitsmokingsimulator.model.UserData;
import com.cyber.quitsmokingsimulator.service.UserDataService;
import com.cyber.quitsmokingsimulator.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserDataController {
    
    @Autowired
    private UserDataService userDataService;
    
    @Autowired
    private AchievementService achievementService;
    
    // 获取用户数据
    @GetMapping("/data")
    public ResponseEntity<UserData> getUserData(@RequestParam String sessionId) {
        UserData userData = userDataService.getUserData(sessionId);
        return ResponseEntity.ok(userData);
    }
    
    // 保存用户数据
    @PostMapping("/save")
    public ResponseEntity<UserData> saveUserData(@RequestBody UserData userData) {
        UserData savedData = userDataService.saveUserData(userData);
        return ResponseEntity.ok(savedData);
    }
    
    // 更新用户数据
    @PutMapping("/update")
    public ResponseEntity<UserData> updateUserData(@RequestParam String sessionId, @RequestBody UserData userData) {
        UserData updatedData = userDataService.updateUserData(sessionId, userData);
        return ResponseEntity.ok(updatedData);
    }
    
    // 重置用户数据
    @PostMapping("/reset")
    public ResponseEntity<UserData> resetUserData(@RequestParam String sessionId) {
        UserData resetData = userDataService.resetUserData(sessionId);
        return ResponseEntity.ok(resetData);
    }
    
    // 删除用户数据
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteUserData(@RequestParam String sessionId) {
        userDataService.deleteUserData(sessionId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "用户数据已删除");
        return ResponseEntity.ok(response);
    }
    
    // 检查用户是否存在
    @GetMapping("/exists")
    public ResponseEntity<Map<String, Boolean>> userExists(@RequestParam String sessionId) {
        boolean exists = userDataService.userExists(sessionId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    
    // 生成新的Session ID
    @PostMapping("/session")
    public ResponseEntity<Map<String, String>> generateSessionId() {
        String sessionId = userDataService.generateSessionId();
        Map<String, String> response = new HashMap<>();
        response.put("sessionId", sessionId);
        return ResponseEntity.ok(response);
    }
    
    // 初始化用户数据（创建新用户）
    @PostMapping("/init")
    public ResponseEntity<UserData> initUserData(@RequestParam String sessionId) {
        UserData userData = userDataService.getUserData(sessionId);
        return ResponseEntity.ok(userData);
    }
    
    // 医院治疗
    @PostMapping("/hospital/visit")
    public ResponseEntity<UserData> visitHospital(@RequestParam String sessionId) {
        try {
            UserData userData = userDataService.visitHospital(sessionId);
            return ResponseEntity.ok(userData);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // 医院义工服务
    @PostMapping("/hospital/volunteer")
    public ResponseEntity<UserData> volunteerAtHospital(@RequestParam String sessionId) {
        try {
            UserData userData = userDataService.volunteerAtHospital(sessionId);
            return ResponseEntity.ok(userData);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // 推进一天
    @PostMapping("/advance-day")
    public ResponseEntity<UserData> advanceDay(@RequestParam String sessionId) {
        System.out.println("推进一天接口被调用，sessionId: " + sessionId);
        try {
            UserData userData = userDataService.advanceDay(sessionId);
            System.out.println("推进一天成功，当前天数: " + userData.getCurrentDay());
            return ResponseEntity.ok(userData);
        } catch (Exception e) {
            System.err.println("推进一天失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    // 吸烟导致的推进一天（健康下降）
    @PostMapping("/advance-day-smoking")
    public ResponseEntity<UserData> advanceDaySmoking(@RequestParam String sessionId) {
        System.out.println("吸烟推进一天接口被调用，sessionId: " + sessionId);
        try {
            UserData userData = userDataService.advanceDaySmoking(sessionId);
            System.out.println("吸烟推进一天成功，当前天数: " + userData.getCurrentDay());
            return ResponseEntity.ok(userData);
        } catch (Exception e) {
            System.err.println("吸烟推进一天失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    // 获取成就列表
    @GetMapping("/achievements")
    public ResponseEntity<Map<String, Object>> getAchievements(@RequestParam String sessionId) {
        UserData userData = userDataService.getUserData(sessionId);
        Map<String, Object> achievements = new HashMap<>();
        
        // 使用AchievementService获取所有成就
        Map<String, String> allAchievements = achievementService.getAllAchievements();
        
        // 获取用户已解锁的成就
        String[] unlockedAchievements = userData.getUnlockedAchievements().isEmpty() ? 
            new String[0] : userData.getUnlockedAchievements().split(",");
        
        achievements.put("unlocked", unlockedAchievements);
        achievements.put("all", allAchievements);
        achievements.put("score", userData.getAchievementScore());
        
        return ResponseEntity.ok(achievements);
    }
} 