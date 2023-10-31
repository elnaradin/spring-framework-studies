package com.example.studentrecorder.repository;

import com.example.studentrecorder.model.Student;

import java.util.List;

public interface StudentRepository {
    void saveAll(List<Student> students);

    void deleteAll();

    void save(Student student);

    void deleteById(Integer id);

    List<Student> findAll();

    boolean existsById(Integer id);
}
