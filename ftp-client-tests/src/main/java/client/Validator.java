package client;

import java.util.regex.Pattern;

public class Validator {

    private static final Pattern idPattern = Pattern.compile("[1-9][0-9]*");

    public static boolean idIsValid(String idLine) {
        if (idLine == null || idLine.isEmpty() | idLine.equals(" ")) {
            System.out.println("Id should not be empty");
            return false;
        }
        if (!idPattern.matcher(idLine).matches()) {
            System.out.println("Id should match pattern");
            return false;
        }
        return true;
    }

    public static boolean nameIsValid(String name) {
        if (name == null || name.isEmpty() | name.equals(" ")) {
            System.out.println("Name should not be empty");
            return false;
        }
        return true;
    }
}
