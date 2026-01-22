<script setup lang="ts">
import {ref, watch} from 'vue';

const props = defineProps<{
  subject: string;
  messageContent: string;
  sharedFields: string[];
  canSend: boolean;
  isSending: boolean;
  isCurrentMessageSaved: boolean;
  isSavingTemplate: boolean;
}>();

const emit = defineEmits<{
  'update:subject': [value: string];
  'update:messageContent': [value: string];
  'save-template': [];
  'clear-message': [];
  'send': [];
}>();

const editorRef = ref<HTMLDivElement | null>(null);
const subjectEditorRef = ref<HTMLDivElement | null>(null);

watch(() => props.subject, (newVal) => {
  if (subjectEditorRef.value && newVal !== getEditorHtml(subjectEditorRef.value, true)) {
    subjectEditorRef.value.innerHTML = convertFieldsToHTML(newVal);
  }
});

watch(() => props.messageContent, (newVal) => {
  if (editorRef.value && newVal !== getEditorHtml(editorRef.value)) {
    editorRef.value.innerHTML = convertFieldsToHTML(newVal);
  }
});

function convertFieldsToHTML(text: string): string {
  const fieldPattern = /\[<\/(.*?)\/>\]/g;
  return text.replace(fieldPattern, (match, fieldName) => {
    return `<span class="field-tag" contenteditable="false" data-field="${match}">${fieldName}</span>`;
  });
}

function extractFieldName(field: string): string {
  const match = field.match(/\[<\/(.*?)\/>\]/);
  return match?.[1] ?? field;
}

function insertFieldIntoEditor(field: string, editor: HTMLDivElement, updateFn: () => void) {

  const displayName = extractFieldName(field);
  const selection = window.getSelection();
  const range = selection?.getRangeAt(0);

  if (range && editor.contains(range.commonAncestorContainer)) {

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

    editor.appendChild(fieldSpan);
    editor.appendChild(document.createTextNode('\u00A0'));
  }

  updateFn();
  editor.focus();
}

function insertFieldIntoSubject(field: string) {

  if (!subjectEditorRef.value) {
    return;
  }

  insertFieldIntoEditor(field, subjectEditorRef.value, updateSubjectContent);
}

function insertField(field: string) {

  if (!editorRef.value) {
    return;
  }

  insertFieldIntoEditor(field, editorRef.value, updateMessageContent);
}

function handleDragStart(event: DragEvent, field: string) {

  event.dataTransfer!.effectAllowed = 'copy';
  event.dataTransfer!.setData('application/x-field', field);
}

function handleDrop(event: DragEvent, isSubject: boolean = false) {

  event.preventDefault();
  const field = event.dataTransfer!.getData('application/x-field');

  if (field && props.sharedFields.includes(field)) {
    isSubject ? insertFieldIntoSubject(field) : insertField(field);
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

function handleInput(isSubject: boolean = false) {

  isSubject ? updateSubjectContent() : updateMessageContent();
  validateFieldTags(isSubject ? subjectEditorRef.value : editorRef.value);
}

function validateFieldTags(editor: HTMLElement | null) {

  if (!editor) {
    return;
  }

  const walker = document.createTreeWalker(
      editor,
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
  emit('update:messageContent', getEditorHtml(editorRef.value));
}

function updateSubjectContent() {
  emit('update:subject', getEditorHtml(subjectEditorRef.value, true));
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
</script>

<template>
  <div class="middle-panel">
    <div class="compose-section">
      <div class="subject-section">
        <div class="subject-header">
          <label for="subject">Subject</label>
          <div class="subject-actions">
            <button @click="emit('save-template')" class="bookmark-btn" :class="{ saved: isCurrentMessageSaved }"
                    :disabled="!subject.trim() || !messageContent.trim() || isSavingTemplate" title="Save as template">
              <svg v-if="isCurrentMessageSaved" width="20" height="20" viewBox="0 0 20 20" fill="currentColor"
                   xmlns="http://www.w3.org/2000/svg">
                <path d="M5 2a2 2 0 0 0-2 2v14l7-5 7 5V4a2 2 0 0 0-2-2H5z"/>
              </svg>
              <svg v-else width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M5 2a2 2 0 0 0-2 2v14l7-5 7 5V4a2 2 0 0 0-2-2H5z" stroke="currentColor" stroke-width="2"
                      stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </button>
            <button @click="emit('clear-message')" class="clear-btn"
                    :disabled="!subject.trim() && !messageContent.trim()" title="Clear message">
              <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M15 5L5 15M5 5l10 10" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                      stroke-linejoin="round"/>
              </svg>
            </button>
          </div>
        </div>
        <div ref="subjectEditorRef" class="subject-editor" contenteditable="true" @input="handleInput(true)"
             @drop="handleDrop($event, true)" @dragover.prevent placeholder="Enter subject..."></div>
      </div>

      <div class="editor-section">
        <div class="editor-toolbar">
          <button @click="execCommand('bold')" class="toolbar-btn" title="Bold"><strong>B</strong></button>
          <button @click="execCommand('italic')" class="toolbar-btn" title="Italic"><em>I</em></button>
          <button @click="execCommand('underline')" class="toolbar-btn" title="Underline"><u>U</u></button>
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

        <div ref="editorRef" class="message-editor" contenteditable="true" @input="handleInput()" @paste="handlePaste"
             @drop="handleDrop($event)" @dragover.prevent placeholder="Type your message here..."></div>
      </div>

      <div v-if="sharedFields.length > 0" class="shared-fields-section">
        <h3>Shared Fields</h3>
        <p class="fields-hint">Click or drag fields into your message</p>
        <div class="fields-grid">
          <div v-for="field in sharedFields" :key="field" class="field-chip" draggable="true"
               @dragstart="handleDragStart($event, field)" @click="insertField(field)">
            <svg width="14" height="14" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M3 7h14M3 13h14" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            {{ extractFieldName(field) }}
          </div>
        </div>
      </div>

      <div class="action-section">
        <button @click="emit('send')" class="send-btn" :disabled="!canSend || isSending">
          <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M2 10l16-8-8 16-2-8-6-2z" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                  stroke-linejoin="round"/>
          </svg>
          {{ isSending ? 'Sending...' : 'Send Message' }}
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.middle-panel {
  display: flex;
  flex-direction: column;
  min-width: 0;
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

.subject-section {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.subject-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.subject-header label {
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.875rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.subject-actions {
  display: flex;
  gap: 0.5rem;
}

.bookmark-btn,
.clear-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  background-color: rgba(40, 40, 40, 0.5);
  border: 1px solid #282828;
  border-radius: 6px;
  color: rgba(255, 255, 255, 0.6);
  cursor: pointer;
  transition: all 0.3s;
}

.bookmark-btn:hover:not(:disabled),
.clear-btn:hover:not(:disabled) {
  border-color: #1E90FF;
  background-color: rgba(30, 144, 255, 0.1);
  color: #1E90FF;
}

.bookmark-btn.saved {
  background: linear-gradient(135deg, rgba(30, 144, 255, 0.2), rgba(30, 144, 255, 0.3));
  border-color: #1E90FF;
  color: #1E90FF;
}

.bookmark-btn:disabled,
.clear-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
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

@media (max-width: 900px) {
  .compose-section {
    padding: 1.5rem;
  }

  .message-editor {
    min-height: 250px;
    max-height: 400px;
  }

  .fields-grid {
    justify-content: flex-start;
  }

  .field-chip {
    font-size: 0.8rem;
    padding: 0.4rem 0.8rem;
  }
}

@media (max-width: 480px) {
  .compose-section {
    padding: 1rem;
    gap: 1rem;
  }

  .subject-editor {
    min-height: 42px;
    font-size: 0.9rem;
  }

  .message-editor {
    min-height: 200px;
    max-height: 300px;
    font-size: 0.9rem;
  }

  .toolbar-btn {
    width: 28px;
    height: 28px;
  }

  .send-btn {
    width: 100%;
    justify-content: center;
  }
}
</style>