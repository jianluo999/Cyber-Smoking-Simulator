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

    <!-- 左侧小卖部 -->
    <div class="shop-panel left-panel">
      <div class="panel-header">
        <h3>小卖部</h3>
        <button @click="toggleShop" class="shop-btn">
          {{ shop.isOpen ? '关闭' : '购买香烟' }}
        </button>
      </div>
      <div class="shop-money">
        <span>💰 金钱: ¥{{ economy.money }}</span>
      </div>
      <div class="shop-stock">
        <span>🚬 香烟库存: {{ economy.cigaretteStock }}支</span>
      </div>
      <div class="shop-content" v-if="shop.isOpen">
        <div class="shop-item" v-for="item in shop.items" :key="item.id">
          <div class="item-info">
            <h4>{{ item.name }}</h4>
            <p>{{ item.description }}</p>
            <span class="price">¥{{ item.price }}</span>
            <span class="quantity">库存: {{ item.quantity }}</span>
          </div>
          <button @click="buyItem(item)" 
                  :disabled="economy.money < item.price || item.quantity <= 0"
                  class="buy-btn">
            购买
          </button>
        </div>
      </div>
    </div>

    <!-- 右侧健康面板 -->
    <div class="health-panel right-panel">
      <div class="panel-header">
        <h3>健康状况</h3>
        <div class="health-warning" v-if="health.lungHealth < 50">
          ⚠️ 健康危险
        </div>
      </div>
      <div class="health-stats">
        <div class="health-item" :class="{ 'critical-health': health.lungHealth < 30 }">
          <span class="health-label">🫁 肺部健康</span>
          <div class="health-bar">
            <div class="health-fill lung" :style="{ width: health.lungHealth + '%' }" 
                 :class="{ 'critical': health.lungHealth < 30 }"></div>
          </div>
          <span class="health-value" :class="{ 'critical-text': health.lungHealth < 30 }">{{ Math.round(health.lungHealth) }}%</span>
        </div>
        <div class="health-item" :class="{ 'critical-health': health.heartHealth < 30 }">
          <span class="health-label">❤️ 心脏健康</span>
          <div class="health-bar">
            <div class="health-fill heart" :style="{ width: health.heartHealth + '%' }"
                 :class="{ 'critical': health.heartHealth < 30 }"></div>
          </div>
          <span class="health-value" :class="{ 'critical-text': health.heartHealth < 30 }">{{ Math.round(health.heartHealth) }}%</span>
        </div>
        <div class="health-item" :class="{ 'critical-health': health.liverHealth < 30 }">
          <span class="health-label">🫀 肝脏健康</span>
          <div class="health-bar">
            <div class="health-fill liver" :style="{ width: health.liverHealth + '%' }"
                 :class="{ 'critical': health.liverHealth < 30 }"></div>
          </div>
          <span class="health-value" :class="{ 'critical-text': health.liverHealth < 30 }">{{ Math.round(health.liverHealth) }}%</span>
        </div>
        <div class="health-item">
          <span class="health-label">🩸 血压</span>
          <div class="health-number">{{ Math.round(health.bloodPressure) }} mmHg</div>
        </div>
        <div class="health-item">
          <span class="health-label">🫁 血氧</span>
          <div class="health-number">{{ Math.round(health.oxygenLevel) }}%</div>
        </div>
        <div class="health-item">
          <span class="health-label">🛡️ 免疫力</span>
          <div class="health-bar">
            <div class="health-fill immunity" :style="{ width: health.immunity + '%' }"></div>
          </div>
          <span class="health-value">{{ Math.round(health.immunity) }}%</span>
        </div>
        <div class="health-item life-expectancy">
          <span class="health-label">⏰ 预期寿命</span>
          <div class="health-number">{{ Math.round(health.lifeExpectancy) }}岁</div>
        </div>
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
              <!-- 内部进度条 -->
              <div class="internal-progress" v-if="isSmoking">
                <div class="progress-fill" :style="{ height: (100 - smokingProgress) + '%' }"></div>
                <div class="progress-info">
                  <div class="progress-percentage">{{ Math.round(smokingProgress) }}%</div>
                  <div class="progress-timer">{{ timeRemaining }}s</div>
                </div>
              </div>
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

     <!-- 吸烟进度条 -->
     <div class="smoking-progress" v-if="isSmoking" :class="progressClass">
        <div class="progress-text">{{ themeConfig.progressText }}: {{ Math.round(smokingProgress) }}%</div>
        <div class="progress-time">{{ themeConfig.timeRemaining }}: {{ timeRemaining }}秒</div>
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

    <!-- 左下角工地 -->
    <div class="work-panel left-bottom-panel">
      <div class="panel-header">
        <h3>🏗️ 工地打工</h3>
        <div class="work-pay">💵 ¥{{ economy.workPay }}/次</div>
      </div>
      <div class="work-content">
        <div class="work-progress" v-if="economy.isWorking">
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: economy.workProgress + '%' }"></div>
          </div>
          <span>工作中... {{ economy.workProgress }}%</span>
        </div>
        <button @click="startWork" 
                :disabled="economy.isWorking"
                class="work-btn">
          {{ economy.isWorking ? '工作中...' : '开始工作' }}
        </button>
      </div>
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
    const smokingProgress = ref(0)
    const timeRemaining = ref(0)

    const stats = reactive({
      todaySmokes: 0,
      totalSmokes: 0
    })

    // 经济系统
    const economy = reactive({
      money: 100, // 初始金钱
      cigaretteStock: 0, // 香烟库存
      cigarettePrice: 20, // 香烟价格（每包）
      workPay: 50, // 打工收入
      isWorking: false, // 是否正在打工
      workProgress: 0 // 打工进度
    })

    // 健康系统
    const health = reactive({
      lungHealth: 100, // 肺部健康
      heartHealth: 100, // 心脏健康
      liverHealth: 100, // 肝脏健康
      bloodPressure: 120, // 血压
      oxygenLevel: 98, // 血氧水平
      immunity: 100, // 免疫力
      lifeExpectancy: 80, // 预期寿命
      smokingDamage: 0 // 累积吸烟损害
    })

    // 健康损害动画状态


    // 商店系统
    const shop = reactive({
      isOpen: false, // 是否打开商店
      items: [
        { id: 1, name: '普通香烟', price: 20, quantity: 20, description: '最便宜的香烟' },
        { id: 2, name: '高档香烟', price: 50, quantity: 10, description: '更高档的香烟' },
        { id: 3, name: '进口香烟', price: 80, quantity: 5, description: '昂贵的进口香烟' }
      ]
    })
    
    let smokeInterval = null
    let streamInterval = null
    let wispInterval = null
    let progressInterval = null
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
        resetText: '重置统计',
        progressText: '吸烟进度',
        timeRemaining: '剩余时间'
      },
      traditional: {
        title: '戒烟助手',
        subtitle: '健康生活从这里开始',
        hintText: '点击香烟体验吸烟感受',
        startText: '体验吸烟',
        smokingText: '体验中...',
        resetText: '清空记录',
        progressText: '体验进度',
        timeRemaining: '剩余时间'
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

    const progressClass = computed(() => ({
      'cyber-progress': currentTheme.value === 'cyber',
      'traditional-progress': currentTheme.value === 'traditional'
    }))



    // 主题切换函数
    const toggleTheme = () => {
      currentTheme.value = currentTheme.value === 'cyber' ? 'traditional' : 'cyber'
      document.body.className = `theme-${currentTheme.value}`
    }

    // 商店功能
    const toggleShop = () => {
      shop.isOpen = !shop.isOpen
    }

    const buyItem = (item) => {
      if (economy.money >= item.price && item.quantity > 0) {
        economy.money -= item.price
        economy.cigaretteStock += item.quantity
        item.quantity = Math.max(0, item.quantity - 1)
        
        // 补充库存
        setTimeout(() => {
          if (item.quantity < 10) {
            item.quantity += 1
          }
        }, 30000) // 30秒后补充一支
      }
    }

    // 打工功能
    const startWork = () => {
      if (economy.isWorking) return
      
      economy.isWorking = true
      economy.workProgress = 0
      
      const workInterval = setInterval(() => {
        economy.workProgress += 2
        
        if (economy.workProgress >= 100) {
          economy.money += economy.workPay
          economy.isWorking = false
          economy.workProgress = 0
          clearInterval(workInterval)
        }
      }, 100) // 5秒完成一次打工
    }

    // 更新健康参数
    const updateHealth = () => {
      // 每次吸烟对健康的剧烈损害
      const baseDamage = Math.random() * 3 + 2 // 2-5的基础伤害
      
      // 立即显著损害
      health.lungHealth = Math.max(0, health.lungHealth - baseDamage * 1.5)
      health.heartHealth = Math.max(0, health.heartHealth - baseDamage * 1.2)
      health.liverHealth = Math.max(0, health.liverHealth - baseDamage * 0.8)
      health.bloodPressure = Math.min(200, health.bloodPressure + baseDamage * 2)
      health.oxygenLevel = Math.max(70, health.oxygenLevel - baseDamage * 1)
      health.immunity = Math.max(0, health.immunity - baseDamage * 1.1)
      health.lifeExpectancy = Math.max(40, health.lifeExpectancy - baseDamage * 0.3)
      health.smokingDamage += baseDamage
    }

    // 实时健康损害（吸烟过程中持续损害）
    const startHealthDamage = () => {
      let damageCount = 0
      const healthDamageInterval = setInterval(() => {
        if (!isSmoking.value) {
          clearInterval(healthDamageInterval)
          return
        }
        
        damageCount++
        const continuousDamage = Math.random() * 1 + 0.5 // 持续小伤害
        
        // 每2秒造成一次损害
        if (damageCount % 20 === 0) {
          health.lungHealth = Math.max(0, health.lungHealth - continuousDamage * 0.8)
          health.heartHealth = Math.max(0, health.heartHealth - continuousDamage * 0.6)
          health.oxygenLevel = Math.max(70, health.oxygenLevel - continuousDamage * 0.5)
          health.bloodPressure = Math.min(200, health.bloodPressure + continuousDamage * 0.8)
        }
      }, 100) // 每100ms检查一次
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
      
      // 检查是否有香烟库存
      if (economy.cigaretteStock <= 0) {
        alert('没有香烟了！请先到小卖部购买香烟。')
        return
      }
      
      // 消耗一支香烟
      economy.cigaretteStock -= 1
      
      // 影响健康
      updateHealth()
      
      // 开始持续健康损害
      startHealthDamage()
      
      isSmoking.value = true
      ashProgress.value = 0
      smokingProgress.value = 0
      timeRemaining.value = smokingDuration
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

      // 进度和灰烬更新
      const startTime = Date.now()
      const progressInterval = setInterval(() => {
        const elapsed = (Date.now() - startTime) / 1000
        const progress = Math.min((elapsed / smokingDuration) * 100, 100)
        const remaining = Math.max(smokingDuration - elapsed, 0)
        
        smokingProgress.value = progress
        timeRemaining.value = Math.ceil(remaining)
        ashProgress.value = progress
        
        if (progress >= 100) {
          clearInterval(progressInterval)
        }
      }, 100)
      
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
        smokingProgress.value = 0
        timeRemaining.value = 0
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
        if (progressInterval) {
          clearInterval(progressInterval)
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
      economy,
      health,

      shop,
      currentTheme,
      themeConfig,
      ashProgress,
      smokingProgress,
      timeRemaining,
      titleClass,
      subtitleClass,
      hintClass,
      indicatorClass,
      buttonClass,
      progressClass,
      smoke,
      resetStats,
      toggleTheme,
      toggleShop,
      buyItem,
      startWork
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

/* 进度条样式 */
.smoking-progress {
  position: absolute;
  bottom: 200px;
  left: 50%;
  transform: translateX(-50%);
  width: 400px;
  max-width: 90%;
  z-index: 20;
  text-align: center;
}

.progress-container {
  width: 100%;
  height: 12px;
  background: rgba(0, 0, 0, 0.3);
  border-radius: 6px;
  overflow: hidden;
  border: 2px solid var(--cyber-cyan);
  box-shadow: 0 0 20px rgba(0, 255, 255, 0.3);
  margin-bottom: 15px;
  position: relative;
}

.progress-bar {
  height: 100%;
  background: linear-gradient(90deg, var(--cyber-cyan), var(--cyber-pink));
  border-radius: 4px;
  transition: width 0.3s ease;
  position: relative;
  overflow: hidden;
}

.progress-bar::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(90deg, 
    transparent, 
    rgba(255, 255, 255, 0.3), 
    transparent
  );
  animation: progressShine 2s infinite;
}

@keyframes progressShine {
  0% { transform: translateX(-100%); }
  100% { transform: translateX(100%); }
}

.progress-text {
  color: var(--cyber-cyan);
  font-size: 1.1rem;
  font-weight: 700;
  margin-bottom: 8px;
  text-shadow: 0 0 10px var(--cyber-cyan);
  text-transform: uppercase;
  letter-spacing: 2px;
}

.progress-time {
  color: var(--cyber-pink);
  font-size: 0.9rem;
  font-weight: 600;
  text-shadow: 0 0 8px var(--cyber-pink);
  text-transform: uppercase;
  letter-spacing: 1px;
}

/* 传统主题进度条 */
.traditional-progress {
  background: rgba(255, 255, 255, 0.95);
  border: 2px solid #8b4513;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 8px 25px rgba(139, 69, 19, 0.2);
  backdrop-filter: blur(10px);
}

.traditional-progress .progress-container {
  background: rgba(139, 69, 19, 0.2);
  border: 2px solid #8b4513;
  box-shadow: 0 2px 10px rgba(139, 69, 19, 0.3);
}

.traditional-progress .progress-bar {
  background: linear-gradient(90deg, #daa520, #b8860b);
  box-shadow: 0 0 10px rgba(218, 165, 32, 0.5);
}

.traditional-progress .progress-text {
  color: #2d2d2d;
  font-family: 'Microsoft YaHei', '微软雅黑', sans-serif;
  font-size: 1.2rem;
  font-weight: 700;
  text-shadow: 1px 1px 3px rgba(139, 69, 19, 0.3);
  text-transform: none;
  letter-spacing: 1px;
}

.traditional-progress .progress-time {
  color: #8b4513;
  font-family: 'Microsoft YaHei', '微软雅黑', sans-serif;
  font-size: 1rem;
  font-weight: 600;
  text-shadow: 1px 1px 2px rgba(139, 69, 19, 0.2);
  text-transform: none;
  letter-spacing: 0.5px;
  }

/* 侧边面板通用样式 */
.left-panel, .right-panel {
  position: fixed;
  top: 50%;
  transform: translateY(-50%);
  width: 300px;
  background: rgba(0, 20, 40, 0.9);
  border: 2px solid var(--cyber-cyan);
  border-radius: 15px;
  padding: 20px;
  backdrop-filter: blur(10px);
  box-shadow: 0 0 30px rgba(0, 255, 255, 0.3);
  z-index: 100;
  transition: all 0.3s ease;
}

.left-panel {
  left: 20px;
}

.right-panel {
  right: 20px;
}

.left-bottom-panel {
  position: fixed;
  bottom: 20px;
  left: 20px;
  width: 280px;
  background: rgba(0, 20, 40, 0.9);
  border: 2px solid var(--cyber-cyan);
  border-radius: 15px;
  padding: 20px;
  backdrop-filter: blur(10px);
  box-shadow: 0 0 30px rgba(0, 255, 255, 0.3);
  z-index: 100;
  transition: all 0.3s ease;
}

/* 面板头部 */
.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  border-bottom: 1px solid var(--cyber-cyan);
  padding-bottom: 10px;
}

.panel-header h3 {
  color: var(--cyber-cyan);
  margin: 0;
  font-size: 1.2rem;
  text-shadow: 0 0 10px var(--cyber-cyan);
}

/* 小卖部样式 */
.shop-btn {
  background: rgba(0, 255, 255, 0.1);
  border: 1px solid var(--cyber-cyan);
  color: var(--cyber-cyan);
  padding: 8px 15px;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 0.9rem;
}

.shop-btn:hover {
  background: rgba(0, 255, 255, 0.2);
  box-shadow: 0 0 15px rgba(0, 255, 255, 0.5);
}

.shop-money, .shop-stock {
  background: rgba(0, 0, 0, 0.5);
  padding: 8px 12px;
  border-radius: 8px;
  margin-bottom: 10px;
  color: var(--cyber-cyan);
  font-weight: 600;
}

.shop-content {
  max-height: 300px;
  overflow-y: auto;
}

.shop-item {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(0, 255, 255, 0.3);
  border-radius: 10px;
  padding: 15px;
  margin-bottom: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.item-info h4 {
  color: var(--cyber-cyan);
  margin: 0 0 5px 0;
  font-size: 1rem;
}

.item-info p {
  color: rgba(255, 255, 255, 0.7);
  margin: 0 0 8px 0;
  font-size: 0.85rem;
}

.price {
  color: var(--cyber-pink);
  font-weight: 700;
  margin-right: 10px;
}

.quantity {
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.8rem;
}

.buy-btn {
  background: rgba(255, 0, 255, 0.1);
  border: 1px solid var(--cyber-pink);
  color: var(--cyber-pink);
  padding: 8px 15px;
  border-radius: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 0.9rem;
}

.buy-btn:hover:not(:disabled) {
  background: rgba(255, 0, 255, 0.2);
  box-shadow: 0 0 15px rgba(255, 0, 255, 0.5);
}

.buy-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* 健康面板样式 */
.health-warning {
  color: #ff4444;
  font-weight: 700;
  text-shadow: 0 0 10px #ff4444;
  animation: blink 1s infinite;
}



.health-stats {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.health-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  padding: 10px;
}

.health-label {
  color: var(--cyber-cyan);
  font-weight: 600;
  font-size: 0.9rem;
  min-width: 80px;
}

.health-bar {
  flex: 1;
  height: 8px;
  background: rgba(0, 0, 0, 0.5);
  border-radius: 4px;
  margin: 0 10px;
  overflow: hidden;
}

.health-fill {
  height: 100%;
  transition: width 0.5s ease;
  border-radius: 4px;
}

.health-fill.lung {
  background: linear-gradient(90deg, #ff4444, #ffaa44, #44ff44);
  animation: healthPulse 1s infinite ease-in-out;
}

.health-fill.heart {
  background: linear-gradient(90deg, #ff4444, #ff6666, #ff9999);
  animation: healthPulse 1.2s infinite ease-in-out;
}

.health-fill.liver {
  background: linear-gradient(90deg, #aa4444, #cc6666, #ee8888);
  animation: healthPulse 1.5s infinite ease-in-out;
}

.health-fill.immunity {
  background: linear-gradient(90deg, #4444ff, #6666ff, #8888ff);
  animation: healthPulse 1.8s infinite ease-in-out;
}

@keyframes healthPulse {
  0%, 100% { 
    opacity: 0.8; 
    box-shadow: 0 0 5px rgba(255, 255, 255, 0.3);
  }
  50% { 
    opacity: 1; 
    box-shadow: 0 0 15px rgba(255, 255, 255, 0.6);
  }
}

/* 危急健康状态 */
.critical-health {
  border: 2px solid #ff0000 !important;
  background: rgba(255, 0, 0, 0.1) !important;
  animation: criticalBlink 0.5s infinite alternate;
}

.health-fill.critical {
  background: linear-gradient(90deg, #ff0000, #ff4444) !important;
  animation: criticalPulse 0.3s infinite ease-in-out;
}

.critical-text {
  color: #ff0000 !important;
  font-weight: 900 !important;
  animation: textBlink 0.5s infinite;
}

@keyframes criticalBlink {
  0% { box-shadow: 0 0 10px rgba(255, 0, 0, 0.5); }
  100% { box-shadow: 0 0 20px rgba(255, 0, 0, 1); }
}

@keyframes criticalPulse {
  0%, 100% { 
    transform: scaleY(1);
    opacity: 0.8;
  }
  50% { 
    transform: scaleY(1.1);
    opacity: 1;
  }
}

@keyframes textBlink {
  0%, 100% { opacity: 0.7; }
  50% { opacity: 1; }
}

.health-value {
  color: var(--cyber-cyan);
  font-weight: 700;
  font-size: 0.9rem;
  min-width: 40px;
  text-align: right;
}

.health-number {
  color: var(--cyber-pink);
  font-weight: 700;
  font-size: 0.9rem;
}

.life-expectancy {
  border: 2px solid var(--cyber-pink);
  background: rgba(255, 0, 255, 0.1);
}

/* 工作面板样式 */
.work-pay {
  color: var(--cyber-pink);
  font-weight: 700;
  font-size: 0.9rem;
}

.work-content {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.work-progress {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.work-progress .progress-bar {
  width: 100%;
  height: 12px;
  background: rgba(0, 0, 0, 0.5);
  border-radius: 6px;
  overflow: hidden;
  border: 1px solid var(--cyber-cyan);
}

.work-progress .progress-fill {
  height: 100%;
  background: linear-gradient(90deg, var(--cyber-cyan), var(--cyber-pink));
  border-radius: 6px;
  transition: width 0.3s ease;
}

.work-progress span {
  color: var(--cyber-cyan);
  font-weight: 600;
  text-align: center;
}

.work-btn {
  background: rgba(0, 255, 0, 0.1);
  border: 2px solid #00ff00;
  color: #00ff00;
  padding: 12px 20px;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 1rem;
  font-weight: 600;
  text-shadow: 0 0 10px #00ff00;
}

.work-btn:hover:not(:disabled) {
  background: rgba(0, 255, 0, 0.2);
  box-shadow: 0 0 20px rgba(0, 255, 0, 0.5);
  transform: translateY(-2px);
}

.work-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  background: rgba(255, 255, 255, 0.1);
  border-color: rgba(255, 255, 255, 0.3);
  color: rgba(255, 255, 255, 0.5);
}

/* 传统主题面板样式 */
.theme-traditional .left-panel,
.theme-traditional .right-panel,
.theme-traditional .left-bottom-panel {
  background: rgba(255, 255, 255, 0.95);
  border: 2px solid #8b4513;
  box-shadow: 0 8px 25px rgba(139, 69, 19, 0.2);
}

.theme-traditional .panel-header h3 {
  color: #2d2d2d;
  text-shadow: none;
  font-family: 'Microsoft YaHei', '微软雅黑', sans-serif;
}

.theme-traditional .shop-money,
.theme-traditional .shop-stock {
  background: rgba(139, 69, 19, 0.1);
  color: #2d2d2d;
}

.theme-traditional .health-label {
  color: #2d2d2d;
  font-family: 'Microsoft YaHei', '微软雅黑', sans-serif;
}

.theme-traditional .health-value {
  color: #8b4513;
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
    0 0 20px #ff4000,
    0 0 40px #ff4000,
    0 0 60px #ff4000,
    0 0 80px #ff2000;
  animation: burning 0.5s infinite alternate;
}

.fire-core {
  position: absolute;
  width: 16px;
  height: 16px;
  background: radial-gradient(circle, #ffffff 0%, #ffff00 40%, #ff8800 80%, transparent 100%);
  border-radius: 50%;
  top: 20%;
  left: 50%;
  transform: translate(-50%, -50%);
  box-shadow: 0 0 20px #ffffff;
  animation: fireCore 0.3s infinite alternate;
}

.fire-glow {
  position: absolute;
  width: 60px;
  height: 60px;
  background: radial-gradient(circle, 
    rgba(255, 255, 0, 0.8) 0%,
    rgba(255, 170, 0, 0.6) 30%,
    rgba(255, 100, 0, 0.4) 60%,
    transparent 100%);
  border-radius: 50%;
  top: 0%;
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
  background: linear-gradient(180deg, #333 0%, #222 30%, #111 70%, #000 100%);
  border-radius: 0;
  transition: height 0.3s ease-out;
  z-index: 5;
  box-shadow: 
    inset 0 2px 4px rgba(0, 0, 0, 0.3),
    0 0 10px rgba(0, 0, 0, 0.5);
  animation: ashBurn 0.5s ease-out;
}

.cigarette-ash::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, #ff4500, #ff6500, #ff4500);
  animation: ashGlow 1.5s infinite alternate;
  border-radius: 2px;
}

.cigarette-ash::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: repeating-linear-gradient(
    90deg,
    transparent,
    transparent 2px,
    rgba(68, 68, 68, 0.3) 2px,
    rgba(68, 68, 68, 0.3) 4px
  );
}

@keyframes ashGlow {
  0% { 
    opacity: 0.8; 
    transform: scaleY(1);
  }
  100% { 
    opacity: 0.4; 
    transform: scaleY(0.8);
  }
}

@keyframes ashBurn {
  0% { 
    opacity: 0;
    transform: scaleY(0);
  }
  100% { 
    opacity: 1;
    transform: scaleY(1);
  }
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
    transform: translateY(0) scale(0.3) rotate(0deg) translateX(0);
    opacity: 0.9;
  }
  20% {
    transform: translateY(-80px) scale(0.6) rotate(18deg) translateX(5px);
    opacity: 0.8;
  }
  40% {
    transform: translateY(-160px) scale(1) rotate(36deg) translateX(-8px);
    opacity: 0.7;
  }
  60% {
    transform: translateY(-240px) scale(1.4) rotate(54deg) translateX(12px);
    opacity: 0.5;
  }
  80% {
    transform: translateY(-320px) scale(1.7) rotate(72deg) translateX(-15px);
    opacity: 0.3;
  }
  100% {
    transform: translateY(-400px) scale(2) rotate(90deg) translateX(20px);
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
    transform: translateY(0) scale(0.4) rotate(0deg) translateX(0);
    opacity: 0.8;
  }
  15% {
    transform: translateY(-60px) scale(0.6) rotate(54deg) translateX(8px);
    opacity: 0.75;
  }
  30% {
    transform: translateY(-120px) scale(0.8) rotate(108deg) translateX(-12px);
    opacity: 0.7;
  }
  45% {
    transform: translateY(-180px) scale(1) rotate(162deg) translateX(18px);
    opacity: 0.6;
  }
  60% {
    transform: translateY(-240px) scale(1.2) rotate(216deg) translateX(-25px);
    opacity: 0.5;
  }
  75% {
    transform: translateY(-300px) scale(1.4) rotate(270deg) translateX(30px);
    opacity: 0.4;
  }
  90% {
    transform: translateY(-360px) scale(1.6) rotate(324deg) translateX(-35px);
    opacity: 0.2;
  }
  100% {
    transform: translateY(-420px) scale(1.8) rotate(360deg) translateX(40px);
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
    transform: translateY(0) scale(0.5) rotate(0deg) skew(0deg) translateX(0);
    opacity: 0.7;
  }
  12% {
    transform: translateY(-60px) scale(0.7) rotate(25deg) skew(8deg) translateX(6px);
    opacity: 0.65;
  }
  24% {
    transform: translateY(-120px) scale(0.9) rotate(50deg) skew(16deg) translateX(-10px);
    opacity: 0.6;
  }
  36% {
    transform: translateY(-180px) scale(1.1) rotate(75deg) skew(24deg) translateX(15px);
    opacity: 0.55;
  }
  48% {
    transform: translateY(-240px) scale(1.3) rotate(100deg) skew(32deg) translateX(-20px);
    opacity: 0.5;
  }
  60% {
    transform: translateY(-300px) scale(1.5) rotate(125deg) skew(40deg) translateX(25px);
    opacity: 0.4;
  }
  72% {
    transform: translateY(-360px) scale(1.7) rotate(150deg) skew(48deg) translateX(-30px);
    opacity: 0.3;
  }
  84% {
    transform: translateY(-420px) scale(1.9) rotate(175deg) skew(56deg) translateX(35px);
    opacity: 0.2;
  }
  96% {
    transform: translateY(-480px) scale(2.1) rotate(200deg) skew(64deg) translateX(-40px);
    opacity: 0.1;
  }
  100% {
    transform: translateY(-520px) scale(2.3) rotate(225deg) skew(70deg) translateX(45px);
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