# ftp-client

### About

This is a simple FTP protocol client build completely on Sockets without any non-JDK libraries, except "maven-jar-plugin". It could be removed too, but in this case every build will require manual setting.
It is used to send and receive file with the information about students.

### Usage
0) First of all, you need to modify the config.properties file to your needs. It stores 4 variables this app will use:
   1) Path to the students-file on the server. This path is virtual, slashes are important.
   2) Path where to store students-file on your device. This path is absolute.
   3) Port of the server that client will try to connect to. This port will be used for interactions by the FTP commands.
   4) Port of the server that client will try to connect in Active FTP-mode. This port will be used for data transfer in Active mode.
   
1) Build the app by "mvn clean install".
2) Run the jar file by "java -jar ftp-client-1.0-SNAPSHOT.jar" while being in target folder.
3) Type in IP-address of your FTP-server, login and password.
4) App is ready to use! Type the number of the command and provide it the information it requires. App will download file from server,
modify it and save it back to the server.