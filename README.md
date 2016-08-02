# Ticket Service Homework

Ticket Service facilates the discovery, temporary hold, and final reservation of seats within a high-demand performance venue

### Requirement
- Java 8
- Maven 3

### Assumptions
- Seats are allocated at the same level if available
- Limiting number of seats per customer is out of scope
- Seat Hold, Reservation information is not required to be persisted in any kind of data storage
- Security, Logging is out of scope

### Build project using Maven

Run below command at root folder of the project, this will generate TicketService.war file under target folder

    mvn clean install
