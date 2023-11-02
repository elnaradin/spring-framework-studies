package com.example.studentrecorder.repository;

import com.example.studentrecorder.model.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryStudentRepository implements StudentRepository {
    private Integer count = 0;
    private final Map<Integer, Student> students = new TreeMap<>();

    @Override
    public void saveAll(List<Student> students) {
        Map<Integer, Student> studentMap = students
                .stream()
                .peek(student -> student.setId(++count))
                .collect(Collectors.toMap(Student::getId, student -> student));
        this.students.putAll(studentMap);
    }

    @Override
    public void deleteAll() {
        students.clear();
    }

    @Override
    public void save(Student student) {
        student.setId(++count);
        students.put(student.getId(), student);
    }

    @Override
    public void deleteById(Integer id) {
        students.remove(id);
    }

    @Override
    public List<Student> findAll() {
        return students.values().stream().toList();
    }

    @Override
    public boolean existsById(Integer id) {
        return students.containsKey(id);
    }
}
