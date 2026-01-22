<script setup lang="ts">
import {computed, ref} from 'vue';

interface LineTemplate {
  lineName: string;
  lineValue: string;
}

interface Person {
  personId: number;
  email: string;
  creationDate: string;
  personStatus: string;
  personType: string;
  tags: string[];
  lineTemplates: LineTemplate[];
}

const props = defineProps<{
  persons: Person[];
  isLoading: boolean;
  selectedPersonIds: Set<number>;
  findPersonEmail: (person: Person) => string | null;
}>();

const emit = defineEmits<{
  'update:selectedPersonIds': [value: Set<number>];
}>();

const searchKeyword = ref('');

const filteredPersons = computed(() => {

  if (!searchKeyword.value.trim()) {
    return props.persons;
  }

  const keyword = searchKeyword.value.toLowerCase();

  return props.persons.filter(p =>
      props.findPersonEmail(p)?.toLowerCase().includes(keyword) ||
      p.tags.some(t => t.toLowerCase().includes(keyword)) ||
      p.personType.toLowerCase().includes(keyword)
  );
});

const allSelected = computed(() => {
  return filteredPersons.value.length > 0 &&
      filteredPersons.value.every(p => props.selectedPersonIds.has(p.personId));
});

const someSelected = computed(() => {
  return props.selectedPersonIds.size > 0 && !allSelected.value;
});

function toggleSelectAll() {

  const newSet = new Set(props.selectedPersonIds);

  if (allSelected.value) {
    filteredPersons.value.forEach(p => newSet.delete(p.personId));
  } else {
    filteredPersons.value.forEach(p => newSet.add(p.personId));
  }

  emit('update:selectedPersonIds', newSet);
}

function togglePersonSelection(personId: number) {

  const newSet = new Set(props.selectedPersonIds);

  if (newSet.has(personId)) {
    newSet.delete(personId);
  } else {
    newSet.add(personId);
  }

  emit('update:selectedPersonIds', newSet);
}
</script>

<template>
  <div class="left-panel">
    <div class="panel-header">
      <h2>Recipients</h2>
      <div class="search-box">
        <input v-model="searchKeyword" type="text" placeholder="Search recipients..." class="search-input"/>
        <svg class="search-icon" width="16" height="16" viewBox="0 0 20 20" fill="none"
             xmlns="http://www.w3.org/2000/svg">
          <path d="M9 17A8 8 0 1 0 9 1a8 8 0 0 0 0 16zM19 19l-4.35-4.35" stroke="currentColor" stroke-width="2"
                stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </div>
    </div>

    <div class="recipients-header">
      <input type="checkbox" :checked="allSelected" :indeterminate="someSelected" @change="toggleSelectAll"
             class="select-checkbox"/>
      <span class="header-label">SELECT ALL</span>
    </div>

    <div v-if="isLoading" class="loading">
      <span class="spinner"></span>
    </div>

    <div v-else class="recipients-list">
      <div v-for="person in filteredPersons" :key="person.personId" class="recipient-card"
           :class="{ selected: selectedPersonIds.has(person.personId) }"
           @click="togglePersonSelection(person.personId)">
        <input type="checkbox" :checked="selectedPersonIds.has(person.personId)"
               @click.stop="togglePersonSelection(person.personId)" class="select-checkbox"/>
        <div class="recipient-info">
          <div class="recipient-email">{{ findPersonEmail(person) }}</div>
          <div class="recipient-meta">
            <span class="type-badge">{{ person.personType }}</span>
            <span v-for="tag in person.tags" :key="tag" class="tag-badge">{{ tag }}</span>
          </div>
        </div>
      </div>
    </div>

    <div v-if="!isLoading && filteredPersons.length === 0" class="empty-state">
      <p>No recipients found</p>
    </div>

    <div v-if="selectedPersonIds.size > 0" class="selected-count">
      Selected: {{ selectedPersonIds.size }}
    </div>
  </div>
</template>

<style scoped>
.left-panel {
  display: flex;
  flex-direction: column;
  background-color: rgba(10, 10, 10, 0.8);
  border: 1px solid #282828;
  border-radius: 12px;
  overflow: hidden;
  height: fit-content;
  max-height: calc(100vh - 140px);
}

.panel-header {
  padding: 1.5rem;
  border-bottom: 1px solid #282828;
}

.panel-header h2 {
  font-size: 1.25rem;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.87);
  margin-bottom: 1rem;
}

.search-box {
  position: relative;
  display: flex;
  align-items: center;
}

.search-input {
  width: 100%;
  padding: 0.75rem 2.5rem 0.75rem 1rem;
  background-color: rgba(40, 40, 40, 0.5);
  border: 1px solid #282828;
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.87);
  font-size: 0.9rem;
  outline: none;
  transition: border-color 0.3s;
}

.search-input:focus {
  border-color: #1E90FF;
}

.search-input::placeholder {
  color: #666;
}

.search-icon {
  position: absolute;
  right: 0.75rem;
  color: #666;
  pointer-events: none;
}

.recipients-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem 1.5rem;
  background-color: rgba(20, 20, 20, 0.5);
  border-bottom: 1px solid #282828;
}

.header-label {
  font-size: 0.75rem;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.6);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.recipients-list {
  flex: 1;
  overflow-y: auto;
}

.recipient-card {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem 1.5rem;
  border-bottom: 1px solid #282828;
  cursor: pointer;
  transition: all 0.2s;
}

.recipient-card:hover {
  background-color: rgba(30, 144, 255, 0.05);
}

.recipient-card.selected {
  background-color: rgba(30, 144, 255, 0.1);
  border-left: 3px solid #1E90FF;
}

.select-checkbox {
  width: 18px;
  height: 18px;
  cursor: pointer;
  accent-color: #1E90FF;
}

.recipient-info {
  flex: 1;
  min-width: 0;
}

.recipient-email {
  font-size: 0.95rem;
  font-weight: 500;
  color: rgba(255, 255, 255, 0.87);
  margin-bottom: 0.5rem;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.recipient-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.type-badge {
  padding: 0.25rem 0.5rem;
  background-color: rgba(30, 144, 255, 0.2);
  border: 1px solid rgba(30, 144, 255, 0.3);
  border-radius: 4px;
  color: #1E90FF;
  font-size: 0.75rem;
  font-weight: 600;
}

.tag-badge {
  padding: 0.25rem 0.5rem;
  background-color: rgba(255, 255, 255, 0.05);
  border: 1px solid #282828;
  border-radius: 4px;
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.75rem;
}

.selected-count {
  padding: 1rem 1.5rem;
  background-color: rgba(30, 144, 255, 0.1);
  border-top: 1px solid rgba(30, 144, 255, 0.3);
  color: #1E90FF;
  font-weight: 600;
  text-align: center;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 3rem;
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

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 3rem;
  color: #666;
  font-size: 1rem;
}

@media (max-width: 900px) {
  .left-panel {
    max-height: 400px;
  }
}

@media (max-width: 768px) {
  .left-panel {
    max-height: 350px;
  }
}

@media (max-width: 480px) {
  .left-panel {
    max-height: 300px;
  }

  .panel-header {
    padding: 1rem;
  }

  .panel-header h2 {
    font-size: 1.1rem;
  }

  .recipient-card {
    padding: 0.75rem 1rem;
  }

  .recipient-email {
    font-size: 0.875rem;
  }
}
</style>