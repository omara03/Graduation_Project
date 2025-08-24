<template>
  <button class="floating-btn" @click="toggleSidenav">
    <i class="fas fa-bars"></i>
    <!-- Master -->
  </button>
</template>

<script>
export default {
  methods: {
    toggleSidenav() {
      console.log('Toggling sidenav');
      this.$emit('toggle-sidenav');
    },
  },
};
</script>

<style>
.floating-btn {
  position: fixed;
  bottom: 20px;
  left: 20px;
  background-color: rgba(0, 181, 157, 0.522);
  color: white;
  padding: 3px 20px;
  border-radius: 50px;
  border: none;
  cursor: pointer;
  z-index: 1000;
  /* Ensure it stays above other elements */
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
}

/* .fa-bars {
  font-size: 18px;
} */
</style>