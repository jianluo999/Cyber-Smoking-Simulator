package com.cyber.quitsmokingsimulator.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AchievementService {
    
    // 获取所有成就定义
    public Map<String, String> getAllAchievements() {
        Map<String, String> achievements = new HashMap<>();
        
        // 吸烟相关成就
        achievements.put("first_smoke", "不幸的开始 - 第一次吸烟");
        achievements.put("heavy_smoker", "沉沦 - 吸烟100支");
        achievements.put("addicted", "无法自拔 - 吸烟500支");
        achievements.put("smoke_devil", "烟鬼 - 吸烟1000支");
        achievements.put("smoke_king", "烟王 - 吸烟5000支");
        achievements.put("smoke_god", "烟神 - 吸烟10000支");
        achievements.put("smoke_marathon", "一日千支 - 一天内吸烟20支");
        
        // 医院相关成就
        achievements.put("first_hospital_visit", "首次就医 - 第一次去医院");
        achievements.put("hospital_regular", "医院常客 - 访问医院10次");
        achievements.put("hospital_expert", "医院专家 - 访问医院50次");
        achievements.put("hospital_master", "医院之主 - 访问医院100次");
        achievements.put("health_conscious", "健康意识 - 主动就医");
        
        // 义工相关成就
        achievements.put("first_volunteer", "爱心使者 - 第一次义工");
        achievements.put("volunteer_master", "义工达人 - 义工50小时");
        achievements.put("selfless_dedication", "无私奉献 - 义工100小时");
        achievements.put("saint", "圣人 - 义工500小时");
        
        // 时间相关成就
        achievements.put("survive_week", "坚持一周 - 生存7天");
        achievements.put("survive_month", "坚持一月 - 生存30天");
        achievements.put("hundred_days", "百日人生 - 生存100天");
        achievements.put("survive_year", "坚持一年 - 生存365天");
        achievements.put("eternal_legend", "永恒传说 - 生存1000天");
        
        // 工作相关成就
        achievements.put("hard_worker", "努力工作者 - 完成50次工作");
        achievements.put("workaholic", "工作狂 - 完成200次工作");
        achievements.put("work_machine", "工作机器 - 完成500次工作");
        achievements.put("labor_model", "劳模 - 完成1000次工作");
        
        // 捐赠相关成就
        achievements.put("first_donation", "善心萌发 - 第一次捐赠");
        achievements.put("philanthropist", "慈善家 - 捐赠50次");
        achievements.put("charity_master", "慈善大师 - 捐赠200次");
        
        // 财富相关成就
        achievements.put("small_fortune", "小富即安 - 拥有500元");
        achievements.put("rich_man", "富翁 - 拥有2000元");
        achievements.put("millionaire", "百万富翁 - 拥有10000元");
        achievements.put("billionaire", "亿万富翁 - 拥有100000元");
        achievements.put("broke_king", "破产王 - 金钱为0");
        
        // 健康相关成就
        achievements.put("health_king", "健康之王 - 所有健康指标都达到100");
        achievements.put("near_death", "濒死体验 - 健康指标低于10");
        achievements.put("iron_body", "钢铁之躯 - 健康指标保持在90以上超过30天");
        achievements.put("longevity_star", "长寿之星 - 预期寿命超过85岁");
        achievements.put("early_death", "夭折之命 - 预期寿命低于50岁");
        achievements.put("edge_of_death", "生死边缘 - 所有健康指标都低于20");
        
        // 极限成就
        achievements.put("ultimate_hoarder", "终极囤积 - 香烟库存超过100支");
        
        return achievements;
    }
    
    // 获取成就分数
    public int getAchievementScore(String achievementId) {
        // 根据成就的稀有度和难度分配分数
        switch (achievementId) {
            // 基础成就 (10分)
            case "first_smoke":
            case "first_hospital_visit":
            case "first_volunteer":
            case "first_donation":
            case "survive_week":
                return 10;
            
            // 常规成就 (20分)
            case "heavy_smoker":
            case "hospital_regular":
            case "volunteer_master":
            case "hard_worker":
            case "small_fortune":
            case "survive_month":
            case "health_conscious":
                return 20;
            
            // 困难成就 (50分)
            case "addicted":
            case "hospital_expert":
            case "selfless_dedication":
            case "workaholic":
            case "philanthropist":
            case "rich_man":
            case "hundred_days":
            case "health_king":
            case "iron_body":
                return 50;
            
            // 极限成就 (100分)
            case "smoke_devil":
            case "hospital_master":
            case "saint":
            case "work_machine":
            case "charity_master":
            case "millionaire":
            case "survive_year":
            case "longevity_star":
            case "smoke_marathon":
            case "ultimate_hoarder":
                return 100;
            
            // 传说成就 (200分)
            case "smoke_king":
            case "labor_model":
            case "billionaire":
            case "eternal_legend":
                return 200;
            
            // 神话成就 (500分)
            case "smoke_god":
                return 500;
            
            // 特殊成就 (负分或低分)
            case "near_death":
            case "early_death":
            case "edge_of_death":
                return -10;
            
            case "broke_king":
                return 5;
            
            default:
                return 10;
        }
    }
} 