<script setup lang="ts">
import {computed, onMounted, ref} from 'vue';
import {useRouter} from 'vue-router';
import axios from 'axios';
import PersonCard from '../components/PersonCard.vue';
import FilterSidebar from '../components/FilterSidebar.vue';

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

interface SearchFilter {
  keyword: string;
  status: string[];
  types: string[];
  tags: string[];
}

const router = useRouter();
const persons = ref<Person[]>([]);
const showFilters = ref(false);
const selectedPersonIds = ref<Set<number>>(new Set());

const isLoading = ref(false);
const isDeleting = ref(false);

const availableTags = ref<string[]>([]);
const availableTypes = ref<string[]>([]);
const availableStatuses = ref<string[]>([]);

const searchKeyword = ref('');
const searchFilter = ref<SearchFilter>({
  keyword: '',
  status: [],
  types: [],
  tags: []
});

const allSelected = computed(() => {
  return persons.value.length > 0 &&
      persons.value.every(p => selectedPersonIds.value.has(p.personId));
});

const someSelected = computed(() => {
  return selectedPersonIds.value.size > 0 && !allSelected.value;
});

function safelyFindFieldInPerson(person: Person, fieldName: string): string {

  const template = person.lineTemplates.find(t => t.lineName.toLowerCase() === fieldName);
  return template?.lineValue || '';
}

function toggleSelectAll() {

  if (allSelected.value) {
    selectedPersonIds.value.clear();
  } else {
    persons.value.forEach(p => selectedPersonIds.value.add(p.personId));
  }
}

function togglePersonSelection(personId: number) {

  if (selectedPersonIds.value.has(personId)) {
    selectedPersonIds.value.delete(personId);
  } else {
    selectedPersonIds.value.add(personId);
  }
}

async function deleteSelectedPersons() {

  if (selectedPersonIds.value.size === 0) {
    return;
  }

  const token = localStorage.getItem('access_token');

  if (!token) {

    await router.push('/sign-in');
    return;
  }

  isDeleting.value = true;

  try {
    const idsArray = Array.from(selectedPersonIds.value);

    await axios.delete('/api/person/', {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      data: idsArray
    });

    selectedPersonIds.value.clear();

    await fetchPersons();

  } catch (error) {

    console.error('Failed to delete persons:', error);

    if (axios.isAxiosError(error)) {
      alert(`Failed to delete persons: ${error.response?.data || error.message}`);
    }

  } finally {
    isDeleting.value = false;
  }
}

async function fetchPersons() {

  const token = localStorage.getItem('access_token');

  if (!token) {

    await router.push('/sign-in');
    return;
  }

  isLoading.value = true;

  try {
    const response = await axios.get('/api/person/', {
      headers: {Authorization: `Bearer ${token}`}
    });

    persons.value = response.data;

  } catch (error) {

    console.error('Failed to fetch persons:', error);

    if (axios.isAxiosError(error) && error.response?.status === 500) {
      await router.push('/sign-in');
    }

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
    const [tagsRes, typesRes, statusesRes] = await Promise.all([
      axios.get('/api/person/search/tags', {headers: {Authorization: `Bearer ${token}`}}),
      axios.get('/api/person/search/types', {headers: {Authorization: `Bearer ${token}`}}),
      axios.get('/api/person/search/statuses', {headers: {Authorization: `Bearer ${token}`}})
    ]);

    availableTags.value = tagsRes.data;
    availableTypes.value = typesRes.data;
    availableStatuses.value = statusesRes.data;

  } catch (error) {
    console.error('Failed to fetch metadata:', error);
  }
}

async function searchPersons() {

  const token = localStorage.getItem('access_token');

  if (!token) {
    return;
  }

  searchFilter.value.keyword = searchKeyword.value;
  isLoading.value = true;

  try {
    const response = await axios({
      method: 'POST',
      url: '/api/person/search/',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      data: searchFilter.value
    });

    persons.value = response.data;

  } catch (error) {

    console.error('Search failed:', error);

    if (axios.isAxiosError(error)) {
      console.error('Error details:', error.response?.data);
    }

    await fetchPersons();

  } finally {
    isLoading.value = false;
  }
}

function handleSearch() {
  searchPersons();
}

function applyFilters(filters: SearchFilter) {

  searchFilter.value = {...filters, keyword: searchKeyword.value};
  searchPersons();
}

function openPersonView(person: Person) {
  router.push(`/person/${person.personId}`);
}

function toggleFilters() {
  showFilters.value = !showFilters.value;
}

function handlePersonCreate() {
  router.push('/person/create');
}

function openRecycleBin() {
  router.push('/recycle-bin');
}

function sendMessage() {
  router.push('/send-message');
}

onMounted(() => {
  fetchPersons();
  fetchMetadata();
});
</script>

<template>
  <div class="person-view">
    <div class="top-bar">
      <div class="actions-left">
        <button @click="handlePersonCreate" class="icon-btn create-btn">
          <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M10 4V16M4 10H16" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </button>
        <button @click="sendMessage" class="icon-btn message-btn">
          <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M2 3h16v12H6l-4 4V3z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"
                  stroke-linejoin="round"/>
          </svg>
          <span>Write message</span>
        </button>
      </div>

      <div class="actions-right">
        <div class="search-box">
          <input
              v-model="searchKeyword"
              type="text"
              placeholder="Search"
              @keyup.enter="handleSearch"
              class="search-input"
          />
          <button @click="handleSearch" class="search-icon">
            <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M9 17A8 8 0 1 0 9 1a8 8 0 0 0 0 16zM19 19l-4.35-4.35" stroke="currentColor" stroke-width="2"
                    stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </button>
        </div>
        <button @click="toggleFilters" class="icon-btn filter-btn" :class="{ active: showFilters }">
          <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M2 5h16M5 10h10M8 15h4" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </button>
        <button
            @click="selectedPersonIds.size === 0 ? openRecycleBin() : deleteSelectedPersons()"
            class="icon-btn"
            :class="selectedPersonIds.size === 0 ? 'recycle-btn' : 'delete-btn'"
            :disabled="isDeleting"
        >
          <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M3 5h14M8 5V3h4v2M8 9v6M12 9v6M5 5l1 12a2 2 0 0 0 2 2h4a2 2 0 0 0 2-2l1-12" stroke="currentColor"
                  stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          <span class="btn-text">
            {{
              selectedPersonIds.size === 0 ? 'Recycle Bin' : (isDeleting ? 'DELETING...' : `DELETE (${selectedPersonIds.size})`)
            }}
          </span>
        </button>
      </div>
    </div>

    <div class="content-wrapper">
      <div class="main-content" :class="{ 'with-sidebar': showFilters }">
        <div class="persons-header">
          <div class="header-cell checkbox-col">
            <input
                type="checkbox"
                :checked="allSelected"
                :indeterminate="someSelected"
                @change="toggleSelectAll"
                class="select-checkbox"
            />
          </div>
          <div class="header-cell avatar-col"></div>
          <div class="header-cell">FIRST NAME</div>
          <div class="header-cell">LAST NAME</div>
          <div class="header-cell">TYPE</div>
          <div class="header-cell">TAGS</div>
          <div class="header-cell">STATUS</div>
          <div class="header-cell">{{ persons.length }}</div>
        </div>

        <div v-if="isLoading" class="loading">
          <span class="spinner"></span>
        </div>

        <div v-else class="persons-list">
          <PersonCard
              v-for="person in persons"
              :key="person.personId"
              :person="person"
              :first-name="safelyFindFieldInPerson(person, 'first name')"
              :last-name="safelyFindFieldInPerson(person, 'last name')"
              :is-selected="selectedPersonIds.has(person.personId)"
              @click="openPersonView(person)"
              @toggle-select="togglePersonSelection(person.personId)"
          />
        </div>

        <div v-if="!isLoading && persons.length === 0" class="empty-state">
          <p>No persons found</p>
        </div>
      </div>

      <FilterSidebar
          v-if="showFilters"
          :available-tags="availableTags"
          :available-types="availableTypes"
          :available-statuses="availableStatuses"
          @apply="applyFilters"
          @close="toggleFilters"
      />
    </div>
  </div>
</template>

<style scoped>
.person-view {
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
}

.icon-btn:hover {
  border-color: #1E90FF;
  background-color: rgba(30, 144, 255, 0.1);
}

.icon-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.create-btn {
  width: 48px;
  height: 48px;
  justify-content: center;
  padding: 0;
}

.delete-btn {
  border-color: #f44336;
  background-color: rgba(244, 67, 54, 0.1);
  color: #f44336;
}

.delete-btn:hover:not(:disabled) {
  background-color: rgba(244, 67, 54, 0.2);
  border-color: #f44336;
}

.recycle-btn {
  min-width: 48px;
  height: 48px;
  justify-content: center;
}

.recycle-btn:hover {
  border-color: #1E90FF;
  background-color: rgba(30, 144, 255, 0.1);
}

.btn-text {
  display: none;
}

@media (min-width: 769px) {
  .recycle-btn .btn-text {
    display: inline;
  }

  .delete-btn .btn-text {
    display: inline;
  }
}

.message-btn {
  letter-spacing: 0.05em;
}

.filter-btn.active {
  border-color: #1E90FF;
  background-color: rgba(30, 144, 255, 0.2);
  color: #1E90FF;
}

.search-box {
  display: flex;
  align-items: center;
  background-color: rgba(40, 40, 40, 0.5);
  border: 1px solid #282828;
  border-radius: 8px;
  overflow: hidden;
  transition: border-color 0.3s;
}

.search-box:focus-within {
  border-color: #1E90FF;
}

.search-input {
  padding: 0.75rem 1rem;
  background: transparent;
  border: none;
  color: rgba(255, 255, 255, 0.87);
  font-size: 0.9rem;
  outline: none;
  width: 250px;
}

.search-input::placeholder {
  color: #666;
}

.search-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0.75rem;
  background: transparent;
  border: none;
  color: #666;
  cursor: pointer;
  transition: color 0.3s;
}

.search-icon:hover {
  color: #1E90FF;
}

.content-wrapper {
  display: flex;
  gap: 1.5rem;
  flex: 1;
}

.main-content {
  flex: 1;
  transition: all 0.3s;
}

.main-content.with-sidebar {
  margin-right: 0;
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

.select-checkbox {
  width: 18px;
  height: 18px;
  cursor: pointer;
  accent-color: #1E90FF;
}

.persons-list {
  display: flex;
  flex-direction: column;
  gap: 0;
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
  justify-content: center;
  align-items: center;
  padding: 3rem;
  background-color: rgba(10, 10, 10, 0.8);
  border: 1px solid #282828;
  border-top: none;
  border-radius: 0 0 12px 12px;
  color: #666;
  font-size: 1.1rem;
}

@media (max-width: 1200px) {
  .persons-header {
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
  }

  .search-input {
    width: 100%;
  }

  .persons-header {
    display: none;
  }
}
</style>