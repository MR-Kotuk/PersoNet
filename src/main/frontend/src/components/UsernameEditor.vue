<script setup lang="ts">
import {ref} from 'vue';
import axios from 'axios';

const props = defineProps<{
  username: string;
}>();

const emit = defineEmits<{
  (e: 'username-changed', newUsername: string): void;
}>();

const isEditing = ref(false);
const editedUsername = ref('');
const isLoading = ref(false);
const message = ref('');
const isError = ref(false);

function startEdit() {

  isEditing.value = true;

  editedUsername.value = props.username;
  message.value = '';
}

function cancelEdit() {

  isEditing.value = false;

  editedUsername.value = '';
  message.value = '';
}

async function save() {

  if (!editedUsername.value.trim()) {

    message.value = 'Username cannot be empty';
    isError.value = true;

    return;
  }

  if (editedUsername.value === props.username) {

    cancelEdit();
    return;
  }

  isLoading.value = true;
  message.value = '';

  try {
    const token = localStorage.getItem('access_token');

    const response = await axios.put('/api/account/set-username', editedUsername.value, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'text/plain'
      }
    });

    if (response.status === 200) {

      emit('username-changed', editedUsername.value);

      isEditing.value = false;
      message.value = 'Username updated successfully';
      isError.value = false;

      setTimeout(() => {
        message.value = '';
      }, 3000);
    }

  } catch (error: unknown) {

    if (axios.isAxiosError(error)) {
      message.value = error.response?.data || 'Failed to change username';
    } else {
      message.value = 'Unexpected error occurred';
    }

    isError.value = true;

  } finally {
    isLoading.value = false;
  }
}
</script>

<template>
  <div class="setting-item">
    <label class="setting-label">Username</label>

    <div v-if="!isEditing" class="setting-value-row">
      <span class="setting-value">{{ username }}</span>
      <button @click="startEdit" class="icon-button" title="Edit username">
        <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path
              d="M11.333 2.00004C11.5084 1.82463 11.7163 1.68648 11.9451 1.59345C12.1739 1.50043 12.419 1.45435 12.6663 1.45837C12.9137 1.46239 13.1572 1.51644 13.3827 1.61729C13.6083 1.71814 13.8114 1.86388 13.9813 2.04629C14.1512 2.22869 14.2847 2.44407 14.3742 2.67923C14.4638 2.91439 14.5078 3.16476 14.5038 3.41671C14.4998 3.66865 14.4479 3.91752 14.3512 4.14986C14.2545 4.3822 14.1149 4.59354 13.9397 4.77004L5.15967 13.55L1.66634 14.3334L2.44967 10.84L11.333 2.00004Z"
              stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </button>
    </div>

    <div v-else class="edit-row">
      <input
          v-model="editedUsername"
          type="text"
          class="edit-input"
          :disabled="isLoading"
          @keyup.enter="save"
          @keyup.escape="cancelEdit"
          autofocus
      />
      <button
          @click="save"
          class="icon-button success"
          :disabled="isLoading"
          title="Save"
      >
        <svg v-if="!isLoading" width="16" height="16" viewBox="0 0 16 16" fill="none"
             xmlns="http://www.w3.org/2000/svg">
          <path d="M13.3333 4L6 11.3333L2.66667 8" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                stroke-linejoin="round"/>
        </svg>
        <span v-else class="mini-spinner"></span>
      </button>
      <button
          @click="cancelEdit"
          class="icon-button danger"
          :disabled="isLoading"
          title="Cancel"
      >
        <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M12 4L4 12M4 4L12 12" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                stroke-linejoin="round"/>
        </svg>
      </button>
    </div>

    <div v-if="message" class="message" :class="{ error: isError, success: !isError }">
      {{ message }}
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

.setting-value-row {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.setting-value {
  color: rgba(255, 255, 255, 0.87);
  font-size: 1rem;
  font-weight: 500;
}

.edit-row {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.edit-input {
  flex: 1;
  padding: 0.5rem 0.75rem;
  background-color: rgba(40, 40, 40, 0.5);
  border: 1px solid #282828;
  border-radius: 6px;
  color: rgba(255, 255, 255, 0.87);
  font-size: 1rem;
  transition: border-color 0.3s, background-color 0.3s;
}

.edit-input:focus {
  outline: none;
  border-color: #1E90FF;
  background-color: rgba(40, 40, 40, 0.7);
}

.edit-input:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.icon-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  background-color: transparent;
  border: 1px solid #282828;
  border-radius: 6px;
  color: #666;
  cursor: pointer;
  transition: all 0.3s;
  padding: 0;
}

.icon-button:hover:not(:disabled) {
  border-color: #1E90FF;
  color: #1E90FF;
}

.icon-button.success:hover:not(:disabled) {
  border-color: #1E90FF;
  background-color: rgba(30, 144, 255, 0.1);
  color: #1E90FF;
}

.icon-button.danger:hover:not(:disabled) {
  border-color: #FF0080;
  background-color: rgba(255, 0, 128, 0.1);
  color: #FF0080;
}

.icon-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.mini-spinner {
  display: inline-block;
  width: 14px;
  height: 14px;
  border: 2px solid rgba(30, 144, 255, 0.3);
  border-top-color: #1E90FF;
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