<script setup lang="ts">
import {onMounted, ref} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import axios from 'axios';

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

const route = useRoute();
const router = useRouter();

const person = ref<Person | null>(null);
const originalPerson = ref<Person | null>(null);

const isLoading = ref(false);
const isEditMode = ref(false);

const personId = Number(route.params.id);
const availableTypes = ref<string[]>([]);
const newTag = ref('');

function isBoolean(value: string): boolean {
  return value.toLowerCase() === 'true' || value.toLowerCase() === 'false';
}

function getBooleanValue(value: string): boolean {
  return value.toLowerCase() === 'true';
}

function toggleBoolean(line: LineTemplate) {

  if (!isEditMode.value) {
    return;
  }

  const currentValue = getBooleanValue(line.lineValue);
  line.lineValue = (!currentValue).toString();
}

function addTag() {

  if (!person.value || !newTag.value.trim()) {
    return;
  }

  const tag = newTag.value.trim();

  if (!person.value.tags.includes(tag)) {
    person.value.tags.push(tag);
  }

  newTag.value = '';
}

function removeTag(index: number) {

  if (!person.value) {
    return;
  }

  person.value.tags.splice(index, 1);
}

function handleTagKeydown(event: KeyboardEvent) {

  if (event.key === 'Enter') {

    event.preventDefault();
    addTag();
  }
}

async function fetchPerson() {

  const token = localStorage.getItem('access_token');

  if (!token) {
    return;
  }

  isLoading.value = true;

  try {
    const response = await axios.get(`/api/person/${personId}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    person.value = response.data;
    originalPerson.value = JSON.parse(JSON.stringify(response.data));

  } finally {
    isLoading.value = false;
  }
}

async function fetchMetadata() {

  const token = localStorage.getItem('access_token');

  if (!token) {
    return;
  }

  try {
    const typesRes = await axios.get('/api/person/search/types', {
      headers: {Authorization: `Bearer ${token}`}
    });

    availableTypes.value = typesRes.data;

  } catch (error) {
    console.error('Failed to fetch metadata:', error);
  }
}

function enableEdit() {
  isEditMode.value = true;
}

function cancelEdit() {

  person.value = JSON.parse(JSON.stringify(originalPerson.value));
  isEditMode.value = false;
}

async function savePerson() {

  const token = localStorage.getItem('access_token');

  if (!token || !person.value) {
    return;
  }

  isLoading.value = true;

  try {
    const response = await axios.put('/api/person/', person.value, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    person.value = response.data;
    originalPerson.value = JSON.parse(JSON.stringify(response.data));
    isEditMode.value = false;

  } finally {
    isLoading.value = false;
  }
}

async function deletePerson() {

  const token = localStorage.getItem('access_token');

  if (!token || !person.value) {
    return;
  }

  if (!confirm('Delete this person?')) {
    return;
  }

  await axios.delete('/api/person/', {
    headers: {
      Authorization: `Bearer ${token}`
    },
    data: [person.value.personId]
  });

  await router.push('/contacts');
}

function goBack() {
  router.push('/contacts');
}

onMounted(() => {
  fetchPerson();
  fetchMetadata();
});
</script>

<template>
  <div class="person-view">
    <div class="top-bar">
      <div class="actions-left">
        <button @click="goBack" class="icon-btn back-btn">
          <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 4L6 10L12 16" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                  stroke-linejoin="round"/>
          </svg>
        </button>
        <h2 class="page-title">Person Details</h2>
      </div>

      <div class="actions-right">
        <button v-if="!isEditMode" class="icon-btn edit-btn" @click="enableEdit">
          <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M14.5 2.5a2.121 2.121 0 0 1 3 3L6 17l-4 1 1-4L14.5 2.5z" stroke="currentColor" stroke-width="1.5"
                  stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          <span>EDIT</span>
        </button>

        <button v-if="isEditMode" class="icon-btn save-btn" @click="savePerson">
          <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M16 2H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V4a2 2 0 0 0-2-2z" stroke="currentColor"
                  stroke-width="1.5"/>
            <path d="M14 2v6H6V2M6 18v-6h8v6" stroke="currentColor" stroke-width="1.5"/>
          </svg>
          <span>SAVE</span>
        </button>

        <button v-if="isEditMode" class="icon-btn cancel-btn" @click="cancelEdit">
          <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M15 5L5 15M5 5l10 10" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
          <span>CANCEL</span>
        </button>

        <button class="icon-btn delete-btn" @click="deletePerson">
          <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M3 5h14M8 5V3h4v2M8 9v6M12 9v6M5 5l1 12a2 2 0 0 0 2 2h4a2 2 0 0 0 2-2l1-12" stroke="currentColor"
                  stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          <span>DELETE</span>
        </button>
      </div>
    </div>

    <div v-if="isLoading" class="loading">
      <span class="spinner"></span>
    </div>

    <div v-else-if="person" class="content">
      <div class="card-section">
        <div class="section-header">
          <h3>Basic Information</h3>
        </div>
        <div class="fields-grid">

          <div class="field-group">
            <label>Type</label>
            <select v-model="person.personType" :disabled="!isEditMode" class="field-input field-select">
              <option v-for="type in availableTypes" :key="type" :value="type">{{ type }}</option>
            </select>
          </div>

          <div class="field-group full-width">
            <label>Tags</label>
            <div class="tags-container">
              <div class="tags-list">
                <span
                    v-for="(tag, index) in person.tags"
                    :key="index"
                    class="tag-chip"
                >
                  {{ tag }}
                  <button
                      v-if="isEditMode"
                      @click="removeTag(index)"
                      class="tag-remove"
                      type="button"
                  >
                    <svg width="14" height="14" viewBox="0 0 14 14" fill="none" xmlns="http://www.w3.org/2000/svg">
                      <path d="M10.5 3.5L3.5 10.5M3.5 3.5l7 7" stroke="currentColor" stroke-width="1.5"
                            stroke-linecap="round"/>
                    </svg>
                  </button>
                </span>
              </div>
              <div v-if="isEditMode" class="tag-input-wrapper">
                <input
                    v-model="newTag"
                    @keydown="handleTagKeydown"
                    placeholder="Type tag and press Enter"
                    class="field-input tag-input"
                />
                <button @click="addTag" class="add-tag-btn" type="button">
                  <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M8 3v10M3 8h10" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                  </svg>
                  Add
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="card-section" v-if="person.lineTemplates.length > 0">
        <div class="section-header">
          <h3>Additional Details</h3>
        </div>
        <div class="fields-grid">
          <div
              v-for="(line, index) in person.lineTemplates"
              :key="index"
              class="field-group"
          >
            <label>{{ line.lineName }}</label>

            <div v-if="isBoolean(line.lineValue)" class="toggle-wrapper">
              <button
                  @click="toggleBoolean(line)"
                  :disabled="!isEditMode"
                  class="toggle-button"
                  :class="{ 'active': getBooleanValue(line.lineValue), 'disabled': !isEditMode }"
              >
                <span class="toggle-slider"></span>
              </button>
              <span class="toggle-label">{{ getBooleanValue(line.lineValue) ? 'Yes' : 'No' }}</span>
            </div>

            <input
                v-else
                v-model="line.lineValue"
                :disabled="!isEditMode"
                class="field-input"
            />
          </div>
        </div>
      </div>

      <div class="card-section">
        <div class="fields-grid">
          <div class="field-group">
            <label>Creation Date</label>
            <input v-model="person.creationDate" disabled class="field-input"/>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<style scoped>
.person-view {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  gap: 1rem;
}

.actions-left,
.actions-right {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.page-title {
  margin: 0;
  font-size: 1.5rem;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.87);
}

.icon-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1rem;
  background-color: rgba(40, 40, 40, 0.5);
  border: 1px solid #282828;
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.87);
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.icon-btn:hover {
  border-color: #1E90FF;
  background-color: rgba(30, 144, 255, 0.1);
}

.back-btn {
  width: 48px;
  height: 48px;
  justify-content: center;
  padding: 0;
}

.save-btn:hover {
  border-color: #4CAF50;
  background-color: rgba(76, 175, 80, 0.1);
  color: #4CAF50;
}

.delete-btn:hover {
  border-color: #f44336;
  background-color: rgba(244, 67, 54, 0.1);
  color: #f44336;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
  background-color: rgba(10, 10, 10, 0.8);
  border: 1px solid #282828;
  border-radius: 12px;
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

.content {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.card-section {
  background-color: rgba(10, 10, 10, 0.8);
  border: 1px solid #282828;
  border-radius: 12px;
  overflow: hidden;
}

.section-header {
  padding: 1.5rem;
  border-bottom: 1px solid #282828;
  background-color: rgba(20, 20, 20, 0.5);
}

.section-header h3 {
  margin: 0;
  font-size: 1.1rem;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.87);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.fields-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
  padding: 1.5rem;
}

.field-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.field-group.full-width {
  grid-column: 1 / -1;
}

.field-group label {
  font-size: 0.875rem;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.6);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.field-input {
  padding: 0.75rem 1rem;
  background-color: rgba(40, 40, 40, 0.5);
  border: 1px solid #282828;
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.87);
  font-size: 0.95rem;
  transition: all 0.3s;
  outline: none;
}

.field-select {
  cursor: pointer;
}

.field-select:disabled {
  cursor: not-allowed;
}

.field-input:focus:not(:disabled) {
  border-color: #1E90FF;
  background-color: rgba(30, 144, 255, 0.05);
}

.field-input:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.field-input::placeholder {
  color: #666;
}

.toggle-wrapper {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.toggle-button {
  position: relative;
  width: 56px;
  height: 32px;
  background-color: rgba(40, 40, 40, 0.8);
  border: 1px solid #282828;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.3s;
  padding: 0;
  outline: none;
}

.toggle-button:not(.disabled):hover {
  border-color: #1E90FF;
}

.toggle-button.active {
  background-color: #1E90FF;
  border-color: #1E90FF;
}

.toggle-button.disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.toggle-slider {
  position: absolute;
  top: 3px;
  left: 3px;
  width: 24px;
  height: 24px;
  background-color: white;
  border-radius: 50%;
  transition: transform 0.3s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.toggle-button.active .toggle-slider {
  transform: translateX(24px);
}

.toggle-label {
  font-size: 0.95rem;
  color: rgba(255, 255, 255, 0.87);
  font-weight: 500;
  min-width: 40px;
}

.tags-container {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.tags-list {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  min-height: 36px;
}

.tag-chip {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 0.75rem;
  background-color: rgba(30, 144, 255, 0.15);
  border: 1px solid rgba(30, 144, 255, 0.4);
  border-radius: 20px;
  color: rgba(255, 255, 255, 0.87);
  font-size: 0.875rem;
  font-weight: 500;
  transition: all 0.2s;
}

.tag-chip:hover {
  background-color: rgba(30, 144, 255, 0.25);
  border-color: rgba(30, 144, 255, 0.6);
}

.tag-remove {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0;
  margin: 0;
  background: none;
  border: none;
  color: rgba(255, 255, 255, 0.6);
  cursor: pointer;
  transition: color 0.2s;
  width: 16px;
  height: 16px;
}

.tag-remove:hover {
  color: #f44336;
}

.tag-input-wrapper {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

.tag-input {
  flex: 1;
  margin: 0;
}

.add-tag-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1rem;
  background-color: rgba(30, 144, 255, 0.2);
  border: 1px solid rgba(30, 144, 255, 0.4);
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.87);
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  white-space: nowrap;
}

.add-tag-btn:hover {
  background-color: rgba(30, 144, 255, 0.3);
  border-color: #1E90FF;
}

.add-tag-btn:active {
  transform: scale(0.98);
}

@media (max-width: 768px) {
  .top-bar {
    flex-direction: column;
    align-items: stretch;
  }

  .actions-left,
  .actions-right {
    justify-content: space-between;
  }

  .fields-grid {
    grid-template-columns: 1fr;
  }

  .icon-btn span {
    display: none;
  }
}
</style>