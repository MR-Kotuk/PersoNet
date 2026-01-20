<script setup lang="ts">
import {computed, onMounted, ref, watch} from 'vue';
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
}

const router = useRouter();
const persons = ref<Person[]>([]);
const selectedPersonIds = ref<Set<number>>(new Set());
const sharedFields = ref<string[]>([]);
const isLoading = ref(false);
const isSending = ref(false);
const showConfirmation = ref(false);
const showValidationError = ref(false);
const invalidFields = ref<string[]>([]);

const subject = ref('');
const messageContent = ref('');
const editorRef = ref<HTMLDivElement | null>(null);
const subjectEditorRef = ref<HTMLDivElement | null>(null);

const searchKeyword = ref('');

const filteredPersons = computed(() => {

  if (!searchKeyword.value.trim()) {
    return persons.value;
  }

  const keyword = searchKeyword.value.toLowerCase();

  return persons.value.filter(p =>
      findPersonEmail(p)?.toLowerCase().includes(keyword) ||
      p.tags.some(t => t.toLowerCase().includes(keyword)) ||
      p.personType.toLowerCase().includes(keyword)
  );
});

const selectedPersons = computed(() => {
  return persons.value.filter(p => selectedPersonIds.value.has(p.personId));
});

const allSelected = computed(() => {
  return filteredPersons.value.length > 0 &&
      filteredPersons.value.every(p => selectedPersonIds.value.has(p.personId));
});

const someSelected = computed(() => {
  return selectedPersonIds.value.size > 0 && !allSelected.value;
});

const canSend = computed(() => {
  return selectedPersonIds.value.size > 0 &&
      subject.value.trim() !== '' &&
      messageContent.value.trim() !== '';
});

watch(selectedPersonIds, async (newIds) => {

  if (newIds.size > 0) {
    await fetchSharedFields();
  } else {
    sharedFields.value = [];
  }

}, {deep: true});

function toggleSelectAll() {

  if (allSelected.value) {
    filteredPersons.value.forEach(p => selectedPersonIds.value.delete(p.personId));
  } else {
    filteredPersons.value.forEach(p => selectedPersonIds.value.add(p.personId));
  }

  selectedPersonIds.value = new Set(selectedPersonIds.value);
}

function togglePersonSelection(personId: number) {

  if (selectedPersonIds.value.has(personId)) {
    selectedPersonIds.value.delete(personId);
  } else {
    selectedPersonIds.value.add(personId);
  }

  selectedPersonIds.value = new Set(selectedPersonIds.value);
}

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

function extractFieldName(field: string): string {
  const match = field.match(/\[<\/(.*?)\/>\]/);
  return match?.[1] ?? field;
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

    highlightInvalidFields();
    return false;
  }

  return true;
}

function highlightInvalidFields() {

  if (subjectEditorRef.value) {
    highlightInvalidFieldsInElement(subjectEditorRef.value);
  }

  if (editorRef.value) {
    highlightInvalidFieldsInElement(editorRef.value);
  }
}

function highlightInvalidFieldsInElement(element: HTMLElement) {

  const fieldTags = element.querySelectorAll('.field-tag');

  fieldTags.forEach(tag => {

    const field = tag.getAttribute('data-field');

    if (field && invalidFields.value.includes(field)) {
      tag.classList.add('invalid-field');
    } else {
      tag.classList.remove('invalid-field');
    }
  });
}

function closeValidationError() {

  showValidationError.value = false;
  invalidFields.value = [];

  if (subjectEditorRef.value) {
    const tags = subjectEditorRef.value.querySelectorAll('.invalid-field');
    tags.forEach(tag => tag.classList.remove('invalid-field'));
  }

  if (editorRef.value) {
    const tags = editorRef.value.querySelectorAll('.invalid-field');
    tags.forEach(tag => tag.classList.remove('invalid-field'));
  }
}

function insertFieldIntoSubject(field: string) {

  if (!subjectEditorRef.value) {
    return;
  }

  const displayName = extractFieldName(field);
  const selection = window.getSelection();
  const range = selection?.getRangeAt(0);

  if (range && subjectEditorRef.value.contains(range.commonAncestorContainer)) {

    const fieldSpan = document.createElement('span');

    fieldSpan.className = 'field-tag';
    fieldSpan.contentEditable = 'false';
    fieldSpan.textContent = displayName;
    fieldSpan.setAttribute('data-field', field);

    range.deleteContents();
    range.insertNode(fieldSpan);

    const spaceNode = document.createTextNode('\u00A0');

    range.setStartAfter(fieldSpan);
    range.insertNode(spaceNode);
    range.setStartAfter(spaceNode);
    range.collapse(true);

    selection?.removeAllRanges();
    selection?.addRange(range);

  } else {

    const fieldSpan = document.createElement('span');

    fieldSpan.className = 'field-tag';
    fieldSpan.contentEditable = 'false';
    fieldSpan.textContent = displayName;
    fieldSpan.setAttribute('data-field', field);

    subjectEditorRef.value.appendChild(fieldSpan);
    subjectEditorRef.value.appendChild(document.createTextNode('\u00A0'));
  }

  updateSubjectContent();
  subjectEditorRef.value.focus();
}

function insertField(field: string) {

  if (!editorRef.value) {
    return;
  }

  const displayName = extractFieldName(field);
  const selection = window.getSelection();
  const range = selection?.getRangeAt(0);

  if (range && editorRef.value.contains(range.commonAncestorContainer)) {

    const fieldSpan = document.createElement('span');

    fieldSpan.className = 'field-tag';
    fieldSpan.contentEditable = 'false';
    fieldSpan.textContent = displayName;
    fieldSpan.setAttribute('data-field', field);

    range.deleteContents();
    range.insertNode(fieldSpan);

    const spaceNode = document.createTextNode('\u00A0');

    range.setStartAfter(fieldSpan);
    range.insertNode(spaceNode);
    range.setStartAfter(spaceNode);
    range.collapse(true);

    selection?.removeAllRanges();
    selection?.addRange(range);

  } else {

    const fieldSpan = document.createElement('span');

    fieldSpan.className = 'field-tag';
    fieldSpan.contentEditable = 'false';
    fieldSpan.textContent = displayName;
    fieldSpan.setAttribute('data-field', field);

    editorRef.value.appendChild(fieldSpan);
    editorRef.value.appendChild(document.createTextNode('\u00A0'));
  }

  updateMessageContent();
  editorRef.value.focus();
}

function handleDragStart(event: DragEvent, field: string) {
  event.dataTransfer!.effectAllowed = 'copy';
  event.dataTransfer!.setData('application/x-field', field);
}

function handleSubjectDrop(event: DragEvent) {

  event.preventDefault();
  const field = event.dataTransfer!.getData('application/x-field');

  if (field && sharedFields.value.includes(field)) {
    insertFieldIntoSubject(field);
  }
}

function handleDrop(event: DragEvent) {

  event.preventDefault();
  const field = event.dataTransfer!.getData('application/x-field');

  if (field && sharedFields.value.includes(field)) {
    insertField(field);
  }
}

function handlePaste(event: ClipboardEvent) {

  event.preventDefault();

  const text = event.clipboardData?.getData('text/plain') || '';
  const sanitized = text.replace(/\[<\/.*?\/>\]/g, '');

  const selection = window.getSelection();

  if (selection && selection.rangeCount > 0) {

    const range = selection.getRangeAt(0);

    range.deleteContents();
    range.insertNode(document.createTextNode(sanitized));
    range.collapse(false);
  }

  updateMessageContent();
}

function handleInput() {
  updateMessageContent();
  validateFieldTags();
}

function handleSubjectInput() {
  updateSubjectContent();
  validateSubjectFieldTags();
}

function validateSubjectFieldTags() {

  if (!subjectEditorRef.value) {
    return;
  }

  const walker = document.createTreeWalker(
      subjectEditorRef.value,
      NodeFilter.SHOW_TEXT,
      null
  );

  const textNodes: Node[] = [];

  let node;
  while (node = walker.nextNode()) {
    textNodes.push(node);
  }

  textNodes.forEach(textNode => {

    const text = textNode.textContent || '';
    const pattern = /\[<\/.*?\/>\]/g;

    if (pattern.test(text)) {

      const cleaned = text.replace(pattern, '');

      if (textNode.parentNode) {
        textNode.textContent = cleaned;
      }
    }
  });
}

function validateFieldTags() {

  if (!editorRef.value) {
    return;
  }

  const walker = document.createTreeWalker(
      editorRef.value,
      NodeFilter.SHOW_TEXT,
      null
  );

  const textNodes: Node[] = [];

  let node;
  while (node = walker.nextNode()) {
    textNodes.push(node);
  }

  textNodes.forEach(textNode => {

    const text = textNode.textContent || '';
    const pattern = /\[<\/.*?\/>\]/g;

    if (pattern.test(text)) {

      const cleaned = text.replace(pattern, '');

      if (textNode.parentNode) {
        textNode.textContent = cleaned;
      }
    }
  });
}

function updateMessageContent() {
  messageContent.value = getEditorHtml(editorRef.value);
}

function updateSubjectContent() {
  subject.value = getEditorHtml(subjectEditorRef.value, true);
}

function getEditorHtml(editor: HTMLElement | null, cleanSubject = false) {

  if (!editor) {
    return '';
  }

  const clone = editor.cloneNode(true) as HTMLElement;
  const fieldTags = clone.querySelectorAll('.field-tag');

  fieldTags.forEach(tag => {

    const field = tag.getAttribute('data-field');

    if (field) {
      tag.replaceWith(document.createTextNode(field));
    }
  });

  let html = clone.innerHTML;
  html = html.replace(/&nbsp;/g, ' ');
  html = html.replace(/&lt;/g, '<');
  html = html.replace(/&gt;/g, '>');

  if (cleanSubject) {
    html = html.replace(/<br>/g, '');
    html = html.replace(/<div>/g, '');
    html = html.replace(/<\/div>/g, '');
  }

  return html;
}

function execCommand(command: string, value: string | undefined = undefined) {

  document.execCommand(command, false, value);

  editorRef.value?.focus();
  updateMessageContent();
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

    subject.value = '';
    messageContent.value = '';

    if (editorRef.value) {
      editorRef.value.innerHTML = '';
    }

    if (subjectEditorRef.value) {
      subjectEditorRef.value.innerHTML = '';
    }

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
});
</script>

<template>
  <div class="message-sender">
    <div class="header">
      <h1>Send Messages</h1>
      <button @click="router.back()" class="icon-btn back-btn">
        <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M15 10H5M5 10L10 5M5 10L10 15" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                stroke-linejoin="round"/>
        </svg>
        <span>Back</span>
      </button>
    </div>

    <div class="content-wrapper">
      <div class="left-panel">
        <div class="panel-header">
          <h2>Recipients</h2>
          <div class="search-box">
            <input
                v-model="searchKeyword"
                type="text"
                placeholder="Search recipients..."
                class="search-input"
            />
            <svg class="search-icon" width="16" height="16" viewBox="0 0 20 20" fill="none"
                 xmlns="http://www.w3.org/2000/svg">
              <path d="M9 17A8 8 0 1 0 9 1a8 8 0 0 0 0 16zM19 19l-4.35-4.35" stroke="currentColor" stroke-width="2"
                    stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
        </div>

        <div class="recipients-header">
          <input
              type="checkbox"
              :checked="allSelected"
              :indeterminate="someSelected"
              @change="toggleSelectAll"
              class="select-checkbox"
          />
          <span class="header-label">SELECT ALL</span>
        </div>

        <div v-if="isLoading" class="loading">
          <span class="spinner"></span>
        </div>

        <div v-else class="recipients-list">
          <div
              v-for="person in filteredPersons"
              :key="person.personId"
              class="recipient-card"
              :class="{ selected: selectedPersonIds.has(person.personId) }"
              @click="togglePersonSelection(person.personId)"
          >
            <input
                type="checkbox"
                :checked="selectedPersonIds.has(person.personId)"
                @click.stop="togglePersonSelection(person.personId)"
                class="select-checkbox"
            />
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

      <div class="right-panel">
        <div class="compose-section">
          <div class="subject-section">
            <label for="subject">Subject</label>
            <div
                ref="subjectEditorRef"
                class="subject-editor"
                contenteditable="true"
                @input="handleSubjectInput"
                @drop="handleSubjectDrop"
                @dragover.prevent
                placeholder="Enter subject..."
            ></div>
          </div>

          <div class="editor-section">
            <div class="editor-toolbar">
              <button @click="execCommand('bold')" class="toolbar-btn" title="Bold">
                <strong>B</strong>
              </button>
              <button @click="execCommand('italic')" class="toolbar-btn" title="Italic">
                <em>I</em>
              </button>
              <button @click="execCommand('underline')" class="toolbar-btn" title="Underline">
                <u>U</u>
              </button>
              <div class="toolbar-separator"></div>
              <button @click="execCommand('insertUnorderedList')" class="toolbar-btn" title="Bullet List">
                <svg width="16" height="16" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M8 6h10M8 12h10M8 18h10M4 6h.01M4 12h.01M4 18h.01" stroke="currentColor" stroke-width="2"
                        stroke-linecap="round"/>
                </svg>
              </button>
              <button @click="execCommand('insertOrderedList')" class="toolbar-btn" title="Numbered List">
                <svg width="16" height="16" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M8 6h10M8 12h10M8 18h10M4 6v4M4 18v-4m0 0h1m-1 0v-2" stroke="currentColor" stroke-width="2"
                        stroke-linecap="round"/>
                </svg>
              </button>
            </div>

            <div
                ref="editorRef"
                class="message-editor"
                contenteditable="true"
                @input="handleInput"
                @paste="handlePaste"
                @drop="handleDrop"
                @dragover.prevent
                placeholder="Type your message here..."
            ></div>
          </div>

          <div v-if="sharedFields.length > 0" class="shared-fields-section">
            <h3>Shared Fields</h3>
            <p class="fields-hint">Click or drag fields into your message (can be used multiple times)</p>
            <div class="fields-grid">
              <div
                  v-for="field in sharedFields"
                  :key="field"
                  class="field-chip"
                  draggable="true"
                  @dragstart="handleDragStart($event, field)"
                  @click="insertField(field)"
              >
                <svg width="14" height="14" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M3 7h14M3 13h14" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                </svg>
                {{ extractFieldName(field) }}
              </div>
            </div>
          </div>

          <div class="action-section">
            <button
                @click="openConfirmation"
                class="send-btn"
                :disabled="!canSend || isSending"
            >
              <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M2 10l16-8-8 16-2-8-6-2z" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                      stroke-linejoin="round"/>
              </svg>
              {{ isSending ? 'Sending...' : 'Send Message' }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <div v-if="showValidationError" class="modal-overlay" @click="closeValidationError">
      <div class="modal-content error-modal" @click.stop>
        <div class="error-icon">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="12" cy="12" r="10" stroke="#EF4444" stroke-width="2"/>
            <path d="M12 8v4M12 16h.01" stroke="#EF4444" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <h2>Invalid Fields Detected</h2>
        <p>The following fields are not available for the selected recipients:</p>
        <div class="invalid-fields-list">
          <div v-for="field in invalidFields" :key="field" class="invalid-field-item">
            <svg width="16" height="16" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M10 10l5-5M10 10l-5 5M10 10l5 5M10 10l-5-5" stroke="#EF4444" stroke-width="2"
                    stroke-linecap="round"/>
            </svg>
            {{ extractFieldName(field) }}
          </div>
        </div>
        <p class="error-hint">Please remove these fields or select different recipients that have these fields.</p>
        <div class="modal-actions">
          <button @click="closeValidationError" class="confirm-btn error-btn">Got it</button>
        </div>
      </div>
    </div>

    <div v-if="showConfirmation" class="modal-overlay" @click="closeConfirmation">
      <div class="modal-content" @click.stop>
        <h2>Confirm Send</h2>
        <p>Are you sure you want to send this message to:</p>
        <div class="confirmation-recipients">
          <div v-for="person in selectedPersons" :key="person.personId" class="confirmation-email">
            {{ findPersonEmail(person) }}
          </div>
        </div>
        <div class="modal-actions">
          <button @click="closeConfirmation" class="cancel-btn">Cancel</button>
          <button @click="sendMessage" class="confirm-btn" :disabled="isSending">
            {{ isSending ? 'Sending...' : 'Yes, Send' }}
          </button>
        </div>
      </div>
    </div>
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
}

.header h1 {
  font-size: 2rem;
  font-weight: 700;
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
}

.icon-btn:hover {
  border-color: #1E90FF;
  background-color: rgba(30, 144, 255, 0.1);
}

.content-wrapper {
  display: grid;
  grid-template-columns: 350px 1fr;
  gap: 2rem;
  flex: 1;
}

.left-panel {
  display: flex;
  flex-direction: column;
  background-color: rgba(10, 10, 10, 0.8);
  border: 1px solid #282828;
  border-radius: 12px;
  overflow: hidden;
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

.right-panel {
  display: flex;
  flex-direction: column;
}

.compose-section {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  background-color: rgba(10, 10, 10, 0.8);
  border: 1px solid #282828;
  border-radius: 12px;
  padding: 2rem;
}

.subject-section label {
  display: block;
  margin-bottom: 0.5rem;
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.875rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.subject-editor {
  width: 100%;
  min-height: 48px;
  max-height: 100px;
  padding: 0.875rem 1rem;
  background-color: rgba(40, 40, 40, 0.5);
  border: 1px solid #282828;
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.87);
  font-size: 1rem;
  outline: none;
  transition: border-color 0.3s;
  overflow-y: auto;
  line-height: 1.5;
}

.subject-editor:focus {
  border-color: #1E90FF;
}

.subject-editor:empty:before {
  content: attr(placeholder);
  color: #666;
  pointer-events: none;
}

.subject-editor :deep(.field-tag) {
  display: inline-block;
  padding: 0.25rem 0.75rem;
  margin: 0 0.25rem;
  background: linear-gradient(135deg, rgba(30, 144, 255, 0.2), rgba(30, 144, 255, 0.3));
  border: 1px solid rgba(30, 144, 255, 0.5);
  border-radius: 6px;
  color: #1E90FF;
  font-size: 0.875rem;
  font-weight: 600;
  font-family: 'Courier New', monospace;
  letter-spacing: 0.05em;
  cursor: default;
  user-select: none;
  box-shadow: 0 2px 8px rgba(30, 144, 255, 0.2);
}

.subject-editor :deep(.field-tag.invalid-field) {
  background: linear-gradient(135deg, rgba(239, 68, 68, 0.2), rgba(239, 68, 68, 0.3));
  border: 1px solid rgba(239, 68, 68, 0.5);
  color: #EF4444;
  box-shadow: 0 2px 8px rgba(239, 68, 68, 0.2);
  animation: shake 0.5s;
}

.editor-section {
  display: flex;
  flex-direction: column;
  border: 1px solid #282828;
  border-radius: 8px;
  overflow: hidden;
}

.editor-toolbar {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  padding: 0.75rem;
  background-color: rgba(20, 20, 20, 0.5);
  border-bottom: 1px solid #282828;
}

.toolbar-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  background: transparent;
  border: 1px solid transparent;
  border-radius: 4px;
  color: rgba(255, 255, 255, 0.7);
  cursor: pointer;
  transition: all 0.2s;
}

.toolbar-btn:hover {
  background-color: rgba(30, 144, 255, 0.1);
  border-color: #1E90FF;
  color: #1E90FF;
}

.toolbar-separator {
  width: 1px;
  height: 20px;
  background-color: #282828;
  margin: 0 0.5rem;
}

.message-editor {
  min-height: 300px;
  max-height: 500px;
  padding: 1rem;
  background-color: rgba(40, 40, 40, 0.5);
  color: rgba(255, 255, 255, 0.87);
  font-size: 0.95rem;
  line-height: 1.6;
  overflow-y: auto;
  outline: none;
}

.message-editor:empty:before {
  content: attr(placeholder);
  color: #666;
  pointer-events: none;
}

.message-editor :deep(.field-tag) {
  display: inline-block;
  padding: 0.25rem 0.75rem;
  margin: 0 0.25rem;
  background: linear-gradient(135deg, rgba(30, 144, 255, 0.2), rgba(30, 144, 255, 0.3));
  border: 1px solid rgba(30, 144, 255, 0.5);
  border-radius: 6px;
  color: #1E90FF;
  font-size: 0.875rem;
  font-weight: 600;
  font-family: 'Courier New', monospace;
  letter-spacing: 0.05em;
  cursor: default;
  user-select: none;
  box-shadow: 0 2px 8px rgba(30, 144, 255, 0.2);
}

.message-editor :deep(.field-tag.invalid-field) {
  background: linear-gradient(135deg, rgba(239, 68, 68, 0.2), rgba(239, 68, 68, 0.3));
  border: 1px solid rgba(239, 68, 68, 0.5);
  color: #EF4444;
  box-shadow: 0 2px 8px rgba(239, 68, 68, 0.2);
  animation: shake 0.5s;
}

@keyframes shake {
  0%, 100% {
    transform: translateX(0);
  }
  25% {
    transform: translateX(-4px);
  }
  75% {
    transform: translateX(4px);
  }
}

.shared-fields-section {
  padding: 1.5rem;
  background-color: rgba(20, 20, 20, 0.5);
  border: 1px solid #282828;
  border-radius: 8px;
}

.shared-fields-section h3 {
  font-size: 1rem;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.87);
  margin-bottom: 0.5rem;
}

.fields-hint {
  font-size: 0.875rem;
  color: rgba(255, 255, 255, 0.5);
  margin-bottom: 1rem;
}

.fields-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 0.75rem;
}

.field-chip {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background: linear-gradient(135deg, rgba(30, 144, 255, 0.15), rgba(30, 144, 255, 0.25));
  border: 1px solid rgba(30, 144, 255, 0.4);
  border-radius: 8px;
  color: #1E90FF;
  font-size: 0.875rem;
  font-weight: 600;
  cursor: grab;
  transition: all 0.2s;
  user-select: none;
}

.field-chip:hover {
  background: linear-gradient(135deg, rgba(30, 144, 255, 0.25), rgba(30, 144, 255, 0.35));
  border-color: #1E90FF;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(30, 144, 255, 0.3);
}

.field-chip:active {
  cursor: grabbing;
}

.action-section {
  display: flex;
  justify-content: flex-end;
}

.send-btn {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem 2rem;
  background: linear-gradient(135deg, #1E90FF, #1873CC);
  border: none;
  border-radius: 8px;
  color: white;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(30, 144, 255, 0.3);
}

.send-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, #1873CC, #1E90FF);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(30, 144, 255, 0.4);
}

.send-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
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

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.75);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  backdrop-filter: blur(4px);
}

.modal-content {
  background-color: rgba(20, 20, 20, 0.95);
  border: 1px solid #282828;
  border-radius: 16px;
  padding: 2rem;
  max-width: 500px;
  width: 90%;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5);
}

.error-modal {
  border: 1px solid rgba(239, 68, 68, 0.3);
}

.error-icon {
  display: flex;
  justify-content: center;
  margin-bottom: 1rem;
}

.modal-content h2 {
  font-size: 1.5rem;
  font-weight: 700;
  color: rgba(255, 255, 255, 0.87);
  margin-bottom: 1rem;
  text-align: center;
}

.modal-content > p {
  font-size: 0.95rem;
  color: rgba(255, 255, 255, 0.7);
  margin-bottom: 1.5rem;
}

.invalid-fields-list {
  max-height: 200px;
  overflow-y: auto;
  padding: 1rem;
  background-color: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.3);
  border-radius: 8px;
  margin-bottom: 1.5rem;
}

.invalid-field-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem;
  color: #EF4444;
  font-size: 0.9rem;
  font-weight: 600;
  font-family: 'Courier New', monospace;
}

.error-hint {
  font-size: 0.875rem;
  color: rgba(255, 255, 255, 0.6);
  margin-bottom: 1.5rem;
  text-align: center;
}

.confirmation-recipients {
  max-height: 200px;
  overflow-y: auto;
  padding: 1rem;
  background-color: rgba(40, 40, 40, 0.5);
  border: 1px solid #282828;
  border-radius: 8px;
  margin-bottom: 1.5rem;
}

.confirmation-email {
  padding: 0.5rem;
  color: #1E90FF;
  font-size: 0.9rem;
  font-weight: 500;
}

.modal-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
}

.cancel-btn,
.confirm-btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 8px;
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.cancel-btn {
  background-color: rgba(255, 255, 255, 0.05);
  border: 1px solid #282828;
  color: rgba(255, 255, 255, 0.7);
}

.cancel-btn:hover {
  background-color: rgba(255, 255, 255, 0.1);
  border-color: #444;
}

.confirm-btn {
  background: linear-gradient(135deg, #1E90FF, #1873CC);
  color: white;
  box-shadow: 0 4px 12px rgba(30, 144, 255, 0.3);
}

.confirm-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, #1873CC, #1E90FF);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(30, 144, 255, 0.4);
}

.confirm-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.error-btn {
  background: linear-gradient(135deg, #EF4444, #DC2626);
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.3);
}

.error-btn:hover {
  background: linear-gradient(135deg, #DC2626, #EF4444);
  box-shadow: 0 6px 16px rgba(239, 68, 68, 0.4);
}

@media (max-width: 1200px) {
  .content-wrapper {
    grid-template-columns: 300px 1fr;
  }
}

@media (max-width: 768px) {
  .content-wrapper {
    grid-template-columns: 1fr;
  }

  .left-panel {
    max-height: 400px;
  }

  .fields-grid {
    justify-content: center;
  }
}
</style>