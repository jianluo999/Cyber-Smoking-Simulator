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
    
    @Autowired
    private AchievementService achievementService;
    
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
        
        // 检查所有成就
        checkAllAchievements(userData);
        
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
        
        // 检查所有成就
        checkAllAchievements(userData);
        
        return saveUserData(userData);
    }
    
    // 推进一天时间
    @Transactional
    public UserData advanceDay(String sessionId) {
        UserData userData = getUserData(sessionId);
        userData.setCurrentDay(userData.getCurrentDay() + 1);
        
        // 时间推进的效果：增加健康度但减少寿命（带波动）
        userData.setLungHealth(Math.min(100.0, userData.getLungHealth() + 1.5));
        userData.setHeartHealth(Math.min(100.0, userData.getHeartHealth() + 1.5));
        userData.setLiverHealth(Math.min(100.0, userData.getLiverHealth() + 1.0));
        userData.setImmunity(Math.min(100.0, userData.getImmunity() + 1.2));
        
        // 寿命减少带随机波动：0.1-0.5年之间
        double lifeDecrease = 0.1 + Math.random() * 0.4; // 0.1到0.5年的随机波动
        userData.setLifeExpectancy(Math.max(30.0, userData.getLifeExpectancy() - lifeDecrease));
        
        // 检查是否一周未去医院，如果是则开始健康衰退
        if (!userData.hasVisitedHospitalThisWeek() && userData.shouldGoToHospital()) {
            // 每天衰退
            userData.setLungHealth(Math.max(0.0, userData.getLungHealth() - 2));
            userData.setHeartHealth(Math.max(0.0, userData.getHeartHealth() - 2));
            userData.setLifeExpectancy(Math.max(30.0, userData.getLifeExpectancy() - 0.5));
            userData.setNeedsHospital(true);
        }
        
        // 检查所有成就
        checkAllAchievements(userData);
        
        return saveUserData(userData);
    }
    
    // 吸烟导致的推进一天时间（健康下降）
    @Transactional
    public UserData advanceDaySmoking(String sessionId) {
        UserData userData = getUserData(sessionId);
        userData.setCurrentDay(userData.getCurrentDay() + 1);
        
        // 计算指数损害系数，基于总吸烟次数
        int smokeCount = userData.getTotalSmokes();
        // 指数增长公式：1 + 0.15 * log(吸烟次数/10 + 1)
        double exponentialFactor = 1.0 + 0.15 * Math.log(smokeCount / 10.0 + 1.0);
        
        // 吸烟导致的时间推进：健康下降（指数增长），寿命也减少
        userData.setLungHealth(Math.max(0.0, userData.getLungHealth() - 0.8 * exponentialFactor));
        userData.setHeartHealth(Math.max(0.0, userData.getHeartHealth() - 0.6 * exponentialFactor));
        userData.setLiverHealth(Math.max(0.0, userData.getLiverHealth() - 0.4 * exponentialFactor));
        userData.setImmunity(Math.max(0.0, userData.getImmunity() - 0.7 * exponentialFactor));
        
        // 吸烟推进时寿命减少更多，带随机波动：0.2-0.7年之间，并考虑指数因子
        double baseLifeDecrease = 0.2 + Math.random() * 0.5; // 0.2到0.7年的随机波动
        double lifeDecrease = baseLifeDecrease * exponentialFactor;
        userData.setLifeExpectancy(Math.max(30.0, userData.getLifeExpectancy() - lifeDecrease));
        
        // 检查是否一周未去医院，如果是则开始健康衰退
        if (!userData.hasVisitedHospitalThisWeek() && userData.shouldGoToHospital()) {
            // 每天衰退
            userData.setLungHealth(Math.max(0.0, userData.getLungHealth() - 2));
            userData.setHeartHealth(Math.max(0.0, userData.getHeartHealth() - 2));
            userData.setLifeExpectancy(Math.max(30.0, userData.getLifeExpectancy() - 0.5));
            userData.setNeedsHospital(true);
        }
        
        // 检查所有成就
        checkAllAchievements(userData);
        
        return saveUserData(userData);
    }
    
    // 检查医院相关成就
    private void checkHospitalAchievements(UserData userData) {
        // 首次就医
        if (userData.getHospitalVisits() == 1) {
            addAchievementWithScore(userData, "first_hospital_visit");
        }
        
        // 常客 - 访问医院10次
        if (userData.getHospitalVisits() >= 10) {
            addAchievementWithScore(userData, "hospital_regular");
        }
        
        // 健康意识 - 主动就医（健康未到危险线就去医院）
        if (!userData.shouldGoToHospital()) {
            addAchievementWithScore(userData, "health_conscious");
        }
        
        // 医院专家 - 访问医院50次
        if (userData.getHospitalVisits() >= 50) {
            addAchievementWithScore(userData, "hospital_expert");
        }
        
        // 医院之主 - 访问医院100次
        if (userData.getHospitalVisits() >= 100) {
            addAchievementWithScore(userData, "hospital_master");
        }
    }
    
    // 检查义工相关成就
    private void checkVolunteerAchievements(UserData userData) {
        // 爱心使者 - 第一次义工
        if (userData.getVolunteerHours() == 1) {
            addAchievementWithScore(userData, "first_volunteer");
        }
        
        // 义工达人 - 义工50小时
        if (userData.getVolunteerHours() >= 50) {
            addAchievementWithScore(userData, "volunteer_master");
        }
        
        // 无私奉献 - 义工100小时
        if (userData.getVolunteerHours() >= 100) {
            addAchievementWithScore(userData, "selfless_dedication");
        }
        
        // 圣人 - 义工500小时
        if (userData.getVolunteerHours() >= 500) {
            addAchievementWithScore(userData, "saint");
        }
    }
    
    // 检查时间相关成就
    private void checkTimeAchievements(UserData userData) {
        // 坚持一周
        if (userData.getCurrentDay() == 7) {
            addAchievementWithScore(userData, "survive_week");
        }
        
        // 坚持一月
        if (userData.getCurrentDay() == 30) {
            addAchievementWithScore(userData, "survive_month");
        }
        
        // 百日人生
        if (userData.getCurrentDay() == 100) {
            addAchievementWithScore(userData, "hundred_days");
        }
        
        // 坚持一年
        if (userData.getCurrentDay() == 365) {
            addAchievementWithScore(userData, "survive_year");
        }
        
        // 永恒传说
        if (userData.getCurrentDay() == 1000) {
            addAchievementWithScore(userData, "eternal_legend");
        }
    }
    
    // 检查吸烟相关成就
    public void checkSmokingAchievements(UserData userData) {
        // 不幸的开始 - 第一次吸烟
        if (userData.getTotalSmokes() == 1) {
            addAchievementWithScore(userData, "first_smoke");
        }
        
        // 沉沦 - 吸烟100支
        if (userData.getTotalSmokes() == 100) {
            addAchievementWithScore(userData, "heavy_smoker");
        }
        
        // 无法自拔 - 吸烟500支
        if (userData.getTotalSmokes() == 500) {
            addAchievementWithScore(userData, "addicted");
        }
        
        // 烟鬼 - 吸烟1000支
        if (userData.getTotalSmokes() == 1000) {
            addAchievementWithScore(userData, "smoke_devil");
        }
        
        // 烟王 - 吸烟5000支
        if (userData.getTotalSmokes() == 5000) {
            addAchievementWithScore(userData, "smoke_king");
        }
        
        // 烟神 - 吸烟10000支
        if (userData.getTotalSmokes() == 10000) {
            addAchievementWithScore(userData, "smoke_god");
        }
    }
    
    // 检查工作相关成就
    public void checkWorkAchievements(UserData userData) {
        // 努力工作者 - 完成50次工作
        if (userData.getTotalWorkDays() >= 50) {
            addAchievementWithScore(userData, "hard_worker");
        }
        
        // 工作狂 - 完成200次工作
        if (userData.getTotalWorkDays() >= 200) {
            addAchievementWithScore(userData, "workaholic");
        }
        
        // 工作机器 - 完成500次工作
        if (userData.getTotalWorkDays() >= 500) {
            addAchievementWithScore(userData, "work_machine");
        }
        
        // 劳模 - 完成1000次工作
        if (userData.getTotalWorkDays() >= 1000) {
            addAchievementWithScore(userData, "labor_model");
        }
    }
    
    // 检查捐赠相关成就
    public void checkDonationAchievements(UserData userData) {
                // 善心萌发 - 第一次捐赠
        if (userData.getTotalDonations() == 1) {
            addAchievementWithScore(userData, "first_donation");
        }
        
        // 慈善家 - 捐赠50次
        if (userData.getTotalDonations() >= 50) {
            addAchievementWithScore(userData, "philanthropist");
        }
        
        // 慈善大师 - 捐赠200次
        if (userData.getTotalDonations() >= 200) {
            addAchievementWithScore(userData, "charity_master");
        }
     }
     
     // 检查财富相关成就
     public void checkWealthAchievements(UserData userData) {
         // 小富即安 - 拥有500元
         if (userData.getMoney() >= 500) {
             addAchievementWithScore(userData, "small_fortune");
         }
         
         // 富翁 - 拥有2000元
         if (userData.getMoney() >= 2000) {
             addAchievementWithScore(userData, "rich_man");
         }
         
         // 百万富翁 - 拥有10000元
         if (userData.getMoney() >= 10000) {
             addAchievementWithScore(userData, "millionaire");
         }
         
         // 亿万富翁 - 拥有100000元
         if (userData.getMoney() >= 100000) {
             addAchievementWithScore(userData, "billionaire");
         }
     }
     
     // 检查健康相关成就
     public void checkHealthAchievements(UserData userData) {
         // 健康之王 - 所有健康指标都达到100
         if (userData.getLungHealth() >= 100 && userData.getHeartHealth() >= 100 && 
             userData.getLiverHealth() >= 100 && userData.getImmunity() >= 100) {
             addAchievementWithScore(userData, "health_king");
         }
         
         // 濒死体验 - 健康指标低于10
         if (userData.getLungHealth() < 10 || userData.getHeartHealth() < 10 || 
             userData.getLiverHealth() < 10 || userData.getImmunity() < 10) {
             addAchievementWithScore(userData, "near_death");
         }
         
         // 钢铁之躯 - 健康指标保持在90以上超过30天
         if (userData.getLungHealth() >= 90 && userData.getHeartHealth() >= 90 && 
             userData.getLiverHealth() >= 90 && userData.getImmunity() >= 90 && 
             userData.getCurrentDay() >= 30) {
             addAchievementWithScore(userData, "iron_body");
         }
         
         // 长寿之星 - 预期寿命超过85岁
         if (userData.getLifeExpectancy() >= 85) {
             addAchievementWithScore(userData, "longevity_star");
         }
         
         // 夭折之命 - 预期寿命低于50岁
         if (userData.getLifeExpectancy() < 50) {
             addAchievementWithScore(userData, "early_death");
         }
     }
     
     // 检查极限成就
     public void checkExtremeAchievements(UserData userData) {
         // 一日千支 - 一天内吸烟20支（极限）
         if (userData.getTodaySmokes() >= 20) {
             addAchievementWithScore(userData, "smoke_marathon");
         }
         
         // 破产王 - 金钱为0
         if (userData.getMoney() == 0) {
             addAchievementWithScore(userData, "broke_king");
         }
         
         // 终极囤积 - 香烟库存超过100支
         if (userData.getCigaretteStock() >= 100) {
             addAchievementWithScore(userData, "ultimate_hoarder");
         }
         
         // 生死边缘 - 所有健康指标都低于20
         if (userData.getLungHealth() < 20 && userData.getHeartHealth() < 20 && 
             userData.getLiverHealth() < 20 && userData.getImmunity() < 20) {
             addAchievementWithScore(userData, "edge_of_death");
         }
     }
    
    // 检查所有成就
    public void checkAllAchievements(UserData userData) {
        checkHospitalAchievements(userData);
        checkVolunteerAchievements(userData);
        checkTimeAchievements(userData);
        checkSmokingAchievements(userData);
        checkWorkAchievements(userData);
        checkDonationAchievements(userData);
        checkWealthAchievements(userData);
        checkHealthAchievements(userData);
        checkExtremeAchievements(userData);
    }
    
    // 添加成就辅助方法
    private void addAchievementWithScore(UserData userData, String achievementId) {
        if (!userData.hasAchievement(achievementId)) {
            int score = achievementService.getAchievementScore(achievementId);
            userData.addAchievement(achievementId, score);
        }
    }
} 