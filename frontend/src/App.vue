<template>
  <div class="app">
    <div class="cyber-grid">
      <div class="grid-line horizontal" v-for="i in 20" :key="'h' + i"></div>
      <div class="grid-line vertical" v-for="i in 20" :key="'v' + i"></div>
    </div>
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style scoped>
.app {
  width: 100vw;
  height: 100vh;
  background: radial-gradient(circle at 50% 50%, #1a1a2e 0%, #0a0a0a 100%);
  position: relative;
  overflow: hidden;
  transition: all 0.8s ease;
}

/* 传统主题下的App样式 */
:global(.theme-traditional) .app {
  background: linear-gradient(135deg, #f5f5dc 0%, #e6ddc4 50%, #d4c5a9 100%);
}

.cyber-grid {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0.1;
  z-index: 1;
  transition: all 0.8s ease;
}

/* 传统主题下的网格样式 */
:global(.theme-traditional) .cyber-grid {
  opacity: 0.08;
}

.grid-line {
  position: absolute;
  background: var(--cyber-cyan);
  transition: all 0.8s ease;
}

/* 传统主题下的网格线条 */
:global(.theme-traditional) .grid-line {
  background: #8b4513;
}

.grid-line.horizontal {
  width: 100%;
  height: 1px;
  top: calc(var(--i) * 5%);
}

.grid-line.vertical {
  height: 100%;
  width: 1px;
  left: calc(var(--i) * 5%);
}

.grid-line:nth-child(odd) {
  opacity: 0.3;
}

.grid-line:nth-child(even) {
  opacity: 0.1;
}
</style> 