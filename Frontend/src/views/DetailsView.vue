<template>
    <div>
        <SidenavComponent :class="{ 'show': showSidenav }"/>
        <FloatingButtonComponent @toggle-sidenav="toggleSidenav" />
        <div :class="{ 'main-content': true, 'margin-left': showSidenav }">
            <MainContentComponent :approach="$route.params.approach" :model="this.model"/> 
        </div>
    </div>
</template>

<script>
import SidenavComponent from '@/components/SideNavDetailsComponent.vue';
import FloatingButtonComponent from '@/components/FloatingButtonDetailsComponent.vue'; 
import MainContentComponent from '@/components/MainContentDetailsComponent.vue';

export default {
    components: {
        SidenavComponent,
        FloatingButtonComponent,
        MainContentComponent,
    },
    data() {
        return {
            showSidenav: false,
            model: ''
        };
    },
    methods: {
        toggleSidenav() {
            this.showSidenav = !this.showSidenav;
            console.log('Toggling sidenav in details page', this.showSidenav);

        },
    }
};
</script>


<style>
.loading-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.7) !important;
    /* Dark overlay */
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    z-index: 9999;
}

.spinner {
    border: 12px solid #f3f3f3 !important;
    border-top: 12px solid #3498db !important;
    border-radius: 50%;
    width: 80px;
    height: 80px;
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

.loading-overlay p {
    color: #fff !important;
    margin-top: 20px;
    font-size: 18px;
    text-align: center;
}

body {
    background-color: rgb(18, 22, 25, 1) !important;
    color: white;
}


.card-custom .card-header {
    color: #fff;
    border-bottom: none;
    display: flex;
    align-items: center;
    padding: 15px;
    /* Ensure padding inside the header */
}

.main-content {
    margin-left: 0;
    padding: 20px;
    transition: margin-left 0.8s ease;

}

.margin-left {
    margin-left: 250px;
    /* Adjust according to your sidebar width */
}



.card-custom {
    background-color: #0E0F11;
    border-radius: 24px;
    margin-bottom: 20px;
}

.text-fields {
    margin-top: 20px !important;
}

.text-field-row {
    display: flex;
    margin-bottom: 15px;
}

.text-field-col {
    flex: 1;
    margin-right: 10px;
}

.text-field-col:last-child {
    margin-right: 0;
}

.form-control {
    background-color: #1E2329 !important;
    /* Background color for text fields */
    color: white !important;
    /* Text color for text fields */
    border: 1px solid #333 !important;
    /* Border color for text fields */
    resize: vertical;
    /* Allow vertical resizing */
    min-height: 60px;
    /* Set initial height */
}

.form-control:focus {
    background-color: #1E2329;
    color: white;
    border-color: #555;
    /* Border color on focus */
    box-shadow: none;
    /* Remove default box-shadow on focus */
}

::placeholder {
    font-style: italic;
    color: grey !important;

}


body::-webkit-scrollbar-thumb,
textarea::-webkit-scrollbar-thumb {
    background-color: rgb(255, 255, 255) !important;
}

body::-webkit-scrollbar,
textarea::-webkit-scrollbar {
    width: 7px;
}

body::-webkit-scrollbar-track,
textarea::-webkit-scrollbar-track {
    background-color: black;
}
</style>
