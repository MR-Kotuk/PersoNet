<script setup lang="ts">
import {onMounted, ref} from 'vue';
import {useRouter} from 'vue-router';
import axios from 'axios';
import AccountInfo from '@/components/AccountInfo.vue';

interface AccountData {
  id: number;
  username: string;
  email: string;
  password: string;
  verified: boolean;
}

const router = useRouter();
const accountData = ref<AccountData | null>(null);
const isLoading = ref<boolean>(true);

async function fetchAccountInfo() {

  const token = localStorage.getItem('access_token');

  if (!token) {

    await router.push('/sign-in');
    return;
  }

  isLoading.value = true;

  try {
    const response = await axios.get('/api/account/', {
      headers: {
        Authorization: `Bearer ${token}`
      },

      validateStatus: (status) => (status >= 200 && status < 300) || status === 302
    });

    accountData.value = response.data;

  } catch (error: unknown) {

    if (axios.isAxiosError(error) && error.response?.status === 500) {

      localStorage.removeItem('access_token');
      await router.push('/sign-in');

    } else {
      console.error('Failed to fetch account info:', error);
    }

  } finally {
    isLoading.value = false;
  }
}

function handleUsernameChanged(newUsername: string) {
  if (accountData.value) {
    accountData.value.username = newUsername;
  }
}

onMounted(() => {
  fetchAccountInfo();
});
</script>

<template>
  <div class="account-container">
    <div class="account-content">
      <h1 class="page-title">Settings</h1>

      <div v-if="isLoading" class="loading">
        <span class="spinner"></span>
      </div>

      <AccountInfo
          v-else-if="accountData"
          :account-data="accountData"
          @username-changed="handleUsernameChanged"
      />
    </div>
  </div>
</template>

<style scoped>
.account-container {
  display: flex;
  justify-content: center;
  min-height: 90vh;
  padding: 40px 20px;
  box-sizing: border-box;
}

.account-content {
  width: 100%;
  max-width: 900px;
}

.page-title {
  color: #1E90FF;
  font-size: 2.5rem;
  font-weight: bold;
  margin: 0 0 2rem 0;
  text-align: center;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
}

.spinner {
  width: 40px;
  height: 40px;
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

@media (max-width: 768px) {
  .page-title {
    font-size: 2rem;
  }
}
</style>