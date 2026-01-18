<script setup lang="ts">
import {nextTick, onMounted, ref} from 'vue';
import axios from 'axios';
import {useRouter} from 'vue-router';

const router = useRouter();
const username = ref<string | null>(null);

function handleSignUp(): void {
  router.push('/sign-up');
}

async function fetchUsername(): Promise<void> {

  const token = localStorage.getItem('access_token');
  if (!token) {
    return;
  }

  try {
    const response = await axios.get<string>('/api/', {
      headers: {Authorization: `Bearer ${token}`},
    });

    if (response.status === 200) {
      username.value = response.data;
      await nextTick();
    }
  } catch (error) {
    console.error(error);
  }
}

function handleGoToContacts(): void {
  router.push('/contacts');
}

onMounted(() => {
  fetchUsername();
});
</script>

<template>
  <div class="welcome-hero">
    <p class="title">
      <span class="default-text">Welcome</span>
      <span v-if="username" class="highlight-text">{{ username }}</span>
      <span class="default-text">to</span>
      <span class="highlight-text">Perso</span>
      <span class="default-text n-lines">|||</span>
      <span class="highlight-text">et</span>
    </p>

    <p v-if="!username" class="subtitle">
      The best way to manage and organize your contacts effortlessly.
    </p>

    <button v-if="!username" @click="handleSignUp">Start Managing Contacts</button>
    <button v-else @click="handleGoToContacts">Go to Contacts</button>
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
  gap: 0.8rem;
}

.title {
  font-size: 2.3rem;
  display: flex;
  gap: 0.8rem;
  justify-content: center;
  font-weight: bold;
  margin: 0;
}

.n-lines {
  margin: -3px -0.8rem;
}

.default-text {
  color: #1E90FF;
}

.highlight-text {
  color: white;
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