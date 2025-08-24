# 🚀 Setup Instructions

### 1️⃣ Clone the repository
git clone https://github.com/omara03/Graduation_Project.git

cd Graduation_Project/Backend

### 2️⃣ Start PostgreSQL with Docker
docker-compose up -d

### 3️⃣ Build the Backend (Spring Boot Microservices)
mvn clean install

### 4️⃣ Run a Backend Service
cd service-name

mvn spring-boot:run
