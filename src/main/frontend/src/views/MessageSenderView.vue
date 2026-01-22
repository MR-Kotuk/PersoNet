<script setup lang="ts">
import {computed, onMounted, ref, watch} from 'vue';
import {useRouter} from 'vue-router';
import axios from 'axios';
import RecipientsList from '../components/RecipientsList.vue';
import MessageComposer from '../components/MessageComposer.vue';
import TemplatesModal from '../components/TemplatesModal.vue';
import ConfirmationModal from '../components/ConfirmationModal.vue';
import ValidationErrorModal from '../components/ValidationErrorModal.vue';

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

interface MessageTemplate {
  id: number;
  email: string;
  name: string;
  message: string;
}

const router = useRouter();

const persons = ref<Person[]>([]);
const selectedPersonIds = ref<Set<number>>(new Set());
const sharedFields = ref<string[]>([]);

const isLoading = ref(false);
const isSending = ref(false);
const isLoadingTemplates = ref(false);
const isSavingTemplate = ref(false);

const showConfirmation = ref(false);
const showValidationError = ref(false);
const showTemplatePanel = ref(false);

const invalidFields = ref<string[]>([]);

const subject = ref('');
const messageContent = ref('');

const templates = ref<MessageTemplate[]>([]);

const selectedPersons = computed(() => {
  return persons.value.filter(p => selectedPersonIds.value.has(p.personId));
});

const canSend = computed(() => {
  return selectedPersonIds.value.size > 0 &&
      subject.value.trim() !== '' &&
      messageContent.value.trim() !== '';
});

const isCurrentMessageSaved = computed(() => {
  return subject.value.trim() && messageContent.value.trim()
      ? templates.value.some(t => t.name === subject.value && t.message === messageContent.value)
      : false;
});

watch(selectedPersonIds, async (newIds) => {
  if (newIds.size > 0) {
    await fetchSharedFields();
  } else {
    sharedFields.value = [];
  }
}, {deep: true});

async function fetchPersons() {

  const token = localStorage.getItem('access_token');

  if (!token) {

    await router.push('/sign-in');
    return;
  }

  isLoading.value = true;

  try {
    const response = await axios.get('/api/sender/', {
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

async function fetchSharedFields() {

  const token = localStorage.getItem('access_token');

  if (!token || selectedPersonIds.value.size === 0) {
    return;
  }

  try {
    const idsArray = Array.from(selectedPersonIds.value);
    const response = await axios.post('/api/sender/shared-lines', idsArray, {
      headers: {Authorization: `Bearer ${token}`}
    });

    sharedFields.value = response.data;

  } catch (error) {
    console.error('Failed to fetch shared fields:', error);
  }
}

async function fetchTemplates() {

  const token = localStorage.getItem('access_token');

  if (!token) {
    return;
  }

  isLoadingTemplates.value = true;

  try {
    const response = await axios.get('/api/message-templates/', {
      headers: {Authorization: `Bearer ${token}`}
    });

    templates.value = response.data;

  } catch (error) {
    console.error('Failed to fetch templates:', error);

  } finally {
    isLoadingTemplates.value = false;
  }
}

async function saveAsTemplate() {

  if (isCurrentMessageSaved.value) {
    return;
  }

  const token = localStorage.getItem('access_token');

  if (!token) {

    await router.push('/sign-in');
    return;
  }

  if (!subject.value.trim() || !messageContent.value.trim()) {

    alert('Please fill in subject and message before saving');
    return;
  }

  isSavingTemplate.value = true;

  try {
    const payload = {
      name: subject.value,
      message: messageContent.value
    };

    const response = await axios.post('/api/message-templates/', payload, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    templates.value.push(response.data);

  } catch (error) {
    console.error('Failed to save template:', error);

    if (axios.isAxiosError(error)) {
      alert(`Failed to save template: ${error.response?.data || error.message}`);
    }
  } finally {
    isSavingTemplate.value = false;
  }
}

async function deleteTemplate(templateId: number) {

  const token = localStorage.getItem('access_token');

  if (!token) {

    await router.push('/sign-in');
    return;
  }

  if (!confirm('Are you sure you want to delete this template?')) {
    return;
  }

  try {
    await axios.delete('/api/message-templates/', {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      data: [templateId]
    });

    templates.value = templates.value.filter(t => t.id !== templateId);

  } catch (error) {
    console.error('Failed to delete template:', error);

    if (axios.isAxiosError(error)) {
      alert(`Failed to delete template: ${error.response?.data || error.message}`);
    }
  }
}

function loadTemplate(template: MessageTemplate) {

  subject.value = template.name;
  messageContent.value = template.message;

  showTemplatePanel.value = false
}

function clearMessage() {

  subject.value = '';
  messageContent.value = '';
}

function extractUsedFields(content: string): string[] {

  const fieldPattern = /\[<\/.*?\/>\]/g;
  const matches = content.match(fieldPattern);

  return matches || [];
}

function validateFields(): boolean {

  const subjectFields = extractUsedFields(subject.value);
  const messageFields = extractUsedFields(messageContent.value);
  const allUsedFields = [...new Set([...subjectFields, ...messageFields])];

  const invalid = allUsedFields.filter(field => !sharedFields.value.includes(field));

  if (invalid.length > 0) {

    invalidFields.value = invalid;
    showValidationError.value = true;

    return false;
  }

  return true;
}

function openConfirmation() {

  if (!canSend.value) {
    return;
  }

  if (!validateFields()) {
    return;
  }

  showConfirmation.value = true;
}

function closeConfirmation() {
  showConfirmation.value = false;
}

function closeValidationError() {

  showValidationError.value = false;
  invalidFields.value = [];
}

async function sendMessage() {

  const token = localStorage.getItem('access_token');

  if (!token) {

    await router.push('/sign-in');
    return;
  }

  isSending.value = true;

  try {
    const payload = {
      subject: subject.value,
      message: messageContent.value,
      recipient: Array.from(selectedPersonIds.value)
    };

    await axios.post('/api/sender/send', payload, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    alert('Message sent successfully!');

    clearMessage();
    selectedPersonIds.value.clear();
    closeConfirmation();

  } catch (error) {
    console.error('Failed to send message:', error);

    if (axios.isAxiosError(error)) {
      alert(`Failed to send message: ${error.response?.data || error.message}`);
    }

  } finally {
    isSending.value = false;
  }
}

function findPersonEmail(person: Person): string | null {

  for (const lineTemplate of person.lineTemplates) {
    if (lineTemplate.lineName.toLowerCase() === "email") {
      return lineTemplate.lineValue;
    }
  }

  return null;
}

onMounted(() => {
  fetchPersons();
  fetchTemplates();
});
</script>

<template>
  <div class="message-sender">
    <div class="header">
      <h1>Send Messages</h1>
      <div class="header-actions">
        <button @click="showTemplatePanel = !showTemplatePanel" class="icon-btn templates-btn">
          <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M5 2a2 2 0 0 0-2 2v14l7-5 7 5V4a2 2 0 0 0-2-2H5z" stroke="currentColor" stroke-width="2"
                  stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          <span>Templates</span>
          <span v-if="templates.length > 0" class="badge">{{ templates.length }}</span>
        </button>
        <button @click="router.back()" class="icon-btn back-btn">
          <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M15 10H5M5 10L10 5M5 10L10 15" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                  stroke-linejoin="round"/>
          </svg>
          <span>Back</span>
        </button>
      </div>
    </div>

    <div class="content-wrapper">
      <RecipientsList :persons="persons" :isLoading="isLoading" :selectedPersonIds="selectedPersonIds"
                      @update:selectedPersonIds="selectedPersonIds = $event" :findPersonEmail="findPersonEmail"/>

      <MessageComposer v-model:subject="subject" v-model:messageContent="messageContent" :sharedFields="sharedFields"
                       :canSend="canSend" :isSending="isSending" :isCurrentMessageSaved="isCurrentMessageSaved"
                       :isSavingTemplate="isSavingTemplate" @save-template="saveAsTemplate" @send="openConfirmation"
                       @clear-message="clearMessage"/>
    </div>

    <TemplatesModal v-if="showTemplatePanel" :templates="templates" :isLoading="isLoadingTemplates"
                    @close="showTemplatePanel = false" @load="loadTemplate" @delete="deleteTemplate"/>

    <ValidationErrorModal v-if="showValidationError" :invalidFields="invalidFields" @close="closeValidationError"/>

    <ConfirmationModal v-if="showConfirmation" :selectedPersons="selectedPersons" :isSending="isSending"
                       :findPersonEmail="findPersonEmail" @close="closeConfirmation" @confirm="sendMessage"/>
  </div>
</template>

<style scoped>
.message-sender {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  position: relative;
  z-index: 50;
}

.header h1 {
  font-size: 2rem;
  font-weight: 700;
  color: rgba(255, 255, 255, 0.87);
}

.header-actions {
  display: flex;
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
  position: relative;
}

.icon-btn:hover {
  border-color: #1E90FF;
  background-color: rgba(30, 144, 255, 0.1);
}

.badge {
  padding: 0.125rem 0.5rem;
  background-color: rgba(30, 144, 255, 0.3);
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 700;
  color: #1E90FF;
}

.content-wrapper {
  display: grid;
  grid-template-columns: 320px 1fr;
  gap: 2rem;
  flex: 1;
  position: relative;
  max-width: 100%;
}

@media (max-width: 1200px) {
  .content-wrapper {
    grid-template-columns: 280px 1fr;
  }
}

@media (max-width: 900px) {
  .message-sender {
    padding: 1rem;
  }

  .header {
    margin-bottom: 1.5rem;
  }

  .header h1 {
    font-size: 1.75rem;
  }

  .content-wrapper {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }
}

@media (max-width: 768px) {
  .header h1 {
    font-size: 1.5rem;
  }
}

@media (max-width: 480px) {
  .message-sender {
    padding: 0.75rem;
  }

  .header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }

  .header h1 {
    font-size: 1.25rem;
  }

  .icon-btn {
    padding: 0.6rem 0.9rem;
    font-size: 0.85rem;
  }

  .content-wrapper {
    gap: 1rem;
  }
}
</style>