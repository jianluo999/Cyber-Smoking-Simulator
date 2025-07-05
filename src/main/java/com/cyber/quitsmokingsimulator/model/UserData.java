package com.cyber.quitsmokingsimulator.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_data")
public class UserData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String sessionId;
    
    @Column(nullable = false)
    private LocalDateTime lastUpdated;
    
    // 健康数据
    @Column(nullable = false)
    private Double lungHealth = 100.0;
    
    @Column(nullable = false)
    private Double heartHealth = 100.0;
    
    @Column(nullable = false)
    private Double liverHealth = 100.0;
    
    @Column(nullable = false)
    private Double bloodPressure = 120.0;
    
    @Column(nullable = false)
    private Double oxygenLevel = 98.0;
    
    @Column(nullable = false)
    private Double immunity = 100.0;
    
    @Column(nullable = false)
    private Double lifeExpectancy = 80.0;
    
    @Column(nullable = false)
    private Double smokingDamage = 0.0;
    
    // 经济数据
    @Column(nullable = false)
    private Integer money = 100;
    
    @Column(nullable = false)
    private Integer cigaretteStock = 0;
    
    @Column(nullable = false)
    private Integer workPay = 50;
    
    @Column(nullable = false)
    private Boolean isWorking = false;
    
    @Column(nullable = false)
    private Integer workProgress = 0;
    
    @Column(nullable = false)
    private Boolean darkAgencyUnlocked = false;
    
    // 统计数据
    @Column(nullable = false)
    private Integer todaySmokes = 0;
    
    @Column(nullable = false)
    private Integer totalSmokes = 0;
    
    @Column(nullable = false)
    private Integer totalDonations = 0;
    
    @Column(nullable = false)
    private Integer totalWorkDays = 0;
    
    // 状态数据
    @Column(nullable = false)
    private Boolean isDead = false;
    
    @Column(nullable = false)
    private String currentTheme = "cyber";
    
    // 时间系统
    @Column(nullable = false)
    private Integer currentDay = 1;
    
    @Column(nullable = false)
    private Integer lastHospitalDay = 0;
    
    @Column(nullable = false)
    private Boolean needsHospital = false;
    
    // 成就系统
    @Column(nullable = false)
    private Integer achievementScore = 0;
    
    @Column(nullable = false)
    private String unlockedAchievements = "";
    
    // 医院系统
    @Column(nullable = false)
    private Integer hospitalVisits = 0;
    
    @Column(nullable = false)
    private Integer volunteerHours = 0;
    
    public UserData() {
        this.lastUpdated = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    
    public Double getLungHealth() { return lungHealth; }
    public void setLungHealth(Double lungHealth) { this.lungHealth = lungHealth; }
    
    public Double getHeartHealth() { return heartHealth; }
    public void setHeartHealth(Double heartHealth) { this.heartHealth = heartHealth; }
    
    public Double getLiverHealth() { return liverHealth; }
    public void setLiverHealth(Double liverHealth) { this.liverHealth = liverHealth; }
    
    public Double getBloodPressure() { return bloodPressure; }
    public void setBloodPressure(Double bloodPressure) { this.bloodPressure = bloodPressure; }
    
    public Double getOxygenLevel() { return oxygenLevel; }
    public void setOxygenLevel(Double oxygenLevel) { this.oxygenLevel = oxygenLevel; }
    
    public Double getImmunity() { return immunity; }
    public void setImmunity(Double immunity) { this.immunity = immunity; }
    
    public Double getLifeExpectancy() { return lifeExpectancy; }
    public void setLifeExpectancy(Double lifeExpectancy) { this.lifeExpectancy = lifeExpectancy; }
    
    public Double getSmokingDamage() { return smokingDamage; }
    public void setSmokingDamage(Double smokingDamage) { this.smokingDamage = smokingDamage; }
    
    public Integer getMoney() { return money; }
    public void setMoney(Integer money) { this.money = money; }
    
    public Integer getCigaretteStock() { return cigaretteStock; }
    public void setCigaretteStock(Integer cigaretteStock) { this.cigaretteStock = cigaretteStock; }
    
    public Integer getWorkPay() { return workPay; }
    public void setWorkPay(Integer workPay) { this.workPay = workPay; }
    
    public Boolean getIsWorking() { return isWorking; }
    public void setIsWorking(Boolean isWorking) { this.isWorking = isWorking; }
    
    public Integer getWorkProgress() { return workProgress; }
    public void setWorkProgress(Integer workProgress) { this.workProgress = workProgress; }
    
    public Boolean getDarkAgencyUnlocked() { return darkAgencyUnlocked; }
    public void setDarkAgencyUnlocked(Boolean darkAgencyUnlocked) { this.darkAgencyUnlocked = darkAgencyUnlocked; }
    
    public Integer getTodaySmokes() { return todaySmokes; }
    public void setTodaySmokes(Integer todaySmokes) { this.todaySmokes = todaySmokes; }
    
    public Integer getTotalSmokes() { return totalSmokes; }
    public void setTotalSmokes(Integer totalSmokes) { this.totalSmokes = totalSmokes; }
    
    public Integer getTotalDonations() { return totalDonations; }
    public void setTotalDonations(Integer totalDonations) { this.totalDonations = totalDonations; }
    
    public Integer getTotalWorkDays() { return totalWorkDays; }
    public void setTotalWorkDays(Integer totalWorkDays) { this.totalWorkDays = totalWorkDays; }
    
    public Boolean getIsDead() { return isDead; }
    public void setIsDead(Boolean isDead) { this.isDead = isDead; }
    
    public String getCurrentTheme() { return currentTheme; }
    public void setCurrentTheme(String currentTheme) { this.currentTheme = currentTheme; }
    
    public Integer getCurrentDay() { return currentDay; }
    public void setCurrentDay(Integer currentDay) { this.currentDay = currentDay; }
    
    public Integer getLastHospitalDay() { return lastHospitalDay; }
    public void setLastHospitalDay(Integer lastHospitalDay) { this.lastHospitalDay = lastHospitalDay; }
    
    public Boolean getNeedsHospital() { return needsHospital; }
    public void setNeedsHospital(Boolean needsHospital) { this.needsHospital = needsHospital; }
    
    public Integer getAchievementScore() { return achievementScore; }
    public void setAchievementScore(Integer achievementScore) { this.achievementScore = achievementScore; }
    
    public String getUnlockedAchievements() { return unlockedAchievements; }
    public void setUnlockedAchievements(String unlockedAchievements) { this.unlockedAchievements = unlockedAchievements; }
    
    public Integer getHospitalVisits() { return hospitalVisits; }
    public void setHospitalVisits(Integer hospitalVisits) { this.hospitalVisits = hospitalVisits; }
    
    public Integer getVolunteerHours() { return volunteerHours; }
    public void setVolunteerHours(Integer volunteerHours) { this.volunteerHours = volunteerHours; }
    
    // 更新最后修改时间
    public void updateLastModified() {
        this.lastUpdated = LocalDateTime.now();
    }
    
    // 检查是否需要医院治疗
    public boolean shouldGoToHospital() {
        return this.lungHealth < 30 || this.heartHealth < 30 || this.liverHealth < 30 || this.immunity < 20;
    }
    
    // 检查一周内是否去过医院
    public boolean hasVisitedHospitalThisWeek() {
        return (this.currentDay - this.lastHospitalDay) <= 7;
    }
    
    // 添加成就
    public void addAchievement(String achievementId) {
        if (!this.unlockedAchievements.contains(achievementId)) {
            if (!this.unlockedAchievements.isEmpty()) {
                this.unlockedAchievements += ",";
            }
            this.unlockedAchievements += achievementId;
            // 默认分数，实际分数会在Service层处理
            this.achievementScore += 10; 
        }
    }
    
    // 添加成就（带分数）
    public void addAchievement(String achievementId, int score) {
        if (!this.unlockedAchievements.contains(achievementId)) {
            if (!this.unlockedAchievements.isEmpty()) {
                this.unlockedAchievements += ",";
            }
            this.unlockedAchievements += achievementId;
            this.achievementScore += score;
        }
    }
    
    // 检查是否有成就
    public boolean hasAchievement(String achievementId) {
        return this.unlockedAchievements.contains(achievementId);
    }
} 