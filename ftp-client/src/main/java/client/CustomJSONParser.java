package client;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomJSONParser {
    private final ScriptEngine engine;

    public CustomJSONParser() {
        ScriptEngineManager sem = new ScriptEngineManager();
        this.engine = sem.getEngineByName("javascript");
    }

    public List<Student> parseFromJsonString(String jsonString) throws ScriptException {
        String script = "Java.asJSONCompatible(" + jsonString + ")";
        Object result = this.engine.eval(script);
        Map contents = (Map) result;
        if (contents==null) return null;
        List<?> studentsObjects = (List) contents.get("students");
        if (studentsObjects==null) return null;
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < studentsObjects.size(); i++) {
            Map studentMap = (Map) studentsObjects.get(i);
            students.add(new Student((int) studentMap.get("id"), (String) studentMap.get("name")));
        }
        return students;
    }

    public String parseToJsonString(List<Student> list) {
        list = list.stream().sorted(Comparator.comparing(Student::getName))
                .collect(Collectors.toList());
        StringBuilder stringBuilder = new StringBuilder("{\"students\" : [");
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append("{ \"id\" : " + list.get(i).getId() + ", ");
            stringBuilder.append("\"name\" : \"" + list.get(i).getName() + "\"} ");
            if (i != list.size() - 1) stringBuilder.append(", ");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }
}
