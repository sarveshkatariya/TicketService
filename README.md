# Ticket Service Homework

Ticket Service facilates the discovery, temporary hold, and final reservation of seats within a high-demand performance venue

### Requirement
- Java 8
- Maven 3

### Assumptions
- Seats are allocated at the same level if available
- Limiting number of seats per customer is out of scope
- Exterrnalizing configurable properties is out of sccope. 
- Default Lifetime of SeatHold is considered to be 2 mins.
- Seat Hold, Reservation information is not required to be persisted in any kind of data storage
- Security, Logging is out of scope

### Build project using Maven

Run below command at root level of the project, this will generate TicketService.war file under target folder

    mvn clean install

### Run Maven Test

Run below command at root level of the project, this will execute all the test methods and generate a report named surefire-report.html under target/site/

Note: The below command will take around 250 secs to execute, as there are test cases which will wait for SeatHold to expiry.

    mvn surefire-report:report
