# Ethiopia EMR Custom Module

## Introduction
This is an OpenMRS module designed for custom backend features within the Ethiopian context. It focuses on interoperability with various external systems, including **MPI**, **eAPTS**, and other health information systems.

---

## Prerequisites for Development
Before you begin, ensure you have the following installed:

1. **Java Development Kit (JDK) 8**: Most OpenMRS 2.x versions require Java 8 for compatibility.
2. **Apache Maven**: Used for project management and builds.
3. **OpenMRS SDK**: Install the SDK by running the following command in your terminal:
   ```bash
   mvn org.openmrs.maven.plugins:openmrs-sdk-maven-plugin:setup-sdk
   ```
4. **An OpenMRS Server**: Create a local server for testing:
   ```bash
   mvn openmrs-sdk:setup -DserverId=mydevserver
   ```

## Build and Deploy

### 1. Build the Project
In the root directory of this module, run:
```bash
mvn clean install
```

### 2. Deploy to the SDK Server
You can automatically deploy your module to sync changes instantly:
```bash
mvn openmrs-sdk:deploy -DserverId=mydevserver
```

**Alternative Method:** Navigate to the OpenMRS Admin UI → **Manage Modules** → **Add or Upgrade Module** and manually upload the `.omod` file found in the `omod/target/` directory.

---

## Testing
- **Unit Tests**: Place your tests in `api/src/test/java`. Run them using:
```bash
mvn test
```

- **Manual Testing**: Start your server and monitor the logs or UI to ensure your module status is "Started":
```bash
mvn openmrs-sdk:run -DserverId=mydevserver
```