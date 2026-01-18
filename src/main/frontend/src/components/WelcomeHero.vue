<script setup lang="ts">
import {type Router, useRouter} from 'vue-router'

const router: Router = useRouter();

function handleSignUp(): void {
  router.push('/sign-up');
}
</script>

<template>
  <div class="welcome-hero">
    <p class="title">Welcome to Perso|||et</p>
    <p class="subtitle">The best way to manage and organize your contacts effortlessly.</p>

    <button @click="handleSignUp">Start Managing Contacts</button>
  </div>
</template>

<style scoped>
.welcome-hero {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 90vh;
  text-align: center;
  box-sizing: border-box;
  transform: scale(min(1, 100vw / 800));
  transition: transform 0.3s ease;
  gap: 1.5rem;
}

.welcome-hero p {
  font-weight: bold;
  margin: 0;
  line-height: 1;
}

.title {
  font-size: 3rem;
  color: #1E90FF;
}

.subtitle {
  font-size: 1rem;
  color: white;
}

button {
  font-weight: bold;
  color: white;
  background-color: transparent;
  border: 2px solid #1E90FF;
  border-radius: 0.5rem;
  transition: background-color 0.3s;

  padding: 10px 15px;
}

button:hover {
  background-color: rgba(32, 138, 243, 0.2);
}
</style>