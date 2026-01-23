<script setup lang="ts">
import {nextTick, reactive, ref} from 'vue';
import {useRouter} from 'vue-router';
import axios from 'axios';

const router = useRouter();

const otpValues = reactive<string[]>(['', '', '', '']);
const inputRefs = ref<HTMLInputElement[]>([]);

const isLoading = ref<boolean>(false);

async function handleVerify() {

  const otpCode = otpValues.join('');

  if (otpCode.length !== 4) {
    return;
  }

  isLoading.value = true;

  try {
    const response = await axios.get(`/api/auth/verify-email/${otpCode}`);

    if (response.status === 200) {
      localStorage.setItem('access_token', response.data);
      await router.push('/');
    }

  } catch (error: unknown) {

    isLoading.value = false;
    console.error('OTP verification error:', error);

    if (axios.isAxiosError(error) && error.response?.status === 400) {

      alert(error.response.data.message || 'Invalid OTP code');

      otpValues.fill('');
      inputRefs.value[0]?.focus();
    }
  }
}

async function handleResend() {

  const email = sessionStorage.getItem('otp_email');

  if (!email) {

    await router.push('/login');
    return;
  }

  try {
    await axios.post(`/api/auth/verify-email/send/${email}`);

  } catch (error: unknown) {
    console.error('Resend error:', error);
  }
}

function handleInput(index: number, event: Event) {

  const input = event.target as HTMLInputElement;
  const value = input.value;

  if (value.length > 1) {
    input.value = value.charAt(value.length - 1);
  }

  otpValues[index] = input.value;

  if (input.value && index < 3) {
    nextTick(() => {
      inputRefs.value[index + 1]?.focus();
    });
  }
}

function handleKeyDown(index: number, event: KeyboardEvent) {

  if (event.key === 'Backspace' && !otpValues[index] && index > 0) {
    nextTick(() => {
      inputRefs.value[index - 1]?.focus();
    });
  }
}

function handlePaste(event: ClipboardEvent) {

  event.preventDefault();
  const pastedData = event.clipboardData?.getData('text');

  if (pastedData) {

    const digits = pastedData.replace(/\D/g, '').slice(0, 4);

    for (let i = 0; i < digits.length; i++) {
      otpValues[i] = digits[i]!;
    }

    nextTick(() => {
      const nextIndex = Math.min(digits.length, 3);
      inputRefs.value[nextIndex]?.focus();
    });
  }
}
</script>

<template>
  <div class="otp-container">
    <div class="otp-form">
      <h2 class="form-title">Verify Email</h2>

      <form @submit.prevent="handleVerify">
        <div class="otp-inputs">
          <input
              v-for="(_, index) in otpValues"
              :key="index"
              :ref="el => { if (el) inputRefs[index] = el as HTMLInputElement }"
              v-model="otpValues[index]"
              type="text"
              inputmode="numeric"
              pattern="[0-9]*"
              maxlength="1"
              class="otp-input"
              @input="handleInput(index, $event)"
              @keydown="handleKeyDown(index, $event)"
              @paste="handlePaste"
              :disabled="isLoading"
          />
        </div>

        <p class="resend-text">
          Don't get the code?
          <button type="button" class="resend-link" @click="handleResend">Resend</button>
        </p>

        <button type="submit" class="verify-button" :disabled="isLoading">
          <span v-if="!isLoading">Verify</span>
          <span v-else class="spinner"></span>
        </button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.otp-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 90vh;
  padding: 20px;
  box-sizing: border-box;
}

.otp-form {
  background: rgba(10, 10, 10, 0.8);
  border: 1px solid #282828;
  border-radius: 12px;
  padding: 3rem;
  width: 100%;
  max-width: 450px;
  box-shadow: 0 8px 32px rgba(30, 144, 255, 0.1);
}

.form-title {
  color: #1E90FF;
  font-size: 2rem;
  font-weight: bold;
  text-align: center;
  margin: 0 0 2rem 0;
}

.otp-inputs {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-bottom: 2rem;
}

.otp-input {
  width: 60px;
  height: 60px;
  background: rgba(20, 20, 20, 0.9);
  border: 1px solid #282828;
  border-radius: 8px;
  color: #1E90FF;
  font-size: 1.75rem;
  font-weight: bold;
  text-align: center;
  transition: all 0.3s ease;
  outline: none;
}

.otp-input:focus {
  border-color: #1E90FF;
  box-shadow: 0 0 0 2px rgba(30, 144, 255, 0.2);
}

.resend-text {
  text-align: center;
  color: #666;
  font-size: 0.9rem;
  margin: 0 0 1.5rem 0;
}

.resend-link {
  background: none;
  border: none;
  color: #1E90FF;
  text-decoration: underline;
  cursor: pointer;
  font-size: 0.9rem;
  font-weight: bold;
  transition: color 0.3s;
  padding: 0;
}

.resend-link:hover {
  color: #1873CC;
}

.verify-button {
  width: 100%;
  padding: 0.85rem;
  background-color: #1E90FF;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
}

.verify-button:hover {
  background-color: #1873CC;
}

@media (max-width: 600px) {
  .otp-inputs {
    gap: 0.75rem;
  }

  .otp-input {
    width: 55px;
    height: 55px;
    font-size: 1.5rem;
  }

  .otp-form {
    padding: 2rem;
  }
}
</style>