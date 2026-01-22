<script setup lang="ts">
import {useRouter} from 'vue-router';

const router = useRouter();

function goBack() {
  router.back();
}
</script>

<template>
  <div class="about-view">
    <div class="top-bar">
      <button @click="goBack" class="icon-btn back-btn">
        <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M15 10H5M5 10L10 5M5 10L10 15" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                stroke-linejoin="round"/>
        </svg>
        <span>Back</span>
      </button>
    </div>

    <div class="content-wrapper">
      <div class="about-content">
        <h1 class="main-title">About PersoNet</h1>

        <div class="github-link">
          <a href="https://github.com/MR-Kotuk" target="_blank" rel="noopener noreferrer" class="github-btn">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path
                  d="M12 2C6.477 2 2 6.477 2 12c0 4.42 2.865 8.17 6.839 9.49.5.092.682-.217.682-.482 0-.237-.008-.866-.013-1.7-2.782.603-3.369-1.34-3.369-1.34-.454-1.156-1.11-1.463-1.11-1.463-.908-.62.069-.608.069-.608 1.003.07 1.531 1.03 1.531 1.03.892 1.529 2.341 1.087 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.11-4.555-4.943 0-1.091.39-1.984 1.029-2.683-.103-.253-.446-1.27.098-2.647 0 0 .84-.269 2.75 1.025A9.578 9.578 0 0112 6.836c.85.004 1.705.114 2.504.336 1.909-1.294 2.747-1.025 2.747-1.025.546 1.377.203 2.394.1 2.647.64.699 1.028 1.592 1.028 2.683 0 3.842-2.339 4.687-4.566 4.935.359.309.678.919.678 1.852 0 1.336-.012 2.415-.012 2.743 0 .267.18.578.688.48C19.138 20.167 22 16.418 22 12c0-5.523-4.477-10-10-10z"
                  fill="currentColor"/>
            </svg>
            <span>MR_Kotuk</span>
          </a>
        </div>

        <section class="about-section">
          <h2>Project Evolution</h2>
          <p>
            PersoNet represents my transition from game development to web development and documents a year of technical
            growth.
            What began in 2024 as a simple command-line tool called "Contacts-Saver" evolved through multiple
            iterations, each
            reflecting a new skill I acquired.
          </p>
          <p>
            The journey started with a basic Java CLI application. As I learned database management, I integrated MySQL
            using JDBC,
            then migrated to Hibernate ORM for better data handling. When it was time to learn web technologies, I
            rebuilt the entire
            system as PersoNet using Spring Boot, creating a proper REST API backend by the end of 2024.
          </p>
          <p>
            After focusing on other projects throughout 2025, I returned to PersoNet in December to tackle frontend
            development.
            Thanks to the comprehensive Swagger documentation and Docker image I had created a year earlier, I could
            seamlessly
            integrate a modern Vue.js frontend. During this process, I discovered several backend issues and was
            genuinely surprised
            by my previous code style—a year of continuous learning had significantly improved my programming practices.
          </p>
        </section>

        <section class="about-section">
          <h2>A Deliberate Decision</h2>
          <p>
            I intentionally left the backend code largely unchanged for two important reasons:
          </p>
          <p>
            <strong>Historical Value:</strong> This project serves as a tangible record of my technical evolution. By
            preserving
            the original implementation, I can demonstrate concrete improvement over time—something far more valuable
            than a polished
            portfolio piece that hides the learning process.
          </p>
          <p>
            <strong>Resource Management:</strong> Rather than spending extensive time refactoring a functional system, I
            chose to
            allocate my learning efforts toward new technologies and projects, maximizing skill acquisition.
          </p>
        </section>

        <section class="about-section">
          <h2>Frontend as a Learning Ground</h2>
          <p>
            The frontend represents my first substantial Vue.js project, and I approached it as a practical learning
            environment
            rather than a showcase of perfect code. This experimental mindset allowed me to focus on understanding
            component
            architecture, state management, and API integration without the pressure of achieving perfection.
          </p>
        </section>

        <section class="about-section">
          <h2>Technical Foundation</h2>
          <div class="tech-group">
            <h3>Backend Architecture</h3>
            <ul>
              <li>Java with Spring Boot (REST API)</li>
              <li>Spring Data JPA for data persistence</li>
              <li>MySQL database</li>
              <li>BCrypt encryption for security</li>
              <li>Swagger documentation for API clarity</li>
              <li>Docker containerization for deployment</li>
            </ul>
          </div>
          <div class="tech-group">
            <h3>Frontend Implementation</h3>
            <ul>
              <li>Vue.js (TypeScript, Composition API)</li>
              <li>Vuetify for UI components</li>
              <li>Axios for HTTP communication</li>
            </ul>
          </div>
          <div class="tech-group">
            <h3>Core Functionality</h3>
            <ul>
              <li>Flexible contact management with standard and custom templates</li>
              <li>Template-based message generation (FlexiMail)</li>
              <li>Advanced search with multiple filters (QuickSearch)</li>
              <li>Rapid template creation (SwiftText)</li>
              <li>Secure data encryption</li>
            </ul>
          </div>
        </section>

        <section class="about-section">
          <h2>Technical Insights Gained</h2>
          <p>
            Through PersoNet's evolution, I developed practical understanding of layered architecture and separation of
            concerns,
            RESTful API design and documentation, database schema design for flexibility, ORM frameworks and their
            abstractions,
            modern frontend frameworks and reactive programming, full-stack integration patterns, and containerization
            and deployment
            workflows.
          </p>
          <p>
            The most valuable lesson was discovering my own code improvements when revisiting the backend—concrete
            evidence that
            consistent practice and learning genuinely elevate technical capabilities.
          </p>
        </section>
      </div>
    </div>
  </div>
</template>

<style scoped>
.about-view {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  padding: 20px;
}

.top-bar {
  display: flex;
  align-items: center;
  margin-bottom: 2rem;
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
  display: flex;
  justify-content: center;
}

.about-content {
  max-width: 900px;
  width: 100%;
  background-color: rgba(10, 10, 10, 0.8);
  border: 1px solid #282828;
  border-radius: 12px;
  padding: 3rem;
}

.main-title {
  color: rgba(255, 255, 255, 0.95);
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 1.5rem;
  text-align: center;
}

.github-link {
  display: flex;
  justify-content: center;
  margin-bottom: 2.5rem;
}

.github-btn {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.75rem 1.5rem;
  background-color: rgba(40, 40, 40, 0.5);
  border: 1px solid #282828;
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.87);
  font-size: 1rem;
  font-weight: 600;
  text-decoration: none;
  transition: all 0.3s;
}

.github-btn:hover {
  border-color: #1E90FF;
  background-color: rgba(30, 144, 255, 0.1);
  transform: translateY(-2px);
}

.about-section {
  margin-bottom: 2.5rem;
}

.about-section:last-child {
  margin-bottom: 0;
}

.about-section h2 {
  color: rgba(255, 255, 255, 0.9);
  font-size: 1.75rem;
  font-weight: 600;
  margin-bottom: 1.25rem;
  border-bottom: 2px solid #282828;
  padding-bottom: 0.5rem;
}

.about-section h3 {
  color: rgba(255, 255, 255, 0.85);
  font-size: 1.25rem;
  font-weight: 600;
  margin-bottom: 0.75rem;
  margin-top: 1.5rem;
}

.about-section p {
  color: rgba(255, 255, 255, 0.75);
  font-size: 1rem;
  line-height: 1.8;
  margin-bottom: 1rem;
}

.about-section p:last-child {
  margin-bottom: 0;
}

.about-section strong {
  color: rgba(255, 255, 255, 0.9);
  font-weight: 600;
}

.tech-group {
  margin-bottom: 1.5rem;
}

.tech-group:last-child {
  margin-bottom: 0;
}

.tech-group h3:first-child {
  margin-top: 0;
}

.about-section ul {
  list-style: none;
  padding-left: 0;
  margin-top: 0.75rem;
}

.about-section ul li {
  color: rgba(255, 255, 255, 0.75);
  font-size: 1rem;
  line-height: 1.8;
  padding-left: 1.5rem;
  position: relative;
  margin-bottom: 0.5rem;
}

.about-section ul li:before {
  content: "•";
  color: #1E90FF;
  font-weight: bold;
  position: absolute;
  left: 0.5rem;
}

@media (max-width: 768px) {
  .about-content {
    padding: 2rem 1.5rem;
  }

  .main-title {
    font-size: 2rem;
  }

  .about-section h2 {
    font-size: 1.5rem;
  }

  .about-section h3 {
    font-size: 1.125rem;
  }

  .about-section p,
  .about-section ul li {
    font-size: 0.95rem;
  }
}
</style>