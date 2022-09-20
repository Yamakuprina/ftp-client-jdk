import client.CustomJSONParser;
import client.Student;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.script.ScriptException;
import java.util.List;

public class CustomJSONParserTest {
    CustomJSONParser customJSONParser = new CustomJSONParser();

    @DataProvider(name = "data-provider-1")
    public Object[][] dpMethod(){
        return new Object[][] {
                {"{\"students\" : [{ \"id\" : 1, \"name\" : \"Student1\"} , { \"id\" : 2, \"name\" : \"Student2\"} , { \"id\" : 3, \"name\" : \"Student3\"} ]}"},
                {"{\"students\" : [{ \"id\" : 1, \"name\" : \"Student1\"}]}"},
                {"{\"students\" : []}"}
        };
    }

    @DataProvider(name = "data-provider-2")
    public Object[][] dpMethod2(){
        return new Object[][] {
                {"{ }"},
                {""},
                {" "},
                {null}
        };
    }

    @Test(dataProvider = "data-provider-1")
    void parseOkayJSONString(String jsonString) throws ScriptException {
        List<Student> students = customJSONParser.parseFromJsonString(jsonString);
        Assert.assertNotNull(students);
    }

    @Test(dataProvider = "data-provider-2")
    void parseForbiddenJSONString(String jsonString) throws ScriptException {
        List<Student> students = customJSONParser.parseFromJsonString(jsonString);
        Assert.assertNull(students);
    }
}
