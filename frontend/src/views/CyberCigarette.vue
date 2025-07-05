<template>
  <div class="cyber-cigarette-container">
    <!-- 数据加载中提示 -->
    <div v-if="!isDataLoaded" class="loading-overlay">
      <div class="loading-spinner">
        <div class="spinner-ring"></div>
        <div class="loading-text">加载用户数据中...</div>
      </div>
    </div>
    
    <!-- 背景动画层 -->
    <div class="background-animation">
      <div class="matrix-rain"></div>
      <div class="cyber-grid"></div>
    </div>

    <!-- 左上角成就面板 -->
    <div class="achievement-panel left-top-panel">
      <div class="panel-header">
        <h3>🏆 成就系统</h3>
        <button @click="showAchievements" class="achievement-btn">
          查看成就
        </button>
      </div>
      <div class="achievement-summary">
        <div class="achievement-score">总分: {{ achievementSystem.achievements.score }}</div>
        <div class="achievement-count">已解锁: {{ achievementSystem.achievements.unlocked.length }}</div>
      </div>
    </div>

    <!-- 右上角综合信息面板 -->
    <div class="unified-info-panel right-top-panel">
      <div class="panel-header">
        <h3 class="panel-title">{{ currentTheme === 'cyber' ? '🎯 游戏状态' : '📊 游戏状态' }}</h3>
        <button @click="advanceDay" class="advance-day-btn">
          推进一天
        </button>
      </div>
      
      <!-- 选项卡 -->
      <div class="panel-tabs">
        <button class="tab-button" :class="{ active: currentTab === 'game' }" @click="currentTab = 'game'">
          游戏信息
        </button>
        <button class="tab-button" :class="{ active: currentTab === 'health' }" @click="currentTab = 'health'">
          健康状况
        </button>
      </div>
      
      <!-- 游戏信息选项卡 -->
      <div v-if="currentTab === 'game'" class="tab-content">
        <div class="time-info">
          <div class="current-day">第 {{ timeSystem.currentDay }} 天</div>
          <div class="hospital-status" :class="{ 'needs-hospital': timeSystem.needsHospital }">
            {{ timeSystem.needsHospital ? '⚠️ 需要就医' : '✅ 健康状况良好' }}
          </div>
          <div class="last-hospital">
            上次就医: {{ timeSystem.lastHospitalDay > 0 ? `第${timeSystem.lastHospitalDay}天` : '从未就医' }}
          </div>
        </div>
        <div class="stats-info">
          <div class="stat-row">
            <span class="stat-icon">🚬</span>
            <span class="stat-label">今日吸烟:</span>
            <span class="stat-value">{{ stats.todaySmokes }}支</span>
          </div>
          <div class="stat-row">
            <span class="stat-icon">📊</span>
            <span class="stat-label">总计吸烟:</span>
            <span class="stat-value">{{ stats.totalSmokes }}支</span>
          </div>
          <div class="stat-row">
            <span class="stat-icon">💼</span>
            <span class="stat-label">工作天数:</span>
            <span class="stat-value">{{ stats.totalWorkDays }}天</span>
          </div>
          <div class="stat-row">
            <span class="stat-icon">❤️</span>
            <span class="stat-label">捐赠次数:</span>
            <span class="stat-value">{{ stats.totalDonations }}次</span>
          </div>
        </div>
      </div>
      
      <!-- 健康状况选项卡 -->
      <div v-if="currentTab === 'health'" class="tab-content">
        <div class="health-warning" v-if="health.lungHealth < 50">
          ⚠️ 健康危险
        </div>
        <div class="health-stats">
          <div class="health-item" :class="{ 'critical-health': health.lungHealth < 30, 'pulse': isSmoking }">
            <span class="health-label">🫁 肺部健康</span>
            <div class="health-bar">
              <div class="health-fill lung" :style="{ width: health.lungHealth + '%' }" 
                   :class="{ 'critical': health.lungHealth < 30, 'pulsing': isSmoking }"></div>
            </div>
            <span class="health-value" :class="{ 'critical-text': health.lungHealth < 30, 'jumping-value': isSmoking }">{{ Math.round(health.lungHealth) }}%</span>
          </div>
          <div class="health-item" :class="{ 'critical-health': health.heartHealth < 30, 'pulse': isSmoking }">
            <span class="health-label">❤️ 心脏健康</span>
            <div class="health-bar">
              <div class="health-fill heart" :style="{ width: health.heartHealth + '%' }"
                   :class="{ 'critical': health.heartHealth < 30, 'pulsing': isSmoking }"></div>
            </div>
            <span class="health-value" :class="{ 'critical-text': health.heartHealth < 30, 'jumping-value': isSmoking }">{{ Math.round(health.heartHealth) }}%</span>
          </div>
          <div class="health-item" :class="{ 'critical-health': health.liverHealth < 30, 'pulse': isSmoking }">
            <span class="health-label">🫀 肝脏健康</span>
            <div class="health-bar">
              <div class="health-fill liver" :style="{ width: health.liverHealth + '%' }"
                   :class="{ 'critical': health.liverHealth < 30, 'pulsing': isSmoking }"></div>
            </div>
            <span class="health-value" :class="{ 'critical-text': health.liverHealth < 30, 'jumping-value': isSmoking }">{{ Math.round(health.liverHealth) }}%</span>
          </div>
          <div class="health-item" :class="{ 'pulse': isSmoking }">
            <span class="health-label">🩸 血压</span>
            <div class="health-number" :class="{ 'jumping-value': isSmoking }">{{ Math.round(health.bloodPressure) }} mmHg</div>
          </div>
          <div class="health-item" :class="{ 'pulse': isSmoking }">
            <span class="health-label">🫁 血氧</span>
            <div class="health-number" :class="{ 'jumping-value': isSmoking }">{{ Math.round(health.oxygenLevel) }}%</div>
          </div>
          <div class="health-item" :class="{ 'pulse': isSmoking }">
            <span class="health-label">🛡️ 免疫力</span>
            <div class="health-bar">
              <div class="health-fill immunity" :style="{ width: health.immunity + '%' }" :class="{ 'pulsing': isSmoking }"></div>
            </div>
            <span class="health-value" :class="{ 'jumping-value': isSmoking }">{{ Math.round(health.immunity) }}%</span>
          </div>
          <div class="health-item life-expectancy" :class="{ 'pulse': isSmoking }">
            <span class="health-label">⏰ 预期寿命</span>
            <div class="health-number" :class="{ 'jumping-value': isSmoking }">{{ Math.round(health.lifeExpectancy) }}岁</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 标题 -->
    <div class="title-section">
      <h1 class="main-title" :class="titleClass">{{ themeConfig.title }}</h1>
      <p class="subtitle" :class="subtitleClass">{{ themeConfig.subtitle }}</p>
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
        <div class="hint-text neon-text pulse">点击香烟开始模拟吸烟</div>
        <div class="click-indicator cyber-indicator">
          <div class="ripple"></div>
          <div class="ripple"></div>
          <div class="ripple"></div>
        </div>
      </div>

    </div>

     <!-- 吸烟进度条 -->
     <div class="smoking-progress cyber-progress" v-if="isSmoking">
        <div class="progress-text">吸烟进度: {{ Math.round(smokingProgress) }}%</div>
        <div class="progress-time">剩余时间: {{ timeRemaining }}秒</div>
      </div>

    <!-- 控制面板 -->
    <div class="control-panel">
      <button class="control-button primary cyber-button" @click="smoke" :disabled="isSmoking">
        <span class="button-text">{{ isSmoking ? '吸烟中...' : '开始吸烟' }}</span>
        <div class="button-glow"></div>
      </button>
      <button class="control-button secondary cyber-button" @click="resetStats">
        <span class="button-text">重置统计</span>
        <div class="button-glow"></div>
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
                :disabled="economy.isWorking || shouldGoToHospital()"
                class="work-btn">
          {{ economy.isWorking ? '工作中...' : (shouldGoToHospital() ? '健康太差，无法工作' : '开始工作') }}
        </button>
      </div>
    </div>

    <!-- 右下角按钮区域 -->
    <div class="bottom-right-actions">
      <!-- 医院按钮 -->
      <div class="hospital-corner">
        <button class="corner-hospital-btn" @click="toggleHospital" :disabled="isDead">
          <span class="hospital-icon">🏥</span>
          <span class="hospital-text">医院</span>
        </button>
      </div>
      
      <!-- 捐赠按钮 -->
      <div class="donation-corner">
        <button class="corner-donation-btn" @click="donate" :disabled="economy.money < 100 || isDead">
          <span class="donation-icon">🏫</span>
          <span class="donation-text">捐赠小学</span>
          <span class="donation-amount">¥100</span>
        </button>
      </div>
    </div>

    <!-- 医院面板 -->
    <div class="hospital-panel" v-if="hospitalSystem.isHospitalOpen">
      <div class="hospital-header">
        <h3>🏥 医院</h3>
        <button class="close-btn" @click="toggleHospital">×</button>
      </div>
      
      <div class="hospital-tabs">
        <button class="tab-btn" :class="{ active: hospitalTab === 'treatment' }" @click="hospitalTab = 'treatment'">
          治疗
        </button>
        <button class="tab-btn" :class="{ active: hospitalTab === 'volunteer' }" @click="hospitalTab = 'volunteer'">
          义工
        </button>
      </div>
      
      <div class="hospital-content">
        <!-- 治疗选项卡 -->
        <div v-if="hospitalTab === 'treatment'">
          <div class="hospital-info">
            <span>费用: ¥200</span>
            <span>就诊记录: {{ hospitalSystem.hospitalVisits }}次</span>
          </div>
          <div class="hospital-services">
            <div class="service-item">
              <span class="service-icon">🫁</span>
              <span class="service-text">肺部健康 +30%</span>
            </div>
            <div class="service-item">
              <span class="service-icon">❤️</span>
              <span class="service-text">心脏健康 +30%</span>
            </div>
            <div class="service-item">
              <span class="service-icon">🛡️</span>
              <span class="service-text">免疫力 +35%</span>
            </div>
          </div>
          <button @click="visitHospital" 
                  :disabled="economy.money < 200 || isDead" 
                  class="hospital-treatment-btn">
            {{ economy.money < 200 ? '金钱不足' : '接受治疗' }}
          </button>
        </div>
        
        <!-- 义工服务选项卡 -->
        <div v-if="hospitalTab === 'volunteer'">
          <div class="volunteer-info">
            <div class="volunteer-description">无报酬 | 提升健康</div>
            <div class="volunteer-hours">义工时间: {{ hospitalSystem.volunteerHours }}小时</div>
          </div>
          <div class="volunteer-services">
            <div class="service-item">
              <span class="service-icon">🫁</span>
              <span class="service-text">肺部健康 +15%</span>
            </div>
            <div class="service-item">
              <span class="service-icon">❤️</span>
              <span class="service-text">心脏健康 +15%</span>
            </div>
            <div class="service-item">
              <span class="service-icon">🛡️</span>
              <span class="service-text">免疫力 +20%</span>
            </div>
          </div>
          <div class="volunteer-progress" v-if="hospitalSystem.isVolunteerWorking">
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: hospitalSystem.volunteerProgress + '%' }"></div>
            </div>
            <span>义工服务中... {{ hospitalSystem.volunteerProgress }}%</span>
          </div>
          <button @click="startVolunteer" 
                  :disabled="hospitalSystem.isVolunteerWorking || shouldGoToHospital()"
                  class="volunteer-btn">
            {{ hospitalSystem.isVolunteerWorking ? '义工服务中...' : (shouldGoToHospital() ? '健康太差，先治疗' : '开始义工') }}
          </button>
        </div>
      </div>
    </div>

    <!-- 死亡弹窗 -->
    <div class="death-overlay" v-if="isDead">
      <div class="death-modal">
        <div class="death-content">
          <div class="death-icon">💀</div>
          <h2 class="death-title">生命终结</h2>
          <div class="death-message">
            <p>由于长期吸烟和过度劳累，您的健康状况已经恶化到了无法挽回的地步。</p>
            <p>最终寿命：{{ Math.round(health.lifeExpectancy) }}岁</p>
            <p>总共吸烟：{{ stats.totalSmokes }}支</p>
            <p>累计健康损害：{{ Math.round(health.smokingDamage) }}点</p>
          </div>
          <div class="death-actions">
            <button class="restart-btn" @click="restartLife">重新开始</button>
            <button class="reflect-btn" @click="showReflection">反思人生</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 反思弹窗 -->
    <div class="reflection-overlay" v-if="showReflectionModal">
      <div class="reflection-modal">
        <div class="reflection-content">
          <div class="reflection-icon">🌟</div>
          <h2 class="reflection-title">人生反思</h2>
          <div class="reflection-message">
            <p>吸烟和过度劳累都会损害您的健康，缩短您的寿命。</p>
            <p>真正的幸福来自健康的生活方式和对他人的关爱。</p>
            <p>每一次捐赠都是对美好未来的投资，让我们一起传递希望！</p>
          </div>
          <div class="reflection-actions">
            <button class="close-reflection-btn" @click="closeReflection">明白了</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 捐赠小学弹窗 -->
    <div class="donation-school-overlay" v-if="showDonationModal">
      <div class="donation-school-modal">
        <div class="school-scene">
          <div class="school-building">
            <div class="school-roof"></div>
            <div class="school-walls">
              <div class="school-door"></div>
              <div class="school-window broken"></div>
              <div class="school-window cracked"></div>
              <div class="school-window"></div>
            </div>
            <div class="school-foundation"></div>
          </div>
          <div class="school-yard">
            <div class="old-tree"></div>
            <div class="rusty-fence"></div>
          </div>
        </div>
        <div class="donation-message">
          <h2 class="donation-title">感谢您的爱心捐赠！</h2>
          <div class="donation-content">
            <p>您的100元捐赠将用于改善这所偏远山区小学的教学条件。</p>
            <p>每一份爱心都能照亮孩子们的求学路。</p>
            <p>您的善举让世界变得更加美好！</p>
          </div>
          <div class="donation-effects">
            <div class="heart-float" v-for="n in 6" :key="n"></div>
          </div>
          <button class="close-donation-btn" @click="closeDonationModal">继续传递希望</button>
        </div>
      </div>
    </div>

    <!-- 成就系统弹窗 -->
    <div class="achievement-overlay" v-if="achievementSystem.showAchievementModal">
      <div class="achievement-modal">
        <div class="achievement-content">
          <div class="achievement-header">
            <h2 class="achievement-title">🏆 成就系统</h2>
            <button class="close-achievement-btn" @click="closeAchievements">×</button>
          </div>
          
          <div class="achievement-summary">
            <div class="achievement-stats">
              <div class="stat-item">
                <span class="stat-label">总分</span>
                <span class="stat-value">{{ achievementSystem.achievements.score }}</span>
              </div>
              <div class="stat-item">
                <span class="stat-label">已解锁</span>
                <span class="stat-value">{{ achievementSystem.achievements.unlocked.length }}</span>
              </div>
              <div class="stat-item">
                <span class="stat-label">总数</span>
                <span class="stat-value">{{ Object.keys(achievementSystem.achievements.all).length }}</span>
              </div>
            </div>
          </div>
          
          <!-- 新获得的成就 -->
          <div class="new-achievements" v-if="achievementSystem.newAchievements.length > 0">
            <h3>🎉 新获得的成就</h3>
            <div class="new-achievement-list">
              <div class="achievement-item new" v-for="id in achievementSystem.newAchievements" :key="id">
                <div class="achievement-icon">🏆</div>
                <div class="achievement-info">
                  <div class="achievement-name">{{ achievementSystem.achievements.all[id] }}</div>
                  <div class="achievement-reward">+10 分</div>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 所有成就列表 -->
          <div class="achievements-list">
            <h3>所有成就</h3>
            <div class="achievement-categories">
              <div class="achievement-item" 
                   v-for="(description, id) in achievementSystem.achievements.all" 
                   :key="id"
                   :class="{ 'unlocked': achievementSystem.achievements.unlocked.includes(id) }">
                <div class="achievement-icon">
                  {{ achievementSystem.achievements.unlocked.includes(id) ? '🏆' : '🔒' }}
                </div>
                <div class="achievement-info">
                  <div class="achievement-name">{{ description }}</div>
                  <div class="achievement-status">
                    {{ achievementSystem.achievements.unlocked.includes(id) ? '已解锁' : '未解锁' }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import { ref, reactive, computed, onMounted, onUnmounted, watch } from 'vue'
import axios from 'axios'

export default {
  name: 'CyberCigarette',
  setup() {
    const isSmoking = ref(false)
    const smokeParticles = ref([])
    const smokeStreams = ref([])
    const smokeWisps = ref([])

    const ashProgress = ref(0) // 灰烬进度 0-100%
    const smokingProgress = ref(0)
    const timeRemaining = ref(0)
    
    // 死亡和捐赠状态
    const isDead = ref(false)
    const isDonating = ref(false)
    const showReflectionModal = ref(false)
    const showDonationModal = ref(false)
    
    // 用户会话管理
    const sessionId = ref('')
    const isDataLoaded = ref(false)
    const lastSaveTime = ref(0)
    
    // 主题系统
    const currentTheme = ref('cyber')
    
    // 主题配置
    const themeConfig = computed(() => ({
      title: currentTheme.value === 'cyber' ? '赛博戒烟模拟器' : '戒烟模拟器',
      subtitle: currentTheme.value === 'cyber' ? '未来科技体验' : '健康生活从戒烟开始'
    }))
    
    // 样式类
    const titleClass = computed(() => ({
      'cyber-title': currentTheme.value === 'cyber',
      'traditional-title': currentTheme.value === 'traditional'
    }))
    
    const subtitleClass = computed(() => ({
      'cyber-subtitle': currentTheme.value === 'cyber',
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
    
    // 时间系统
    const timeSystem = reactive({
      currentDay: 1,
      lastHospitalDay: 0,
      needsHospital: false
    })
    
    // 医院系统
    const hospitalSystem = reactive({
      hospitalVisits: 0,
      volunteerHours: 0,
      isHospitalOpen: false,
      isVolunteerWorking: false,
      volunteerProgress: 0
    })
    
    // 医院选项卡
    const hospitalTab = ref('treatment')
    
    // 统合信息面板选项卡
    const currentTab = ref('game')
    
    // 成就系统
    const achievementSystem = reactive({
      achievements: {
        unlocked: [],
        all: {},
        score: 0
      },
      showAchievementModal: false,
      newAchievements: [] // 新获得的成就
    })

    const stats = reactive({
      todaySmokes: 0,
      totalSmokes: 0,
      totalDonations: 0,
      totalWorkDays: 0
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
    let smokingDuration = 5 // 吸烟总时长（秒）

    // 主题切换函数
    const toggleTheme = () => {
      currentTheme.value = currentTheme.value === 'cyber' ? 'traditional' : 'cyber'
      document.body.className = `theme-${currentTheme.value}`
    }

    // 商店功能
    const toggleShop = () => {
      shop.isOpen = !shop.isOpen
    }
    
    // 医院功能
    const toggleHospital = () => {
      hospitalSystem.isHospitalOpen = !hospitalSystem.isHospitalOpen
    }
    
    // 就医治疗
    const visitHospital = async () => {
      if (economy.money < 200) {
        alert('您的金钱不足，无法支付医疗费用！需要200元。')
        return
      }
      
      try {
        const response = await axios.post('/api/user/hospital/visit', null, {
          params: { sessionId: sessionId.value }
        })
        
        // 更新数据
        economy.money = response.data.money
        health.lungHealth = response.data.lungHealth
        health.heartHealth = response.data.heartHealth
        health.liverHealth = response.data.liverHealth
        health.immunity = response.data.immunity
        health.lifeExpectancy = response.data.lifeExpectancy
        hospitalSystem.hospitalVisits = response.data.hospitalVisits
        timeSystem.lastHospitalDay = response.data.lastHospitalDay
        timeSystem.needsHospital = response.data.needsHospital
        
        alert('治疗成功！您的健康状况得到了改善。')
        checkForNewAchievements()
      } catch (error) {
        console.error('医院治疗失败:', error)
        alert('治疗失败，请稍后重试。')
      }
    }
    
    // 医院义工
    const startVolunteer = async () => {
      if (hospitalSystem.isVolunteerWorking) return
      
      if (shouldGoToHospital()) {
        alert('您的健康状况太差，无法进行义工服务，请先接受治疗！')
        return
      }
      
      hospitalSystem.isVolunteerWorking = true
      hospitalSystem.volunteerProgress = 0
      
      const volunteerInterval = setInterval(async () => {
        hospitalSystem.volunteerProgress += 2
        
        if (hospitalSystem.volunteerProgress >= 100) {
          hospitalSystem.isVolunteerWorking = false
          hospitalSystem.volunteerProgress = 0
          clearInterval(volunteerInterval)
          
          try {
                    const response = await axios.post('/api/user/hospital/volunteer', null, {
          params: { sessionId: sessionId.value }
        })
            
            // 更新数据
            health.lungHealth = response.data.lungHealth
            health.heartHealth = response.data.heartHealth
            health.immunity = response.data.immunity
            health.lifeExpectancy = response.data.lifeExpectancy
            hospitalSystem.volunteerHours = response.data.volunteerHours
            
            alert('义工服务完成！您的健康得到了改善，同时为社会做出了贡献。')
            checkForNewAchievements()
          } catch (error) {
            console.error('义工服务失败:', error)
            alert('义工服务失败，请稍后重试。')
          }
        }
      }, 100) // 5秒完成一次义工服务
    }
    
    // 检查是否需要去医院
    const shouldGoToHospital = () => {
      return health.lungHealth < 30 || health.heartHealth < 30 || health.liverHealth < 30 || health.immunity < 20
    }
    
    // 推进时间
    const advanceDay = async () => {
      try {
        const response = await axios.post('/api/user/advance-day', null, {
          params: { sessionId: sessionId.value }
        })
        
        timeSystem.currentDay = response.data.currentDay
        timeSystem.needsHospital = response.data.needsHospital
        
        // 更新健康数据
        health.lungHealth = response.data.lungHealth
        health.heartHealth = response.data.heartHealth
        health.lifeExpectancy = response.data.lifeExpectancy
        
        checkForNewAchievements()
      } catch (error) {
        console.error('推进时间失败:', error)
      }
    }
    
    // 检查新成就
    const checkForNewAchievements = async () => {
      try {
        const response = await axios.get('/api/user/achievements', {
          params: { sessionId: sessionId.value }
        })
        
        achievementSystem.achievements = response.data
        
        // 检查是否有新成就
        const newUnlocked = response.data.unlocked.filter(id => 
          !achievementSystem.achievements.unlocked.includes(id)
        )
        
        if (newUnlocked.length > 0) {
          achievementSystem.newAchievements = newUnlocked
          achievementSystem.showAchievementModal = true
        }
      } catch (error) {
        console.error('获取成就失败:', error)
      }
    }
    
    // 显示成就面板
    const showAchievements = () => {
      achievementSystem.showAchievementModal = true
    }
    
    // 关闭成就面板
    const closeAchievements = () => {
      achievementSystem.showAchievementModal = false
      achievementSystem.newAchievements = []
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
      if (economy.isWorking || isDead.value) return
      
      // 检查健康状况
      if (shouldGoToHospital()) {
        alert('您的健康状况太差，无法工作！请先去医院治疗。')
        return
      }
      
      economy.isWorking = true
      economy.workProgress = 0
      
      const workInterval = setInterval(() => {
        economy.workProgress += 2
        
        if (economy.workProgress >= 100) {
          economy.money += economy.workPay
          economy.isWorking = false
          economy.workProgress = 0
          clearInterval(workInterval)
          
          // 更新工作统计
          stats.totalWorkDays += 1
          
          // 打工会损害健康和寿命
          const workDamage = Math.random() * 2 + 1 // 1-3点伤害
          health.lungHealth = Math.max(0, health.lungHealth - workDamage * 0.5)
          health.heartHealth = Math.max(0, health.heartHealth - workDamage * 0.8)
          health.liverHealth = Math.max(0, health.liverHealth - workDamage * 0.3)
          health.immunity = Math.max(0, health.immunity - workDamage * 0.6)
          health.lifeExpectancy = Math.max(30, health.lifeExpectancy - workDamage * 0.2)
          
          // 检查是否死亡
          checkDeath()
          
          // 检查成就
          checkForNewAchievements()
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
      health.lifeExpectancy = Math.max(30, health.lifeExpectancy - baseDamage * 0.4)
      health.smokingDamage += baseDamage
      
      // 检查是否死亡
      checkDeath()
      
      // 检查吸烟相关成就
      checkForNewAchievements()
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

    // 死亡检查
    const checkDeath = () => {
      if (isDead.value) return
      
      // 如果寿命低于35岁，触发死亡
      if (health.lifeExpectancy <= 35) {
        isDead.value = true
        isSmoking.value = false // 停止吸烟
        economy.isWorking = false // 停止工作
        
        // 清除所有定时器
        if (smokeInterval) clearInterval(smokeInterval)
        if (streamInterval) clearInterval(streamInterval)
        if (wispInterval) clearInterval(wispInterval)
      }
    }
    
    // 捐赠功能
    const donate = () => {
      if (economy.money < 100 || isDead.value) return
      
      economy.money -= 100
      isDonating.value = true
      
      // 更新捐赠统计
      stats.totalDonations += 1
      
      // 捐赠会恢复部分健康和寿命
      health.lungHealth = Math.min(100, health.lungHealth + 5)
      health.heartHealth = Math.min(100, health.heartHealth + 5)
      health.liverHealth = Math.min(100, health.liverHealth + 3)
      health.immunity = Math.min(100, health.immunity + 7)
      health.lifeExpectancy = Math.min(85, health.lifeExpectancy + 2)
      
      // 检查捐赠相关成就
      checkForNewAchievements()
      
      // 显示捐赠小学弹窗
      showDonationModal.value = true
      
      // 希望动画效果
      setTimeout(() => {
        isDonating.value = false
      }, 3000)
    }
    
    // 关闭捐赠弹窗
    const closeDonationModal = () => {
      showDonationModal.value = false
    }
    
    // 重新开始生命
    const restartLife = async () => {
      try {
        // 调用后端重置接口
        await resetUserDataToServer()
        
        // 重置死亡状态
        showReflectionModal.value = false
        
        console.log('游戏重新开始')
      } catch (error) {
        console.error('重新开始失败:', error)
        // 如果后端失败，使用本地重置
        health.lungHealth = 100
        health.heartHealth = 100
        health.liverHealth = 100
        health.bloodPressure = 120
        health.oxygenLevel = 98
        health.immunity = 100
        health.lifeExpectancy = 80
        health.smokingDamage = 0
        
        economy.money = 100
        economy.cigaretteStock = 0
        economy.isWorking = false
        economy.workProgress = 0
        
        stats.todaySmokes = 0
        stats.totalSmokes = 0
        stats.totalDonations = 0
        stats.totalWorkDays = 0
        
        // 重置时间系统
        timeSystem.currentDay = 1
        timeSystem.lastHospitalDay = 0
        timeSystem.needsHospital = false
        
        // 重置医院系统
        hospitalSystem.hospitalVisits = 0
        hospitalSystem.volunteerHours = 0
        hospitalSystem.isHospitalOpen = false
        hospitalSystem.isVolunteerWorking = false
        hospitalSystem.volunteerProgress = 0
        
        // 重置成就系统
        achievementSystem.achievements.score = 0
        achievementSystem.achievements.unlocked = []
        achievementSystem.showAchievementModal = false
        achievementSystem.newAchievements = []
        
        isDead.value = false
        showReflectionModal.value = false
      }
    }
    
    // 显示反思
    const showReflection = () => {
      showReflectionModal.value = true
    }
    
    // 关闭反思
    const closeReflection = () => {
      showReflectionModal.value = false
    }
    
    // 数据持久化功能
    // 获取Session ID
    const getSessionId = () => {
      let storedSessionId = localStorage.getItem('smokingSimulatorSessionId')
      if (!storedSessionId) {
        storedSessionId = generateUUID()
        localStorage.setItem('smokingSimulatorSessionId', storedSessionId)
      }
      return storedSessionId
    }
    
    // 生成UUID
    const generateUUID = () => {
      return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        const r = Math.random() * 16 | 0
        const v = c == 'x' ? r : (r & 0x3 | 0x8)
        return v.toString(16)
      })
    }
    
    // 从后端加载用户数据
    const loadUserData = async () => {
      try {
        const response = await axios.get('/api/user/data', {
          params: { sessionId: sessionId.value }
        })
        
        const userData = response.data
        
        // 更新健康数据
        health.lungHealth = userData.lungHealth
        health.heartHealth = userData.heartHealth
        health.liverHealth = userData.liverHealth
        health.bloodPressure = userData.bloodPressure
        health.oxygenLevel = userData.oxygenLevel
        health.immunity = userData.immunity
        health.lifeExpectancy = userData.lifeExpectancy
        health.smokingDamage = userData.smokingDamage
        
        // 更新经济数据
        economy.money = userData.money
        economy.cigaretteStock = userData.cigaretteStock
        economy.workPay = userData.workPay
        economy.isWorking = userData.isWorking
        economy.workProgress = userData.workProgress
        
        // 更新统计数据
        stats.todaySmokes = userData.todaySmokes
        stats.totalSmokes = userData.totalSmokes
        stats.totalDonations = userData.totalDonations || 0
        stats.totalWorkDays = userData.totalWorkDays || 0
        
        // 更新状态数据
        isDead.value = userData.isDead
        currentTheme.value = userData.currentTheme
        
        // 更新时间系统
        timeSystem.currentDay = userData.currentDay || 1
        timeSystem.lastHospitalDay = userData.lastHospitalDay || 0
        timeSystem.needsHospital = userData.needsHospital || false
        
        // 更新医院系统
        hospitalSystem.hospitalVisits = userData.hospitalVisits || 0
        hospitalSystem.volunteerHours = userData.volunteerHours || 0
        
        // 更新成就系统
        achievementSystem.achievements.score = userData.achievementScore || 0
        achievementSystem.achievements.unlocked = userData.unlockedAchievements ? userData.unlockedAchievements.split(',').filter(id => id) : []
        
        // 加载成就数据
        checkForNewAchievements()
        
        isDataLoaded.value = true
        console.log('用户数据加载成功')
      } catch (error) {
        console.error('加载用户数据失败:', error)
        isDataLoaded.value = true
      }
    }
    
    // 保存用户数据到后端
    const saveUserData = async () => {
      if (!isDataLoaded.value) return
      
      try {
        const userData = {
          sessionId: sessionId.value,
          // 健康数据
          lungHealth: health.lungHealth,
          heartHealth: health.heartHealth,
          liverHealth: health.liverHealth,
          bloodPressure: health.bloodPressure,
          oxygenLevel: health.oxygenLevel,
          immunity: health.immunity,
          lifeExpectancy: health.lifeExpectancy,
          smokingDamage: health.smokingDamage,
          // 经济数据
          money: economy.money,
          cigaretteStock: economy.cigaretteStock,
          workPay: economy.workPay,
          isWorking: economy.isWorking,
          workProgress: economy.workProgress,
          // 统计数据
          todaySmokes: stats.todaySmokes,
          totalSmokes: stats.totalSmokes,
          totalDonations: stats.totalDonations,
          totalWorkDays: stats.totalWorkDays,
          // 状态数据
          isDead: isDead.value,
          currentTheme: currentTheme.value,
          // 时间系统
          currentDay: timeSystem.currentDay,
          lastHospitalDay: timeSystem.lastHospitalDay,
          needsHospital: timeSystem.needsHospital,
          // 医院系统
          hospitalVisits: hospitalSystem.hospitalVisits,
          volunteerHours: hospitalSystem.volunteerHours,
          // 成就系统
          achievementScore: achievementSystem.achievements.score,
          unlockedAchievements: achievementSystem.achievements.unlocked.join(',')
        }
        
        await axios.put('/api/user/update', userData, {
          params: { sessionId: sessionId.value }
        })
        
        lastSaveTime.value = Date.now()
        console.log('用户数据保存成功')
      } catch (error) {
        console.error('保存用户数据失败:', error)
      }
    }
    
    // 重置用户数据
    const resetUserDataToServer = async () => {
      try {
        await axios.post('/api/user/reset', {}, {
          params: { sessionId: sessionId.value }
        })
        
        // 重新加载数据
        await loadUserData()
        console.log('用户数据重置成功')
      } catch (error) {
        console.error('重置用户数据失败:', error)
      }
    }

    // 开始吸烟
    const smoke = async () => {
      if (isSmoking.value || isDead.value) return
      
      // 检查是否有香烟库存
      if (economy.cigaretteStock <= 0) {
        alert('没有香烟了！请先到小卖部购买香烟。')
        return
      }
      
      // 消耗一支香烟
      economy.cigaretteStock -= 1
      
      // 影响健康
      updateHealth()
      
      // 更新统计数据
      stats.todaySmokes += 1
      stats.totalSmokes += 1
      
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
        
        // 检查成就
        checkForNewAchievements()
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

    // 初始化数据
    onMounted(async () => {
      // 获取Session ID
      sessionId.value = getSessionId()
      
      // 加载用户数据
      await loadUserData()
      
      // 设置自动保存（每30秒）
      setInterval(() => {
        saveUserData()
      }, 30000)
      
      // 加载统计
      loadStats()
    })
    
    // 监听数据变化，延迟保存
    watch([health, economy, stats, isDead, currentTheme], () => {
      if (isDataLoaded.value) {
        // 防抖：避免频繁保存
        clearTimeout(window.saveTimeout)
        window.saveTimeout = setTimeout(() => {
          saveUserData()
        }, 2000)
      }
    }, { deep: true })

    onUnmounted(() => {
      // 清理定时器
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
      if (window.saveTimeout) {
        clearTimeout(window.saveTimeout)
      }
      
      // 页面关闭前保存数据
      saveUserData()
    })

    return {
      isSmoking,
      smokeParticles,
      smokeStreams,
      smokeWisps,
      stats,
      economy,
      health,
      isDead,
      isDonating,
      showReflectionModal,
      showDonationModal,
      sessionId,
      isDataLoaded,
      shop,
      currentTheme,
      themeConfig,
      titleClass,
      subtitleClass,
      hintClass,
      indicatorClass,
      buttonClass,
      progressClass,
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
      startWork,
      checkDeath,
      donate,
      restartLife,
      showReflection,
      closeReflection,
      closeDonationModal,
      saveUserData,
      loadUserData,
      
      // 时间系统
      timeSystem,
      advanceDay,
      
      // 医院系统
      hospitalSystem,
      hospitalTab,
      toggleHospital,
      visitHospital,
      startVolunteer,
      shouldGoToHospital,
      
      // 成就系统
      achievementSystem,
      showAchievements,
      closeAchievements,
      checkForNewAchievements,
      
      // 统合信息面板
      currentTab
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

.traditional-footer .footer-text {
  color: #2d2d2d !important;
  font-weight: 600;
  font-size: 1.1rem;
}

/* 捐赠按钮样式 */
.donation-section {
  margin-top: 15px;
  padding: 15px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.donation-btn {
  width: 100%;
  padding: 15px;
  background: linear-gradient(45deg, #ff6b6b, #ff8e8e);
  border: 2px solid #ff4444;
  border-radius: 25px;
  color: white;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.donation-btn:hover:not(:disabled) {
  background: linear-gradient(45deg, #ff8e8e, #ffb3b3);
  box-shadow: 0 0 20px rgba(255, 68, 68, 0.5);
  transform: translateY(-2px);
}

.donation-btn:disabled {
  background: #666;
  border-color: #555;
  cursor: not-allowed;
  opacity: 0.5;
}

.donation-icon {
  font-size: 1.2rem;
  margin-right: 8px;
  animation: heartbeat 1.5s infinite;
}

@keyframes heartbeat {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.1); }
}

/* 希望粒子动画 */
.hope-particles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.hope-particle {
  position: absolute;
  width: 6px;
  height: 6px;
  background: #fff;
  border-radius: 50%;
  animation: floatUp 3s ease-out forwards;
}

.hope-particle:nth-child(1) { left: 20%; animation-delay: 0s; }
.hope-particle:nth-child(2) { left: 40%; animation-delay: 0.5s; }
.hope-particle:nth-child(3) { left: 60%; animation-delay: 1s; }
.hope-particle:nth-child(4) { left: 80%; animation-delay: 1.5s; }
.hope-particle:nth-child(5) { left: 50%; animation-delay: 2s; }

@keyframes floatUp {
  0% {
    bottom: 10%;
    opacity: 1;
    transform: translateY(0) scale(1);
  }
  100% {
    bottom: 90%;
    opacity: 0;
    transform: translateY(-20px) scale(0.5);
  }
}

/* 死亡弹窗样式 */
.death-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.9);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 10000;
  animation: fadeIn 0.5s ease-in-out;
}

.death-modal {
  background: linear-gradient(135deg, #2d2d2d, #1a1a1a);
  border: 3px solid #ff4444;
  border-radius: 20px;
  padding: 30px;
  max-width: 500px;
  width: 90%;
  text-align: center;
  box-shadow: 0 0 50px rgba(255, 68, 68, 0.8);
  animation: slideIn 0.5s ease-out;
}

.death-icon {
  font-size: 4rem;
  margin-bottom: 20px;
  animation: pulse 2s infinite;
}

.death-title {
  color: #ff4444;
  font-size: 2.5rem;
  margin-bottom: 20px;
  font-weight: 700;
  text-shadow: 0 0 10px rgba(255, 68, 68, 0.8);
}

.death-message {
  color: #ccc;
  font-size: 1.1rem;
  line-height: 1.6;
  margin-bottom: 30px;
}

.death-message p {
  margin: 10px 0;
}

.death-actions {
  display: flex;
  gap: 20px;
  justify-content: center;
  flex-wrap: wrap;
}

.restart-btn, .reflect-btn {
  padding: 15px 30px;
  border: 2px solid #00ff00;
  border-radius: 25px;
  background: linear-gradient(45deg, #00ff00, #44ff44);
  color: #000;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.restart-btn:hover, .reflect-btn:hover {
  background: linear-gradient(45deg, #44ff44, #66ff66);
  box-shadow: 0 0 20px rgba(0, 255, 0, 0.5);
  transform: translateY(-2px);
}

.reflect-btn {
  border-color: #ffaa00;
  background: linear-gradient(45deg, #ffaa00, #ffcc44);
}

.reflect-btn:hover {
  background: linear-gradient(45deg, #ffcc44, #ffdd66);
  box-shadow: 0 0 20px rgba(255, 170, 0, 0.5);
}

/* 反思弹窗样式 */
.reflection-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 10001;
  animation: fadeIn 0.5s ease-in-out;
}

.reflection-modal {
  background: linear-gradient(135deg, #f0f8ff, #e6f3ff);
  border: 3px solid #4169e1;
  border-radius: 20px;
  padding: 30px;
  max-width: 500px;
  width: 90%;
  text-align: center;
  box-shadow: 0 0 50px rgba(65, 105, 225, 0.6);
  animation: slideIn 0.5s ease-out;
}

.reflection-icon {
  font-size: 4rem;
  margin-bottom: 20px;
  animation: twinkle 2s infinite;
}

.reflection-title {
  color: #4169e1;
  font-size: 2.5rem;
  margin-bottom: 20px;
  font-weight: 700;
  text-shadow: 0 0 10px rgba(65, 105, 225, 0.5);
}

.reflection-message {
  color: #2d2d2d;
  font-size: 1.1rem;
  line-height: 1.6;
  margin-bottom: 30px;
}

.reflection-message p {
  margin: 10px 0;
}

.close-reflection-btn {
  padding: 15px 30px;
  border: 2px solid #4169e1;
  border-radius: 25px;
  background: linear-gradient(45deg, #4169e1, #6495ed);
  color: white;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.close-reflection-btn:hover {
  background: linear-gradient(45deg, #6495ed, #87ceeb);
  box-shadow: 0 0 20px rgba(65, 105, 225, 0.5);
  transform: translateY(-2px);
}

/* 动画效果 */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideIn {
  from { 
    transform: translateY(-50px);
    opacity: 0;
  }
  to { 
    transform: translateY(0);
    opacity: 1;
  }
}

@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.1); }
}

@keyframes twinkle {
  0%, 100% { 
    transform: rotate(0deg) scale(1);
    opacity: 0.8;
  }
  50% { 
    transform: rotate(180deg) scale(1.2);
    opacity: 1;
  }
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



.corner-donation-btn {
  background: linear-gradient(45deg, #ff6b6b, #ff8e8e);
  border: 2px solid #ff4444;
  border-radius: 20px;
  padding: 15px 20px;
  color: white;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(255, 68, 68, 0.3);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
}

.corner-donation-btn:hover:not(:disabled) {
  background: linear-gradient(45deg, #ff8e8e, #ffb3b3);
  box-shadow: 0 6px 20px rgba(255, 68, 68, 0.5);
  transform: translateY(-2px);
}

.corner-donation-btn:disabled {
  background: #666;
  border-color: #555;
  cursor: not-allowed;
  opacity: 0.5;
}

.corner-donation-btn .donation-icon {
  font-size: 1.5rem;
  animation: bounce 2s infinite;
}

.corner-donation-btn .donation-text {
  font-size: 0.9rem;
  font-weight: 600;
}

.corner-donation-btn .donation-amount {
  font-size: 0.8rem;
  font-weight: 500;
  opacity: 0.9;
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-3px); }
}

@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.05); }
}

/* 捐赠小学弹窗 */
.donation-school-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(135, 206, 235, 0.8), rgba(255, 182, 193, 0.6));
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 10000;
  animation: fadeIn 0.8s ease-in-out;
}

.donation-school-modal {
  background: linear-gradient(135deg, #f0f8ff, #fff8dc);
  border: 3px solid #8b4513;
  border-radius: 20px;
  padding: 30px;
  max-width: 700px;
  width: 90%;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 0 50px rgba(139, 69, 19, 0.6);
  animation: slideInUp 0.8s ease-out;
}

/* 小学场景 */
.school-scene {
  position: relative;
  height: 300px;
  background: linear-gradient(to bottom, #87ceeb 0%, #98fb98 40%, #8fbc8f 100%);
  border-radius: 15px;
  overflow: hidden;
  margin-bottom: 20px;
}

.school-building {
  position: absolute;
  bottom: 50px;
  left: 50%;
  transform: translateX(-50%);
  width: 200px;
  height: 150px;
}

.school-roof {
  position: absolute;
  top: -20px;
  left: -10px;
  width: 220px;
  height: 40px;
  background: linear-gradient(45deg, #8b4513, #a0522d);
  border-radius: 10px 10px 0 0;
  transform: perspective(100px) rotateX(20deg);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
}

.school-roof::before {
  content: '';
  position: absolute;
  top: -5px;
  left: 10px;
  width: 15px;
  height: 25px;
  background: #654321;
  border-radius: 3px;
}

.school-walls {
  position: relative;
  width: 100%;
  height: 130px;
  background: linear-gradient(to bottom, #ddd 0%, #ccc 50%, #bbb 100%);
  border: 2px solid #999;
  border-radius: 5px;
  box-shadow: inset 0 0 20px rgba(0, 0, 0, 0.2);
}

.school-walls::before {
  content: '';
  position: absolute;
  top: 10px;
  left: 10px;
  width: 180px;
  height: 110px;
  background: repeating-linear-gradient(
    0deg,
    transparent,
    transparent 10px,
    rgba(0, 0, 0, 0.1) 10px,
    rgba(0, 0, 0, 0.1) 11px
  );
}

.school-door {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 30px;
  height: 60px;
  background: linear-gradient(to bottom, #8b4513, #654321);
  border-radius: 5px 5px 0 0;
  border: 1px solid #5d4037;
}

.school-door::before {
  content: '';
  position: absolute;
  top: 25px;
  right: 3px;
  width: 3px;
  height: 3px;
  background: #fff;
  border-radius: 50%;
}

.school-window {
  position: absolute;
  width: 25px;
  height: 25px;
  background: linear-gradient(45deg, #87ceeb, #b0e0e6);
  border: 2px solid #696969;
  border-radius: 3px;
}

.school-window:nth-child(2) {
  top: 30px;
  left: 30px;
}

.school-window:nth-child(3) {
  top: 30px;
  right: 30px;
}

.school-window:nth-child(4) {
  top: 70px;
  left: 30px;
}

.school-window.broken {
  background: linear-gradient(45deg, #555, #333);
  border-color: #333;
}

.school-window.broken::before {
  content: '';
  position: absolute;
  top: 5px;
  left: 5px;
  width: 15px;
  height: 15px;
  background: repeating-linear-gradient(
    45deg,
    transparent,
    transparent 2px,
    #222 2px,
    #222 3px
  );
}

.school-window.cracked::after {
  content: '';
  position: absolute;
  top: 0;
  left: 12px;
  width: 1px;
  height: 100%;
  background: #333;
}

.school-foundation {
  position: absolute;
  bottom: -10px;
  left: -5px;
  width: 210px;
  height: 20px;
  background: linear-gradient(to bottom, #8b4513, #654321);
  border-radius: 0 0 10px 10px;
}

.school-yard {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 50px;
  background: linear-gradient(to bottom, #8fbc8f, #556b2f);
}

.old-tree {
  position: absolute;
  bottom: 0;
  left: 50px;
  width: 15px;
  height: 40px;
  background: linear-gradient(to bottom, #8b4513, #654321);
  border-radius: 50% 50% 0 0;
}

.old-tree::before {
  content: '';
  position: absolute;
  top: -15px;
  left: -10px;
  width: 35px;
  height: 25px;
  background: radial-gradient(circle, #228b22 30%, #006400 70%);
  border-radius: 50%;
  opacity: 0.8;
}

.rusty-fence {
  position: absolute;
  bottom: 0;
  right: 30px;
  width: 80px;
  height: 25px;
  background: repeating-linear-gradient(
    90deg,
    #8b4513,
    #8b4513 3px,
    transparent 3px,
    transparent 8px
  );
  border-bottom: 2px solid #654321;
}

.rusty-fence::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background: #654321;
}

/* 感谢信息 */
.donation-title {
  color: #ff6b6b;
  font-size: 2rem;
  font-weight: 700;
  text-align: center;
  margin-bottom: 15px;
  text-shadow: 2px 2px 4px rgba(255, 107, 107, 0.3);
}

.donation-content {
  color: #2d2d2d;
  font-size: 1.1rem;
  line-height: 1.6;
  text-align: center;
  margin-bottom: 20px;
}

.donation-content p {
  margin: 10px 0;
}

.donation-effects {
  position: relative;
  height: 60px;
  margin-bottom: 20px;
}

.heart-float {
  position: absolute;
  width: 20px;
  height: 20px;
  background: #ff6b6b;
  border-radius: 50%;
  animation: heartFloat 3s infinite ease-in-out;
}

.heart-float::before {
  content: '❤️';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 16px;
}

.heart-float:nth-child(1) { left: 10%; animation-delay: 0s; }
.heart-float:nth-child(2) { left: 25%; animation-delay: 0.5s; }
.heart-float:nth-child(3) { left: 40%; animation-delay: 1s; }
.heart-float:nth-child(4) { left: 55%; animation-delay: 1.5s; }
.heart-float:nth-child(5) { left: 70%; animation-delay: 2s; }
.heart-float:nth-child(6) { left: 85%; animation-delay: 2.5s; }

@keyframes heartFloat {
  0%, 100% {
    transform: translateY(0) scale(1);
    opacity: 0.8;
  }
  50% {
    transform: translateY(-30px) scale(1.2);
    opacity: 1;
  }
}

.close-donation-btn {
  width: 100%;
  padding: 15px;
  background: linear-gradient(45deg, #4169e1, #6495ed);
  border: 2px solid #4169e1;
  border-radius: 25px;
  color: white;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.close-donation-btn:hover {
  background: linear-gradient(45deg, #6495ed, #87ceeb);
  box-shadow: 0 0 20px rgba(65, 105, 225, 0.5);
  transform: translateY(-2px);
}

@keyframes slideInUp {
  from {
    transform: translateY(100px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

/* 加载状态样式 */
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
  backdrop-filter: blur(5px);
}

.loading-spinner {
  text-align: center;
  color: white;
}

.spinner-ring {
  display: inline-block;
  width: 64px;
  height: 64px;
  margin-bottom: 20px;
}

.spinner-ring::after {
  content: " ";
  display: block;
  width: 46px;
  height: 46px;
  margin: 1px;
  border-radius: 50%;
  border: 5px solid #fff;
  border-color: #fff transparent #fff transparent;
  animation: spinning 1.2s linear infinite;
}

@keyframes spinning {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.loading-text {
  font-size: 1.2rem;
  font-weight: 600;
  color: #00ffff;
  text-shadow: 0 0 10px rgba(0, 255, 255, 0.5);
  animation: pulse 2s infinite ease-in-out;
}

@keyframes pulse {
  0%, 100% {
    opacity: 0.6;
  }
  50% {
    opacity: 1;
  }
}

/* 时间统计系统样式 */
.time-stats-panel {
  position: fixed;
  top: 20px;
  right: 20px;
  width: 280px;
  background: rgba(0, 20, 40, 0.95);
  border: 2px solid #00ffff;
  border-radius: 15px;
  padding: 15px;
  box-shadow: 0 0 30px rgba(0, 255, 255, 0.3);
  backdrop-filter: blur(10px);
  z-index: 100;
}

.time-stats-panel h3 {
  color: #00ffff;
  font-size: 1.2rem;
  margin-bottom: 15px;
  text-align: center;
  text-shadow: 0 0 10px rgba(0, 255, 255, 0.5);
}

.time-stats-content {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.advance-day-btn {
  background: linear-gradient(45deg, #ff6b6b, #ff8e8e);
  border: none;
  border-radius: 10px;
  color: white;
  padding: 8px 15px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-left: 10px;
}

.advance-day-btn:hover {
  background: linear-gradient(45deg, #ff8e8e, #ffb3b3);
  box-shadow: 0 0 15px rgba(255, 107, 107, 0.5);
}

.current-day {
  font-size: 1.5rem;
  font-weight: bold;
  color: #00ff00;
  text-align: center;
  margin-bottom: 10px;
  text-shadow: 0 0 10px rgba(0, 255, 0, 0.5);
}

.hospital-status {
  text-align: center;
  padding: 8px;
  border-radius: 8px;
  margin-bottom: 10px;
  font-weight: 600;
  background: rgba(0, 255, 0, 0.1);
  color: #00ff00;
  border: 1px solid #00ff00;
}

.hospital-status.needs-hospital {
  background: rgba(255, 0, 0, 0.1);
  color: #ff0000;
  border: 1px solid #ff0000;
  animation: blink 1s infinite;
}

.last-hospital {
  text-align: center;
  color: #cccccc;
  font-size: 0.9rem;
}

/* 统计信息样式 */
.stats-info {
  background: rgba(0, 255, 255, 0.1);
  border: 1px solid rgba(0, 255, 255, 0.3);
  border-radius: 8px;
  padding: 10px;
}

.stat-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  font-size: 0.9rem;
}

.stat-row:last-child {
  margin-bottom: 0;
}

.stat-row .stat-icon {
  font-size: 1rem;
  width: 20px;
  text-align: center;
}

.stat-row .stat-label {
  color: #cccccc;
  flex: 1;
}

.stat-row .stat-value {
  color: #00ffff;
  font-weight: 600;
  text-shadow: 0 0 5px rgba(0, 255, 255, 0.5);
}

@keyframes blink {
  0%, 50% { opacity: 1; }
  51%, 100% { opacity: 0.5; }
}

/* 成就系统样式 */
.achievement-panel {
  position: fixed;
  width: 260px;
  background: rgba(40, 20, 0, 0.95);
  border: 2px solid #ffd700;
  border-radius: 15px;
  padding: 15px;
  box-shadow: 0 0 30px rgba(255, 215, 0, 0.3);
  backdrop-filter: blur(10px);
  z-index: 100;
}

.achievement-panel.left-top-panel {
  top: 20px;
  left: 20px;
}

.achievement-panel.right-top-panel {
  top: 200px;
  right: 20px;
}

.achievement-panel h3 {
  color: #ffd700;
  font-size: 1.2rem;
  margin-bottom: 15px;
  text-align: center;
  text-shadow: 0 0 10px rgba(255, 215, 0, 0.5);
}

.achievement-btn {
  background: linear-gradient(45deg, #ffd700, #ffed4a);
  border: none;
  border-radius: 10px;
  color: #333;
  padding: 8px 15px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-left: 10px;
}

.achievement-btn:hover {
  background: linear-gradient(45deg, #ffed4a, #fff59d);
  box-shadow: 0 0 15px rgba(255, 215, 0, 0.5);
}

.achievement-summary {
  display: flex;
  justify-content: space-between;
  color: #ffd700;
  font-size: 0.9rem;
}

.achievement-score, .achievement-count {
  text-align: center;
  padding: 5px;
  border-radius: 5px;
  background: rgba(255, 215, 0, 0.1);
}

/* 右下角按钮区域 */
.bottom-right-actions {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 1000;
  display: flex;
  flex-direction: column;
  gap: 15px;
  align-items: flex-end;
}

/* 医院按钮 */
.hospital-corner {
  display: flex;
  justify-content: flex-end;
}

.corner-hospital-btn {
  background: linear-gradient(45deg, #ff6b6b, #ff8e8e);
  border: 2px solid #ff6b6b;
  border-radius: 20px;
  color: white;
  padding: 15px 20px;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
  box-shadow: 0 4px 15px rgba(255, 107, 107, 0.3);
}

.corner-hospital-btn:hover:not(:disabled) {
  background: linear-gradient(45deg, #ff8e8e, #ffb3b3);
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(255, 107, 107, 0.4);
}

.corner-hospital-btn:disabled {
  background: #666;
  border-color: #555;
  cursor: not-allowed;
  opacity: 0.5;
}

.hospital-icon {
  font-size: 1.5rem;
  animation: pulse 2s infinite;
}

.hospital-text {
  font-weight: 600;
  font-size: 0.9rem;
}

/* 捐赠按钮 */
.donation-corner {
  display: flex;
  justify-content: flex-end;
}

.hospital-panel {
  position: absolute;
  bottom: 120px;
  right: 20px;
  width: 300px;
  background: rgba(255, 255, 255, 0.95);
  border: 2px solid #ff6b6b;
  border-radius: 15px;
  padding: 20px;
  box-shadow: 0 0 30px rgba(255, 107, 107, 0.3);
  backdrop-filter: blur(10px);
  z-index: 1050;
}

.hospital-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.hospital-header h3 {
  color: #ff6b6b;
  font-size: 1.2rem;
  margin: 0;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  color: #ff6b6b;
  cursor: pointer;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.close-btn:hover {
  background: rgba(255, 107, 107, 0.1);
}

.hospital-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
  color: #333;
  font-size: 0.9rem;
}

.hospital-services {
  margin-bottom: 15px;
}

.service-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 5px 0;
  color: #333;
  font-size: 0.9rem;
}

.service-icon {
  font-size: 1.2rem;
}

.hospital-treatment-btn {
  width: 100%;
  background: linear-gradient(45deg, #ff6b6b, #ff8e8e);
  border: none;
  border-radius: 10px;
  color: white;
  padding: 12px;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.hospital-treatment-btn:hover:not(:disabled) {
  background: linear-gradient(45deg, #ff8e8e, #ffb3b3);
  box-shadow: 0 0 15px rgba(255, 107, 107, 0.5);
}

.hospital-treatment-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* 医院选项卡样式 */
.hospital-tabs {
  display: flex;
  margin-bottom: 20px;
  border-bottom: 2px solid rgba(255, 107, 107, 0.3);
}

.tab-btn {
  flex: 1;
  padding: 10px;
  background: transparent;
  border: none;
  color: #ff6b6b;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
  border-bottom: 2px solid transparent;
}

.tab-btn.active {
  color: #333;
  border-bottom-color: #ff6b6b;
  background: rgba(255, 107, 107, 0.1);
}

.tab-btn:hover {
  background: rgba(255, 107, 107, 0.05);
}

.volunteer-info {
  background: rgba(0, 255, 0, 0.1);
  border: 1px solid rgba(0, 255, 0, 0.3);
  border-radius: 8px;
  padding: 10px;
  margin-bottom: 15px;
}

.volunteer-description {
  color: #00aa00;
  font-size: 14px;
  margin-bottom: 5px;
}

.volunteer-hours {
  color: #333;
  font-size: 12px;
}

.volunteer-services {
  margin-bottom: 15px;
}

.volunteer-btn {
  width: 100%;
  padding: 12px;
  background: linear-gradient(45deg, #00aa00, #00dd00);
  border: 2px solid #00aa00;
  border-radius: 15px;
  color: white;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.volunteer-btn:hover:not(:disabled) {
  background: linear-gradient(45deg, #00dd00, #00aa00);
  box-shadow: 0 0 20px rgba(0, 170, 0, 0.5);
}

.volunteer-btn:disabled {
  background: #666;
  border-color: #555;
  color: #999;
  cursor: not-allowed;
  opacity: 0.5;
}

.volunteer-progress {
  margin-bottom: 15px;
  text-align: center;
  color: #00aa00;
}

/* 义工系统样式 */
.volunteer-panel {
  position: fixed;
  bottom: 20px;
  left: 320px;
  width: 260px;
  background: rgba(0, 40, 20, 0.95);
  border: 2px solid #00ff00;
  border-radius: 15px;
  padding: 15px;
  box-shadow: 0 0 30px rgba(0, 255, 0, 0.3);
  backdrop-filter: blur(10px);
  z-index: 100;
}

.volunteer-panel h3 {
  color: #00ff00;
  font-size: 1.2rem;
  margin-bottom: 10px;
  text-align: center;
  text-shadow: 0 0 10px rgba(0, 255, 0, 0.5);
}

.volunteer-info {
  text-align: center;
  color: #cccccc;
  font-size: 0.9rem;
  margin-bottom: 15px;
}

.volunteer-hours {
  color: #00ff00;
  font-weight: 600;
  text-align: center;
  margin-bottom: 10px;
}

.volunteer-btn {
  width: 100%;
  background: linear-gradient(45deg, #00ff00, #32ff32);
  border: none;
  border-radius: 10px;
  color: #333;
  padding: 12px;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 600;
}

.volunteer-btn:hover:not(:disabled) {
  background: linear-gradient(45deg, #32ff32, #66ff66);
  box-shadow: 0 0 15px rgba(0, 255, 0, 0.5);
}

.volunteer-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* 成就弹窗样式 */
.achievement-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  backdrop-filter: blur(5px);
}

.achievement-modal {
  background: linear-gradient(135deg, #2d1810, #4a2c18);
  border: 3px solid #ffd700;
  border-radius: 20px;
  padding: 30px;
  max-width: 600px;
  width: 90%;
  max-height: 80vh;
  overflow-y: auto;
  box-shadow: 0 0 50px rgba(255, 215, 0, 0.3);
}

.achievement-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.achievement-title {
  color: #ffd700;
  font-size: 1.8rem;
  font-weight: 700;
  text-shadow: 0 0 15px rgba(255, 215, 0, 0.5);
  margin: 0;
}

.close-achievement-btn {
  background: none;
  border: none;
  font-size: 2rem;
  color: #ffd700;
  cursor: pointer;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.close-achievement-btn:hover {
  background: rgba(255, 215, 0, 0.1);
}

.achievement-stats {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;
}

.stat-item {
  text-align: center;
  padding: 15px;
  background: rgba(255, 215, 0, 0.1);
  border-radius: 10px;
  border: 1px solid #ffd700;
}

.stat-label {
  display: block;
  color: #ffd700;
  font-size: 0.9rem;
  margin-bottom: 5px;
}

.stat-value {
  display: block;
  color: #fff;
  font-size: 1.5rem;
  font-weight: bold;
  text-shadow: 0 0 10px rgba(255, 215, 0, 0.3);
}

.new-achievements {
  margin-bottom: 20px;
}

.new-achievements h3 {
  color: #ffd700;
  text-align: center;
  margin-bottom: 15px;
  font-size: 1.3rem;
}

.achievement-item {
  display: flex;
  align-items: center;
  padding: 15px;
  margin-bottom: 10px;
  background: rgba(255, 215, 0, 0.05);
  border-radius: 10px;
  border: 1px solid rgba(255, 215, 0, 0.3);
  transition: all 0.3s ease;
}

.achievement-item.new {
  background: rgba(255, 215, 0, 0.15);
  border-color: #ffd700;
  animation: newAchievementGlow 2s infinite alternate;
}

.achievement-item.unlocked {
  background: rgba(255, 215, 0, 0.1);
  border-color: #ffd700;
}

.achievement-icon {
  font-size: 2rem;
  margin-right: 15px;
}

.achievement-info {
  flex: 1;
}

.achievement-name {
  color: #ffd700;
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 5px;
}

.achievement-reward {
  color: #32ff32;
  font-size: 0.9rem;
  font-weight: 600;
}

.achievement-status {
  color: #cccccc;
  font-size: 0.9rem;
}

@keyframes newAchievementGlow {
  0% {
    box-shadow: 0 0 10px rgba(255, 215, 0, 0.3);
  }
  100% {
    box-shadow: 0 0 20px rgba(255, 215, 0, 0.6);
  }
}

/* 调整捐赠按钮位置 */
.donation-school {
  bottom: 120px !important;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .time-stats-panel, .achievement-panel {
    width: 250px;
    right: 10px;
  }
  
  .hospital-corner {
    bottom: 10px;
    right: 10px;
  }
  
  .volunteer-panel {
    width: 250px;
    bottom: 10px;
    left: 10px;
  }
  
  .hospital-panel {
    width: 280px;
  }
}

/* 统合信息面板样式 */
.unified-info-panel {
  position: absolute;
  top: 20px;
  right: 20px;
  width: 320px;
  background: rgba(0, 255, 255, 0.1);
  border: 2px solid #00ffff;
  border-radius: 15px;
  padding: 20px;
  backdrop-filter: blur(10px);
  box-shadow: 0 0 30px rgba(0, 255, 255, 0.5);
  z-index: 1000;
  max-height: 600px;
  overflow-y: auto;
}

.unified-info-panel .panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.unified-info-panel .panel-title {
  color: #00ffff;
  font-size: 1.2rem;
  margin: 0;
  font-weight: 600;
}

.unified-info-panel .advance-day-btn {
  background: linear-gradient(45deg, #00ffff, #ff00ff);
  border: none;
  border-radius: 12px;
  color: #000;
  padding: 8px 16px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 600;
}

.unified-info-panel .advance-day-btn:hover {
  background: linear-gradient(45deg, #ff00ff, #00ffff);
  box-shadow: 0 0 15px rgba(0, 255, 255, 0.5);
}

/* 选项卡样式 */
.panel-tabs {
  display: flex;
  margin-bottom: 20px;
  border-radius: 12px;
  overflow: hidden;
  background: rgba(0, 0, 0, 0.3);
}

.tab-button {
  flex: 1;
  padding: 12px;
  background: transparent;
  border: none;
  color: rgba(255, 255, 255, 0.7);
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
}

.tab-button.active {
  color: #00ffff;
  background: rgba(0, 255, 255, 0.2);
}

.tab-button:hover {
  color: #00ffff;
  background: rgba(0, 255, 255, 0.1);
}

.tab-button.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, #00ffff, #ff00ff);
}

/* 选项卡内容 */
.tab-content {
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

/* 游戏信息样式 */
.time-info {
  margin-bottom: 20px;
}

.current-day {
  font-size: 1.3rem;
  font-weight: bold;
  color: #00ffff;
  text-align: center;
  margin-bottom: 10px;
  text-shadow: 0 0 10px rgba(0, 255, 255, 0.5);
}

.hospital-status {
  text-align: center;
  padding: 8px 12px;
  border-radius: 8px;
  font-size: 0.9rem;
  margin-bottom: 5px;
  background: rgba(0, 255, 0, 0.1);
  color: #00ff00;
  border: 1px solid rgba(0, 255, 0, 0.3);
}

.hospital-status.needs-hospital {
  background: rgba(255, 0, 0, 0.1);
  color: #ff4444;
  border-color: rgba(255, 0, 0, 0.3);
  animation: pulse 2s infinite;
}

.last-hospital {
  text-align: center;
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.8rem;
}

.stats-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.stat-row {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.stat-icon {
  font-size: 1.2rem;
  width: 20px;
  text-align: center;
}

.stat-label {
  flex: 1;
  color: rgba(255, 255, 255, 0.9);
  font-size: 0.9rem;
}

.stat-value {
  color: #00ffff;
  font-weight: bold;
  font-size: 0.9rem;
}

/* 健康状态样式 */
.health-warning {
  background: rgba(255, 0, 0, 0.2);
  border: 1px solid #ff4444;
  border-radius: 8px;
  padding: 10px;
  text-align: center;
  color: #ff4444;
  font-weight: bold;
  margin-bottom: 15px;
  animation: pulse 2s infinite;
}

.health-stats {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.health-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px;
  background: rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  transition: all 0.3s ease;
}

.health-item.pulse {
  border-color: #ff4444;
}

.health-item.critical-health {
  border-color: #ff4444;
  background: rgba(255, 0, 0, 0.1);
}

.health-item.critical-health.pulse {
  animation: healthPulse 0.5s infinite;
}

@keyframes healthPulse {
  0%, 100% { border-color: #ff4444; }
  50% { border-color: #ff0000; }
}

.health-label {
  font-size: 0.9rem;
  color: rgba(255, 255, 255, 0.9);
  min-width: 80px;
}

.health-bar {
  flex: 1;
  height: 8px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 4px;
  overflow: hidden;
  position: relative;
}

.health-fill {
  height: 100%;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.health-fill.pulsing {
  animation: healthFillPulse 0.5s infinite;
}

@keyframes healthFillPulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.7; }
}

.health-fill.lung {
  background: linear-gradient(90deg, #ff4444, #ff6b6b);
}

.health-fill.heart {
  background: linear-gradient(90deg, #e74c3c, #c0392b);
}

.health-fill.liver {
  background: linear-gradient(90deg, #f39c12, #e67e22);
}

.health-fill.immunity {
  background: linear-gradient(90deg, #9b59b6, #8e44ad);
}

.health-fill.critical {
  background: linear-gradient(90deg, #ff0000, #cc0000);
}

.health-value {
  color: #00ffff;
  font-weight: bold;
  font-size: 0.9rem;
  min-width: 40px;
  text-align: right;
}

.health-value.critical-text {
  color: #ff4444;
  animation: textFlash 1s infinite;
}

.health-value.jumping-value {
  animation: jumpingValue 0.3s infinite;
}

@keyframes textFlash {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

@keyframes jumpingValue {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.1); }
}

.health-number {
  color: #00ffff;
  font-weight: bold;
  font-size: 0.9rem;
  min-width: 80px;
  text-align: right;
}

.health-number.jumping-value {
  animation: jumpingValue 0.3s infinite;
}

.life-expectancy {
  border-color: #ff00ff;
  background: rgba(255, 0, 255, 0.1);
}

.life-expectancy .health-number {
  color: #ff00ff;
  font-size: 1rem;
}

/* 传统主题样式 */
.theme-traditional .unified-info-panel {
  background: rgba(255, 255, 255, 0.95);
  border: 2px solid #8b4513;
  box-shadow: 0 0 20px rgba(139, 69, 19, 0.3);
}

.theme-traditional .unified-info-panel .panel-title {
  color: #8b4513;
}

.theme-traditional .unified-info-panel .advance-day-btn {
  background: linear-gradient(45deg, #8b4513, #a0522d);
  color: white;
}

.theme-traditional .panel-tabs {
  background: rgba(139, 69, 19, 0.1);
}

.theme-traditional .tab-button {
  color: #8b4513;
}

.theme-traditional .tab-button.active {
  color: #654321;
  background: rgba(139, 69, 19, 0.2);
}

.theme-traditional .tab-button.active::after {
  background: linear-gradient(90deg, #8b4513, #a0522d);
}

.theme-traditional .current-day {
  color: #8b4513;
}

.theme-traditional .hospital-status {
  color: #228b22;
  background: rgba(34, 139, 34, 0.1);
  border-color: rgba(34, 139, 34, 0.3);
}

.theme-traditional .hospital-status.needs-hospital {
  color: #dc143c;
  background: rgba(220, 20, 60, 0.1);
  border-color: rgba(220, 20, 60, 0.3);
}

.theme-traditional .last-hospital {
  color: #666;
}

.theme-traditional .stat-row {
  background: rgba(139, 69, 19, 0.1);
  border-color: rgba(139, 69, 19, 0.2);
}

.theme-traditional .stat-label {
  color: #333;
}

.theme-traditional .stat-value {
  color: #8b4513;
}

.theme-traditional .health-item {
  background: rgba(139, 69, 19, 0.1);
  border-color: rgba(139, 69, 19, 0.2);
}

.theme-traditional .health-label {
  color: #333;
}

.theme-traditional .health-value {
  color: #8b4513;
}

.theme-traditional .health-number {
  color: #8b4513;
}

.theme-traditional .life-expectancy {
  border-color: #8b4513;
  background: rgba(139, 69, 19, 0.1);
}

.theme-traditional .life-expectancy .health-number {
  color: #8b4513;
}
</style> 