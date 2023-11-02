package com.example.studentrecorder.service;

import com.example.studentrecorder.event.AddStudentEvent;
import com.example.studentrecorder.event.DeleteStudentEvent;
import com.example.studentrecorder.model.Student;
import com.example.studentrecorder.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RecorderServiceImpl implements RecorderService {
    private final StudentRepository studentRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public String saveAll(List<Student> students) {
        studentRepository.saveAll(students);
        return "Saved!";
    }

    @Override
    public String clearStudentList() {
        studentRepository.deleteAll();
        return "All student records are deleted!";
    }

    @Override
    public String addStudent(Student student) {
        applicationEventPublisher.publishEvent(new AddStudentEvent(this, student));
        return MessageFormat.format("New student is saved! Student: {0} {1}, {2} years old",
                student.getFirstName(), student.getLastName(), student.getAge());
    }

    @Override
    public String removeStudent(Integer id) {
        if (!studentRepository.existsById(id)) {
            return MessageFormat.format("Student with id {0} doesn't exist!", id);
        }
        applicationEventPublisher.publishEvent(new DeleteStudentEvent(this, id));
        return MessageFormat.format("Student with id {0} is deleted!", id);
    }

    @Override
    public String getStudentList() {
        List<Student> students = studentRepository.findAll();
        return students.isEmpty()? "List is empty" : students.stream()
                .map(Student::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
