import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final String pathToStudentsFileOnFtpServer = Config.getProperty("server.pathToStudentsFile");
    private static final String pathToStudentsFileOnLocal = Config.getProperty("client.pathToStudentsFile");
    private static final CustomJSONParser parser = new CustomJSONParser();
    private static FtpClient ftpClient;

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Enter IP of the FTP Server: ");


            String ip = reader.readLine();
            System.out.println("Enter username: ");
            String username = reader.readLine();
            System.out.println("Enter password: ");
            String password = reader.readLine();
            ftpClient = new FtpClient(ip, username, password);
            ftpClient.open();


            boolean exitFlag = false;
            while (!exitFlag) {
                System.out.println("\nChoose operation: ");
                System.out.println("1. Get students by Name");
                System.out.println("2. Get student by Id");
                System.out.println("3. Add student");
                System.out.println("4. Delete student by Id");
                System.out.println("5. Change mode");
                System.out.println("6. Get all students");
                System.out.println("7. Quit");
                String reply = reader.readLine();
                switch (reply) {
                    case "1":
                        System.out.println("Enter Name: ");
                        String name = reader.readLine();
                        if (!Validator.nameIsValid(name)) continue;
                        String studentsJson = downloadAndReadStudentsFile();
                        List<Student> students = parser.parseFromJsonString(studentsJson);
                        List<Student> studentsFiltered = students.stream()
                                .filter(s -> s.getName().contains(name))
                                .sorted(Comparator.comparing(Student::getName)).collect(Collectors.toList());
                        if (studentsFiltered.size() == 0) {
                            System.out.println("Not found");
                            continue;
                        }
                        for (int i = 0; i < studentsFiltered.size(); i++) {
                            System.out.println((i + 1) + ". Id: " + studentsFiltered.get(i).getId() + " Name: " + studentsFiltered.get(i).getName());
                        }
                        continue;
                    case "2":
                        System.out.println("Enter Id: ");
                        String idLine = reader.readLine();
                        if (!Validator.idIsValid(idLine)) continue;
                        int id = Integer.parseInt(idLine);
                        studentsJson = downloadAndReadStudentsFile();
                        students = parser.parseFromJsonString(studentsJson);
                        Student student = students.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
                        if (student == null) {
                            System.out.println("Not found");
                        } else {
                            System.out.println("Id: " + student.getId() + " Name: " + student.getName());
                        }
                        continue;
                    case "3":
                        System.out.println("Enter new student's Name: ");
                        name = reader.readLine();
                        if (!Validator.nameIsValid(name)) continue;
                        studentsJson = downloadAndReadStudentsFile();
                        students = parser.parseFromJsonString(studentsJson);
                        int uniqueId = IdGenerator.generateAndCheckUnique(students);
                        students.add(new Student(uniqueId, name));
                        String studentsJsonNew = parser.parseToJsonString(students);
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToStudentsFileOnLocal, false))) {
                            writer.write(studentsJsonNew);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ftpClient.sendFileToPath(new File(pathToStudentsFileOnLocal), pathToStudentsFileOnFtpServer);
                        System.out.println("Successfully added new student");
                        continue;
                    case "4":
                        System.out.println("Enter Id: ");
                        idLine = reader.readLine();
                        if (!Validator.idIsValid(idLine)) continue;
                        int deleteId = Integer.parseInt(idLine);
                        studentsJson = downloadAndReadStudentsFile();
                        students = parser.parseFromJsonString(studentsJson);
                        students = students.stream().filter(s -> s.getId() != deleteId).collect(Collectors.toList());
                        studentsJsonNew = parser.parseToJsonString(students);
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToStudentsFileOnLocal, false))) {
                            writer.write(studentsJsonNew);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ftpClient.sendFileToPath(new File(pathToStudentsFileOnLocal), pathToStudentsFileOnFtpServer);
                        System.out.println("Successfully deleted student");
                        continue;
                    case "5":
                        System.out.println("Choose mode:");
                        System.out.println("1. Active");
                        System.out.println("2. Passive");
                        String modeReply = reader.readLine();
                        switch (modeReply) {
                            case "1":
                                ftpClient.switchToActive();
                                System.out.println("Successfully set mode to Active");
                                continue;
                            case "2":
                                ftpClient.switchToPassive();
                                System.out.println("Successfully set mode to Passive");
                                continue;
                            default:
                                System.out.println("Typo Error. Enter only number of the mode");
                        }
                        continue;
                    case "6":
                        studentsJson = downloadAndReadStudentsFile();
                        students = parser.parseFromJsonString(studentsJson)
                                .stream().sorted(Comparator.comparing(Student::getName))
                                .collect(Collectors.toList());
                        for (int i = 0; i < students.size(); i++) {
                            System.out.println((i + 1) + ". Id: " + students.get(i).getId() + " Name: " + students.get(i).getName());
                        }
                        continue;
                    case "7":
                        System.out.println("Good Bye!");
                        exitFlag = true;
                        continue;
                    default:
                        System.out.println("Typo Error. Enter only number of the operation");
                }
            }
            ftpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String downloadAndReadStudentsFile() throws IOException {
        ftpClient.downloadFile(pathToStudentsFileOnFtpServer, pathToStudentsFileOnLocal);
        Path filePath = Paths.get(pathToStudentsFileOnLocal);
        return Files.readAllLines(filePath).stream().reduce((s, s2) -> s + s2).get();
    }


}
