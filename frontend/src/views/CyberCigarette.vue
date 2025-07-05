<template>
  <div class="cyber-cigarette-container">
    <!-- 背景动画层 -->
    <div class="background-animation">
      <div class="matrix-rain"></div>
      <div class="cyber-grid"></div>
    </div>

    <!-- 主题切换按钮 -->
    <div class="theme-switcher">
      <button class="theme-btn" @click="toggleTheme" :class="{ 'active': currentTheme === 'cyber' }">
        {{ currentTheme === 'cyber' ? '传统版' : '赛博版' }}
      </button>
    </div>

    <!-- 标题 -->
    <div class="title-section">
      <h1 class="main-title" :class="titleClass">{{ themeConfig.title }}</h1>
      <p class="subtitle" :class="subtitleClass">{{ themeConfig.subtitle }}</p>
    </div>

    <!-- 统计面板 -->
    <div class="stats-dashboard" :class="{ 'traditional-dashboard': currentTheme === 'traditional' }">
      <div class="stat-card" :class="{ 'traditional-card': currentTheme === 'traditional' }">
        <div class="stat-icon">🚬</div>
        <div class="stat-value" :class="{ 'neon-text': currentTheme === 'cyber', 'traditional-value': currentTheme === 'traditional' }">{{ stats.todaySmokes }}</div>
        <div class="stat-label">今日吸烟</div>
      </div>
      <div class="stat-card" :class="{ 'traditional-card': currentTheme === 'traditional' }">
        <div class="stat-icon">📊</div>
        <div class="stat-value" :class="{ 'neon-text': currentTheme === 'cyber', 'traditional-value': currentTheme === 'traditional' }">{{ stats.totalSmokes }}</div>
        <div class="stat-label">总计</div>
      </div>
    </div>

    <!-- 主要香烟区域 -->
    <div class="cigarette-stage">
      <div class="hologram-frame">
        <div class="cigarette-holder" @click="smoke">
          <!-- 香烟主体 -->
          <div class="cigarette" :class="{ 'smoking': isSmoking, 'burning': isSmoking }">
            <!-- 烟头发光效果 -->
            <div class="cigarette-tip" :class="{ 'burning': isSmoking }">
              <div class="fire-core" v-if="isSmoking"></div>
              <div class="fire-glow" v-if="isSmoking"></div>
            </div>
            <!-- 烟身 -->
            <div class="cigarette-body">
              <div class="cigarette-paper"></div>
              <div class="cigarette-tobacco"></div>
              <!-- 灰烬层 -->
              <div class="cigarette-ash" :style="{ height: ashProgress + '%' }"></div>
            </div>
            <!-- 过滤嘴 -->
            <div class="cigarette-filter">
              <div class="filter-pattern"></div>
            </div>
          </div>
          
          <!-- 超级烟雾效果 -->
          <div class="mega-smoke-container" v-if="isSmoking">
            <div class="smoke-stream" 
                 v-for="(stream, index) in smokeStreams" 
                 :key="'stream-' + index"
                 :style="stream.style"
                 :class="stream.class">
            </div>
            <div class="smoke-particle" 
                 v-for="(particle, index) in smokeParticles" 
                 :key="'particle-' + index"
                 :style="particle.style"
                 :class="particle.class">
            </div>
            <div class="smoke-wisp" 
                 v-for="(wisp, index) in smokeWisps" 
                 :key="'wisp-' + index"
                 :style="wisp.style"
                 :class="wisp.class">
            </div>
          </div>
        </div>
      </div>
      
      <!-- 互动提示 -->
      <div class="interaction-hint" v-if="!isSmoking">
        <div class="hint-text" :class="hintClass">{{ themeConfig.hintText }}</div>
        <div class="click-indicator" :class="indicatorClass">
          <div class="ripple"></div>
          <div class="ripple"></div>
          <div class="ripple"></div>
        </div>
      </div>


    </div>

    <!-- 控制面板 -->
    <div class="control-panel">
      <button class="control-button primary" :class="buttonClass" @click="smoke" :disabled="isSmoking">
        <span class="button-text">{{ isSmoking ? themeConfig.smokingText : themeConfig.startText }}</span>
        <div class="button-glow" v-if="currentTheme === 'cyber'"></div>
      </button>
      <button class="control-button secondary" :class="buttonClass" @click="resetStats">
        <span class="button-text">{{ themeConfig.resetText }}</span>
        <div class="button-glow" v-if="currentTheme === 'cyber'"></div>
      </button>
    </div>


  </div>
</template>

<script>
import { ref, reactive, computed, onMounted, onUnmounted } from 'vue'
import axios from 'axios'

export default {
  name: 'CyberCigarette',
  setup() {
    const isSmoking = ref(false)
    const smokeParticles = ref([])
    const smokeStreams = ref([])
    const smokeWisps = ref([])
    const currentTheme = ref('cyber')
    const ashProgress = ref(0) // 灰烬进度 0-100%

    const stats = reactive({
      todaySmokes: 0,
      totalSmokes: 0
    })
    
    let smokeInterval = null
    let streamInterval = null
    let wispInterval = null
    let animationFrame = null
    let smokingDuration = 15 // 吸烟总时长（秒）

    // 主题配置
    const themes = {
      cyber: {
        title: '赛博戒烟模拟器',
        subtitle: '未来科技体验',
        hintText: '点击香烟开始模拟吸烟',
        startText: '开始吸烟',
        smokingText: '吸烟中...',
        resetText: '重置统计'
      },
      traditional: {
        title: '戒烟助手',
        subtitle: '健康生活从这里开始',
        hintText: '点击香烟体验吸烟感受',
        startText: '体验吸烟',
        smokingText: '体验中...',
        resetText: '清空记录'
      }
    }

    // 当前主题配置
    const themeConfig = computed(() => themes[currentTheme.value])

    // 样式类计算
    const titleClass = computed(() => ({
      'neon-text glitch': currentTheme.value === 'cyber',
      'traditional-title': currentTheme.value === 'traditional'
    }))

    const subtitleClass = computed(() => ({
      'typing-effect': currentTheme.value === 'cyber',
      'traditional-subtitle': currentTheme.value === 'traditional'
    }))

    const hintClass = computed(() => ({
      'neon-text pulse': currentTheme.value === 'cyber',
      'traditional-hint': currentTheme.value === 'traditional'
    }))

    const indicatorClass = computed(() => ({
      'cyber-indicator': currentTheme.value === 'cyber',
      'traditional-indicator': currentTheme.value === 'traditional'
    }))

    const buttonClass = computed(() => ({
      'cyber-button': currentTheme.value === 'cyber',
      'traditional-button': currentTheme.value === 'traditional'
    }))



    // 主题切换函数
    const toggleTheme = () => {
      currentTheme.value = currentTheme.value === 'cyber' ? 'traditional' : 'cyber'
      document.body.className = `theme-${currentTheme.value}`
    }

    // 创建烟雾粒子
    const createSmokeParticle = () => {
      const particle = {
        id: Date.now() + Math.random(),
        style: {
          left: `${50 + Math.random() * 15 - 7.5}%`,
          bottom: '90%',
          transform: `rotate(${Math.random() * 360}deg)`,
          animationDelay: `${Math.random() * 0.5}s`,
          animationDuration: `${2 + Math.random() * 3}s`,
          opacity: Math.random() * 0.7 + 0.3
        },
        class: `smoke-particle-${Math.floor(Math.random() * 5) + 1}`
      }
      return particle
    }

    // 创建烟雾流
    const createSmokeStream = () => {
      const stream = {
        id: Date.now() + Math.random(),
        style: {
          left: `${48 + Math.random() * 4}%`,
          bottom: '88%',
          transform: `rotate(${Math.random() * 20 - 10}deg)`,
          animationDelay: `${Math.random() * 0.3}s`,
          animationDuration: `${3 + Math.random() * 2}s`,
          opacity: Math.random() * 0.8 + 0.2
        },
        class: `smoke-stream-${Math.floor(Math.random() * 3) + 1}`
      }
      return stream
    }

    // 创建烟雾缕
    const createSmokeWisp = () => {
      const wisp = {
        id: Date.now() + Math.random(),
        style: {
          left: `${47 + Math.random() * 6}%`,
          bottom: '85%',
          transform: `rotate(${Math.random() * 360}deg)`,
          animationDelay: `${Math.random() * 1}s`,
          animationDuration: `${4 + Math.random() * 3}s`,
          opacity: Math.random() * 0.6 + 0.4
        },
        class: `smoke-wisp-${Math.floor(Math.random() * 4) + 1}`
      }
      return wisp
    }

    // 开始吸烟
    const smoke = async () => {
      if (isSmoking.value) return
      
      isSmoking.value = true
      ashProgress.value = 0
      smokeParticles.value = []
      smokeStreams.value = []
      smokeWisps.value = []
      
      // 创建烟雾粒子效果
      smokeInterval = setInterval(() => {
        const newParticles = []
        for (let i = 0; i < 8; i++) {
          newParticles.push(createSmokeParticle())
        }
        smokeParticles.value = [...smokeParticles.value, ...newParticles]
        
        // 限制粒子数量
        if (smokeParticles.value.length > 50) {
          smokeParticles.value = smokeParticles.value.slice(-50)
        }
      }, 150)

      // 创建烟雾流效果
      streamInterval = setInterval(() => {
        const newStreams = []
        for (let i = 0; i < 3; i++) {
          newStreams.push(createSmokeStream())
        }
        smokeStreams.value = [...smokeStreams.value, ...newStreams]
        
        // 限制流数量
        if (smokeStreams.value.length > 20) {
          smokeStreams.value = smokeStreams.value.slice(-20)
        }
      }, 300)

      // 创建烟雾缕效果
      wispInterval = setInterval(() => {
        const newWisps = []
        for (let i = 0; i < 2; i++) {
          newWisps.push(createSmokeWisp())
        }
        smokeWisps.value = [...smokeWisps.value, ...newWisps]
        
        // 限制缕数量
        if (smokeWisps.value.length > 15) {
          smokeWisps.value = smokeWisps.value.slice(-15)
        }
      }, 500)

      // 灰烬进度更新
      const startTime = Date.now()
      const ashInterval = setInterval(() => {
        const elapsed = (Date.now() - startTime) / 1000
        const progress = Math.min((elapsed / smokingDuration) * 100, 100)
        
        ashProgress.value = progress
        
        if (progress >= 100) {
          clearInterval(ashInterval)
        }
      }, 200)
      
      // 记录吸烟数据
      try {
        await axios.post('/api/smoking/smoke', {
          userAgent: navigator.userAgent,
          smokingIntensity: Math.random() * 10 + 5
        })
      } catch (error) {
        console.error('记录吸烟数据失败:', error)
      }
      
      // 15秒后停止吸烟
      setTimeout(() => {
        isSmoking.value = false
        ashProgress.value = 0
        smokeParticles.value = []
        smokeStreams.value = []
        smokeWisps.value = []
        
        if (smokeInterval) {
          clearInterval(smokeInterval)
          smokeInterval = null
        }
        if (streamInterval) {
          clearInterval(streamInterval)
          streamInterval = null
        }
        if (wispInterval) {
          clearInterval(wispInterval)
          wispInterval = null
        }
        loadStats()
      }, smokingDuration * 1000)
    }

    // 加载统计数据
    const loadStats = async () => {
      try {
        const response = await axios.get('/api/smoking/stats')
        stats.todaySmokes = response.data.todaySmokes
        stats.totalSmokes = response.data.totalSmokes
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    }

    // 重置统计
    const resetStats = () => {
      stats.todaySmokes = 0
      stats.totalSmokes = 0
    }

    onMounted(() => {
      loadStats()
    })

    onUnmounted(() => {
      if (smokeInterval) {
        clearInterval(smokeInterval)
      }
      if (streamInterval) {
        clearInterval(streamInterval)
      }
      if (wispInterval) {
        clearInterval(wispInterval)
      }

      if (animationFrame) {
        cancelAnimationFrame(animationFrame)
      }
    })

    return {
      isSmoking,
      smokeParticles,
      smokeStreams,
      smokeWisps,
      stats,
      currentTheme,
      themeConfig,
      ashProgress,
      titleClass,
      subtitleClass,
      hintClass,
      indicatorClass,
      buttonClass,
      smoke,
      resetStats,
      toggleTheme
    }
  }
}
</script>

<style scoped>
/* 主题切换按钮 */
.theme-switcher {
  position: absolute;
  top: 20px;
  right: 20px;
  z-index: 1000;
}

.theme-btn {
  padding: 12px 24px;
  background: rgba(0, 255, 255, 0.1);
  border: 2px solid #00ffff;
  border-radius: 25px;
  color: #00ffff;
  font-family: 'Orbitron', monospace;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.theme-btn:hover {
  background: rgba(0, 255, 255, 0.2);
  box-shadow: 0 0 20px rgba(0, 255, 255, 0.5);
  transform: translateY(-2px);
}

.theme-btn.active {
  background: linear-gradient(45deg, #00ffff, #ff00ff);
  color: #000;
  box-shadow: 0 0 30px rgba(0, 255, 255, 0.8);
}

/* 传统主题样式 */
:global(.theme-traditional) {
  background: linear-gradient(135deg, #f9f7f4 0%, #f0ede8 50%, #e8e2d7 100%);
  color: #2d2d2d;
}

:global(.theme-traditional) .cyber-cigarette-container {
  background: linear-gradient(135deg, #f9f7f4 0%, #f0ede8 50%, #e8e2d7 100%);
  color: #2d2d2d;
  font-family: 'Microsoft YaHei', '微软雅黑', sans-serif;
}

:global(.theme-traditional) .theme-btn {
  background: rgba(139, 69, 19, 0.15);
  border: 2px solid #8b4513;
  color: #8b4513;
  font-weight: 600;
}

:global(.theme-traditional) .theme-btn:hover {
  background: rgba(139, 69, 19, 0.3);
  box-shadow: 0 0 20px rgba(139, 69, 19, 0.4);
}

/* 传统风格标题 */
.traditional-title {
  color: #2d2d2d !important;
  font-family: 'Microsoft YaHei', '微软雅黑', sans-serif;
  font-size: 3.5rem;
  font-weight: 700;
  text-shadow: 2px 2px 6px rgba(139, 69, 19, 0.3);
  margin-bottom: 20px;
}

.traditional-subtitle {
  color: #8b4513 !important;
  font-family: 'Microsoft YaHei', '微软雅黑', sans-serif;
  font-size: 1.4rem;
  font-weight: 500;
  text-shadow: 1px 1px 3px rgba(139, 69, 19, 0.2);
  margin-bottom: 10px;
}

/* 传统风格统计面板 */
.traditional-dashboard {
  background: rgba(255, 255, 255, 0.95) !important;
  border: 2px solid #8b4513 !important;
  border-radius: 15px;
  box-shadow: 0 8px 25px rgba(139, 69, 19, 0.2);
}

.traditional-card {
  background: rgba(255, 255, 255, 0.9) !important;
  border: 2px solid #cd853f !important;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(139, 69, 19, 0.15);
  padding: 25px !important;
  margin: 5px !important;
}

.traditional-card:hover {
  background: rgba(255, 255, 255, 1) !important;
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(139, 69, 19, 0.25);
}

.traditional-value {
  color: #2d2d2d !important;
  font-family: 'Microsoft YaHei', '微软雅黑', sans-serif;
  font-weight: 700;
  font-size: 2.8rem !important;
  text-shadow: 1px 1px 3px rgba(139, 69, 19, 0.2);
}

/* 传统风格提示 */
.traditional-hint {
  color: #2d2d2d !important;
  font-family: 'Microsoft YaHei', '微软雅黑', sans-serif;
  font-size: 1.3rem;
  font-weight: 600;
  text-shadow: 1px 1px 3px rgba(139, 69, 19, 0.2);
  margin-bottom: 30px;
}

/* 传统风格按钮 */
.traditional-button {
  background: linear-gradient(45deg, #daa520, #b8860b) !important;
  border: 2px solid #8b4513 !important;
  color: white !important;
  font-family: 'Microsoft YaHei', '微软雅黑', sans-serif;
  font-weight: 600;
  font-size: 1.1rem;
  border-radius: 25px;
  box-shadow: 0 6px 20px rgba(139, 69, 19, 0.3);
  padding: 15px 30px !important;
  margin: 10px !important;
}

.traditional-button:hover {
  background: linear-gradient(45deg, #b8860b, #daa520) !important;
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(139, 69, 19, 0.4);
}

.traditional-button:disabled {
  background: #ccc !important;
  border-color: #999 !important;
  color: #666 !important;
  cursor: not-allowed;
}

/* 传统风格底部信息 */
.traditional-footer {
  background: rgba(255, 255, 255, 0.9) !important;
  border: 2px solid #cd853f !important;
  border-radius: 12px;
  color: #2d2d2d !important;
  font-family: 'Microsoft YaHei', '微软雅黑', sans-serif;
  backdrop-filter: blur(5px);
  padding: 20px !important;
  margin-top: 30px !important;
}

.traditional-footer .footer-text {
  color: #2d2d2d !important;
  font-weight: 600;
  font-size: 1.1rem;
}

.traditional-footer .footer-subtext {
  color: #8b4513 !important;
  font-size: 1rem;
  margin-top: 8px;
}

/* 传统风格指示器 */
.traditional-indicator .ripple {
  border: 2px solid #8b4513 !important;
  background: rgba(139, 69, 19, 0.15) !important;
}

@keyframes traditionalPulse {
  0% { transform: scale(0.9); opacity: 0.8; }
  50% { transform: scale(1.1); opacity: 0.6; }
  100% { transform: scale(0.9); opacity: 0.8; }
}

.traditional-indicator .ripple:nth-child(1) {
  animation: traditionalPulse 2s infinite;
}

.traditional-indicator .ripple:nth-child(2) {
  animation: traditionalPulse 2s infinite 0.5s;
}

.traditional-indicator .ripple:nth-child(3) {
  animation: traditionalPulse 2s infinite 1s;
}



.cyber-cigarette-container {
  position: relative;
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #0a0a0a 0%, #1a1a2e 50%, #16213e 100%);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  transition: all 0.8s ease;
}

/* 背景动画 */
.background-animation {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  opacity: 0.3;
}

.cyber-grid {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    linear-gradient(rgba(0, 255, 255, 0.1) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0, 255, 255, 0.1) 1px, transparent 1px);
  background-size: 50px 50px;
  animation: gridMove 20s linear infinite;
}

@keyframes gridMove {
  0% { transform: translate(0, 0); }
  100% { transform: translate(50px, 50px); }
}

.title-section {
  position: absolute;
  top: 40px;
  left: 50%;
  transform: translateX(-50%);
  text-align: center;
  z-index: 10;
}

/* 传统主题下的标题区域 */
:global(.theme-traditional) .title-section {
  top: 60px;
}

.main-title {
  font-size: 3.5rem;
  font-weight: 900;
  color: var(--cyber-cyan);
  margin-bottom: 10px;
  text-transform: uppercase;
  letter-spacing: 8px;
  text-shadow: 
    0 0 5px var(--cyber-cyan),
    0 0 10px var(--cyber-cyan),
    0 0 15px var(--cyber-cyan),
    0 0 20px var(--cyber-cyan);
}

.subtitle {
  font-size: 1.2rem;
  color: var(--cyber-pink);
  font-weight: 400;
  letter-spacing: 4px;
  opacity: 0.8;
}

.typing-effect {
  overflow: hidden;
  border-right: 3px solid var(--cyber-pink);
  animation: typing 4s steps(40, end), blink 0.75s step-end infinite;
}

@keyframes typing {
  from { width: 0; }
  to { width: 100%; }
}

@keyframes blink {
  from, to { border-color: transparent; }
  50% { border-color: var(--cyber-pink); }
}

.stats-dashboard {
  position: absolute;
  top: 30px;
  right: 30px;
  display: flex;
  flex-direction: column;
  gap: 15px;
  z-index: 10;
}

.stat-card {
  background: rgba(0, 0, 0, 0.7);
  backdrop-filter: blur(10px);
  border: 2px solid var(--cyber-cyan);
  border-radius: 15px;
  padding: 20px;
  text-align: center;
  min-width: 120px;
  box-shadow: 0 0 20px rgba(0, 255, 255, 0.3);
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 25px rgba(0, 255, 255, 0.5);
}

.stat-icon {
  font-size: 2rem;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 2.5rem;
  font-weight: 900;
  color: var(--cyber-cyan);
  text-shadow: 0 0 10px var(--cyber-cyan);
}

.stat-label {
  font-size: 0.9rem;
  color: var(--cyber-pink);
  margin-top: 5px;
}

.cigarette-stage {
  position: relative;
  z-index: 5;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 40px;
}

.hologram-frame {
  position: relative;
  padding: 50px;
  border: 3px solid var(--cyber-cyan);
  border-radius: 20px;
  background: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(5px);
  box-shadow: 
    0 0 30px rgba(0, 255, 255, 0.5),
    inset 0 0 30px rgba(0, 255, 255, 0.1);
}

.cigarette-holder {
  position: relative;
  cursor: pointer;
  transition: all 0.3s ease;
}

.cigarette-holder:hover {
  transform: scale(1.05);
}

.cigarette {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 80px;
  height: 600px;
  transition: all 0.3s ease;
  filter: drop-shadow(0 0 20px rgba(255, 255, 255, 0.3));
}

.cigarette.smoking {
  filter: brightness(1.3) drop-shadow(0 0 30px rgba(255, 100, 0, 0.8));
}

.cigarette-tip {
  width: 80px;
  height: 30px;
  background: linear-gradient(180deg, #ff4000, #ff6000);
  border-radius: 50% 50% 0 0;
  position: relative;
  transition: all 0.3s ease;
  box-shadow: 0 0 10px rgba(255, 64, 0, 0.5);
  order: 1;
}

.cigarette-tip.burning {
  background: linear-gradient(180deg, #ff0000, #ff4000, #ffff00);
  box-shadow: 
    0 0 30px #ff4000,
    0 0 50px #ff4000,
    0 0 70px #ff4000,
    0 0 90px #ff2000;
  animation: burning 0.5s infinite alternate;
}

.fire-core {
  position: absolute;
  width: 12px;
  height: 12px;
  background: #ffffff;
  border-radius: 50%;
  top: 30%;
  left: 50%;
  transform: translate(-50%, -50%);
  box-shadow: 0 0 15px #ffffff;
  animation: fireCore 0.3s infinite alternate;
}

.fire-glow {
  position: absolute;
  width: 40px;
  height: 40px;
  background: radial-gradient(circle, rgba(255, 255, 0, 0.8), transparent);
  border-radius: 50%;
  top: 20%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: fireGlow 0.7s infinite alternate;
}

@keyframes burning {
  0% { transform: scale(1); }
  100% { transform: scale(1.1); }
}

@keyframes fireCore {
  0% { opacity: 0.8; }
  100% { opacity: 1; }
}

@keyframes fireGlow {
  0% { opacity: 0.6; transform: translate(-50%, -50%) scale(0.8); }
  100% { opacity: 1; transform: translate(-50%, -50%) scale(1.2); }
}

.cigarette-body {
  width: 80px;
  height: 460px;
  position: relative;
  background: linear-gradient(180deg, #f5f5f5, #e8e8e8);
  border-left: 3px solid #d0d0d0;
  border-right: 3px solid #d0d0d0;
  order: 2;
}

.cigarette-paper {
  position: absolute;
  width: 100%;
  height: 100%;
  background: linear-gradient(180deg, #f8f8f8, #f0f0f0);
}

.cigarette-tobacco {
  position: absolute;
  top: 0;
  left: 3px;
  width: calc(100% - 6px);
  height: 100%;
  background: linear-gradient(180deg, #d4a574, #c49660);
  opacity: 0.3;
}

/* 灰烬层 */
.cigarette-ash {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  background: linear-gradient(180deg, #ddd 0%, #ccc 30%, #bbb 70%, #999 100%);
  border-radius: 0;
  transition: height 0.5s ease;
  z-index: 5;
  box-shadow: 
    inset 0 2px 4px rgba(0, 0, 0, 0.1),
    0 0 8px rgba(153, 153, 153, 0.3);
}

.cigarette-ash::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, #888, #aaa, #888);
  animation: ashGlow 2s infinite alternate;
}

.cigarette-ash::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 90%;
  height: 90%;
  background: repeating-linear-gradient(
    45deg,
    transparent,
    transparent 1px,
    rgba(136, 136, 136, 0.2) 1px,
    rgba(136, 136, 136, 0.2) 2px
  );
}

@keyframes ashGlow {
  0% { opacity: 0.3; }
  100% { opacity: 0.7; }
}

.cigarette-filter {
  width: 80px;
  height: 110px;
  background: linear-gradient(180deg, #8B4513, #A0522D);
  border-radius: 0 0 50% 50%;
  border: 3px solid #654321;
  position: relative;
  box-shadow: inset 0 0 10px rgba(0, 0, 0, 0.3);
  order: 3;
}

.filter-pattern {
  position: absolute;
  width: 100%;
  height: 100%;
  background: repeating-linear-gradient(
    180deg,
    transparent,
    transparent 3px,
    rgba(101, 67, 33, 0.3) 3px,
    rgba(101, 67, 33, 0.3) 6px
  );
}

.mega-smoke-container {
  position: absolute;
  top: -120px;
  left: 50%;
  transform: translateX(-50%);
  width: 300px;
  height: 500px;
  pointer-events: none;
  z-index: 10;
}

/* 烟雾流 */
.smoke-stream {
  position: absolute;
  width: 60px;
  height: 120px;
  background: linear-gradient(180deg, 
    rgba(255,255,255,0.9) 0%,
    rgba(240,240,240,0.7) 30%,
    rgba(220,220,220,0.5) 60%,
    transparent 100%);
  border-radius: 50%;
  animation: smokeStreamRise 5s ease-out forwards;
}

.smoke-stream-1 {
  background: linear-gradient(180deg, 
    rgba(255,255,255,0.95) 0%,
    rgba(245,245,245,0.8) 25%,
    rgba(230,230,230,0.6) 50%,
    rgba(200,200,200,0.4) 75%,
    transparent 100%);
}

.smoke-stream-2 {
  background: linear-gradient(180deg, 
    rgba(250,250,250,0.9) 0%,
    rgba(235,235,235,0.7) 30%,
    rgba(210,210,210,0.5) 60%,
    transparent 100%);
}

.smoke-stream-3 {
  background: linear-gradient(180deg, 
    rgba(240,240,240,0.85) 0%,
    rgba(225,225,225,0.65) 35%,
    rgba(195,195,195,0.45) 65%,
    transparent 100%);
}

@keyframes smokeStreamRise {
  0% {
    transform: translateY(0) scale(0.3) rotate(0deg);
    opacity: 0.9;
  }
  30% {
    transform: translateY(-120px) scale(0.8) rotate(15deg);
    opacity: 0.7;
  }
  60% {
    transform: translateY(-240px) scale(1.2) rotate(30deg);
    opacity: 0.5;
  }
  100% {
    transform: translateY(-360px) scale(1.8) rotate(45deg);
    opacity: 0;
  }
}

/* 烟雾粒子 */
.smoke-particle {
  position: absolute;
  width: 25px;
  height: 25px;
  background: radial-gradient(circle, rgba(255,255,255,0.8) 0%, rgba(200,200,200,0.6) 50%, transparent 100%);
  border-radius: 50%;
  animation: smokeParticleRise 6s ease-out forwards;
}

.smoke-particle-1 {
  background: radial-gradient(circle, rgba(255,255,255,0.9) 0%, rgba(230,230,230,0.7) 40%, transparent 100%);
  filter: blur(0.5px);
}

.smoke-particle-2 {
  background: radial-gradient(circle, rgba(245,245,245,0.85) 0%, rgba(210,210,210,0.65) 45%, transparent 100%);
  filter: blur(0.8px);
}

.smoke-particle-3 {
  background: radial-gradient(circle, rgba(235,235,235,0.8) 0%, rgba(195,195,195,0.6) 50%, transparent 100%);
  filter: blur(1px);
}

.smoke-particle-4 {
  background: radial-gradient(circle, rgba(250,250,250,0.75) 0%, rgba(215,215,215,0.55) 55%, transparent 100%);
  filter: blur(1.2px);
}

.smoke-particle-5 {
  background: radial-gradient(circle, rgba(240,240,240,0.7) 0%, rgba(185,185,185,0.5) 60%, transparent 100%);
  filter: blur(1.5px);
}

@keyframes smokeParticleRise {
  0% {
    transform: translateY(0) scale(0.4) rotate(0deg);
    opacity: 0.8;
  }
  25% {
    transform: translateY(-80px) scale(0.7) rotate(90deg);
    opacity: 0.7;
  }
  50% {
    transform: translateY(-160px) scale(1) rotate(180deg);
    opacity: 0.6;
  }
  75% {
    transform: translateY(-240px) scale(1.3) rotate(270deg);
    opacity: 0.4;
  }
  100% {
    transform: translateY(-320px) scale(1.6) rotate(360deg);
    opacity: 0;
  }
}

/* 烟雾缕 */
.smoke-wisp {
  position: absolute;
  width: 40px;
  height: 80px;
  background: radial-gradient(ellipse, rgba(255,255,255,0.6) 0%, transparent 100%);
  border-radius: 50%;
  animation: smokeWispRise 7s ease-out forwards;
}

.smoke-wisp-1 {
  background: radial-gradient(ellipse, rgba(255,255,255,0.7) 0%, rgba(240,240,240,0.4) 40%, transparent 100%);
}

.smoke-wisp-2 {
  background: radial-gradient(ellipse, rgba(250,250,250,0.65) 0%, rgba(220,220,220,0.35) 45%, transparent 100%);
}

.smoke-wisp-3 {
  background: radial-gradient(ellipse, rgba(245,245,245,0.6) 0%, rgba(200,200,200,0.3) 50%, transparent 100%);
}

.smoke-wisp-4 {
  background: radial-gradient(ellipse, rgba(235,235,235,0.55) 0%, rgba(180,180,180,0.25) 55%, transparent 100%);
}

@keyframes smokeWispRise {
  0% {
    transform: translateY(0) scale(0.5) rotate(0deg) skew(0deg);
    opacity: 0.7;
  }
  20% {
    transform: translateY(-80px) scale(0.8) rotate(30deg) skew(10deg);
    opacity: 0.6;
  }
  40% {
    transform: translateY(-160px) scale(1.1) rotate(60deg) skew(20deg);
    opacity: 0.5;
  }
  60% {
    transform: translateY(-240px) scale(1.4) rotate(90deg) skew(30deg);
    opacity: 0.4;
  }
  80% {
    transform: translateY(-320px) scale(1.7) rotate(120deg) skew(40deg);
    opacity: 0.2;
  }
  100% {
    transform: translateY(-400px) scale(2) rotate(150deg) skew(50deg);
    opacity: 0;
  }
}

.interaction-hint {
  text-align: center;
  position: relative;
  z-index: 10;
}

.hint-text {
  font-size: 1.5rem;
  color: var(--cyber-cyan);
  margin-bottom: 20px;
  text-shadow: 0 0 10px var(--cyber-cyan);
  font-weight: 600;
}

.pulse {
  animation: textPulse 2s infinite;
}

@keyframes textPulse {
  0%, 100% {
    opacity: 0.6;
    transform: scale(1);
  }
  50% {
    opacity: 1;
    transform: scale(1.05);
  }
}

.click-indicator {
  position: relative;
  width: 60px;
  height: 60px;
  margin: 0 auto;
}

.ripple {
  position: absolute;
  width: 100%;
  height: 100%;
  border: 3px solid var(--cyber-cyan);
  border-radius: 50%;
  animation: rippleEffect 2s infinite;
}

.ripple:nth-child(2) {
  animation-delay: 0.7s;
}

.ripple:nth-child(3) {
  animation-delay: 1.4s;
}

@keyframes rippleEffect {
  0% {
    transform: scale(0.5);
    opacity: 1;
  }
  50% {
    transform: scale(1);
    opacity: 0.7;
  }
  100% {
    transform: scale(1.5);
    opacity: 0;
  }
}

.control-panel {
  position: absolute;
  bottom: 60px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 30px;
  z-index: 10;
}

/* 传统主题下的控制面板 */
:global(.theme-traditional) .control-panel {
  bottom: 80px;
  gap: 40px;
}

.cyber-button {
  position: relative;
  background: transparent;
  border: 3px solid var(--cyber-cyan);
  color: var(--cyber-cyan);
  padding: 15px 30px;
  font-family: 'Orbitron', monospace;
  font-weight: 700;
  font-size: 1.1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  text-transform: uppercase;
  letter-spacing: 2px;
  border-radius: 10px;
  overflow: hidden;
  min-width: 180px;
}

.cyber-button.primary {
  border-color: var(--cyber-cyan);
  color: var(--cyber-cyan);
}

.cyber-button.secondary {
  border-color: var(--cyber-pink);
  color: var(--cyber-pink);
}

.cyber-button:hover:not(:disabled) {
  background: var(--cyber-cyan);
  color: var(--dark-bg);
  box-shadow: 
    0 0 20px var(--cyber-cyan),
    0 0 40px var(--cyber-cyan),
    0 0 60px var(--cyber-cyan);
  transform: translateY(-3px);
}

.cyber-button.secondary:hover:not(:disabled) {
  background: var(--cyber-pink);
  color: var(--dark-bg);
  box-shadow: 
    0 0 20px var(--cyber-pink),
    0 0 40px var(--cyber-pink),
    0 0 60px var(--cyber-pink);
}

.cyber-button:disabled {
  opacity: 0.4;
  cursor: not-allowed;
  animation: disabledPulse 2s infinite;
}

@keyframes disabledPulse {
  0%, 100% { opacity: 0.4; }
  50% { opacity: 0.6; }
}

.button-text {
  position: relative;
  z-index: 2;
}

.button-glow {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, transparent, rgba(0, 255, 255, 0.1), transparent);
  transform: translateX(-100%);
  transition: transform 0.6s;
}

.cyber-button:hover .button-glow {
  transform: translateX(100%);
}

.info-footer {
  position: absolute;
  bottom: 15px;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(0, 255, 255, 0.3);
  border-radius: 8px;
  padding: 8px 16px;
  z-index: 10;
  text-align: center;
}

.footer-text {
  color: var(--cyber-cyan);
  font-size: 0.7rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 1px;
  margin-bottom: 2px;
  opacity: 0.8;
}

.footer-subtext {
  color: var(--cyber-pink);
  font-size: 0.6rem;
  font-weight: 400;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  opacity: 0.6;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main-title {
    font-size: 2.5rem;
    letter-spacing: 4px;
  }
  
  .subtitle {
    font-size: 1rem;
    letter-spacing: 2px;
  }
  
  .cigarette {
    width: 60px;
    height: 450px;
  }
  
  .cigarette-tip {
    width: 60px;
    height: 25px;
  }
  
  .cigarette-body {
    width: 60px;
    height: 345px;
  }
  
  .cigarette-filter {
    width: 60px;
    height: 80px;
  }
  
  .stats-dashboard {
    top: 20px;
    right: 20px;
    gap: 10px;
  }
  
  .stat-card {
    padding: 15px;
    min-width: 100px;
  }
  
  .stat-icon {
    font-size: 1.5rem;
  }
  
  .stat-value {
    font-size: 2rem;
  }
  
  .control-panel {
    bottom: 40px;
    flex-direction: row;
    gap: 20px;
  }
  
  .cyber-button {
    min-width: 140px;
    padding: 12px 20px;
    font-size: 0.9rem;
  }
  
  .hologram-frame {
    padding: 30px;
  }
  
  .info-footer {
    bottom: 8px;
    padding: 6px 12px;
  }
  
  .footer-text {
    font-size: 0.65rem;
  }


  
  .footer-subtext {
    font-size: 0.55rem;
  }
  
  .title-section {
    top: 30px;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main-title {
    font-size: 2rem;
  }
  
  .cigarette {
    width: 250px;
    height: 18px;
  }
  
  .cigarette-body {
    width: 180px;
    height: 18px;
  }
  
  .cigarette-filter {
    width: 60px;
    height: 18px;
  }
  
  .stats-panel {
    position: relative;
    top: auto;
    right: auto;
    margin: 20px 0;
  }
  
  .control-panel {
    position: relative;
    bottom: auto;
    left: auto;
    transform: none;
    margin-top: 30px;
  }
}
</style> 