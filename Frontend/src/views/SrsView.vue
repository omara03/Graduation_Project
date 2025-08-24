<template>
    <div class="container-fluid mt-5">
        <div class="row justify-content-center">
            <div class="col-md-9">
                <div class="card text-light" style="background-color: rgb(18, 22, 25, 1); border-radius: 24px; ">
                    <div class="card-body text-center" style="background-color: #0E0F11; border-radius: 22px;">
                        <h6 @click="goToHome" style="font-size: 22px; font-weight: 700;" class="aret-button card-title">
                            PICA</h6>
                        <p class="text-secondary">Project Idea Creation Assistant</p>
                    </div>
                    <form v-if="!isAgile" @submit.prevent>
                        <div v-if="showLoadingBubbles" class="text-center mt-3">
                            <div class="spinner-grow text-primary" role="status">
                                <span class="visually-hidden">Loading...</span>
                            </div>
                            <div class="spinner-grow text-secondary" role="status">
                                <span class="visually-hidden">Loading...</span>
                            </div>
                            <div class="spinner-grow text-success" role="status">
                                <span class="visually-hidden">Loading...</span>
                            </div>
                        </div>
                        <div v-if="buttonClicked" class="d-flex justify-content-center mt-4 mb-4">
                            <span class="text-secondary">Estimated generation time: {{ timerDisplay }}</span>
                        </div>
                        <div class="d-flex justify-content-center mt-4 mb-4">
                            <button @click="downloadDocxTraditional" id="buttonDownload"
                                class="btn btn-primary btn-lg activeb me-3">Download DOCX</button>
                            <button @click="downloadTestPlan" id="buttonDownload"
                                class="btn btn-lg activeb bg-success">Download Test Plan</button>
                        </div>
                    </form>
                    <form v-if="isAgile" @submit.prevent>
                        <div>
                            <p class="text-primary pt-2 text-center">*edit your requirements*</p>
                            <div class="mt-4">
                                <h6 class="text-light d-flex justify-content-center" style="font-size: 28px;">Overview
                                </h6>
                                <textarea v-model="overview" id="responseField1" class="text-light mb-0 p-3 auto-resize"
                                    placeholder="ADD HERE THE RESPONSE" @focus="expandTextarea($event)"
                                    @blur="shrinkTextarea($event)"></textarea>
                            </div>
                            <div class="mt-4">
                                <h6 class="text-light d-flex justify-content-center" style="font-size: 28px;">Functional
                                    Requirements</h6>
                                <textarea v-model="functionalRequirements" id="responseField2"
                                    class="text-light mb-0 p-3 auto-resize" placeholder="ADD HERE THE RESPONSE"
                                    @focus="expandTextarea($event)" @blur="shrinkTextarea($event)"></textarea>

                            </div>
                            <div class="mt-4">
                                <h6 class="text-light d-flex justify-content-center" style="font-size: 28px;">
                                    Non-Functional Requirements</h6>
                                <textarea v-model="nonFunctionalRequirements" id="responseField3"
                                    class="text-light mb-0 p-3 auto-resize" placeholder="ADD HERE THE RESPONSE"
                                    @focus="expandTextarea($event)" @blur="shrinkTextarea($event)"></textarea>

                            </div>
                            <div class="mt-4">
                                <h6 class="text-light d-flex justify-content-center" style="font-size: 28px;">User Story
                                </h6>
                                <textarea v-model="userStory" id="responseField4"
                                    class="text-light mb-0 p-3 auto-resize" placeholder="ADD HERE THE RESPONSE"
                                    @focus="expandTextarea($event)" @blur="shrinkTextarea($event)"></textarea>

                            </div>
                            <div class="mt-4" style="background-color: rgb(18, 22, 25, 1);">
                                <h6 class="text-light d-flex justify-content-center" style="font-size: 28px;">
                                    Stakeholders</h6>

                                <div style="background-color: rgb(18, 22, 25, 1);" class="p-3 rounded mb-2">
                                    <div style="background-color: rgb(18, 22, 25, 1);"
                                        class="d-flex justify-content-between">
                                        <button class="btn btn-link btn-sm text-light"><i
                                                class="bi bi-pencil-square"></i></button>
                                    </div>
                                    <textarea v-model="stakeholders" id="responseField5"
                                        class="text-light mb-0 p-3 auto-resize" placeholder="ADD HERE THE STAKEHOLDERS"
                                        @focus="expandTextarea($event)" @blur="shrinkTextarea($event)"></textarea>

                                </div>
                            </div>

                            <div v-if="showLoadingBubbles" class="text-center mt-3">
                                <div class="spinner-grow text-primary" role="status">
                                    <span class="visually-hidden">Loading...</span>
                                </div>
                                <div class="spinner-grow text-secondary" role="status">
                                    <span class="visually-hidden">Loading...</span>
                                </div>
                                <div class="spinner-grow text-success" role="status">
                                    <span class="visually-hidden">Loading...</span>
                                </div>
                            </div>
                            <div class="d-flex justify-content-center mt-4 mb-4">
                                <button @click="downloadDocxAgile" id="buttonDownload"
                                    class="btn btn-primary btn-lg activeb me-3">Download DOCX</button>
                                <button @click="downloadExcel" id="buttonDownload"
                                    class="btn btn-lg activeb bg-success">Download Excel</button>
                            </div>
                        </div>
                    </form>

                </div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    name: 'SrsView',
    data() {
        return {
            overview: '',
            functionalRequirements: '',
            nonFunctionalRequirements: '',
            userStory: '',
            stakeholders: '',
            showLoadingBubbles: false,  // Initialize the loading bubbles flag
            timer: null,                // Timer interval reference
            estimatedTime: 180,         // 3 minutes in seconds
            timerDisplay: '03:00',      // Display string for timer
            timerNegative: false,        // Track if timer is negative
            buttonClicked: false,       // Track if the download button was clicked
        };
    },
    mounted() {
        console.log(parseInt(localStorage.getItem('stakeholdersLength'), 10));
        console.log(localStorage.getItem('model')); // worked

        const approach = this.$route.params.approach;
        if (approach === 'agile' || approach === 'Agile') {
            // Existing logic for agile
            axios.get('/api/Ai/generate/overview')
                .then(response => {
                    const data = response.data;
                    // console.log("Received overview data:", data);
                    this.overview = data;
                })
                .catch(error => {
                    console.error('Error fetching overview:', error);
                });
            axios.get('/api/Ai/generate/Functional')
                .then(response => {
                    const data = response.data;
                    this.functionalRequirements = data;
                })
                .catch(error => {
                    console.error('Error fetching Functional Requirements:', error);
                });
            axios.get('/api/Ai/generate/NonFunctional')
                .then(response => {
                    const data = response.data;
                    this.nonFunctionalRequirements = data;
                })
                .catch(error => {
                    console.error('Error fetching Non-Functional Requirements:', error);
                });
            axios.get('/api/Ai/generate/userstories')
                .then(response => {
                    const data = response.data;
                    this.userStory = data;
                })
                .catch(error => {
                    console.error('Error fetching user Stories:', error);
                });
            axios.get('/api/stakeholder/getallstakeholders')
                .then(response => {
                    const data = response.data;
                    console.log("Received stakeholders hiiiiiiiiiii: ", data);

                    // Get the last N stakeholders from the array
                    const lastStakeholders = data.slice(-parseInt(localStorage.getItem('stakeholdersLength'), 10));
                    console.log("lastStakeholders", lastStakeholders);

                    // Format the last N stakeholders as a string
                    const formattedStakeholders = lastStakeholders.map(stakeholder =>
                        `Name: ${stakeholder.name}, Description: ${stakeholder.description}`
                    ).join('\n');

                    // Update the stakeholders field with the formatted string
                    this.stakeholders = formattedStakeholders;
                })
                .catch(error => {
                    console.error('Error fetching stakeholders:', error);
                });
        }
    },
    methods: {
        downloadDocxAgile() {
            const approach = this.$route.params.approach;
            this.showLoadingBubbles = true; // Show loading indicator immediately

            console.log("clicked the download button");
            // Log the functional requirements
            console.log('Overview:', this.overview);
            console.log('Functional Requirements:', this.functionalRequirements);
            console.log('Non-Functional Requirements:', this.nonFunctionalRequirements);
            console.log('User Story:', this.userStory);

            // Execute the POST requests and wait for them to complete
            fetch('/api/srs/editoverview', {
                method: 'POST',
                headers: {
                    'Content-Type': 'text/plain'
                },
                body: this.overview
            })
                .then(() => fetch('/api/srs/editfunctional', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'text/plain'
                    },
                    body: this.functionalRequirements
                }))
                .then(() => fetch('/api/srs/editnonfunctional', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'text/plain'
                    },
                    body: this.nonFunctionalRequirements
                }))
                .then(() => fetch('/api/srs/edituserstory', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'text/plain'
                    },
                    body: this.userStory
                }))
                .then(() => fetch('/api/srs/generateAgileDiagrams', { method: 'GET' }))
                .then(() => {
                    return fetch('/api/srs/generatesrs');
                })
                .then(response => {
                    if (!response.ok) throw new Error('Failed to generate SRS document');
                    return response.blob();
                })
                .then(blob => {
                    // Ensure the MIME type is correct
                    const url = window.URL.createObjectURL(blob);
                    // Create and click the download link
                    const link = document.createElement('a');
                    link.href = url;
                    link.setAttribute('download', 'Agile_SRS.docx');
                    document.body.appendChild(link);
                    link.click();
                    // Cleanup
                    document.body.removeChild(link);
                    window.URL.revokeObjectURL(url);
                    this.showLoadingBubbles = false; // Hide loading indicator after download
                })
                .catch(error => {
                    // Handle any errors from the POST or GET requests
                    this.showLoadingBubbles = false; // Hide loading indicator on error
                    console.error('Error during the Word process:', error);
                });


        },
        downloadDocxTraditional() {
            this.buttonClicked = true; // Set buttonClicked to true when the download button is clicked
            const approach = this.$route.params.approach;
            this.showLoadingBubbles = true; // Show loading indicator immediately
            console.log("clicked the SRS download button");
            // Traditional logic
            this.estimatedTime = 180;
            this.timerNegative = false;
            this.timerDisplay = '03:00';
            this.showLoadingBubbles = true;
            if (this.timer) clearInterval(this.timer);
            this.timer = setInterval(() => {
                this.estimatedTime--;
                if (this.estimatedTime >= 0) {
                    const min = String(Math.floor(this.estimatedTime / 60)).padStart(2, '0');
                    const sec = String(this.estimatedTime % 60).padStart(2, '0');
                    this.timerDisplay = `${min}:${sec}`;
                } else {
                    this.timerNegative = true;
                    const over = Math.abs(this.estimatedTime);
                    const min = String(Math.floor(over / 60)).padStart(2, '0');
                    const sec = String(over % 60).padStart(2, '0');
                    this.timerDisplay = `-${min}:${sec}`;
                }
            }, 1000);
            console.log("Generating SRS document for traditional approach...");
            fetch('/api/Ai/generate/Functional')
                .then(response => {
                    if (!response.ok) throw new Error('Error fetching Functional Requirements');
                    return response.text();
                })
                .then(data => {
                    this.functionalRequirements = data;
                    console.log("Returned Functional Requirements:", this.functionalRequirements);
                    return fetch('/api/srs-documents/generate-all');
                })
                .then(() => {
                    console.log("Downloading SRS document for traditional approach.");
                    return fetch('/api/srs-documents/download');
                })
                .then(response => {
                    if (!response.ok) throw new Error('Failed to download SRS document');
                    return response.blob();
                })
                .then(blob => {
                    // Download the file
                    const url = window.URL.createObjectURL(blob);
                    const link = document.createElement('a');
                    link.href = url;
                    link.setAttribute('download', 'Traditional_SRS.docx');
                    document.body.appendChild(link);
                    link.click();
                    document.body.removeChild(link);
                    window.URL.revokeObjectURL(url);
                    this.showLoadingBubbles = false; // Hide loading indicator after download
                    clearInterval(this.timer); // Stop timer
                })
                .catch(error => {
                    this.showLoadingBubbles = false; // Hide loading indicator on error
                    clearInterval(this.timer); // Stop timer
                    console.error('Error in traditional SRS document flow:', error);
                });

        },
        downloadTestPlan() {
            this.buttonClicked = true; // Show timer
            this.showLoadingBubbles = true; // Show loading indicator
            console.log("clicked the Test Plan download button");
            // Timer logic
            this.estimatedTime = 180;
            this.timerNegative = false;
            this.timerDisplay = '03:00';
            if (this.timer) clearInterval(this.timer);
            this.timer = setInterval(() => {
                this.estimatedTime--;
                if (this.estimatedTime >= 0) {
                    const min = String(Math.floor(this.estimatedTime / 60)).padStart(2, '0');
                    const sec = String(this.estimatedTime % 60).padStart(2, '0');
                    this.timerDisplay = `${min}:${sec}`;
                } else {
                    this.timerNegative = true;
                    const over = Math.abs(this.estimatedTime);
                    const min = String(Math.floor(over / 60)).padStart(2, '0');
                    const sec = String(over % 60).padStart(2, '0');
                    this.timerDisplay = `-${min}:${sec}`;
                }
            }, 1000);
            fetch('/api/testplan/initialize', {
                method: 'POST',
                headers: {
                    'Content-Type': 'text/plain'
                },
                })
                .then(() => {
                    console.log("Generating Test Plan document for traditional approach.");
                    return fetch('/api/testdocument/generate-all');
                })
                .then(() => {
                    console.log("Downloading Test Plan document for traditional approach.");
                    return fetch('/api/testdocument/download');
                })
                .then(response => {
                    if (!response.ok) throw new Error('Failed to download Test Plan document');
                    return response.blob();
                })
                .then(blob => {
                    // Download the file
                    const url = window.URL.createObjectURL(blob);
                    const link = document.createElement('a');
                    link.href = url;
                    link.setAttribute('download', 'SOFTWARE TEST PLAN.docx');
                    document.body.appendChild(link);
                    link.click();
                    document.body.removeChild(link);
                    window.URL.revokeObjectURL(url);
                    this.showLoadingBubbles = false; // Hide loading indicator after download
                    clearInterval(this.timer); // Stop timer
                })
                .catch(error => {
                    this.showLoadingBubbles = false; // Hide loading indicator on error
                    clearInterval(this.timer); // Stop timer
                    console.error('Error in Test Plan document flow:', error);
                });

        },
        // downloadExcel() {
        //     // Log the functional requirements
        //     console.log('Overview:', this.overview);
        //     console.log('Functional Requirements:', this.functionalRequirements);
        //     console.log('Non-Functional Requirements:', this.nonFunctionalRequirements);
        //     console.log('User Story:', this.userStory);

        //     this.showLoadingBubbles = true; // Show loading indicator

        //     // Hide loading bubbles after 1.5 seconds and then proceed
        //     setTimeout(() => {
        //         this.showLoadingBubbles = false; // Hide loading indicator
        //         // Execute the POST requests and wait for them to complete
        //         Promise.all([
        //             fetch('/api/srs/editoverview', {
        //                 method: 'POST',
        //                 headers: {
        //                     'Content-Type': 'text/plain'
        //                 },
        //                 body: this.overview
        //             }),
        //             fetch('/api/srs/editfunctional', {
        //                 method: 'POST',
        //                 headers: {
        //                     'Content-Type': 'text/plain'
        //                 },
        //                 body: this.functionalRequirements
        //             }),
        //             fetch('/api/srs/editnonfunctional', {
        //                 method: 'POST',
        //                 headers: {
        //                     'Content-Type': 'text/plain'
        //                 },
        //                 body: this.nonFunctionalRequirements
        //             }),
        //             fetch('/api/srs/edituserstory', {
        //                 method: 'POST',
        //                 headers: {
        //                     'Content-Type': 'application/json'
        //                 },
        //                 body: JSON.stringify(this.userStory)
        //             })
        //         ])
        //             .then(() => {
        //                 // All POST requests have completed, now proceed to the GET request
        //                 return axios.get('Excell/getexcell', {
        //                     responseType: 'blob'
        //                 });
        //             })
        //             .then(response => {
        //                 // Ensure the MIME type is correct
        //                 const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
        //                 const url = window.URL.createObjectURL(blob);

        //                 // Create and click the download link
        //                 const link = document.createElement('a');
        //                 link.href = url;
        //                 link.setAttribute('download', 'AI_Story.xlsx');
        //                 document.body.appendChild(link);
        //                 link.click();

        //                 // Cleanup
        //                 document.body.removeChild(link);
        //                 window.URL.revokeObjectURL(url);
        //             })
        //             .catch(error => {
        //                 // Handle any errors from the POST or GET requests
        //                 console.error('Error during the Word process:', error);
        //             });
        //     }, 1500); // Delay of 1.5 seconds
        // },
        expandTextarea(event) {
            const textarea = event.target;
            // textarea.style.height = "auto"; // Reset height to auto
            textarea.style.height = textarea.scrollHeight + "px"; // Set to scroll height
        },
        shrinkTextarea(event) {
            const textarea = event.target;
            textarea.style.height = "110px"; // Set height back to default
        },
        goToHome() {
            localStorage.clear()
            this.$router.push({ name: 'HomeView' }); // Replace 'Home' with the correct route name for HomeView.vue

        },


    },
    computed: {
        isAgile() {
            return this.$route.params.approach === 'agile' || this.$route.params.approach === 'Agile';
        },
    },
    created() {
        // this.model = this.$route.params.model;
        // this.stakeholdersLength = this.$route.params.stakeholdersLength;
        // console.log("The lengthhhhhhhhhhhhhhhhhhhhhhhhhhhhhh", this.$route.params.stakeholdersLength);
        // console.log("The lengthhhhhhhhhhhhhhhhhhhhhhhhhhhhhh", this.stakeholdersLength);
        // console.log("The lengthhhhhhhhhhhhhhhhhhhhhhhhhhhhhh", this.model);
        // console.log('Approach from route params:', this.$route.params.approach); // Should log the route param 'approach'

        // Access the parameters passed via the router
        // this.stakeholdersLength = this.$route.params.stakeholdersLength;
    }
};
</script>

<style scoped>
textarea {
    height: 110px;
    resize: none;
    overflow: auto;
    padding: 5px;
    transition: height 0.8s ease, padding 0.8s ease;
    border-color: #f90000;
    background-color: #0E0F11;
    border-radius: 10px;
    width: 100%;
    margin-top: 20px;




}


.spinner-border {
    width: 3rem;
    height: 3rem;
}

body::-webkit-scrollbar-thumb {
    background-color: white;
}

body::-webkit-scrollbar {
    width: 7px;
}

body::-webkit-scrollbar-track {
    background-color: transparent;
}

.card {
    border: none;
}


.aret-button {
    display: inline-block;
    cursor: pointer;
    transition: transform 0.3s;
    /* Smooth transition */
    padding: 10px;
}

.aret-button:hover {
    transform: scale(1.1);
    /* Expands the element by 10% */
}
</style>