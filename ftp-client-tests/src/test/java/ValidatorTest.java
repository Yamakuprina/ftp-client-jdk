import client.Validator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ValidatorTest {

    @Test
    void givenNullId(){
        String s = null;
        Assert.assertFalse(Validator.idIsValid(s));
    }

    @Test
    void givenSpaceId(){
        String s = " ";
        Assert.assertFalse(Validator.idIsValid(s));
    }

    @Test
    void givenEmptyId(){
        String s = "";
        Assert.assertFalse(Validator.idIsValid(s));
    }

    @Test
    void givenNonNumericId(){
        String s = "Any text or digits 123 or special symbols @#$%^";
        Assert.assertFalse(Validator.idIsValid(s));
    }

    @Test
    void givenOkayId(){
        String s = "123456";
        Assert.assertTrue(Validator.idIsValid(s));
    }

    @Test
    void givenNullName(){
        String s = null;
        Assert.assertFalse(Validator.nameIsValid(s));
    }

    @Test
    void givenSpaceName(){
        String s = " ";
        Assert.assertFalse(Validator.nameIsValid(s));
    }

    @Test
    void givenEmptyName(){
        String s = "";
        Assert.assertFalse(Validator.nameIsValid(s));
    }

    @Test
    void givenOkayName(){
        String s = "AnyTextOrDigits123";
        Assert.assertTrue(Validator.nameIsValid(s));
    }
}
