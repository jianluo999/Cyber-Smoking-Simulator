package com.cyber.quitsmokingsimulator.controller;

import com.cyber.quitsmokingsimulator.model.UserData;
import com.cyber.quitsmokingsimulator.service.UserDataService;
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
    
    // 获取成就列表
    @GetMapping("/achievements")
    public ResponseEntity<Map<String, Object>> getAchievements(@RequestParam String sessionId) {
        UserData userData = userDataService.getUserData(sessionId);
        Map<String, Object> achievements = new HashMap<>();
        
        // 定义所有成就
        Map<String, String> allAchievements = new HashMap<>();
        allAchievements.put("first_smoke", "不幸的开始 - 第一次吸烟");
        allAchievements.put("heavy_smoker", "沉沦 - 吸烟100支");
        allAchievements.put("addicted", "无法自拔 - 吸烟500支");
        allAchievements.put("first_hospital_visit", "首次就医 - 第一次去医院");
        allAchievements.put("hospital_regular", "医院常客 - 访问医院10次");
        allAchievements.put("health_conscious", "健康意识 - 主动就医");
        allAchievements.put("first_volunteer", "爱心使者 - 第一次义工");
        allAchievements.put("volunteer_master", "义工达人 - 义工50小时");
        allAchievements.put("selfless_dedication", "无私奉献 - 义工100小时");
        allAchievements.put("survive_week", "坚持一周 - 生存7天");
        allAchievements.put("survive_month", "坚持一月 - 生存30天");
        allAchievements.put("hundred_days", "百日人生 - 生存100天");
        allAchievements.put("hard_worker", "努力工作者 - 完成50次工作");
        allAchievements.put("workaholic", "工作狂 - 完成200次工作");
        allAchievements.put("first_donation", "善心萌发 - 第一次捐赠");
        allAchievements.put("philanthropist", "慈善家 - 捐赠50次");
        
        achievements.put("unlocked", userData.getUnlockedAchievements().split(","));
        achievements.put("all", allAchievements);
        achievements.put("score", userData.getAchievementScore());
        
        return ResponseEntity.ok(achievements);
    }
} 