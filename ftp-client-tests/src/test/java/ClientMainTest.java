import client.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ClientMainTest {

    List<String> dummyFileReadAll = Arrays.asList("test");
    String dummyServerIpLoginPassword = "255.255.255.254\ntestUser\ntestPassword\n";
    String quitCommand = "7";

    @DataProvider(name = "data-provider")
    public Object[][] dpMethod(){
        return new Object[][] {
                {" "},
                {null},
                {"{ }\n"},
                {"\n"},
                {" \n"},
                {"1\nTestName\n"},
                {"2\n12345\n"},
                {"3\nTestName\n"},
                {"4\n12345\n"},
                {"5\n1\n"},
                {"5\n2\n"},
                {"6\n"}
        };
    }

    @BeforeTest
    void mockDependencies(){
        Mockito.mockConstruction(FtpClient.class);
        Mockito.mockConstruction(CustomJSONParser.class);
        Mockito.mockConstruction(BufferedWriter.class);
    }

    @Test(dataProvider = "data-provider")
    void runClient(String command){
        System.setIn(new ByteArrayInputStream((dummyServerIpLoginPassword+command+quitCommand).getBytes()));

        try (MockedStatic<Files> dummy = Mockito.mockStatic(Files.class)) {
            dummy.when(() -> Files.readAllLines(Paths.get(Config.getProperty("client.pathToStudentsFile"))))
                    .thenReturn(dummyFileReadAll);

            ClientMain.main(new String[]{});

        } catch (Exception e){
            e.printStackTrace();
            Assert.fail("Exception caught");
        }
    }
}
