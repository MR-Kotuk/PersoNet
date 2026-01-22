<script setup lang="ts">
interface MessageTemplate {
  id: number;
  email: string;
  name: string;
  message: string;
}

defineProps<{
  templates: MessageTemplate[];
  isLoading: boolean;
}>();

const emit = defineEmits<{
  'close': [];
  'load': [template: MessageTemplate];
  'delete': [templateId: number];
}>();

function formatTemplateDisplay(text: string): string {

  const fieldPattern = /\[<\/(.*?)\/>\]/g;
  return text.replace(fieldPattern, (_match, fieldName) => fieldName);
}

function getTemplateSubjectPreview(subject: string): string {

  const cleaned = formatTemplateDisplay(subject);
  return cleaned.substring(0, 40) + (cleaned.length > 40 ? '...' : '');
}
</script>

<template>
  <div class="templates-overlay" @click="emit('close')">
    <div class="templates-modal" @click.stop>
      <div class="templates-modal-header">
        <div class="templates-modal-title">
          <svg width="24" height="24" viewBox="0 0 20 20" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
            <path d="M5 2a2 2 0 0 0-2 2v14l7-5 7 5V4a2 2 0 0 0-2-2H5z"/>
          </svg>
          <h3>Saved Templates</h3>
          <span class="templates-count">{{ templates.length }}</span>
        </div>
        <button @click="emit('close')" class="close-modal-btn">
          <svg width="24" height="24" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M15 5L5 15M5 5l10 10" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                  stroke-linejoin="round"/>
          </svg>
        </button>
      </div>

      <div class="templates-modal-content">
        <div v-if="isLoading" class="loading-small">
          <span class="spinner-small"></span>
        </div>

        <div v-else-if="templates.length === 0" class="empty-templates">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M5 4a2 2 0 0 0-2 2v14l7-5 7 5V6a2 2 0 0 0-2-2H5z" stroke="currentColor" stroke-width="2"
                  stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          <p>No saved templates</p>
          <span>Save a message to see it here</span>
        </div>

        <div v-else class="templates-list">
          <div v-for="template in templates" :key="template.id" class="template-item">
            <div class="template-item-content">
              <div class="template-info" @click="emit('load', template)">
                <svg width="16" height="16" viewBox="0 0 20 20" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                  <path d="M5 2a2 2 0 0 0-2 2v14l7-5 7 5V4a2 2 0 0 0-2-2H5z"/>
                </svg>
                <span class="template-name" :title="formatTemplateDisplay(template.name)">
                  {{ getTemplateSubjectPreview(template.name) }}
                </span>
              </div>
              <div class="template-actions-inline">
                <button @click="emit('load', template)" class="template-icon-btn use-icon-btn" title="Use template">
                  <svg width="18" height="18" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M9 5H7a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2v-2M13 3h6m0 0v6m0-6L9 13"
                          stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </button>
                <button @click="emit('delete', template.id)" class="template-icon-btn delete-icon-btn"
                        title="Delete template">
                  <svg width="18" height="18" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M3 6h14M8 6V4a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v2m3 0v10a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6h10z"
                          stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.templates-overlay {
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
  padding: 1rem;
}

.templates-modal {
  background-color: rgba(20, 20, 20, 0.95);
  border: 1px solid #282828;
  border-radius: 16px;
  max-width: 600px;
  width: 100%;
  max-height: 80vh;
  display: flex;
  flex-direction: column;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5);
  overflow: hidden;
}

.templates-modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid #282828;
  flex-shrink: 0;
}

.templates-modal-title {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  color: #1E90FF;
}

.templates-modal-title h3 {
  font-size: 1.25rem;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.87);
}

.templates-count {
  padding: 0.25rem 0.5rem;
  background-color: rgba(30, 144, 255, 0.2);
  border: 1px solid rgba(30, 144, 255, 0.3);
  border-radius: 12px;
  color: #1E90FF;
  font-size: 0.75rem;
  font-weight: 600;
}

.close-modal-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background-color: rgba(40, 40, 40, 0.5);
  border: 1px solid #282828;
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.6);
  cursor: pointer;
  transition: all 0.3s;
}

.close-modal-btn:hover {
  border-color: #1E90FF;
  background-color: rgba(30, 144, 255, 0.1);
  color: #1E90FF;
}

.templates-modal-content {
  flex: 1;
  overflow-y: auto;
  min-height: 200px;
}

.loading-small {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 3rem;
}

.spinner-small {
  width: 32px;
  height: 32px;
  border: 3px solid rgba(30, 144, 255, 0.2);
  border-top-color: #1E90FF;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.empty-templates {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem 1.5rem;
  color: #666;
  text-align: center;
  gap: 0.75rem;
}

.empty-templates p {
  font-size: 1rem;
  font-weight: 500;
  color: rgba(255, 255, 255, 0.6);
}

.empty-templates span {
  font-size: 0.875rem;
  color: #666;
}

.templates-list {
  display: flex;
  flex-direction: column;
}

.template-item {
  border-bottom: 1px solid rgba(40, 40, 40, 0.5);
  transition: all 0.2s;
}

.template-item:last-child {
  border-bottom: none;
}

.template-item:hover {
  background-color: rgba(30, 144, 255, 0.05);
}

.template-item-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 1.5rem;
  gap: 1rem;
}

.template-info {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  flex: 1;
  min-width: 0;
  cursor: pointer;
  color: rgba(255, 255, 255, 0.87);
}

.template-info svg {
  flex-shrink: 0;
  color: #1E90FF;
}

.template-name {
  font-size: 0.95rem;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.template-actions-inline {
  display: flex;
  gap: 0.5rem;
  flex-shrink: 0;
}

.template-icon-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border: 1px solid #282828;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s;
  background-color: rgba(40, 40, 40, 0.5);
}

.use-icon-btn {
  color: #1E90FF;
  border-color: rgba(30, 144, 255, 0.3);
}

.use-icon-btn:hover {
  background: linear-gradient(135deg, rgba(30, 144, 255, 0.15), rgba(30, 144, 255, 0.25));
  border-color: #1E90FF;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(30, 144, 255, 0.3);
}

.delete-icon-btn {
  color: #EF4444;
  border-color: rgba(239, 68, 68, 0.3);
}

.delete-icon-btn:hover {
  background-color: rgba(239, 68, 68, 0.15);
  border-color: #EF4444;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(239, 68, 68, 0.3);
}

@media (max-width: 768px) {
  .templates-modal {
    max-width: 100%;
    max-height: 90vh;
  }

  .templates-modal-header {
    padding: 1.25rem;
  }

  .templates-modal-title h3 {
    font-size: 1.125rem;
  }

  .close-modal-btn {
    width: 36px;
    height: 36px;
  }

  .template-item-content {
    padding: 0.875rem 1.25rem;
  }

  .template-name {
    font-size: 0.9rem;
  }

  .template-icon-btn {
    width: 32px;
    height: 32px;
  }
}

@media (max-width: 480px) {
  .templates-modal-header {
    padding: 1rem;
  }

  .templates-modal-title {
    gap: 0.5rem;
  }

  .templates-modal-title svg {
    width: 20px;
    height: 20px;
  }

  .templates-modal-title h3 {
    font-size: 1rem;
  }

  .close-modal-btn {
    width: 32px;
    height: 32px;
  }

  .close-modal-btn svg {
    width: 20px;
    height: 20px;
  }

  .template-item-content {
    padding: 0.75rem 1rem;
    gap: 0.75rem;
  }

  .template-info {
    gap: 0.5rem;
  }

  .template-info svg {
    width: 14px;
    height: 14px;
  }

  .template-name {
    font-size: 0.85rem;
  }

  .template-actions-inline {
    gap: 0.375rem;
  }

  .template-icon-btn {
    width: 30px;
    height: 30px;
  }

  .template-icon-btn svg {
    width: 16px;
    height: 16px;
  }
}
</style>