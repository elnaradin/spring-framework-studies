package com.example.studentrecorder.service;

import com.example.studentrecorder.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RecorderServiceImplTest {
    @Autowired
    private RecorderServiceImpl recorderService;

    @Test
    void saveAll() {
        recorderService.saveAll(IntStream
                .range(1, 4)
                .mapToObj(id -> new Student("Name " + id, "Surname" + id, 20 + id))
                .collect(Collectors.toList()));
        String studentList = recorderService.getStudentList();
        assertTrue(studentList.contains("Name 3"));
        assertEquals(4, studentList.split(System.lineSeparator()).length);
    }

    @Test
    void clearStudentList() {
        assertNotEquals("List is empty", recorderService.getStudentList());
        recorderService.clearStudentList();
        assertEquals("List is empty", recorderService.getStudentList());
    }

    @Test
    void addStudent() {
        recorderService.addStudent(new Student("TestName", "TestLastName", 20));
        assertTrue(recorderService.getStudentList().contains("TestName"));
    }

    @Test
    void removeStudent() {
        assertNotEquals("List is empty", recorderService.getStudentList());
        recorderService.removeStudent(1);
        assertFalse(recorderService.getStudentList().contains("Misha"));
    }

    @Test
    void getStudentList() {
        assertTrue(recorderService.getStudentList().contains("Misha"));
    }
}