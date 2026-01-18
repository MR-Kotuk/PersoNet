<script setup lang="ts">
import {useRouter} from 'vue-router';
import UsernameEditor from '@/components/UsernameEditor.vue';
import PasswordChanger from '@/components/PasswordChanger.vue';

interface AccountData {
  username: string;
  email: string;
  password: string;
}

defineProps<{
  accountData: AccountData;
}>();

const emit = defineEmits<{
  (e: 'username-changed', newUsername: string): void;
}>();

const router = useRouter();

function handleLogout() {

  localStorage.removeItem('access_token');
  router.push('/sign-in');
}

function handleUsernameChanged(newUsername: string) {
  emit('username-changed', newUsername);
}
</script>

<template>
  <div class="account-card">
    <div class="card-header">
      <h2 class="card-title">Account Settings</h2>
    </div>

    <div class="settings-section">
      <UsernameEditor
          :username="accountData.username"
          @username-changed="handleUsernameChanged"
      />

      <div class="setting-item">
        <label class="setting-label">Email</label>
        <div class="setting-value">{{ accountData.email }}</div>
      </div>

      <PasswordChanger/>
    </div>

    <button @click="handleLogout" class="logout-button">
      <svg width="18" height="18" viewBox="0 0 18 18" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path
            d="M6.5 16H3.5C3.10218 16 2.72064 15.842 2.43934 15.5607C2.15804 15.2794 2 14.8978 2 14.5V3.5C2 3.10218 2.15804 2.72064 2.43934 2.43934C2.72064 2.15804 3.10218 2 3.5 2H6.5M12 13L16 9M16 9L12 5M16 9H6.5"
            stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      </svg>
      Logout
    </button>
  </div>
</template>

<style scoped>
.account-card {
  background: rgba(10, 10, 10, 0.8);
  border: 1px solid #282828;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 8px 32px rgba(30, 144, 255, 0.1);
  position: relative;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.card-title {
  color: #1E90FF;
  font-size: 1.5rem;
  font-weight: bold;
  margin: 0;
}

.logout-button {
  position: absolute;
  bottom: 2rem;
  right: 2rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background-color: transparent;
  color: #ff3366;
  border: 2px solid #ff3366;
  border-radius: 6px;
  font-size: 0.9rem;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s;
}

.logout-button:hover {
  background-color: rgba(255, 51, 102, 0.1);
  box-shadow: 0 0 12px rgba(255, 51, 102, 0.3);
}

.settings-section {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  padding-bottom: 4rem;
}

.setting-item {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.setting-label {
  color: #666;
  font-size: 0.875rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.setting-value {
  color: rgba(255, 255, 255, 0.87);
  font-size: 1rem;
  font-weight: 500;
}

@media (max-width: 640px) {
  .logout-button {
    position: static;
    width: 100%;
    justify-content: center;
    margin-top: 1rem;
  }

  .settings-section {
    padding-bottom: 0;
  }
}
</style>