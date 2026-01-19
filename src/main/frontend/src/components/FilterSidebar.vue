<script setup lang="ts">
import {ref} from 'vue';

interface SearchFilter {
  keyword: string;
  status: string[];
  types: string[];
  tags: string[];
}

defineProps<{
  availableTags: string[];
  availableTypes: string[];
  availableStatuses: string[];
}>();

const emit = defineEmits<{
  (e: 'apply', filters: SearchFilter): void;
  (e: 'close'): void;
}>();

const selectedStatuses = ref<string[]>([]);
const selectedTypes = ref<string[]>([]);
const selectedTags = ref<string[]>([]);

function toggleField<T>(list: T[], value: T) {

  const index = list.indexOf(value);

  if (index !== -1) {
    list.splice(index, 1);
  } else {
    list.push(value);
  }
}

function applyFilters() {
  emit('apply', {
    keyword: '',
    status: selectedStatuses.value,
    types: selectedTypes.value,
    tags: selectedTags.value
  });
}

function clearFilters() {

  selectedStatuses.value = [];
  selectedTypes.value = [];
  selectedTags.value = [];

  emit('apply', {
    keyword: '',
    status: [],
    types: [],
    tags: []
  });
}
</script>

<template>
  <div class="filter-sidebar">
    <div class="sidebar-header">
      <h3 class="sidebar-title">Filters</h3>
      <button @click="$emit('close')" class="close-btn">
        <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M15 5L5 15M5 5l10 10" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
        </svg>
      </button>
    </div>

    <div class="sidebar-body">

      <div class="filter-section">
        <h4 class="filter-title">Type</h4>
        <div class="filter-options">
          <button
              v-for="type in availableTypes"
              :key="type"
              @click="toggleField(selectedTypes, type);"
              class="filter-toggle"
              :class="{ active: selectedTypes.includes(type) }"
          >
            <span class="toggle-indicator"></span>
            <span class="toggle-label">{{ type }}</span>
          </button>
        </div>
      </div>

      <div class="filter-section">
        <h4 class="filter-title">Tags</h4>
        <div class="filter-options">
          <button
              v-for="tag in availableTags"
              :key="tag"
              @click="toggleField(selectedTags, tag);"
              class="filter-toggle"
              :class="{ active: selectedTags.includes(tag) }"
          >
            <span class="toggle-indicator"></span>
            <span class="toggle-label">{{ tag }}</span>
          </button>
        </div>
      </div>
    </div>

    <div class="sidebar-footer">
      <button @click="clearFilters" class="footer-btn clear-btn">Clear All</button>
      <button @click="applyFilters" class="footer-btn apply-btn">Apply</button>
    </div>
  </div>
</template>

<style scoped>
.filter-sidebar {
  width: 320px;
  background-color: rgba(10, 10, 10, 0.9);
  border: 1px solid #282828;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  max-height: calc(100vh - 180px);
  box-shadow: 0 8px 32px rgba(30, 144, 255, 0.1);
}

.sidebar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid #282828;
}

.sidebar-title {
  color: #1E90FF;
  font-size: 1.25rem;
  font-weight: 700;
  margin: 0;
}

.close-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  background: transparent;
  border: none;
  color: #666;
  cursor: pointer;
  transition: color 0.3s;
}

.close-btn:hover {
  color: #ff3366;
}

.sidebar-body {
  flex: 1;
  overflow-y: auto;
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.sidebar-body::-webkit-scrollbar {
  width: 6px;
}

.sidebar-body::-webkit-scrollbar-track {
  background: rgba(40, 40, 40, 0.3);
  border-radius: 3px;
}

.sidebar-body::-webkit-scrollbar-thumb {
  background: rgba(30, 144, 255, 0.3);
  border-radius: 3px;
}

.sidebar-body::-webkit-scrollbar-thumb:hover {
  background: rgba(30, 144, 255, 0.5);
}

.filter-section {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.filter-title {
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.875rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  margin: 0;
}

.filter-options {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.filter-toggle {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.75rem 1rem;
  background-color: rgba(40, 40, 40, 0.3);
  border: 1px solid #282828;
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.87);
  font-size: 0.9rem;
  font-weight: 500;
  text-align: left;
  cursor: pointer;
  transition: all 0.2s;
}

.filter-toggle:hover {
  border-color: #1E90FF;
  background-color: rgba(30, 144, 255, 0.05);
}

.filter-toggle.active {
  border-color: #1E90FF;
  background-color: rgba(30, 144, 255, 0.15);
}

.toggle-indicator {
  width: 18px;
  height: 18px;
  border: 2px solid #666;
  border-radius: 4px;
  transition: all 0.2s;
  position: relative;
}

.filter-toggle.active .toggle-indicator {
  border-color: #1E90FF;
  background-color: #1E90FF;
}

.filter-toggle.active .toggle-indicator::after {
  content: '';
  position: absolute;
  left: 4px;
  top: 1px;
  width: 4px;
  height: 8px;
  border: solid white;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
}

.toggle-label {
  flex: 1;
}

.sidebar-footer {
  display: flex;
  gap: 0.75rem;
  padding: 1.5rem;
  border-top: 1px solid #282828;
}

.footer-btn {
  flex: 1;
  padding: 0.75rem;
  border-radius: 8px;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.clear-btn {
  background-color: transparent;
  border: 1px solid #282828;
  color: #666;
}

.clear-btn:hover {
  border-color: #666;
  color: rgba(255, 255, 255, 0.87);
}

.apply-btn {
  background-color: #1E90FF;
  border: none;
  color: white;
}

.apply-btn:hover {
  background-color: #1873CC;
  box-shadow: 0 0 12px rgba(30, 144, 255, 0.3);
}

@media (max-width: 768px) {
  .filter-sidebar {
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    width: 100%;
    max-width: 320px;
    max-height: 100vh;
    z-index: 1000;
  }
}
</style>