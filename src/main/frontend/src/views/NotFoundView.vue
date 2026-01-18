<script setup lang="ts">
import {onMounted, onUnmounted, ref} from 'vue';
import {useRouter} from 'vue-router';

const router = useRouter();
const glitchActive = ref(false);

let glitchInterval: number;

const handleGoHome = async () => {
  await router.push('/');
};

const handleGoBack = () => {
  router.back();
};

onMounted(() => {
  glitchInterval = window.setInterval(() => {
    glitchActive.value = true;
    setTimeout(() => {
      glitchActive.value = false;
    }, 200);
  }, 3000);
});

onUnmounted(() => {
  clearInterval(glitchInterval);
});
</script>

<template>
  <div class="not-found-container">
    <div class="content">
      <!-- 404 with glitch effect -->
      <div class="error-code-wrapper">
        <h1 class="error-code">404</h1>
        <h1
            class="error-code-shadow shadow-red"
            :class="{ 'glitch-left': glitchActive }"
        >
          404
        </h1>
        <h1
            class="error-code-shadow shadow-cyan"
            :class="{ 'glitch-right': glitchActive }"
        >
          404
        </h1>
      </div>

      <!-- Message -->
      <h2 class="title">Lost in the Digital Void</h2>
      <p class="message">
        The page you're searching for has drifted into the unknown.
        <br/>
        It might have been moved, deleted, or never existed.
      </p>

      <!-- Action buttons -->
      <div class="button-group">
        <button
            @click="handleGoHome"
            class="primary-button"
        >
          Return Home
        </button>
        <button
            @click="handleGoBack"
            class="secondary-button"
        >
          Go Back
        </button>
      </div>

      <!-- Decorative elements -->
      <div class="decorative-box">
        <div class="corner corner-tl"/>
        <div class="corner corner-tr"/>
        <div class="corner corner-bl"/>
        <div class="corner corner-br"/>
      </div>
    </div>
  </div>
</template>

<style scoped>
@keyframes float {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
    opacity: 0.3;
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
    opacity: 0.6;
  }
}

.not-found-container {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: transparent;
  overflow: hidden;
  padding: 20px;
  box-sizing: border-box;
}

.content {
  position: relative;
  z-index: 1;
  text-align: center;
  max-width: 600px;
  width: 100%;
}

.error-code-wrapper {
  position: relative;
  margin-bottom: 2rem;
  display: inline-block;
}

.error-code {
  position: relative;
  font-size: clamp(6rem, 20vw, 12rem);
  font-weight: bold;
  color: #1E90FF;
  margin: 0;
  line-height: 1;
  text-shadow: 0 0 30px rgba(30, 144, 255, 0.5);
  letter-spacing: 0.1em;
}

.error-code-shadow {
  position: absolute;
  top: 0;
  left: 0;
  font-size: clamp(6rem, 20vw, 12rem);
  font-weight: bold;
  margin: 0;
  line-height: 1;
  letter-spacing: 0.1em;
  opacity: 0.5;
  transition: transform 0.1s ease;
}

.error-code-shadow.glitch-left {
  transform: translateX(-2px);
}

.error-code-shadow.glitch-right {
  transform: translateX(2px);
}

.shadow-red {
  color: #FF0080;
}

.shadow-cyan {
  color: #00FFFF;
}

.title {
  font-size: clamp(1.5rem, 4vw, 2rem);
  font-weight: bold;
  color: rgba(255, 255, 255, 0.87);
  margin: 0 0 1rem 0;
  letter-spacing: 0.05em;
}

.message {
  font-size: clamp(0.9rem, 2vw, 1rem);
  color: #666;
  line-height: 1.6;
  margin: 0 0 3rem 0;
}

.button-group {
  display: flex;
  gap: 1rem;
  justify-content: center;
  flex-wrap: wrap;
}

.primary-button {
  font-weight: bold;
  color: white;
  background-color: transparent;
  border: 2px solid #1E90FF;
  border-radius: 0.5rem;
  transition: background-color 0.3s;
  padding: 10px 15px;
  cursor: pointer;
  font-size: 1rem;
}

.primary-button:hover {
  background-color: rgba(32, 138, 243, 0.2);
}

.secondary-button {
  padding: 10px 15px;
  background-color: transparent;
  color: #666;
  border: 2px solid #282828;
  border-radius: 0.5rem;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
}

.secondary-button:hover {
  border-color: #1E90FF;
  color: #1E90FF;
}

.decorative-box {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: calc(100% + 40px);
  height: calc(100% + 40px);
  pointer-events: none;
  z-index: -1;
}

.corner {
  position: absolute;
  width: 20px;
  height: 20px;
}

.corner-tl {
  top: 0;
  left: 0;
  border-top: 2px solid rgba(30, 144, 255, 0.3);
  border-left: 2px solid rgba(30, 144, 255, 0.3);
}

.corner-tr {
  top: 0;
  right: 0;
  border-top: 2px solid rgba(30, 144, 255, 0.3);
  border-right: 2px solid rgba(30, 144, 255, 0.3);
}

.corner-bl {
  bottom: 0;
  left: 0;
  border-bottom: 2px solid rgba(30, 144, 255, 0.3);
  border-left: 2px solid rgba(30, 144, 255, 0.3);
}

.corner-br {
  bottom: 0;
  right: 0;
  border-bottom: 2px solid rgba(30, 144, 255, 0.3);
  border-right: 2px solid rgba(30, 144, 255, 0.3);
}
</style>