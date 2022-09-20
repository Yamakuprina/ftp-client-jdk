package client;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class IdGenerator {

    public static int generateAndCheckUnique(List<Student> list) {
        HashSet<Integer> ids = new HashSet<>(list.stream().mapToInt(Student::getId).boxed().collect(Collectors.toList()));
        Random random = new Random();
        int uniqueId = 1;
        while (ids.contains(uniqueId)) uniqueId = Math.abs(random.nextInt());
        return uniqueId;
    }
}
