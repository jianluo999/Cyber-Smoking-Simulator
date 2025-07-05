# 赛博戒烟模拟器 | Cyber Quit Smoking Simulator

一个基于 SpringBoot3 + Vue3 技术栈开发的赛博朋克风格戒烟模拟器网页应用。

## 🚀 功能特点

- **双主题设计** - 可切换赛博朋克风格和传统风格，适合不同年龄用户
  - 赛博朋克风格：霓虹灯效果、网格背景、粒子动画
  - 传统风格：温和色彩、适合中老年用户
- **主题切换** - 右上角一键切换主题风格
- **逼真的烟雾效果** - 点击香烟后会产生缭绕的烟雾粒子动画
- **吸烟统计功能** - 记录每日和总计吸烟次数
- **交互式香烟** - 点击香烟开始吸烟，烟头会发光燃烧
- **响应式设计** - 适配桌面和移动设备
- **中文界面** - 完全中文化，无英文内容

## 🛠️ 技术栈

### 后端
- **SpringBoot 3.2.0** - 主框架
- **Java 17** - 编程语言
- **Spring Data JPA** - 数据持久化
- **H2 数据库** - 内存数据库
- **Maven** - 项目管理

### 前端
- **Vue 3** - 前端框架
- **Vite** - 构建工具
- **Vue Router** - 路由管理
- **Pinia** - 状态管理
- **Axios** - HTTP客户端

## 📦 项目结构

```
jieyan/
├── src/                           # SpringBoot 后端
│   ├── main/
│   │   ├── java/
│   │   │   └── com/cyber/quitsmokingsimulator/
│   │   │       ├── QuitSmokingSimulatorApplication.java
│   │   │       ├── controller/
│   │   │       │   └── SmokingController.java
│   │   │       ├── model/
│   │   │       │   └── SmokingRecord.java
│   │   │       ├── service/
│   │   │       │   └── SmokingService.java
│   │   │       └── repository/
│   │   │           └── SmokingRecordRepository.java
│   │   └── resources/
│   │       └── application.yml
├── frontend/                      # Vue3 前端
│   ├── src/
│   │   ├── components/
│   │   ├── views/
│   │   │   └── CyberCigarette.vue
│   │   ├── router/
│   │   │   └── index.js
│   │   ├── main.js
│   │   ├── App.vue
│   │   └── style.css
│   ├── package.json
│   ├── vite.config.js
│   └── index.html
├── pom.xml
└── README.md
```

## 🚀 快速开始

### 环境要求
- Java 17+
- Node.js 16+
- Maven 3.6+

### 启动后端服务

1. 进入项目根目录：
```bash
cd jieyan
```

2. 启动 SpringBoot 应用：
```bash
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动

### 启动前端服务

1. 进入前端目录：
```bash
cd frontend
```

2. 安装依赖：
```bash
npm install
```

3. 启动开发服务器：
```bash
npm run dev
```

前端服务将在 `http://localhost:3000` 启动

### 🎨 一键启动脚本

Windows用户可以直接运行项目根目录下的启动脚本：

```bash
start-project.bat
```

该脚本会自动：
- 检查环境（Java、Maven、Node.js）
- 启动后端服务（SpringBoot）
- 启动前端服务（Vue3）
- 打开浏览器访问应用

## 🎮 如何使用

1. 打开浏览器访问 `http://localhost:3000`
2. 点击右上角的主题切换按钮可在赛博风格和传统风格之间切换
3. 点击香烟或点击"开始吸烟"/"体验吸烟"按钮开始模拟吸烟
4. 观察逼真的烟雾效果和燃烧动画
5. 查看右上角的吸烟统计数据
6. 可以点击"重置统计"/"清空记录"清零数据

## 🔧 API 接口

### 记录吸烟
- **POST** `/api/smoking/smoke`
- **Body**: 
```json
{
  "userAgent": "浏览器信息",
  "smokingIntensity": 7.5
}
```

### 获取统计数据
- **GET** `/api/smoking/stats`
- **Response**:
```json
{
  "todaySmokes": 5,
  "totalSmokes": 23
}
```

### 获取所有记录
- **GET** `/api/smoking/records`

## 🎨 界面特效

### 赛博朋克风格
- **霓虹灯文字效果** - 青色发光标题
- **网格背景** - 赛博朋克风格网格线
- **粒子动画** - 浮动的彩色粒子
- **毛玻璃效果** - 半透明面板

### 传统风格
- **温和色彩** - 温暖的棕色和米色配色
- **传统字体** - 宋体等传统中文字体
- **柔和阴影** - 温和的投影效果
- **简洁界面** - 适合中老年用户的简洁设计

### 通用特效
- **烟雾粒子系统** - 逼真的烟雾上升效果
- **燃烧动画** - 香烟头部发光燃烧效果
- **平滑过渡** - 主题切换时的平滑动画过渡

## 🎯 目标用户

这个项目适合：
- 想要戒烟的人群（通过虚拟体验替代真实吸烟）
- 中老年用户群体（传统风格界面，温和色彩）
- 年轻用户群体（赛博朋克风格，炫酷界面）
- 前端动画效果学习者
- Vue3 + SpringBoot 技术栈学习者

## 📱 移动端适配

项目已适配移动设备，在手机上同样可以享受完整的赛博朋克吸烟体验。

## 🔮 未来计划

- [ ] 添加更多烟雾效果
- [ ] 增加音效支持
- [ ] 添加用户系统
- [ ] 实现戒烟挑战功能
- [ ] 添加VR/AR支持

## 📄 许可证

MIT License

## 🤝 贡献

欢迎提交 Issues 和 Pull Requests！

---

**注意：本项目仅用于娱乐和学习目的，不鼓励真实吸烟行为。吸烟有害健康，请珍爱生命。** 