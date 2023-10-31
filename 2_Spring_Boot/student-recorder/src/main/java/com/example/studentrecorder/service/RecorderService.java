package com.example.studentrecorder.service;

import com.example.studentrecorder.model.Student;

import java.util.List;

public interface RecorderService {
    String saveAll(List<Student> students);

    String clearStudentList();

    String addStudent(Student student);

    String removeStudent(Integer id);

    String getStudentList();
}
