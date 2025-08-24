<template>
    <div id="main-content" class="main-content p-4">
        <div class="card card-custom mt-2">
            <div class="card-body text-center fs-5">
                <div class="aret-button" @click="goToHome"><strong>PICA</strong></div>
                <div><span class="page-title">Project Idea Creation Assistant</span></div>
            </div>
        </div>

        <!-- Text Fields -->
        <div class="text-fields pe-5 ps-5 ">
            <div class="text-field-row" v-for="(field, index) in fields" :key="index">
                <div class="text-field-col text-center">
                    <label :for="'field' + index">{{ field.label }}</label>
                    <textarea :id="'field' + index" class="form-control" :placeholder="field.placeholder"
                        v-model="field.value" @focus="expandTextarea($event)" @blur="shrinkTextarea($event)"></textarea>
                </div>
            </div>

            <!-- Stakeholders Section -->
            <div class="stakeholders-section text-center">
                <label>Stakeholders</label>
                <div v-for="(stakeholder, index) in stakeholders" :key="index" class="stakeholder-row">
                    <div class="text-field-col">
                        <input type="text" class="form-control" v-model="stakeholder.name"
                            placeholder="Stakeholder Type" />
                    </div>
                    <div class="text-field-col">
                        <textarea class="form-control stakeholder" v-model="stakeholder.description"
                            placeholder="Description (including their roles and responsibilities)"></textarea>
                    </div>
                    <button class="remove-btn" @click="removeStakeholder(index)">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
                <button class="btn btn-secondary" @click="addStakeholder">Add Stakeholder</button>
            </div>

            <!-- Generate SRS Button -->
            <div class="text-field-row-2 text-center">
                <button class="btn btn-primary" @click="generateSRS">Generate SRS</button>
                <button class="btn btn-secondary me-2 ms-2" @click="resetForm">Reset Form</button>
                <button class="btn btn-info" @click="goToHistory">View History</button>
            </div>

            <div v-if="showLoadingAnimation" class="loading-overlay">
                <div class="spinner"></div>
                <p>Generating SRS, please wait...</p>
            </div>
        </div>
    </div>
</template>


<script>
import axios from 'axios';

export default {
    props: ['approach'],
    data() {
        return {
            fields: [
                { label: "Project Name", placeholder: "Enter the name of the project here.", value: "" },
                { label: "Overview", placeholder: "Provide a brief description of the project...", value: "" },
                { label: "Core Functionalities", placeholder: "Important features that should be in the system.", value: "" },
                { label: "Interview Notes", placeholder: "Some notes from your stakeholders.", value: "" },
                { label: "Business Requirements", placeholder: "What are your business requirements for the project", value: "" },
                { label: "User Story", placeholder: "Write your user story here", value: "" },
                { label: "Performance", placeholder: "Response Time: Specify the acceptable response time for the application.\nThroughput: Define the expected throughput or number of transactions/operations per second.", value: "" },
                { label: "Scalability", placeholder: "Describe the requirements for handling increasing numbers of users or data volume, and how the system should scale.", value: "" },
                { label: "Availability", placeholder: "Uptime: Specify the desired uptime percentage for the application.\nRedundancy: Detail the failover mechanisms and redundancy requirements for continuous operation.", value: "" },
                { label: "Reliability", placeholder: "Outline the requirements for the application to function correctly and consistently under normal operating conditions.", value: "" },
                { label: "Usability", placeholder: "User Interface (UI): Describe the requirements for the user interface to be intuitive and user-friendly.\nAccessibility: Specify any accessibility guidelines or standards the application must follow.", value: "" },
                { label: "Security", placeholder: "Data Protection: Outline requirements for data confidentiality, integrity, and availability.\nAuthentication and Authorization: Detail the mechanisms for secure user authentication and authorization.\nCompliance: Specify the relevant data protection regulations (e.g., GDPR, HIPAA) that the application must comply with.", value: "" },
                { label: "Maintainability", placeholder: "Code Quality: Describe the standards for code documentation and maintainability.\nSupport: Specify the requirements for updating the application and fixing issues.", value: "" },
                { label: "Interoperability", placeholder: "Describe how the application should integrate with other systems, such as electronic health records (EHRs) or wearable devices.", value: "" },
                { label: "Compatibility", placeholder: "Platform: Specify the operating systems and device types the application should be compatible with (e.g., iOS, Android).\nBrowser Compatibility: If applicable, detail the web browsers the application should work across.", value: "" },
                { label: "Data Handling", placeholder: "Storage: Describe how the application should manage and store data.\nBackup and Recovery: Specify the mechanisms for data backup and recovery.", value: "" },
                { label: "Compliance", placeholder: "Outline any relevant healthcare standards and regulations that the application must adhere to.", value: "" },
                { label: "Localization", placeholder: "Specify the support for multiple languages and regional settings if required.", value: "" },
            ],
            stakeholders: [
                { name: "", description: "" },
            ],
            showLoadingAnimation: false,
            historyItem: JSON.parse(localStorage.getItem('historyItem')) || null,
            expandedFieldIndex: null,// Track the index of the expanded field
            stakeholdersLength: 0
        };
    },
    methods: {
        toggleExpand(index) {
            if (this.expandedFieldIndex === index) {
                this.expandedFieldIndex = null; // Collapse if the same field is clicked again
            } else {
                this.expandedFieldIndex = index; // Expand the clicked field
            }
        },
        addStakeholder() {
            this.stakeholders.push({ name: "", description: "" });
        },
        removeStakeholder(index) {
            this.stakeholders.splice(index, 1);
        },
        async generateSRS() {
            
            localStorage.removeItem('historyItem');

            const fieldsData = this.fields.reduce((acc, field) => {
                acc[field.label] = field.value;
                return acc;
            }, {});

            const nonfunctionalFields = [
                "Performance", "Scalability", "Availability", "Reliability",
                "Usability", "Security", "Maintainability", "Interoperability",
                "Compatibility", "Data Handling", "Compliance", "Localization"
            ];

            const nonfunctionalData = {};
            nonfunctionalFields.forEach(field => {
                if (fieldsData[field]) {
                    nonfunctionalData[field] = fieldsData[field];
                }
            });

            const srsData = {
                nonfunctional: nonfunctionalData
            };

            const stakeholdersData = this.stakeholders.map(stakeholder => ({
                name: stakeholder.name,
                description: stakeholder.description
            }));

            const combinedData = {
                model: localStorage.getItem('model'),
                methodology: this.approach,
                overview: fieldsData["Overview"] || "",
                coreFunctionalities: fieldsData["Core Functionalities"] || "",
                nonFunctionalRequirements: JSON.stringify(srsData, null, 2),
                userStory: fieldsData["User Story"] || "",
                businessRequirements: fieldsData["Business Requirements"] || "",
                stakeholders: stakeholdersData,
                projectName: fieldsData["Project Name"] || "",
                interviewNotes: fieldsData["Interview Notes"] || ""
            };

            try {
                const response = await fetch('/api/surveys/create', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(combinedData)
                });
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                // Add this POST request after creating the survey
                await fetch('/api/srs-documents/initialize', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });

                if (response.status === 200) {
                    this.showLoadingAnimation = true;
                    const delay = Math.random() * 4000 + 1000;

                    await new Promise(resolve => setTimeout(resolve, delay));

                    this.showLoadingAnimation = false;
                    this.stakeholdersLength = this.stakeholders.length
                    console.log("mainconentent stake lengthttttttttttttttt:", this.stakeholders.length);
                    console.log("mainconentent stake lengthttttttttttttttt:", this.stakeholdersLength);

                    localStorage.setItem('model', this.model)
                    localStorage.setItem('stakeholdersLength', this.stakeholdersLength.toString())
                    
                    this.$router.push({ name: 'SrsView' });

                }
            } catch (error) {
                console.error('An error occurred:', error);
                alert('An error occurred while generating the SRS. Please try again later.');
            }
        },
        goToHistory() {
            this.$router.push({ name: 'HistoryView' });
        },
        populateFieldsFromHistory() {
            console.log('The history item has been sent:', this.historyItem);

            if (this.historyItem) {
                console.log("joe", this.historyItem.model);
                localStorage.setItem("model", this.historyItem.model);
                console.log("model set from the populate function ", this.historyItem.model);
                

                const labelMapping = {
                    "Overview": "overview",
                    "Core Functionalities": "coreFunctionalities",
                    "User Story": "userStory",
                    "Business Requirements": "businessRequirements",
                    "Interview Notes": "interviewNotes",
                    "Project Name": "projectName"
                };

                // Use the mapping in your population logic
                this.fields.forEach(field => {
                    const updatedLabel = labelMapping[field.label] || field.label; // Map to new label or use the original if not mapped
                    field.value = this.historyItem[updatedLabel] || '';
                });

                // // Populate general fields from historyItem
                // this.fields.forEach(field => {
                //     field.value = this.historyItem[field.label] || '';
                // });

                // Handle nonFunctionalRequirements field
                if (this.historyItem.nonFunctionalRequirements) {
                    try {
                        // Try to parse as JSON
                        const parsedRequirements = JSON.parse(this.historyItem.nonFunctionalRequirements);
                        const nonFunctionalReqs = parsedRequirements.nonfunctional || {};

                        // Map each non-functional requirement to the corresponding field
                        for (const key in nonFunctionalReqs) {
                            const field = this.fields.find(f => f.label === key);
                            if (field) {
                                field.value = nonFunctionalReqs[key];
                            }
                        }
                    } catch (error) {
                        // If parsing fails, treat as plain string and assign to the first non-functional field
                        const nonFunctionalField = this.fields.find(f =>
                            [
                                "Performance", "Scalability", "Availability", "Reliability",
                                "Usability", "Security", "Maintainability", "Interoperability",
                                "Compatibility", "Data Handling", "Compliance", "Localization"
                            ].includes(f.label)
                        );
                        if (nonFunctionalField) {
                            nonFunctionalField.value = this.historyItem.nonFunctionalRequirements;
                        }
                        // Optionally log the error, but don't alert the user
                        console.warn('nonFunctionalRequirements was not JSON, assigned as plain string.');
                    }
                }

                // Handle stakeholders
                const stakeholdersFromHistory = this.historyItem.stakeholders || [];

                // Ensure the stakeholders array is of the correct length
                while (this.stakeholders.length < stakeholdersFromHistory.length) {
                    this.addStakeholder(); // Add a new stakeholder field
                }

                // Populate stakeholders from historyItem
                this.stakeholders.forEach((stakeholder, index) => {
                    if (stakeholdersFromHistory[index]) {
                        stakeholder.name = stakeholdersFromHistory[index].name || '';
                        stakeholder.description = stakeholdersFromHistory[index].description || '';
                    }

                });

            }

        },
        resetForm() {
            localStorage.setItem('model', 'openai')
            localStorage.setItem('sliderValue', 50);
            localStorage.removeItem('historyItem');

            this.fields.forEach(field => {
                field.value = '';
            });

            this.stakeholders = [{ name: '', description: '' }];
            window.location.reload();

        },
        expandTextarea(event) {
            const textarea = event.target;
            // textarea.style.height = "auto"; // Reset height to auto
            textarea.style.height = textarea.scrollHeight + "px"; // Set to scroll height
        },
        shrinkTextarea(event) {
            const textarea = event.target;
            textarea.style.height = "70px"; // Set height back to default
        },
        goToHome() {
            localStorage.clear()
            this.$router.push({ name: 'HomeView' }); // Replace 'Home' with the correct route name for HomeView.vue
            
        },

    },
    mounted() {

        if (this.historyItem) {
            console.log(this.historyItem);

            this.populateFieldsFromHistory();
        }


    }

};
</script>

<style scoped>
.text-field-col textarea {
    height: 70px;
    resize: none;
    overflow: auto;
    padding: 5px;
    transition: height 0.8s ease, padding 0.8s ease;
}

.text-field-col .stakeholder {
    height: 60px;
}

.text-field-row {
    display: flex;
    flex-direction: column;
    margin-bottom: 10px;
}

.text-field-row-2 {
    display: block;
    flex-direction: column;
    margin-bottom: 30px;
}

.stakeholders-section {
    margin-bottom: 20px;
}

.stakeholder-row {
    display: flex;
    align-items: center;
    margin-bottom: 10px;

    padding: 10px;
    border-radius: 5px;
}

.stakeholder-row .text-field-col {
    flex: 2;
    margin-right: 10px;
}

.stakeholder-row .remove-btn {
    background-color: transparent;
    border: none;
    color: red;
    cursor: pointer;
    font-size: 16px;
}

.remove-btn i {
    margin-right: 5px;
}

.loading-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    color: white;
    z-index: 1000;
}

.spinner {
    border: 4px solid rgba(255, 255, 255, 0.1);
    border-radius: 50%;
    border-top: 4px solid white;
    width: 40px;
    height: 40px;
    animation: spin 1s linear infinite;
}

@keyframes spin {
    0% {
        transform: rotate(0deg);
    }

    100% {
        transform: rotate(360deg);
    }
}

.card-custom,
.card-body {
    border-radius: 24px;

}

.page-title {

    color: grey;
    font-size: smaller;
}

.card {
    background-color: rgb(47, 47, 47);
}

label {
    color: yellow;
    font-size: x-large;
    text-align: center;
    align-content: center;
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
