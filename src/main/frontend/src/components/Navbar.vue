<script setup lang="ts">
import {RouterLink} from 'vue-router';
</script>

<template>
  <div class="header">

    <div class="logo">
      <RouterLink to="/">
        <img src="../assets/logo.png" alt="home">
      </RouterLink>
    </div>

    <div class="menu">
      <RouterLink to="/">Home</RouterLink>
      <RouterLink to="/contacts">Contacts</RouterLink>
      <RouterLink to="/about">About</RouterLink>
      <RouterLink to="/settings">Settings</RouterLink>
    </div>
  </div>
</template>

<style scoped>
.header {
  position: sticky;
  top: 0;

  display: grid;
  grid-template-columns: auto 1fr auto;
  align-items: center;

  padding: 5px 13px;
  background: black;

  border-bottom: 1px #282828 solid;

  z-index: 1000;
}

.logo {
  justify-self: start;
}

.logo img {
  width: 48px;
}

.menu {
  justify-self: center;
  display: flex;
  gap: 30px;
}

.menu a {
  position: relative;
  font-weight: bold;
  color: white;
  text-decoration: none;
  padding-bottom: 3px;
  transition: color 0.3s;
}

.menu a::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 0;
  height: 2px;
  background-color: #0a62b4;
  transition: width 0.3s ease;
}

.menu a:hover::after {
  width: 100%;
}

.menu a:hover {
  color: #208af3;
}
</style>