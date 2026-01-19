<script setup lang="ts">
import {onMounted, ref, watch} from 'vue';
import {useRouter} from 'vue-router';
import axios from 'axios';

interface LineTemplate {
  lineName: string;
  lineValue: string;
}

interface PersonTemplate {
  personType: string;
  tags: string[];
  lineTemplates: LineTemplate[];
}

interface NewPerson {
  personType: string;
  tags: string[];
  lineTemplates: LineTemplate[];
}

const router = useRouter();

const availableTypes = ref<string[]>([]);
const selectedType = ref('');
const template = ref<PersonTemplate | null>(null);

const isLoading = ref(false);
const isSaving = ref(false);
const isLoadingTypes = ref(false);

const newPerson = ref<NewPerson>({
  personType: '',
  tags: [],
  lineTemplates: []
});

const newTag = ref('');
const newFieldName = ref('');

const isBoolean = (value: string) => {
  return value.toLowerCase() === 'true' || value.toLowerCase() === 'false';
};

const getBooleanValue = (value: string) => {
  return value.toLowerCase() === 'true';
};

const toggleBoolean = (line: LineTemplate) => {

  const currentValue = getBooleanValue(line.lineValue);
  line.lineValue = (!currentValue).toString();
};

const addTag = () => {

  if (!newTag.value.trim()) {
    return;
  }

  const tag = newTag.value.trim();

  if (!newPerson.value.tags.includes(tag)) {
    newPerson.value.tags.push(tag);
  }

  newTag.value = '';
};

const removeTag = (index: number) => {
  newPerson.value.tags.splice(index, 1);
};

const handleTagKeydown = (event: KeyboardEvent) => {

  if (event.key === 'Enter') {
    event.preventDefault();

    addTag();
  }
};

const addCustomField = () => {

  if (!newFieldName.value.trim()) {
    return;
  }

  const fieldName = newFieldName.value.trim();
  const exists = newPerson.value.lineTemplates.some(lt => lt.lineName === fieldName);

  if (!exists) {
    newPerson.value.lineTemplates.push({
      lineName: fieldName,
      lineValue: ''
    });
  }

  newFieldName.value = '';
};

const removeCustomField = (index: number) => {
  newPerson.value.lineTemplates.splice(index, 1);
};

const handleCustomFieldKeydown = (event: KeyboardEvent) => {

  if (event.key === 'Enter') {
    event.preventDefault();

    addCustomField();
  }
};

async function fetchPersonTypes() {

  const token = localStorage.getItem('access_token');

  if (!token) {
    return;
  }

  isLoadingTypes.value = true;

  try {
    const response = await axios.get('/api/person/templates/types/all', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    availableTypes.value = response.data;

  } catch (error) {
    console.error('Failed to fetch person types:', error);

  } finally {
    isLoadingTypes.value = false;
  }
}

async function fetchTemplate(personType: string) {

  const token = localStorage.getItem('access_token');

  if (!token) {
    return;
  }

  isLoading.value = true;

  try {
    const response = await axios.get(`/api/person/templates/${personType}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    const templateData = Array.isArray(response.data) ? response.data[0] : response.data;

    if (templateData) {
      template.value = templateData;

      newPerson.value = {
        personType: templateData.personType,
        tags: [...templateData.tags],
        lineTemplates: templateData.lineTemplates.map((lt: LineTemplate) => ({...lt}))
      };
    }

  } catch (error) {
    console.error('Failed to fetch template:', error);

  } finally {
    isLoading.value = false;
  }
}

watch(selectedType, (newType) => {
  if (newType) {
    fetchTemplate(newType);
  }
});

async function savePerson() {

  const token = localStorage.getItem('access_token');

  if (!token) {
    return;
  }

  isSaving.value = true;

  try {
    const response = await axios.post('/api/person/', newPerson.value, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    const createdPerson = Array.isArray(response.data) ? response.data[0] : response.data;
    await router.push(`/person/${createdPerson.personId}`);

  } catch (error) {

    console.error('Failed to create person:', error);
    alert('Failed to create person. Please try again.');

  } finally {
    isSaving.value = false;
  }
}

function goBack() {
  router.push('/contacts');
}

onMounted(() => {
  fetchPersonTypes();
});
</script>

<template>
  <div class="create-person-view">
    <div class="top-bar">
      <div class="actions-left">
        <button @click="goBack" class="icon-btn back-btn">
          <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 4L6 10L12 16" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                  stroke-linejoin="round"/>
          </svg>
        </button>
        <h2 class="page-title">Create New Person</h2>
      </div>

      <div class="actions-right">
        <button
            class="icon-btn save-btn"
            @click="savePerson"
            :disabled="!selectedType || isSaving"
        >
          <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M16 2H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V4a2 2 0 0 0-2-2z" stroke="currentColor"
                  stroke-width="1.5"/>
            <path d="M14 2v6H6V2M6 18v-6h8v6" stroke="currentColor" stroke-width="1.5"/>
          </svg>
          <span>{{ isSaving ? 'SAVING...' : 'SAVE' }}</span>
        </button>
      </div>
    </div>

    <div class="content">
      <div class="card-section">
        <div class="section-header">
          <h3>Select Person Type</h3>
        </div>
        <div class="fields-grid">
          <div class="field-group full-width">
            <label>Type</label>
            <select v-model="selectedType" class="field-input field-select" :disabled="isLoadingTypes">
              <option value="" disabled>{{ isLoadingTypes ? 'Loading types...' : 'Choose a type...' }}</option>
              <option v-for="type in availableTypes" :key="type" :value="type">
                {{ type.charAt(0).toUpperCase() + type.slice(1).toLowerCase() }}
              </option>
            </select>
          </div>
        </div>
      </div>

      <div v-if="isLoading" class="loading">
        <span class="spinner"></span>
      </div>

      <template v-else-if="selectedType && newPerson">
        <div class="card-section">
          <div class="section-header">
            <h3>Basic Information</h3>
          </div>
          <div class="fields-grid">
            <div class="field-group full-width">
              <label>Tags</label>
              <div class="tags-container">
                <div class="tags-list">
                  <span
                      v-for="(tag, index) in newPerson.tags"
                      :key="index"
                      class="tag-chip"
                  >
                    {{ tag }}
                    <button
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
                <div class="tag-input-wrapper">
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

        <div class="card-section" v-if="newPerson.lineTemplates.length > 0 || selectedType === 'CUSTOM'">
          <div class="section-header">
            <h3>{{ selectedType === 'CUSTOM' ? 'Custom Fields' : 'Details' }}</h3>
          </div>
          <div class="fields-grid">
            <div
                v-for="(line, index) in newPerson.lineTemplates"
                :key="index"
                class="field-group"
            >
              <div class="field-header">
                <label>{{ line.lineName }}</label>
                <button
                    v-if="selectedType === 'CUSTOM'"
                    @click="removeCustomField(index)"
                    class="remove-field-btn"
                    type="button"
                >
                  <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M12 4L4 12M4 4l8 8" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
                  </svg>
                </button>
              </div>

              <div v-if="isBoolean(line.lineValue)" class="toggle-wrapper">
                <button
                    @click="toggleBoolean(line)"
                    class="toggle-button"
                    :class="{ 'active': getBooleanValue(line.lineValue) }"
                    type="button"
                >
                  <span class="toggle-slider"></span>
                </button>
                <span class="toggle-label">{{ getBooleanValue(line.lineValue) ? 'Yes' : 'No' }}</span>
              </div>

              <input
                  v-else
                  v-model="line.lineValue"
                  class="field-input"
                  :placeholder="`Enter ${line.lineName.toLowerCase()}`"
              />
            </div>

            <div v-if="selectedType === 'CUSTOM'" class="field-group full-width">
              <label>Add New Field</label>
              <div class="tag-input-wrapper">
                <input
                    v-model="newFieldName"
                    @keydown="handleCustomFieldKeydown"
                    placeholder="Enter field name and press Enter"
                    class="field-input"
                />
                <button @click="addCustomField" class="add-tag-btn" type="button">
                  <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M8 3v10M3 8h10" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                  </svg>
                  Add Field
                </button>
              </div>
            </div>
          </div>
        </div>
      </template>
    </div>
  </div>
</template>

<style scoped>
.create-person-view {
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

.icon-btn:hover:not(:disabled) {
  border-color: #1E90FF;
  background-color: rgba(30, 144, 255, 0.1);
}

.icon-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.back-btn {
  width: 48px;
  height: 48px;
  justify-content: center;
  padding: 0;
}

.save-btn:hover:not(:disabled) {
  border-color: #4CAF50;
  background-color: rgba(76, 175, 80, 0.1);
  color: #4CAF50;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 200px;
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

.field-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.field-group label {
  font-size: 0.875rem;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.6);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.remove-field-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0.25rem;
  background: none;
  border: none;
  color: rgba(255, 255, 255, 0.4);
  cursor: pointer;
  transition: color 0.2s;
  border-radius: 4px;
}

.remove-field-btn:hover {
  color: #f44336;
  background-color: rgba(244, 67, 54, 0.1);
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

.field-input:focus {
  border-color: #1E90FF;
  background-color: rgba(30, 144, 255, 0.05);
}

.field-input::placeholder {
  color: #666;
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

.toggle-button:hover {
  border-color: #1E90FF;
}

.toggle-button.active {
  background-color: #1E90FF;
  border-color: #1E90FF;
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