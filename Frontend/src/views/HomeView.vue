<template>
  <div class="container-fluid">
    <div class="row no-gutters">
      <!-- Left Side -->
      <div class="left-side col-md-6 d-flex flex-column align-items-center justify-content-center">
        <div class="text-center">
          <span class="main-text1">Welcome Back to PICA</span>
          <div class="mt-2">
            <span class="main-text2">Project Idea Creation Assistant using AI</span>
          </div>

          <div class="mt-5">
            <span class="main-text3">Select your preferred approach</span>
          </div>

          <div class="button-container mt-4">
            <button class="btn btn-primary agile-button" @click="selectApproach('agile')">
              Agile requirements management
            </button>
            <button class="btn btn-secondary traditional-button" @click="selectApproach('traditional')">
              Traditional requirements management
            </button>
          </div>
        </div>
      </div>

      <!-- Right Side -->
      <div class="col-md-6 image-container">
        <img src="@/assets/aretlogo.png" alt="logo" class="img-fluid">
      </div>
    </div>
  </div> 
</template>

<script>
export default {
  name: 'ApproachSelection',
  data() {
    return {
      approach : 'agile'
    }
  },
  methods: {
    selectApproach(approach) {
      this.approach = approach
      this.$router.push({ name: 'DetailsView', params: { approach } });
  
    }
  }
};
</script>





<style>

/* Reset and default styles */
html {
  line-height: 1.15;
  background-color: var(--dl-color-theme-neutral-dark);
}

body {
  font-family: Inter, sans-serif;
  font-weight: 400;
  line-height: 1.15;
  color: var(--dl-color-theme-neutral-dark);
  background-color: var(--dl-color-theme-neutral-light);
}

* {
  box-sizing: border-box;
}

button {
  background-color: transparent;
}

a {
  color: inherit;
  text-decoration: none;
}


.frame172-container {
  width: 100%;
  display: flex;
  overflow: auto;
  min-height: 100vh;
  align-items: center;
  flex-direction: column;
}

.frame172-frame172 {
  width: 100%;
  height: 832px;
  display: flex;
  position: relative;
  align-items: flex-start;
  flex-shrink: 0;
}

.main-background {
  display: flex;
  overflow: hidden;
  /* position: absolute; */
  /* align-items: flex-start; */
  /* border-radius: 24px; */
  background-color: var(--dl-color-nobleblack-700);
}

.image-container {
  display: flex;
  justify-content: right;
  align-items: center;
  height: 100vh;
}

.image-container img {
  max-width: 100%;
  height: auto;
  display: block;
}

.copyright-text {
  gap: 558px;
  top: 956px;
  left: 48px;
  width: 624px;
  display: flex;
  position: absolute;
  align-items: center;
  flex-shrink: 0;
  justify-content: space-between;
}

.frame172-text10 {
  color: var(--dl-color-nobleblack-300);
  height: auto;
  text-align: left;
  line-height: 20px;
}

.frame172-text12 {
  color: var(--dl-color-nobleblack-300);
  height: auto;
  text-align: right;
  line-height: 20px;
}

.frame172-frame161 {
  gap: 195px;
  top: 161px;
  left: 112px;
  width: 496px;
  display: flex;
  position: absolute;
  align-items: flex-start;
  flex-direction: column;
}

.frame172-frame159 {
  gap: 24px;
  display: flex;
  align-self: stretch;
  align-items: center;
  flex-direction: column;
}

.main-text1 {
  color: rgba(255, 255, 255, 1);
  height: auto;
  font-size: 40px;
  align-self: stretch;
  font-style: Bold;
  text-align: center;
  font-family: Inter;
  font-weight: 700;
  /* line-height: normal; */
  font-stretch: normal;
  text-decoration: none;
  
}

.frame172-frame153 {
  gap: 44px;
  display: flex;
  align-self: stretch;
  align-items: center;
  flex-direction: column;
  justify-content: center;
}

.main-text2 {
  color: rgba(255, 255, 255, 1);
  height: auto;
  font-size: 24px;
  font-style: Regular;
  text-align: center;
  font-family: Inter;
  font-weight: 400;
  /* line-height: normal; */
  font-stretch: normal;
  text-decoration: none;
}

.main-text3 {
  color: rgba(255, 255, 255, 1);
  height: auto;
  font-size: 24px;
  align-self: stretch;
  font-style: Bold;
  text-align: left;
  font-family: Inter;
  font-weight: 700;
  line-height: 24px;
  font-stretch: normal;
  text-decoration: none;
}

.frame172-frame160 {
  gap: 19px;
  top: 550px;
  left: 209px;
  width: 301px;
  display: flex;
  position: absolute;
  align-items: flex-start;
  flex-shrink: 0;
  flex-direction: column;
}

.agile-button {
  gap: 12px;
  display: flex;
  padding: 8px 24px;
  align-self: stretch;
  align-items: center;
  flex-shrink: 0;
  border-radius: 12px;
  background-color: rgba(0, 74, 202, 1);
  cursor: pointer;
}

.agile-text {
  color: rgba(255, 255, 255, 1);
  height: auto;
  text-align: left;
  line-height: 24px;
}

.traditional-button {
  gap: 12px;
  display: flex;
  padding: 8px 24px;
  align-self: stretch;
  align-items: center;
  flex-shrink: 0;
  border-radius: 12px;
  justify-content: center;
  background-color: rgba(0, 74, 202, 1);
  cursor: pointer;
}

.traditional-text {
  color: rgba(255, 255, 255, 1);
  height: auto;
  text-align: left;
  line-height: 24px;
}

.button-container {
  justify-content: center;
  padding-top: 2rem;
  display: flex;
  gap: 12px;
  flex-direction: row;

}

.left-side {
  padding-top: 5rem; /* Adjust this value to lift the texts up */
}

</style>