<template>
  <div class="history-container pb-4">
    <h1 class="mb-4 pt-5 fs-1">History</h1>
    <div v-if="historyItems.length > 0" id="history-container">
      <div v-for="(item, index) in historyItems" :key="index" class="history-item pb-3 mb-3">
        <div class="history-header">
          <div class="history-timestamp">Saved on: {{ formatDate(item.timestamp) }}</div>
          <div class="history-actions">
            <button class="btn btn-primary btn-sm" @click="openHistory(item)">Open</button>
            <button class="btn btn-danger btn-sm" @click="deleteHistory(item, index)">Delete</button>
          </div>
        </div>
        <div class="history-content">
          <p><strong>Model:</strong> {{ item.model }}</p>
          <p><strong>Methodology:</strong> {{ item.methodology }}</p>
          <p><strong>Overview:</strong> {{ item.overview }}</p>
          <p><strong>Core Functionalities:</strong> {{ item.coreFunctionalities }}</p>
          <p><strong>Non-Functional Requirements:</strong> {{ item.nonFunctionalRequirements }}</p>
          <p><strong>User Story:</strong> {{ item.userStory }}</p>
          <p><strong>Business Requirements:</strong> {{ item.businessRequirements }}</p>
          <p><strong>Project Name:</strong> {{ item.projectName }}</p>
          <p><strong>Interview Notes:</strong> {{ item.interviewNotes }}</p>
          <strong class="ms-2">Stakeholders:</strong>
          <ul>
            <li v-for="(stakeholder, sIndex) in item.stakeholders" :key="sIndex">
              <strong>Name:</strong> {{ stakeholder.name }}<br />
              <strong>Description:</strong> {{ stakeholder.description }}<br />
              <strong>Survey:</strong> {{ stakeholder.survey }}
            </li>
          </ul>
        </div>
      </div>
    </div>
    <div v-else class="no-history">
      No history found!
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      historyItems: [],
    };
  },
  created() {
    this.fetchHistoryItems();
  },
  methods: {
    async fetchHistoryItems() {
      try {
        const response = await fetch('/api/surveys/getall');
        // print the url and port number
        console.log('Fetching history items from:', response.url);
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        console.log('Fetched history items:', data); // Log the fetched data
        this.historyItems = data;
      } catch (error) {
        console.error('Fetch error:', error);
      }
    },
    formatDate(timestamp) {
      return new Date(timestamp).toLocaleString();
    },
    openHistory(item) {
      localStorage.setItem('historyItem', JSON.stringify(item));
      this.$router.push({
        name: 'DetailsView',
        params: { approach: item.methodology },
      });

    },
    async deleteHistory(item, index) {
      console.log(item.id);
      if (confirm('Are you sure you want to delete this history item?')) {
        try {
          await fetch(`/api/surveys/delete?id=${item.id}`, { method: 'DELETE' });
          this.historyItems.splice(index, 1);
          alert('History item deleted successfully.');
        } catch (error) {
          console.error('Error deleting history item:', error);
          alert('Failed to delete the history item.');
        }
      }
    },
  }
};
</script>

<style scoped>
body {
  background-color: #121619;
  color: #ffffff;
  padding: 20px;
  font-family: Arial, sans-serif;
}

.history-container {
  max-width: 1200px;
  margin: 0 auto;
}

.history-item {
  background-color: #1e2225;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.history-timestamp {
  font-size: 1.1em;
  font-weight: bold;
  color: #00bcd4;
}

.history-actions button {
  margin-left: 10px;
}

.no-history {
  text-align: center;
  margin-top: 100px;
  font-size: 1.5em;
  color: #757575;
}

pre {
  background-color: #2c2f33;
  padding: 15px;
  border-radius: 5px;
  overflow-x: auto;
  color: #e0e0e0;
}

h1 {
  color: white;
  font-weight: 400;
}

.history-content {
  color: white;
}

p,
li {
  margin: 10px 10px;
}

strong {
  color: yellow;
}
</style>
