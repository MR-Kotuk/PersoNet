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

const props = defineProps<{
  person: Person;
  firstName: string;
  lastName: string;
  isSelected?: boolean;
}>();

const emit = defineEmits<{
  (e: 'click', person: Person): void;
  (e: 'toggle-select'): void;
}>();

function handleClick(event: MouseEvent) {

  const target = event.target as HTMLElement;

  if ((target instanceof HTMLInputElement && target.type === 'checkbox') || target.closest('.checkbox-cell')) {
    return;
  }

  emit('click', props.person);
}

function handleCheckboxChange(event: Event) {

  event.stopPropagation();
  emit('toggle-select');
}
</script>

<template>
  <div class="person-card" :class="{ selected: isSelected }" @click="handleClick">
    <div class="card-content">
      <div class="checkbox-cell" @click.stop>
        <input
            type="checkbox"
            :checked="isSelected"
            @change="handleCheckboxChange"
            class="select-checkbox"
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
      <div class="info-cell">{{ firstName }}</div>
      <div class="info-cell">{{ lastName }}</div>
      <div class="info-cell">
        <span class="badge type-badge">{{ person.personType }}</span>
      </div>
      <div class="info-cell tags-cell">
        <span v-for="tag in person.tags" :key="tag" class="tag">{{ tag }}</span>
      </div>
      <div class="info-cell">
        <span class="badge status-badge" :class="person.personStatus.toLowerCase()">
          {{ person.personStatus }}
        </span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.person-card {
  background-color: rgba(20, 20, 20, 0.8);
  border: 1px solid #282828;
  border-top: none;
  transition: all 0.2s;
  cursor: pointer;
}

.person-card.selected {
  background-color: rgba(30, 144, 255, 0.1);
  border-color: rgba(30, 144, 255, 0.5);
}

.person-card:first-of-type {
  border-top: 1px solid #282828;
}

.person-card:last-of-type {
  border-radius: 0 0 12px 12px;
}

.person-card:hover {
  background-color: rgba(30, 144, 255, 0.05);
  border-color: rgba(30, 144, 255, 0.3);
  transform: translateX(4px);
}

.person-card.selected:hover {
  background-color: rgba(30, 144, 255, 0.15);
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
  justify-content: center;
  align-items: center;
}

.select-checkbox {
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

@media (max-width: 1200px) {
  .card-content {
    grid-template-columns: 50px 60px 1fr 1fr 1fr 1.5fr 1fr 60px;
  }
}

@media (max-width: 768px) {
  .card-content {
    grid-template-columns: 1fr;
    gap: 0.75rem;
  }

  .checkbox-cell {
    justify-content: flex-start;
  }

  .avatar-cell {
    justify-content: flex-start;
  }

  .info-cell {
    white-space: normal;
  }
}
</style>