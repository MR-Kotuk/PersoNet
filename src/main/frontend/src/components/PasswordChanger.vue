<script setup lang="ts">
import {ref} from 'vue';
import axios from 'axios';

interface PasswordForm {
  current: String;
  new: String;
  confirm: String;
}

const emptyPasswordForm = {
  current: '',
  new: '',
  confirm: ''
}

const showPasswordChange = ref(false);
const passwordForm = ref<PasswordForm>(emptyPasswordForm);

const isLoading = ref(false);
const message = ref('');
const isError = ref(false);

function togglePasswordChange() {

  showPasswordChange.value = !showPasswordChange.value;

  if (!showPasswordChange.value) {
    resetForm();
  }
}

function resetForm() {

  passwordForm.value = emptyPasswordForm;

  message.value = '';
  isError.value = false;
}

async function handleChangePassword() {

  if (!isValidPasswordForm(passwordForm.value)) {
    return;
  }

  isLoading.value = true;
  message.value = '';

  try {
    const token = localStorage.getItem('access_token');

    const response = await axios.put('/api/account/set-password', {
      password: passwordForm.value.current,
      newPassword: passwordForm.value.new
    }, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    if (response.status === 200) {

      message.value = 'Password changed successfully';
      isError.value = false;
      resetForm();

      setTimeout(() => {
        showPasswordChange.value = false;
        message.value = '';
      }, 2000);
    }

  } catch (error: unknown) {

    if (axios.isAxiosError(error)) {

      if (error.response?.status === 400) {
        message.value = error.response.data || 'Wrong password';
      } else {
        message.value = error.response?.data || 'Failed to change password';
      }

    } else {
      message.value = 'Unexpected error occurred';
    }

    isError.value = true;

  } finally {
    isLoading.value = false;
  }
}

function isValidPasswordForm(passwordForm: PasswordForm): boolean {

  if (!passwordForm.current || !passwordForm.new || !passwordForm.confirm) {

    message.value = 'Please fill in all fields';
    isError.value = true;

    return false;
  }

  if (passwordForm.new !== passwordForm.confirm) {

    message.value = 'New passwords do not match';
    isError.value = true;

    return false;
  }

  if (passwordForm.new.length < 4) {

    message.value = 'New password must be at least 4 characters';
    isError.value = true;

    return false;
  }

  return true;
}

</script>

<template>
  <div class="setting-item">
    <label class="setting-label">Password</label>
    <button @click="togglePasswordChange" class="change-password-button">
      {{ showPasswordChange ? 'Cancel' : 'Change Password' }}
    </button>

    <div v-if="showPasswordChange" class="password-change-section">
      <div class="input-group">
        <label for="current-password" class="input-label">Current Password</label>
        <input
            id="current-password"
            v-model="passwordForm.current"
            type="password"
            placeholder="Enter current password"
            :disabled="isLoading"
            class="input-field"
        />
      </div>

      <div class="input-group">
        <label for="new-password" class="input-label">New Password</label>
        <input
            id="new-password"
            v-model="passwordForm.new"
            type="password"
            placeholder="Enter new password"
            :disabled="isLoading"
            class="input-field"
        />
      </div>

      <div class="input-group">
        <label for="confirm-password" class="input-label">Confirm New Password</label>
        <input
            id="confirm-password"
            v-model="passwordForm.confirm"
            type="password"
            placeholder="Confirm new password"
            :disabled="isLoading"
            class="input-field"
        />
      </div>

      <button
          @click="handleChangePassword"
          class="submit-button"
          :disabled="isLoading"
      >
        <span v-if="!isLoading">Update Password</span>
        <span v-else class="spinner"></span>
      </button>

      <div v-if="message" class="message" :class="{ error: isError, success: !isError }">
        {{ message }}
      </div>
    </div>
  </div>
</template>

<style scoped>
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

.change-password-button {
  width: fit-content;
  padding: 0.5rem 1rem;
  background-color: transparent;
  color: #1E90FF;
  border: 2px solid #1E90FF;
  border-radius: 6px;
  font-size: 0.9rem;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s;
}

.change-password-button:hover {
  background-color: rgba(30, 144, 255, 0.1);
}

.password-change-section {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 1rem;
  background-color: rgba(40, 40, 40, 0.3);
  border: 1px solid #282828;
  border-radius: 8px;
  margin-top: 0.5rem;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.input-label {
  color: rgba(255, 255, 255, 0.87);
  font-size: 0.9rem;
  font-weight: 500;
}

.input-field {
  width: 100%;
  padding: 0.75rem;
  background-color: rgba(40, 40, 40, 0.5);
  border: 1px solid #282828;
  border-radius: 6px !important;
  color: rgba(255, 255, 255, 0.87);
  font-size: 1rem;
  transition: border-color 0.3s, background-color 0.3s;
  box-sizing: border-box;
}

.input-field:focus {
  outline: none;
  border-color: #1E90FF;
  background-color: rgba(40, 40, 40, 0.7);
}

.input-field:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.submit-button {
  width: 100%;
  padding: 0.75rem;
  background-color: #1E90FF;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-button:hover:not(:disabled) {
  background-color: #1873CC;
}

.submit-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.spinner {
  display: inline-block;
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.message {
  padding: 0.75rem;
  border-radius: 6px;
  font-size: 0.85rem;
  text-align: center;
}

.message.success {
  background-color: rgba(30, 144, 255, 0.1);
  border: 1px solid #1E90FF;
  color: #1E90FF;
}

.message.error {
  background-color: rgba(255, 0, 128, 0.1);
  border: 1px solid #FF0080;
  color: #FF0080;
}
</style>