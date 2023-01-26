Description
- 
- This is a Java Maven Spring Boot project that uses an H2 database for storage. 
  The purpose of this project is to provide a template for quickly getting started 
  with a new Spring Boot project that uses H2 as the database. The project also 
  includes Swagger UI for easy API documentation and testing, as well as the H2 Console 
  for viewing and editing the database.

Prerequisites
-
- Java 8 or later
- Maven
- Spring Boot 2.x

Getting Started
-
1. Clone the repository: git clone https://github.com/yongning0310/fusionWallet.git
2. Navigate to the project directory: cd fusionWallet
3. Build the project: mvn clean install
4. Run the project: mvn spring-boot:run 
5. Access the application at https://fusionwallet.herokuapp.com
6. Access the Swagger UI at https://fusionwallet.herokuapp.com/swagger-ui/index.html#/ for 
   API documentation and testing 
7. Access the H2 Console at https://fusionwallet.herokuapp.com/h2-console for viewing and 
   editing the database ( JDBC url: jdbc:h2:mem:testdb, username: sa, password: password )


Database Configuration
-
- The project uses an H2 database for storage. The database connection properties can be configured in the application.properties file. By default, the application will connect to an in-memory H2 database.

Additional Dependencies, This project includes the following dependencies:
- 
- Spring Data JPA
- H2 Database
- Springfox Swagger UI
- Spring Boot H2 Console 

Contribution
-
- Please feel free to open issues, fork the repository and send pull requests.