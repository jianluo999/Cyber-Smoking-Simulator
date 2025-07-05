package com.cyber.quitsmokingsimulator.service;

import com.cyber.quitsmokingsimulator.model.UserData;
import com.cyber.quitsmokingsimulator.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserDataService {
    
    @Autowired
    private UserDataRepository userDataRepository;
    
    // 获取用户数据，如果不存在则创建新用户
    public UserData getUserData(String sessionId) {
        return userDataRepository.findBySessionId(sessionId)
            .orElseGet(() -> createNewUser(sessionId));
    }
    
    // 创建新用户
    private UserData createNewUser(String sessionId) {
        UserData userData = new UserData();
        userData.setSessionId(sessionId);
        return userDataRepository.save(userData);
    }
    
    // 保存用户数据
    public UserData saveUserData(UserData userData) {
        userData.updateLastModified();
        return userDataRepository.save(userData);
    }
    
    // 更新用户数据
    @Transactional
    public UserData updateUserData(String sessionId, UserData newData) {
        UserData existingData = getUserData(sessionId);
        
        // 更新健康数据
        existingData.setLungHealth(newData.getLungHealth());
        existingData.setHeartHealth(newData.getHeartHealth());
        existingData.setLiverHealth(newData.getLiverHealth());
        existingData.setBloodPressure(newData.getBloodPressure());
        existingData.setOxygenLevel(newData.getOxygenLevel());
        existingData.setImmunity(newData.getImmunity());
        existingData.setLifeExpectancy(newData.getLifeExpectancy());
        existingData.setSmokingDamage(newData.getSmokingDamage());
        
        // 更新经济数据
        existingData.setMoney(newData.getMoney());
        existingData.setCigaretteStock(newData.getCigaretteStock());
        existingData.setWorkPay(newData.getWorkPay());
        existingData.setIsWorking(newData.getIsWorking());
        existingData.setWorkProgress(newData.getWorkProgress());
        
        // 更新统计数据
        existingData.setTodaySmokes(newData.getTodaySmokes());
        existingData.setTotalSmokes(newData.getTotalSmokes());
        existingData.setTotalDonations(newData.getTotalDonations());
        existingData.setTotalWorkDays(newData.getTotalWorkDays());
        
        // 更新状态数据
        existingData.setIsDead(newData.getIsDead());
        existingData.setCurrentTheme(newData.getCurrentTheme());
        
        // 更新时间系统
        existingData.setCurrentDay(newData.getCurrentDay());
        existingData.setLastHospitalDay(newData.getLastHospitalDay());
        existingData.setNeedsHospital(newData.getNeedsHospital());
        
        // 更新成就系统
        existingData.setAchievementScore(newData.getAchievementScore());
        existingData.setUnlockedAchievements(newData.getUnlockedAchievements());
        
        // 更新医院系统
        existingData.setHospitalVisits(newData.getHospitalVisits());
        existingData.setVolunteerHours(newData.getVolunteerHours());
        
        return saveUserData(existingData);
    }
    
    // 重置用户数据（重新开始游戏）
    @Transactional
    public UserData resetUserData(String sessionId) {
        UserData userData = getUserData(sessionId);
        
        // 重置健康数据
        userData.setLungHealth(100.0);
        userData.setHeartHealth(100.0);
        userData.setLiverHealth(100.0);
        userData.setBloodPressure(120.0);
        userData.setOxygenLevel(98.0);
        userData.setImmunity(100.0);
        userData.setLifeExpectancy(80.0);
        userData.setSmokingDamage(0.0);
        
        // 重置经济数据
        userData.setMoney(100);
        userData.setCigaretteStock(0);
        userData.setWorkPay(50);
        userData.setIsWorking(false);
        userData.setWorkProgress(0);
        
        // 重置统计数据
        userData.setTodaySmokes(0);
        userData.setTotalSmokes(0);
        userData.setTotalDonations(0);
        userData.setTotalWorkDays(0);
        
        // 重置状态数据
        userData.setIsDead(false);
        
        return saveUserData(userData);
    }
    
    // 删除用户数据
    @Transactional
    public void deleteUserData(String sessionId) {
        userDataRepository.deleteBySessionId(sessionId);
    }
    
    // 检查用户是否存在
    public boolean userExists(String sessionId) {
        return userDataRepository.existsBySessionId(sessionId);
    }
    
    // 生成新的Session ID
    public String generateSessionId() {
        return UUID.randomUUID().toString();
    }
    
    // 医院治疗
    @Transactional
    public UserData visitHospital(String sessionId) {
        UserData userData = getUserData(sessionId);
        
        // 扣除医疗费用
        if (userData.getMoney() < 200) {
            throw new RuntimeException("金钱不足，无法支付医疗费用");
        }
        
        userData.setMoney(userData.getMoney() - 200);
        
        // 恢复健康
        userData.setLungHealth(Math.min(100.0, userData.getLungHealth() + 30));
        userData.setHeartHealth(Math.min(100.0, userData.getHeartHealth() + 30));
        userData.setLiverHealth(Math.min(100.0, userData.getLiverHealth() + 25));
        userData.setImmunity(Math.min(100.0, userData.getImmunity() + 35));
        userData.setLifeExpectancy(Math.min(85.0, userData.getLifeExpectancy() + 3));
        
        // 更新医院记录
        userData.setLastHospitalDay(userData.getCurrentDay());
        userData.setHospitalVisits(userData.getHospitalVisits() + 1);
        userData.setNeedsHospital(false);
        
        // 检查成就
        checkHospitalAchievements(userData);
        checkSmokingAchievements(userData);
        checkWorkAchievements(userData);
        checkDonationAchievements(userData);
        
        return saveUserData(userData);
    }
    
    // 医院义工服务
    @Transactional
    public UserData volunteerAtHospital(String sessionId) {
        UserData userData = getUserData(sessionId);
        
        // 检查健康状况是否允许义工服务
        if (userData.shouldGoToHospital()) {
            throw new RuntimeException("健康状况太差，无法进行义工服务，请先接受治疗");
        }
        
        // 义工服务不给钱，但会恢复部分健康和增加成就
        userData.setLungHealth(Math.min(100.0, userData.getLungHealth() + 5));
        userData.setHeartHealth(Math.min(100.0, userData.getHeartHealth() + 8));
        userData.setImmunity(Math.min(100.0, userData.getImmunity() + 10));
        userData.setLifeExpectancy(Math.min(85.0, userData.getLifeExpectancy() + 1));
        
        // 增加义工时间
        userData.setVolunteerHours(userData.getVolunteerHours() + 1);
        
        // 检查成就
        checkVolunteerAchievements(userData);
        checkSmokingAchievements(userData);
        checkWorkAchievements(userData);
        checkDonationAchievements(userData);
        
        return saveUserData(userData);
    }
    
    // 推进一天时间
    @Transactional
    public UserData advanceDay(String sessionId) {
        UserData userData = getUserData(sessionId);
        userData.setCurrentDay(userData.getCurrentDay() + 1);
        
        // 检查是否一周未去医院，如果是则开始健康衰退
        if (!userData.hasVisitedHospitalThisWeek() && userData.shouldGoToHospital()) {
            // 每天衰退
            userData.setLungHealth(Math.max(0.0, userData.getLungHealth() - 2));
            userData.setHeartHealth(Math.max(0.0, userData.getHeartHealth() - 2));
            userData.setLifeExpectancy(Math.max(30.0, userData.getLifeExpectancy() - 0.5));
            userData.setNeedsHospital(true);
        }
        
        // 检查时间相关成就
        checkTimeAchievements(userData);
        checkSmokingAchievements(userData);
        checkWorkAchievements(userData);
        checkDonationAchievements(userData);
        
        return saveUserData(userData);
    }
    
    // 检查医院相关成就
    private void checkHospitalAchievements(UserData userData) {
        // 首次就医
        if (userData.getHospitalVisits() == 1) {
            userData.addAchievement("first_hospital_visit");
        }
        
        // 常客 - 访问医院10次
        if (userData.getHospitalVisits() >= 10) {
            userData.addAchievement("hospital_regular");
        }
        
        // 健康意识 - 主动就医（健康未到危险线就去医院）
        if (!userData.shouldGoToHospital()) {
            userData.addAchievement("health_conscious");
        }
    }
    
    // 检查义工相关成就
    private void checkVolunteerAchievements(UserData userData) {
        // 爱心使者 - 第一次义工
        if (userData.getVolunteerHours() == 1) {
            userData.addAchievement("first_volunteer");
        }
        
        // 义工达人 - 义工50小时
        if (userData.getVolunteerHours() >= 50) {
            userData.addAchievement("volunteer_master");
        }
        
        // 无私奉献 - 义工100小时
        if (userData.getVolunteerHours() >= 100) {
            userData.addAchievement("selfless_dedication");
        }
    }
    
    // 检查时间相关成就
    private void checkTimeAchievements(UserData userData) {
        // 坚持一周
        if (userData.getCurrentDay() == 7) {
            userData.addAchievement("survive_week");
        }
        
        // 坚持一月
        if (userData.getCurrentDay() == 30) {
            userData.addAchievement("survive_month");
        }
        
        // 百日人生
        if (userData.getCurrentDay() == 100) {
            userData.addAchievement("hundred_days");
        }
    }
    
    // 检查吸烟相关成就
    public void checkSmokingAchievements(UserData userData) {
        // 不幸的开始 - 第一次吸烟
        if (userData.getTotalSmokes() == 1) {
            userData.addAchievement("first_smoke");
        }
        
        // 沉沦 - 吸烟100支
        if (userData.getTotalSmokes() == 100) {
            userData.addAchievement("heavy_smoker");
        }
        
        // 无法自拔 - 吸烟500支
        if (userData.getTotalSmokes() == 500) {
            userData.addAchievement("addicted");
        }
    }
    
    // 检查工作相关成就
    public void checkWorkAchievements(UserData userData) {
        // 努力工作者 - 完成50次工作
        if (userData.getTotalWorkDays() >= 50) {
            userData.addAchievement("hard_worker");
        }
        
        // 工作狂 - 完成200次工作
        if (userData.getTotalWorkDays() >= 200) {
            userData.addAchievement("workaholic");
        }
    }
    
    // 检查捐赠相关成就
    public void checkDonationAchievements(UserData userData) {
        // 善心萌发 - 第一次捐赠
        if (userData.getTotalDonations() == 1) {
            userData.addAchievement("first_donation");
        }
        
        // 慈善家 - 捐赠50次
        if (userData.getTotalDonations() >= 50) {
            userData.addAchievement("philanthropist");
        }
    }
} 