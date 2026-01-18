<script setup lang="ts">
import {RouterLink, useRouter} from 'vue-router';
import {reactive, ref} from 'vue';
import axios from 'axios';
import AuthInput from '@/components/AuthInput.vue';
import OAuthButtons from '@/components/OAuthButtons.vue';

interface SignInProps {
  email: string;
  password: string;
}

const signInForm = reactive<SignInProps>({
  email: '',
  password: ''
});

const router = useRouter();
const isLoading = ref<boolean>(false);

async function handleSignIn() {

  isLoading.value = true;

  try {
    const response = await axios.post('/api/auth/login', signInForm);

    if (response.status === 200) {
      await router.push('/');
    }

  } catch (error: unknown) {

    isLoading.value = false;
    console.error('Sign up error:', error);

    if (axios.isAxiosError(error)) {

      if (error.response?.status === 401) {
        alert(error.response.data?.message || 'Invalid credentials');

      } else if (error.response?.status === 400) {
        await handleEmailVerify()
      }
    } else {
      alert('Unexpected error occurred');
    }
  }
}

async function handleEmailVerify() {

  isLoading.value = true;

  try {
    const response = await axios.post(`/api/auth/forgot-password/verify-email/send/${signInForm.email}`);

    if (response.status === 200) {

      sessionStorage.setItem('otp_email', signInForm.email);
      await router.push('/otp-verification');
    }
  } catch (error: unknown) {

    isLoading.value = false;
    console.error('Email verify error:', error);

    alert('Unexpected error occurred');
  }
}

function handleGoogleLogin() {
  window.location.href = "/api/oauth2/authorization/google";
}

function handleGithubLogin() {
  window.location.href = "/api/oauth2/authorization/github";
}
</script>

<template>
  <div class="signin-container">
    <div class="signin-form">
      <h2 class="form-title">Sign In</h2>

      <form @submit.prevent="handleSignIn">
        <AuthInput
            id="email"
            v-model="signInForm.email"
            label="Email"
            type="email"
            placeholder="Enter your email"
            :required="true"
            :disabled="isLoading"
        />

        <AuthInput
            id="password"
            v-model="signInForm.password"
            label="Password"
            type="password"
            placeholder="Enter your password"
            :required="true"
            :disabled="isLoading"
        />

        <button type="submit" class="signin-button" :disabled="isLoading">
          <span v-if="!isLoading">Sign In</span>
          <span v-else class="spinner"></span>
        </button>
      </form>

      <OAuthButtons
          @google-login="handleGoogleLogin"
          @github-login="handleGithubLogin"
      />

      <p class="signup-link">
        Don't have an account?
        <RouterLink to="/sign-up">Sign Up</RouterLink>
      </p>
    </div>
  </div>
</template>

<style scoped>
.signin-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 90vh;
  padding: 20px;
  box-sizing: border-box;
}

.signin-form {
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

.signin-button {
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
  margin-top: 0.5rem;
}

.signin-button:hover {
  background-color: #1873CC;
}

.signup-link {
  text-align: center;
  color: #666;
  font-size: 0.9rem;
  margin: 0;
}

.signup-link a {
  color: #1E90FF;
  text-decoration: none;
  font-weight: bold;
  transition: color 0.3s;
}

.signup-link a:hover {
  color: #1873CC;
}
</style>