<template>
    <div id="sidenav" class="sidenav ml-2 mt-2">
      <div class="card card-custom">
        <div class="card-header mb-5">
          <img :src="profileImage" alt="Profile Picture" id="profile-image" />
          <button class="ui-btn ui-btn--md ui-btn--primary ui-btn--toggler" @click="toggleDropdown">
            <span id="dropdown-text">{{ selectedModel }}</span>
            <i class="fas fa-chevron-down"></i>
          </button>
          <div v-if="showDropdown" class="ui-dropdown" id="my-dropdown">
            <a v-for="model in models" :key="model" class="ui-dropdown-item" @click.prevent="selectModel(model)">
              {{ model }}
            </a>
          </div>
        </div>
  
        <div class="card-body">
          <div class="radio-input mb-4">
            <label class="label">
              <input type="radio" id="value-1" name="value-radio" value="Agile" @change="updateApproach('Agile')" :checked="selectedApproach && selectedApproach.toLowerCase() === 'agile'" />
              <p class="text">Agile</p>
            </label>
            <label class="label">
              <input type="radio" id="value-2" name="value-radio" value="Traditional" @change="updateApproach('Traditional')" :checked="selectedApproach && selectedApproach.toLowerCase() === 'traditional'" />
              <p class="text">Traditional</p>
            </label>
          </div>
  
          <p class="text-center fs-5 text-secondary pb-1">Model Creativity</p>
          <div class="slider-container">
            <input type="range" min="0" max="100" v-model="sliderValue" class="slider" id="mySlider" />
            <span id="slider-value" class="text-secondary fs-5">{{ sliderValue }}</span>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import openaiImage from "@/assets/openaiimg.jpg";
  import ollamaImge from "@/assets/ollamaimg.jpg";
  import geminiImage from "@/assets/geminiimg.jpg";
  
  export default {
    data() {
      return {
        selectedModel: '',
        showDropdown: false,
        models: ["openai", "ollama", "Gemini"],
        imagePaths: {
          openai: openaiImage,
          ollama: ollamaImge,
          Gemini: geminiImage, 
        },
        sliderValue: 50,
        selectedApproach: '', // Track selected approach
      };
    },
    computed: {
      profileImage() {
        return this.imagePaths[this.selectedModel];
      },
    },
    methods: {
      toggleDropdown() {
        this.showDropdown = !this.showDropdown;
      },
      selectModel(model) {
        this.selectedModel = model;
        console.log(model);
        this.showDropdown = false;
        localStorage.setItem("model", model);
      },
      updateApproach(approach) {
        // Update the route parameter and localStorage
        this.selectedApproach = approach;
        localStorage.setItem("selectedApproach", approach);
        this.$router.push({ name: this.$route.name, params: { ...this.$route.params, approach } });
      },
    },
    mounted() {
      console.log("sidenav route approach", this.$route.params.approach);
  
      // Set initial values from localStorage or defaults
      if(localStorage.getItem("model")){
        this.selectedModel = localStorage.getItem("model");

      }
      else{
        this.selectedModel = 'openai';
      }
      this.selectedApproach = localStorage.getItem("selectedApproach") || this.$route.params.approach;
  
      // Set the initial value of the radio button based on stored value
      if (this.selectedApproach && this.selectedApproach !== this.$route.params.approach) {
        this.$router.replace({ name: this.$route.name, params: { ...this.$route.params, approach: this.selectedApproach } });
      }
  
      // Retrieve saved value from localStorage if it exists
      const savedValue = localStorage.getItem("sliderValue");
      if (savedValue !== null) {
        this.sliderValue = parseInt(savedValue, 10);
      }
    },
    watch: {
      sliderValue(newValue) {
        localStorage.setItem("sliderValue", newValue);
      },
    },
  };
  </script>
  


<style>
.sidenav {
    z-index: 1;
    height: 100%;
    width: 250px;
    position: fixed;
    top: 0;
    left: 0;
    background-color: transparent;
    padding: 15px;




    left: -250px;
    /* Initially hide the sidenav off-screen */
    transition: left 0.7s ease;
    /* Transition for smooth sliding effect */

}

.sidenav.show {
    transform: translateX(0);
    /* Slide in when visible */
}

.sidenav .card-custom {
    background-color: #0E0F11;
    border-radius: 24px;
    width: calc(100% - 30px);
    /* Full width minus padding */
    margin: 0 !important;
    /* Remove margins to stretch */
    height: 100% !important;
    /* Full height of the sidebar */
    position: absolute;
    /* Position absolutely to fill the sidebar */
    top: 0;
    left: 3%;
    padding: 15px;
    /* Ensure padding is applied inside the card */
}

/* Ensure the parent of the dropdown is positioned relative */
.card-header {
    position: relative;
}

/* Dropdown styling */
.ui-dropdown {
    position: absolute;
    /* Position it absolutely relative to the .card-header */
    top: 100%;
    /* Position directly below the button */
    left: 33%;
    /* Align to the left edge of the button */
    background-color: #ccc;
    /* Match your card styling */
    border: 1px solid #ccc;
    border-radius: 8px;
    width: 90px;
    /* Adjust width to fit content */
    z-index: 1000;
    /* Make sure it appears above other elements */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* Dropdown item styling */
.ui-dropdown-item {
    padding: 8px 12px;
    cursor: pointer;
    color: black;
    text-align: left;
    display: block;
    text-decoration: none;
    font-size: medium;
}

/* Item hover effect */
.ui-dropdown-item:hover {
    background-color: #333;
    text-decoration-color: blue;
    text-decoration: underline;
    color: blue;
    /* Change to your preferred hover color */
}



.card-custom .card-header img {
    border-radius: 50%;
    width: 40px;
    height: 40px;
    margin-right: 10px;
}

.card-custom .card-header .header-text {
    flex: 1;
}

.card-custom .card-header .fa-chevron-down {
    margin-left: 10px;
    cursor: pointer;
}

.card-custom .card-body {
    background-color: rgb(14, 15, 17);
    color: #ffffff;
}

/* From Uiverse.io by PriyanshuGupta28 */
.slider {
    -webkit-appearance: none;
    width: 100%;
    height: 10px;
    border-radius: 5px;
    background-color: #4158D0;
    background-image: linear-gradient(43deg, #4158D0 0%, #C850C0 46%, #FFCC70 100%);
    outline: none;
    opacity: 0.7;
    -webkit-transition: .2s;
    transition: opacity .2s;
}

.slider::-webkit-slider-thumb {
    -webkit-appearance: none;
    appearance: none;
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background-color: #4c00ff;
    background-image: linear-gradient(160deg, #4900f5 0%, #80D0C7 100%);
    cursor: pointer;
}

.slider::-moz-range-thumb {
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background-color: #0093E9;
    background-image: linear-gradient(160deg, #0093E9 0%, #80D0C7 100%);
    cursor: pointer;
}






/* Sidenav CSS */
.sidenav {}

.sidenav.show {
    left: 0;
    /* Slide in the sidenav */
}

/* Main Content CSS */
.main-content {
    transition: margin-left 0.3s ease;
    /* Transition for main content shift */
}

.margin-left {
    margin-left: 250px;
    /* Space to show the sidenav */
}

.slider-container {
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    /* Center items vertically */
    margin: 0 auto;
    /* Center the container horizontally */
}

.slider {
    -webkit-appearance: none;
    width: 100%;
    height: 10px;
    border-radius: 5px;
    background-color: #4158D0;
    background-image: linear-gradient(43deg, #4158D0 0%, #C850C0 46%, #FFCC70 100%);
    outline: none;
    opacity: 0.7;
    -webkit-transition: .2s;
    transition: opacity .2s;
}

.slider::-webkit-slider-thumb {
    -webkit-appearance: none;
    appearance: none;
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background-color: #4c00ff;
    background-image: linear-gradient(160deg, #4900f5 0%, #80D0C7 100%);
    cursor: pointer;
}

.slider::-moz-range-thumb {
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background-color: #0093E9;
    background-image: linear-gradient(160deg, #0093E9 0%, #80D0C7 100%);
    cursor: pointer;
}

#slider-value {
    margin-top: 10px;
    /* Space between slider and value */
    font-size: 1.25rem;
    /* Adjust size if needed */
}

/* Radio Input CSS */
.radio-input {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.radio-input * {
    box-sizing: border-box;
    padding: 0;
    margin: 0;
}

.radio-input label {
    z-index: 1;
    display: flex;
    align-items: center;
    gap: 15px;
    padding: 0px 20px;
    width: 160px;
    cursor: pointer;
    height: 50px;
    position: relative;
}

.radio-input label::before {
    position: absolute;
    content: "";
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 160px;
    height: 45px;
    z-index: -1;
    transition: all 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);
    border-radius: 10px;
    border: 2px solid transparent;
}

.radio-input label:hover::before {
    transition: all 0.2s ease;
    background-color: #2a2e3c;
}

.radio-input .label:has(input:checked)::before {
    background-color: #2d3750;
    border-color: #435dd8;
    height: 50px;
}

.radio-input .label .text {
    color: #fff;
}

.radio-input .label input[type="radio"] {
    background-color: #202030;
    appearance: none;
    width: 17px;
    height: 17px;
    border-radius: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
}

.radio-input .label input[type="radio"]:checked {
    background-color: #435dd8;
    -webkit-animation: puls 0.7s forwards;
    animation: pulse 0.7s forwards;
}

.radio-input .label input[type="radio"]:before {
    content: "";
    width: 6px;
    height: 6px;
    border-radius: 50%;
    transition: all 0.1s cubic-bezier(0.165, 0.84, 0.44, 1);
    background-color: #fff;
    transform: scale(0);
}

.radio-input .label input[type="radio"]:checked::before {
    transform: scale(1);
}

@keyframes pulse {
    0% {
        box-shadow: 0 0 0 0 rgba(255, 255, 255, 0.4);
    }

    70% {
        box-shadow: 0 0 0 8px rgba(255, 255, 255, 0);
    }

    100% {
        box-shadow: 0 0 0 0 rgba(255, 255, 255, 0);
    }
}
</style>
