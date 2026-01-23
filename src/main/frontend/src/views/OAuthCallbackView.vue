<script setup lang="ts">
import {onMounted} from 'vue';
import {useRoute, useRouter} from 'vue-router';

const router = useRouter();
const route = useRoute();

onMounted(() => {

  const token = route.query.token as string;

  if (token) {
    localStorage.setItem('access_token', token);
    router.push('/');

  } else {
    console.error('No token found in OAuth callback');
    router.push('/sign-in');
  }
});
</script>

<template>
  <div class="oauth-callback">
    <div class="callback-container">
      <div class="spinner"></div>
      <p class="callback-text">Completing sign in...</p>
    </div>
  </div>
</template>

<style scoped>
.oauth-callback {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #0a0a0a 0%, #1a1a1a 100%);
}

.callback-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2rem;
  padding: 3rem;
  background-color: rgba(40, 40, 40, 0.5);
  border: 1px solid #282828;
  border-radius: 16px;
  backdrop-filter: blur(10px);
}

.spinner {
  width: 48px;
  height: 48px;
  border: 4px solid rgba(30, 144, 255, 0.2);
  border-top-color: #1E90FF;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.callback-text {
  color: rgba(255, 255, 255, 0.87);
  font-size: 1.1rem;
  font-weight: 500;
  letter-spacing: 0.02em;
}
</style>