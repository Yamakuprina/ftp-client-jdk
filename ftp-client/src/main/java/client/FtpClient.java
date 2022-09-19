package client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

class FtpClient {

    private final String serverIp;
    private final int controlPort = Integer.parseInt(Config.getProperty("server.controlPort"));
    private final int activeDataPort = Integer.parseInt(Config.getProperty("server.activeDataPort"));
    private final String user;
    private final String password;
    private int passiveDataPort;
    private Socket controlSocket;
    private ServerSocket dataServerSocket;
    private Socket dataSocket;
    private BufferedReader dataSocketReader;
    private BufferedWriter dataSocketWriter;
    private BufferedReader controlSocketReader;
    private BufferedWriter controlSocketWriter;
    private String clientIpAndPorts;
    private boolean passiveModeFlag;

    public FtpClient(String ip, String user, String password) {
        this.serverIp = ip;
        this.user = user;
        this.password = password;
    }

    void open() {
        try {
            controlSocket = new Socket(serverIp, controlPort);
            controlSocketReader = new BufferedReader(new InputStreamReader(controlSocket.getInputStream(), StandardCharsets.ISO_8859_1));
            controlSocketWriter = new BufferedWriter(new OutputStreamWriter(controlSocket.getOutputStream(), StandardCharsets.ISO_8859_1));
            clientIpAndPorts = controlSocket.getLocalAddress()
                    .toString().replace(".", ",").replace("/", "") + "," + activeDataPortToPortsString();
            controlSocketWriter.write("USER " + user + "\r\n");
            controlSocketWriter.flush();
            controlSocketWriter.write("PASS " + password + "\r\n");
            controlSocketWriter.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    void sendFileToPath(File file, String path) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] contents = new byte[fis.available()];
        fis.read(contents);
        fis.close();
        String contentsString = new String(contents);
        if (!passiveModeFlag) {
            controlSocketWriter.write("PORT " + clientIpAndPorts + "\r\n");
            controlSocketWriter.flush();
        } else {
            controlSocketWriter.write("PASV" + "\r\n");
            controlSocketWriter.flush();
            String passiveModeReply = "";
            while (!passiveModeReply.startsWith("227")) {
                passiveModeReply = controlSocketReader.readLine();
            }
            passiveDataPort = passiveDataPortStringToInt(passiveModeReply);
        }

        controlSocketWriter.write("STOR " + path + "\r\n");
        controlSocketWriter.flush();

        if (!passiveModeFlag) {
            dataServerSocket = new ServerSocket(activeDataPort, 1, controlSocket.getLocalAddress());
            dataSocket = dataServerSocket.accept();
        } else {
            dataSocket = new Socket(serverIp, passiveDataPort);
        }
        dataSocketWriter = new BufferedWriter(new OutputStreamWriter(dataSocket.getOutputStream()));
        dataSocketReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
        dataSocketWriter.write(contentsString);
        dataSocketWriter.flush();
        dataSocket.close();
        dataServerSocket.close();
    }

    void downloadFile(String pathToFile, String saveDestination) throws IOException {
        if (!passiveModeFlag) {
            controlSocketWriter.write("PORT " + clientIpAndPorts + "\r\n");
            controlSocketWriter.flush();
        } else {
            controlSocketWriter.write("PASV" + "\r\n");
            controlSocketWriter.flush();
            String passiveModeReply = "";
            while (!passiveModeReply.startsWith("227")) {
                passiveModeReply = controlSocketReader.readLine();
            }
            passiveDataPort = passiveDataPortStringToInt(passiveModeReply);
        }

        controlSocketWriter.write("RETR " + pathToFile + "\r\n");
        controlSocketWriter.flush();

        if (!passiveModeFlag) {
            dataServerSocket = new ServerSocket(activeDataPort, 1, controlSocket.getLocalAddress());
            dataSocket = dataServerSocket.accept();
        } else {
            dataSocket = new Socket(serverIp, passiveDataPort);
        }

        dataSocketWriter = new BufferedWriter(new OutputStreamWriter(dataSocket.getOutputStream()));
        dataSocketReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
        StringBuilder fileContents = new StringBuilder();
        String line;
        while ((line = dataSocketReader.readLine()) != null) {
            fileContents.append(line);
        }
        //OutputStream out = getClass().getResourceAsStream(saveDestination);
        //OutputStream out = getClass().getResource(saveDestination);
        //
        FileOutputStream out = new FileOutputStream(saveDestination);
        out.write(fileContents.toString().getBytes());
        out.close();
        dataSocket.close();
        dataServerSocket.close();
    }

    void switchToPassive() {
        passiveModeFlag = true;
    }

    void switchToActive() {
        passiveModeFlag = false;
    }

    public void close() throws IOException {
        controlSocket.close();
    }

    private String activeDataPortToPortsString() {
        int firstNum = activeDataPort / 256;
        return "" + firstNum + "," + (activeDataPort - firstNum);
    }

    private int passiveDataPortStringToInt(String s) {
        String[] portNums = s.split(",");
        return Integer.parseInt(portNums[4]) * 256 + Integer.parseInt(portNums[5].replace(")", ""));
    }
}
