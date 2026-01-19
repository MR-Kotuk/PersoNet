<script setup lang="ts">
import {RouterLink, useRouter} from 'vue-router';
import {reactive, ref} from 'vue';
import axios from 'axios';
import AuthInput from '@/components/AuthInput.vue';
import OAuthButtons from '@/components/OAuthButtons.vue';

interface SignUpProps {
  username: string;
  email: string;
  password: string;
}

const signUpForm = reactive<SignUpProps>({
  username: '',
  email: '',
  password: ''
});

const router = useRouter();
const isLoading = ref<boolean>(false);

async function handleSignUp() {

  isLoading.value = true;

  try {
    const response = await axios.post('/api/auth/register', signUpForm);

    if (response.status === 201) {

      sessionStorage.setItem('otp_email', signUpForm.email);
      await router.push('/otp-verification');
    }

  } catch (error: unknown) {

    isLoading.value = false;
    console.error('Sign up error:', error);

    if (axios.isAxiosError(error) && error.response?.status === 400) {
      alert(error.response.data?.message || 'Registration failed');
    } else {
      alert('Unexpected error occurred');
    }
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
  <div class="signup-container">
    <div class="signup-form">
      <h2 class="form-title">Sign Up</h2>

      <form @submit.prevent="handleSignUp">
        <AuthInput
            id="username"
            v-model="signUpForm.username"
            label="Username"
            type="text"
            placeholder="Enter your username"
            :required="true"
            :disabled="isLoading"
        />

        <AuthInput
            id="email"
            v-model="signUpForm.email"
            label="Email"
            type="email"
            placeholder="Enter your email"
            :required="true"
            :disabled="isLoading"
        />

        <AuthInput
            id="password"
            v-model="signUpForm.password"
            label="Password"
            type="password"
            placeholder="Enter your password"
            :required="true"
            :disabled="isLoading"
        />

        <button type="submit" class="signup-button" :disabled="isLoading">
          <span v-if="!isLoading">Sign Up</span>
          <span v-else class="spinner"></span>
        </button>
      </form>

      <OAuthButtons
          @google-login="handleGoogleLogin"
          @github-login="handleGithubLogin"
      />

      <p class="signin-link">
        Already have an account?
        <RouterLink to="/sign-in">Sign In</RouterLink>
      </p>
    </div>
  </div>
</template>

<style scoped>
.signup-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 90vh;
  padding: 20px;
  box-sizing: border-box;
}

.signup-form {
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

.signup-button {
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
  position: relative;
}

.signup-button:hover:not(:disabled) {
  background-color: #1873CC;
}

.signup-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.spinner {
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 3px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.signin-link {
  text-align: center;
  color: #666;
  font-size: 0.9rem;
  margin: 0;
}

.signin-link a {
  color: #1E90FF;
  text-decoration: none;
  font-weight: bold;
  transition: color 0.3s;
}

.signin-link a:hover {
  color: #1873CC;
}
</style>