import client.IdGenerator;
import client.Student;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IdGeneratorTest {

    @DataProvider(name = "data-provider")
    public Object[][] dpMethod(){
        return new Object[][] {
                {Arrays.asList(new Student(1,"Test1"),new Student(2,"Test1"))},
                {Arrays.asList(new Student(1000,"Test2"),new Student(500,"Test2"))},
                {Arrays.asList(new Student(Integer.MAX_VALUE,"Test3"),new Student(Integer.MIN_VALUE,"Test3"))},
                {Arrays.asList(new Student(1,"Test4"),new Student(1,"Test4"))},
                {new ArrayList<>()}
        };
    }

    @Test(dataProvider = "data-provider")
    void idIsGeneratedAndUnique(List<Student> list){
        List<Integer> ids = list.stream().map(Student::getId).collect(Collectors.toList());
        int newId = IdGenerator.generateAndCheckUnique(list);
        Assert.assertFalse(ids.contains(newId));
    }
}
