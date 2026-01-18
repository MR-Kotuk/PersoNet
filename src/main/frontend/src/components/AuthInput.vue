<script setup lang="ts">
defineProps<{
  id: string;
  label: string;
  type: string;
  placeholder: string;
  modelValue: string;
  required?: boolean;
}>();

defineEmits<{
  'update:modelValue': [value: string];
}>();
</script>

<template>
  <div class="input-group">
    <label :for="id">{{ label }}</label>
    <input
        :id="id"
        :type="type"
        :placeholder="placeholder"
        :required="required"
        :value="modelValue"
        @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
    />
  </div>
</template>

<style scoped>
.input-group {
  margin-bottom: 1.5rem;
}

.input-group label {
  display: block;
  color: white;
  font-weight: bold;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
}

.input-group input {
  width: 100%;
  padding: 0.75rem 1rem;
  background: transparent;
  border: 2px solid #282828;
  border-radius: 8px !important;
  color: white;
  font-size: 1rem;
  transition: border-color 0.3s;
  box-sizing: border-box;
}

.input-group input:focus {
  outline: none;
  border-color: #1E90FF;
}

.input-group input::placeholder {
  color: #666;
}
</style>