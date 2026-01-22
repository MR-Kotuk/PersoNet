<script setup lang="ts">
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

defineProps<{
  selectedPersons: Person[];
  isSending: boolean;
  findPersonEmail: (person: Person) => string | null;
}>();

const emit = defineEmits<{
  'close': [];
  'confirm': [];
}>();
</script>

<template>
  <div class="modal-overlay" @click="emit('close')">
    <div class="modal-content" @click.stop>
      <h2>Confirm Send</h2>
      <p>Are you sure you want to send this message to:</p>
      <div class="confirmation-recipients">
        <div v-for="person in selectedPersons" :key="person.personId" class="confirmation-email">
          {{ findPersonEmail(person) }}
        </div>
      </div>
      <div class="modal-actions">
        <button @click="emit('close')" class="cancel-btn">Cancel</button>
        <button @click="emit('confirm')" class="confirm-btn" :disabled="isSending">
          {{ isSending ? 'Sending...' : 'Yes, Send' }}
        </button>
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

  .cancel-btn,
  .confirm-btn {
    width: 100%;
  }
}
</style>