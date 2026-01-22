<script setup lang="ts">
defineProps<{
  invalidFields: string[];
}>();

const emit = defineEmits<{
  'close': [];
}>();

function extractFieldName(field: string): string {

  const match = field.match(/\[<\/(.*?)\/>\]/);
  return match?.[1] ?? field;
}
</script>

<template>
  <div class="modal-overlay" @click="emit('close')">
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
        <button @click="emit('close')" class="confirm-btn error-btn">Got it</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
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

.modal-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
}

.confirm-btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 8px;
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.error-btn {
  background: linear-gradient(135deg, #EF4444, #DC2626);
  color: white;
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.3);
}

.error-btn:hover {
  background: linear-gradient(135deg, #DC2626, #EF4444);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(239, 68, 68, 0.4);
}

@media (max-width: 480px) {
  .modal-content {
    padding: 1.5rem;
    width: 95%;
  }

  .modal-content h2 {
    font-size: 1.25rem;
  }

  .modal-actions {
    flex-direction: column;
  }

  .confirm-btn {
    width: 100%;
  }
}
</style>