# ftp-client-jdk
 
This is the console app with the FTP-client built on Sockets without any non-JDK libraries. It can upload and download files from different FTP-servers such as FileZilla,
Core FTP Server, Home FTP Server etc. This particular app uses this FtpClient to get and update information about students stored as a file on the FTP-server.

### Subject area
FTP-server stores students.json file with following structure:
```
{
  "students": [
    {
      "id": 1,
      "name": "Student1"
    },
    {
      "id": 2,
      "name": "Student2"
    },
    {
      "id": 3,
      "name": "Student3"
    }
  ]
}
```
App should take IP-address of the server, username and password to log in to the FTP-server and provide following functionality:

1) Get students by Name
2) Get student by ID
3) Add student
4) Delete student by ID
5) Use Active and Passive FTP-modes