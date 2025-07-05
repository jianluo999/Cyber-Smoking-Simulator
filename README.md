# 🚬 赛博戒烟模拟器 / Cyber Smoking Simulator

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-brightgreen)
![Vue.js](https://img.shields.io/badge/Vue.js-3.0-4FC08D)
![License](https://img.shields.io/badge/License-MIT-blue)

[English](#english) | [中文](#中文)

## 中文

### 📖 项目简介

这是一个基于 **SpringBoot3 + Vue3** 的赛博朋克风格戒烟模拟器。通过真实模拟吸烟对人体健康的影响，让用户直观地感受到吸烟的危害，从而起到戒烟警示的作用。
![image](https://github.com/user-attachments/assets/bc5df1f9-2cbf-43e4-9deb-4be7c059fdd3)
![Uploading 屏幕截图 2025-07-05 144508.png…]()



### ✨ 核心功能

#### 🏥 健康系统
- **多器官健康监测**: 肺部、心脏、肝脏、免疫系统
- **指数损害机制**: 吸烟次数越多，健康损害越严重
- **实时健康状态**: 血压、血氧、预期寿命等指标

#### 💰 经济系统
- **金钱管理**: 工作赚钱、购买香烟
- **小卖部系统**: 香烟库存管理
- **黑心中介**: 健康恶化时的危险工作选择

#### 🏥 医院系统
- **健康治疗**: 花费金钱恢复健康
- **义工服务**: 通过帮助他人获得健康提升
- **医院访问记录**: 追踪治疗历史

#### 🏆 成就系统
- **30+ 成就**: 涵盖财富、健康、极限、生存等多个类别
- **积分系统**: 不同难度成就给予不同分数
- **成就弹窗**: 完成成就时的即时反馈

#### ⏰ 时间系统
- **天数推进**: 时间流逝对健康的影响
- **吸烟时间**: 每次吸烟会推进时间并恶化健康
- **寿命波动**: 随机化的寿命减少机制

#### 💀 死亡系统
- **多样化死因**: 肺癌、心脏病、工地意外、过劳死等7种死亡原因
- **智能死因判断**: 根据不同健康状态和行为模式判断死因
- **死亡统计**: 详细的生前数据统计

#### 🎨 界面系统
- **双主题支持**: 赛博朋克风格 & 传统风格
- **自定义弹窗**: 美观的提示系统
- **响应式设计**: 适配移动端和桌面端
- **烟雾特效**: 吸烟时的视觉效果

### 🛠️ 技术栈

**后端**
- Spring Boot 3.0+
- Spring Data JPA
- H2 Database
- Maven

**前端**
- Vue.js 3.0+
- JavaScript ES6+
- CSS3 动画
- Axios HTTP客户端

### 🚀 快速开始

#### 环境要求
- Java 17+
- Node.js 16+
- Maven 3.8+

#### 安装步骤

1. **克隆仓库**
```bash
git clone https://github.com/jianluo999/Cyber-Smoking-Simulator.git
cd Cyber-Smoking-Simulator
```

2. **启动后端**
```bash
mvn spring-boot:run
```

3. **启动前端**
```bash
cd frontend
npm install
npm run serve
```

4. **访问应用**
- 前端: http://localhost:3000
- 后端: http://localhost:8080

### 🎮 游戏玩法

1. **开始游戏**: 你从100%的健康状态开始
2. **购买香烟**: 在小卖部花钱购买香烟
3. **吸烟模拟**: 点击吸烟按钮开始模拟，观察健康下降
4. **赚钱工作**: 通过工作赚钱购买更多香烟
5. **医院治疗**: 健康恶化时去医院治疗
6. **成就解锁**: 完成各种成就获得积分
7. **死亡结局**: 根据不同行为模式体验不同死亡结局

### 📊 数据统计

- **健康指标**: 实时监测多个器官健康状态
- **经济状况**: 金钱、库存、工作收入追踪
- **行为统计**: 吸烟次数、工作天数、医院访问等
- **成就进度**: 解锁成就数量和总积分

### 🌟 特色亮点

- **教育意义**: 通过游戏化方式展示吸烟危害
- **真实模拟**: 基于医学数据的健康影响模拟
- **指数损害**: 越吸烟越难戒，真实反映成瘾性
- **多重结局**: 不同生活方式导致不同结局
- **数据持久化**: 游戏进度自动保存

---

## English

### 📖 Project Introduction

A cyberpunk-style smoking cessation simulator built with **SpringBoot3 + Vue3**. It helps users understand the real impact of smoking on health through realistic simulation, serving as a warning against smoking.

### ✨ Core Features

#### 🏥 Health System
- **Multi-organ Health Monitoring**: Lungs, heart, liver, immune system
- **Exponential Damage Mechanism**: More smoking leads to more severe health damage
- **Real-time Health Status**: Blood pressure, oxygen levels, life expectancy indicators

#### 💰 Economic System
- **Money Management**: Work to earn money, buy cigarettes
- **Convenience Store System**: Cigarette inventory management
- **Dark Agency**: Dangerous work options when health deteriorates

#### 🏥 Hospital System
- **Health Treatment**: Spend money to restore health
- **Volunteer Service**: Gain health improvement by helping others
- **Hospital Visit Records**: Track treatment history

#### 🏆 Achievement System
- **30+ Achievements**: Covering wealth, health, extreme, survival categories
- **Scoring System**: Different difficulty achievements give different scores
- **Achievement Popups**: Instant feedback when completing achievements

#### ⏰ Time System
- **Day Progression**: Impact of time passage on health
- **Smoking Time**: Each smoking session advances time and worsens health
- **Life Fluctuation**: Randomized life reduction mechanism

#### 💀 Death System
- **Diverse Death Causes**: 7 death reasons including lung cancer, heart disease, work accidents, overwork
- **Smart Death Determination**: Judge death causes based on different health states and behavior patterns
- **Death Statistics**: Detailed lifetime data statistics

#### 🎨 Interface System
- **Dual Theme Support**: Cyberpunk style & Traditional style
- **Custom Popups**: Beautiful notification system
- **Responsive Design**: Compatible with mobile and desktop
- **Smoke Effects**: Visual effects during smoking

### 🛠️ Tech Stack

**Backend**
- Spring Boot 3.0+
- Spring Data JPA
- H2 Database
- Maven

**Frontend**
- Vue.js 3.0+
- JavaScript ES6+
- CSS3 Animations
- Axios HTTP Client

### 🚀 Quick Start

#### Requirements
- Java 17+
- Node.js 16+
- Maven 3.8+

#### Installation Steps

1. **Clone Repository**
```bash
git clone https://github.com/jianluo999/Cyber-Smoking-Simulator.git
cd Cyber-Smoking-Simulator
```

2. **Start Backend**
```bash
mvn spring-boot:run
```

3. **Start Frontend**
```bash
cd frontend
npm install
npm run serve
```

4. **Access Application**
- Frontend: http://localhost:3000
- Backend: http://localhost:8080

### 🎮 Gameplay

1. **Start Game**: Begin with 100% health status
2. **Buy Cigarettes**: Purchase cigarettes at the convenience store
3. **Smoking Simulation**: Click smoke button to simulate, observe health decline
4. **Work for Money**: Earn money through work to buy more cigarettes
5. **Hospital Treatment**: Get treatment when health deteriorates
6. **Unlock Achievements**: Complete various achievements to earn points
7. **Death Endings**: Experience different death endings based on behavior patterns

### 📊 Data Statistics

- **Health Indicators**: Real-time monitoring of multiple organ health
- **Economic Status**: Money, inventory, work income tracking
- **Behavior Statistics**: Smoking count, work days, hospital visits, etc.
- **Achievement Progress**: Number of unlocked achievements and total score

### 🌟 Highlights

- **Educational Value**: Demonstrates smoking harm through gamification
- **Realistic Simulation**: Health impact simulation based on medical data
- **Exponential Damage**: Harder to quit with more smoking, reflecting real addiction
- **Multiple Endings**: Different lifestyles lead to different outcomes
- **Data Persistence**: Game progress automatically saved

### 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

### 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

### 📧 Contact

- GitHub: [@jianluo999](https://github.com/jianluo999)
- Email: your-email@example.com

---

⚠️ **健康提醒 / Health Warning**: 此游戏仅供教育和娱乐目的，不能替代专业医疗建议。如需戒烟帮助，请咨询专业医护人员。

*This game is for educational and entertainment purposes only and cannot replace professional medical advice. For smoking cessation help, please consult professional healthcare providers.* 
