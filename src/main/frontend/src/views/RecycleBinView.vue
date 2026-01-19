<script setup lang="ts">
import {onMounted, ref} from 'vue';
import {useRouter} from 'vue-router';
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
  previewPhotoUrl?: string;
}

const router = useRouter();
const persons = ref<Person[]>([]);
const isLoading = ref(false);
const selectedPersons = ref<Set<number>>(new Set());

function safelyFindFieldInPerson(person: Person, fieldName: string): string {

  const template = person.lineTemplates.find(t => t.lineName.toLowerCase() === fieldName);
  return template?.lineValue || '';
}

async function fetchRecycleBin() {

  const token = localStorage.getItem('access_token');

  if (!token) {

    await router.push('/sign-in');
    return;
  }

  isLoading.value = true;

  try {
    const response = await axios.get('/api/recycle-bin/', {
      headers: {Authorization: `Bearer ${token}`}
    });

    persons.value = response.data;

  } catch (error) {

    console.error('Failed to fetch recycle bin:', error);

    if (axios.isAxiosError(error) && error.response?.status === 404) {
      persons.value = [];
    }

  } finally {
    isLoading.value = false;
  }
}

function togglePersonSelection(personId: number) {

  if (selectedPersons.value.has(personId)) {
    selectedPersons.value.delete(personId);
  } else {
    selectedPersons.value.add(personId);
  }
}

function toggleSelectAll() {

  if (selectedPersons.value.size === persons.value.length) {
    selectedPersons.value.clear();
  } else {
    selectedPersons.value = new Set(persons.value.map(p => p.personId));
  }
}

async function restoreSelected() {

  if (selectedPersons.value.size === 0) {
    return;
  }

  const token = localStorage.getItem('access_token');

  if (!token) {
    return;
  }

  try {
    await axios.post('/api/recycle-bin/', Array.from(selectedPersons.value), {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    await fetchRecycleBin();
    selectedPersons.value.clear();

  } catch (error) {

    console.error('Failed to restore persons:', error);
    alert('Failed to restore persons. Please try again.');
  }
}

async function deleteSelectedPermanently() {

  if (selectedPersons.value.size === 0) {
    return;
  }

  if (!confirm(`Permanently delete ${selectedPersons.value.size} person(s)? This action cannot be undone.`)) {
    return;
  }

  const token = localStorage.getItem('access_token');

  if (!token) {
    return;
  }

  try {
    await axios.delete('/api/recycle-bin/', {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      data: Array.from(selectedPersons.value)
    });

    await fetchRecycleBin();
    selectedPersons.value.clear();

  } catch (error) {

    console.error('Failed to delete persons:', error);
    alert('Failed to delete persons. Please try again.');
  }
}

async function emptyRecycleBin() {

  if (persons.value.length === 0) {
    return;
  }

  if (!confirm('Permanently delete all persons in the recycle bin? This action cannot be undone.')) {
    return;
  }

  const token = localStorage.getItem('access_token');

  if (!token) {
    return;
  }

  try {
    await axios.delete('/api/recycle-bin/clean', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    await fetchRecycleBin();
    selectedPersons.value.clear();

  } catch (error) {

    console.error('Failed to empty recycle bin:', error);
    alert('Failed to empty recycle bin. Please try again.');
  }
}

function goBack() {
  router.push('/contacts');
}

onMounted(() => {
  fetchRecycleBin();
});
</script>

<template>
  <div class="recycle-bin-view">
    <div class="top-bar">
      <div class="actions-left">
        <button @click="goBack" class="icon-btn back-btn">
          <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 4L6 10L12 16" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                  stroke-linejoin="round"/>
          </svg>
        </button>
        <h2 class="page-title">Recycle Bin</h2>
      </div>

      <div class="actions-right">
        <button
            v-if="selectedPersons.size > 0"
            @click="restoreSelected"
            class="icon-btn restore-btn"
        >
          <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M3 10h11M10 6l4 4-4 4M17 3v14" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"
                  stroke-linejoin="round"/>
          </svg>
          <span>RESTORE ({{ selectedPersons.size }})</span>
        </button>

        <button
            v-if="selectedPersons.size > 0"
            @click="deleteSelectedPermanently"
            class="icon-btn delete-btn"
        >
          <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M3 5h14M8 5V3h4v2M8 9v6M12 9v6M5 5l1 12a2 2 0 0 0 2 2h4a2 2 0 0 0 2-2l1-12" stroke="currentColor"
                  stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          <span>DELETE ({{ selectedPersons.size }})</span>
        </button>

        <button
            v-if="persons.length > 0 && selectedPersons.size == 0"
            @click="emptyRecycleBin"
            class="icon-btn empty-btn"
        >
          <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M3 5h14M8 5V3h4v2M8 9v6M12 9v6M5 5l1 12a2 2 0 0 0 2 2h4a2 2 0 0 0 2-2l1-12" stroke="currentColor"
                  stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          <span>EMPTY BIN</span>
        </button>
      </div>
    </div>

    <div class="content-wrapper">
      <div class="main-content">
        <div class="persons-header">
          <div class="header-cell checkbox-col">
            <input
                type="checkbox"
                @change="toggleSelectAll"
                :checked="persons.length > 0 && selectedPersons.size === persons.length"
                class="checkbox"
            />
          </div>
          <div class="header-cell avatar-col"></div>
          <div class="header-cell">FIRST NAME</div>
          <div class="header-cell">LAST NAME</div>
          <div class="header-cell">TYPE</div>
          <div class="header-cell">TAGS</div>
          <div class="header-cell">DELETED</div>
          <div class="header-cell">{{ persons.length }}</div>
        </div>

        <div v-if="isLoading" class="loading">
          <span class="spinner"></span>
        </div>

        <div v-else class="persons-list">
          <div
              v-for="person in persons"
              :key="person.personId"
              class="person-row"
              :class="{ 'selected': selectedPersons.has(person.personId) }"
          >
            <div class="card-content">
              <div class="checkbox-cell">
                <input
                    type="checkbox"
                    :checked="selectedPersons.has(person.personId)"
                    @change="togglePersonSelection(person.personId)"
                    class="checkbox"
                />
              </div>
              <div class="avatar-cell">
                <div class="avatar">
                  <svg width="32" height="32" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <circle cx="16" cy="16" r="15" stroke="#1E90FF" stroke-width="2"/>
                    <path d="M16 16a4 4 0 1 0 0-8 4 4 0 0 0 0 8zM10 24c0-3.5 2.5-6 6-6s6 2.5 6 6" stroke="#1E90FF"
                          stroke-width="2" stroke-linecap="round"/>
                  </svg>
                </div>
              </div>
              <div class="info-cell">{{ safelyFindFieldInPerson(person, 'first name') }}</div>
              <div class="info-cell">{{ safelyFindFieldInPerson(person, 'last name') }}</div>
              <div class="info-cell">
                <span class="badge type-badge">{{ person.personType }}</span>
              </div>
              <div class="info-cell tags-cell">
                <span v-for="tag in person.tags" :key="tag" class="tag">{{ tag }}</span>
              </div>
              <div class="info-cell">
                <span class="badge status-badge deleted">DELETED</span>
              </div>
            </div>
          </div>
        </div>

        <div v-if="!isLoading && persons.length === 0" class="empty-state">
          <svg width="64" height="64" viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 20h40M24 20V12h16v8M24 28v16M32 28v16M40 28v16M20 20l2 32a4 4 0 0 0 4 4h12a4 4 0 0 0 4-4l2-32"
                  stroke="#666" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          <p>Recycle bin is empty</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.recycle-bin-view {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  padding: 20px;
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

.restore-btn:hover {
  border-color: #4CAF50;
  background-color: rgba(76, 175, 80, 0.1);
  color: #4CAF50;
}

.delete-btn:hover,
.empty-btn:hover {
  border-color: #f44336;
  background-color: rgba(244, 67, 54, 0.1);
  color: #f44336;
}

.content-wrapper {
  display: flex;
  gap: 1.5rem;
  flex: 1;
}

.main-content {
  flex: 1;
}

.persons-header {
  display: grid;
  grid-template-columns: 60px 80px 1fr 1fr 1fr 2fr 1fr 80px;
  gap: 1rem;
  padding: 1rem 1.5rem;
  background-color: rgba(10, 10, 10, 0.8);
  border: 1px solid #282828;
  border-radius: 12px 12px 0 0;
  margin-bottom: 0;
}

.header-cell {
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.875rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  display: flex;
  align-items: center;
}

.checkbox-col {
  justify-content: center;
}

.persons-list {
  display: flex;
  flex-direction: column;
  gap: 0;
}

.person-row {
  background-color: rgba(20, 20, 20, 0.8);
  border: 1px solid #282828;
  border-top: none;
  transition: all 0.2s;
}

.person-row:last-child {
  border-radius: 0 0 12px 12px;
}

.person-row.selected {
  background-color: rgba(30, 144, 255, 0.1);
  border-color: rgba(30, 144, 255, 0.5);
}

.person-row:hover {
  background-color: rgba(30, 144, 255, 0.05);
  border-color: rgba(30, 144, 255, 0.3);
}

.card-content {
  display: grid;
  grid-template-columns: 60px 80px 1fr 1fr 1fr 2fr 1fr 80px;
  gap: 1rem;
  padding: 1rem 1.5rem;
  align-items: center;
}

.checkbox-cell {
  display: flex;
  align-items: center;
  justify-content: center;
}

.checkbox {
  width: 18px;
  height: 18px;
  cursor: pointer;
  accent-color: #1E90FF;
}

.avatar-cell {
  display: flex;
  justify-content: center;
}

.avatar {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(30, 144, 255, 0.1);
  border-radius: 50%;
}

.info-cell {
  color: rgba(255, 255, 255, 0.87);
  font-size: 0.95rem;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.tags-cell {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  white-space: normal;
}

.tag {
  display: inline-block;
  padding: 0.25rem 0.75rem;
  background-color: rgba(100, 100, 100, 0.3);
  border: 1px solid #666;
  border-radius: 12px;
  color: rgba(255, 255, 255, 0.87);
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
}

.badge {
  display: inline-block;
  padding: 0.35rem 0.85rem;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.type-badge {
  background-color: rgba(30, 144, 255, 0.2);
  color: #1E90FF;
  border: 1px solid #1E90FF;
}

.status-badge.deleted {
  background-color: rgba(244, 67, 54, 0.2);
  color: #f44336;
  border: 1px solid #f44336;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
  background-color: rgba(10, 10, 10, 0.8);
  border: 1px solid #282828;
  border-top: none;
  border-radius: 0 0 12px 12px;
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
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 1.5rem;
  padding: 4rem;
  background-color: rgba(10, 10, 10, 0.8);
  border: 1px solid #282828;
  border-top: none;
  border-radius: 0 0 12px 12px;
  color: #666;
}

.empty-state p {
  font-size: 1.1rem;
  margin: 0;
}

@media (max-width: 1200px) {
  .persons-header,
  .card-content {
    grid-template-columns: 50px 60px 1fr 1fr 1fr 1.5fr 1fr 60px;
  }
}

@media (max-width: 768px) {
  .top-bar {
    flex-direction: column;
    align-items: stretch;
  }

  .actions-left,
  .actions-right {
    justify-content: space-between;
    flex-wrap: wrap;
  }

  .persons-header {
    display: none;
  }

  .card-content {
    grid-template-columns: 1fr;
    gap: 0.75rem;
  }

  .checkbox-cell,
  .avatar-cell {
    justify-content: flex-start;
  }

  .info-cell {
    white-space: normal;
  }

  .icon-btn span {
    display: none;
  }
}
</style>