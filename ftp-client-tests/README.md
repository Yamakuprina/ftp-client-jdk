# ftp-client-tests

### About

This is the same FTP-client project, but with additional tests built with TestNG and Mockito frameworks. 
If you need an instruction of the FTP-client usage, visit ["ftp-client"](https://github.com/Yamakuprina/ftp-client-jdk/tree/main/ftp-client) folder.

### Usage 
1) Run tests by "mvn test" and build app with "mvn clean install".
2) Run the jar file by "java -jar ftp-client-tests-1.0-SNAPSHOT.jar" while being in target folder.

### Tests

Tests for the FTP-client cover main console app, json parser, id generator and input validator. Tests try different input for these classes and check if their 
behaviour is predictable on the incorrect input.