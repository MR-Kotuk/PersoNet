<script setup lang="ts">
import NetworkBackground from "@/components/NetworkBackground.vue";
import Navbar from "@/components/Navbar.vue";
import {RouterView} from "vue-router";
</script>

<template>
  <Navbar/>
  <NetworkBackground/>
  <RouterView/>
</template>