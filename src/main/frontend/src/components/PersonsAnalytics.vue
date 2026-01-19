<script setup lang="ts">
import {computed, onMounted, ref} from 'vue';
import axios from 'axios';

interface PersonTypeCount {
  GENERAL: number;
  FRIEND: number;
  FAMILY: number;
  CLIENT: number;
  COLLEAGUE: number;
  CUSTOM: number;
}

const personStats = ref<PersonTypeCount>({
  GENERAL: 0,
  FRIEND: 0,
  FAMILY: 0,
  CLIENT: 0,
  COLLEAGUE: 0,
  CUSTOM: 0
});

const isLoading = ref(false);

const totalPersons = computed(() => {
  return Object.values(personStats.value).reduce((sum, count) => sum + count, 0);
});

const chartData = computed(() => {

  const data = Object.entries(personStats.value).map(([type, count]) => ({
    type,
    count,
    percentage: totalPersons.value > 0 ? (count / totalPersons.value) * 100 : 0
  }));

  return data.filter(item => item.count > 0);
});

const typeColors: Record<string, string> = {
  GENERAL: '#1E90FF',
  FRIEND: '#32CD32',
  FAMILY: '#FF69B4',
  CLIENT: '#FFD700',
  COLLEAGUE: '#9370DB',
  CUSTOM: '#FF6347'
};

async function fetchPersonsAnalytics() {

  isLoading.value = true;
  const token = localStorage.getItem('access_token');

  if (!token) {
    return;
  }

  try {

    const response = await axios.get<PersonTypeCount>('/api/person/analytic', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    if (response.status === 200) {
      personStats.value = response.data;
    }

  } catch (error) {
    console.error('Failed to fetch persons analytics:', error);

  } finally {
    isLoading.value = false;
  }
}

onMounted(() => {
  fetchPersonsAnalytics();
});
</script>

<template>
  <div v-if="totalPersons" class="analytics-container">
    <div class="analytics-card">
      <h2 class="card-title">Persons Analytics</h2>

      <div v-if="isLoading" class="loading-state">
        <span class="spinner"></span>
        <p>Loading analytics...</p>
      </div>

      <div v-else class="analytics-content">
        <!-- Total Count -->
        <div class="total-section">
          <div class="total-count">
            <span class="count-number">{{ totalPersons }}</span>
            <span class="count-label">Total Persons</span>
          </div>
        </div>

        <!-- Chart Section -->
        <div class="chart-section">
          <h3 class="section-title">Distribution by Type</h3>

          <div v-if="chartData.length === 0" class="empty-state">
            <p>No persons data available</p>
          </div>

          <div v-else class="chart-container">
            <!-- Bar Chart -->
            <div class="bar-chart">
              <div
                  v-for="item in chartData"
                  :key="item.type"
                  class="bar-item"
              >
                <div class="bar-label">{{ item.type }}</div>
                <div class="bar-wrapper">
                  <div
                      class="bar-fill"
                      :style="{
                      width: item.percentage + '%',
                      backgroundColor: typeColors[item.type]
                    }"
                  >
                    <span class="bar-count">{{ item.count }}</span>
                  </div>
                </div>
                <div class="bar-percentage">{{ item.percentage.toFixed(1) }}%</div>
              </div>
            </div>

            <!-- Legend -->
            <div class="legend">
              <div
                  v-for="item in chartData"
                  :key="item.type"
                  class="legend-item"
              >
                <span
                    class="legend-color"
                    :style="{ backgroundColor: typeColors[item.type] }"
                ></span>
                <span class="legend-text">{{ item.type }}: {{ item.count }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.analytics-container {
  margin-top: -20vh;
  display: flex;
  justify-content: center;
  padding: 2rem;
  min-height: 60vh;
}

.analytics-card {
  background: rgba(10, 10, 10, 0.8);
  border: 1px solid #282828;
  border-radius: 12px;
  padding: 2rem;
  width: 100%;
  max-width: 900px;
  box-shadow: 0 8px 32px rgba(30, 144, 255, 0.1);
}

.card-title {
  color: #1E90FF;
  font-size: 2rem;
  font-weight: bold;
  text-align: center;
  margin: 0 0 2rem 0;
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem;
  gap: 1rem;
}

.loading-state p {
  color: #666;
  margin: 0;
}

.spinner {
  display: inline-block;
  width: 40px;
  height: 40px;
  border: 4px solid rgba(30, 144, 255, 0.3);
  border-top-color: #1E90FF;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.analytics-content {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.total-section {
  display: flex;
  justify-content: center;
  padding: 1rem 0;
}

.total-count {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
}

.count-number {
  color: #1E90FF;
  font-size: 4rem;
  font-weight: bold;
  line-height: 1;
}

.count-label {
  color: #999;
  font-size: 1.1rem;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.chart-section {
  background: rgba(20, 20, 20, 0.5);
  border: 1px solid #282828;
  border-radius: 8px;
  padding: 1.5rem;
}

.section-title {
  color: #fff;
  font-size: 1.3rem;
  margin: 0 0 1.5rem 0;
  font-weight: 600;
}

.empty-state {
  text-align: center;
  padding: 2rem;
  color: #666;
}

.empty-state p {
  margin: 0;
}

.chart-container {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.bar-chart {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.bar-item {
  display: grid;
  grid-template-columns: 100px 1fr 60px;
  align-items: center;
  gap: 1rem;
}

.bar-label {
  color: #fff;
  font-size: 0.9rem;
  font-weight: 500;
}

.bar-wrapper {
  background: rgba(40, 40, 40, 0.5);
  border-radius: 8px;
  height: 32px;
  overflow: hidden;
}

.bar-fill {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding-right: 8px;
  transition: width 0.5s ease;
  min-width: 40px;
}

.bar-count {
  color: white;
  font-size: 0.85rem;
  font-weight: bold;
}

.bar-percentage {
  color: #999;
  font-size: 0.9rem;
  text-align: right;
}

.legend {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  justify-content: center;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.legend-color {
  width: 16px;
  height: 16px;
  border-radius: 4px;
}

.legend-text {
  color: #ccc;
  font-size: 0.9rem;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
  gap: 1rem;
}

.stat-card {
  background: rgba(20, 20, 20, 0.5);
  border: 1px solid #282828;
  border-radius: 8px;
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.75rem;
  transition: transform 0.2s, border-color 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
  border-color: #1E90FF;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  font-weight: bold;
}

.stat-label {
  color: #999;
  font-size: 0.85rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

@media (max-width: 768px) {
  .analytics-container {
    padding: 1rem;
  }

  .analytics-card {
    padding: 1.5rem;
  }

  .count-number {
    font-size: 3rem;
  }

  .bar-item {
    grid-template-columns: 80px 1fr 50px;
    gap: 0.5rem;
  }

  .stats-grid {
    grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  }
}
</style>