package com.example.studentrecorder.shell;

import com.example.studentrecorder.model.Student;
import com.example.studentrecorder.service.RecorderService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class StudentRecorderShell {
    private final RecorderService recorderService;

    @ShellMethod
    public String add(
            @ShellOption("f") String firstName,
            @ShellOption("l") String lastName,
            @ShellOption("a") Integer age
    ) {
        return recorderService.addStudent(new Student(firstName, lastName, age));
    }

    @ShellMethod(key = "rm")
    public String remove(@ShellOption("id") Integer id) {
        return recorderService.removeStudent(id);
    }

    @ShellMethod(key = "ls")
    public String list() {
        return recorderService.getStudentList();
    }

    @ShellMethod(key = "cl")
    public String clean() {
        return recorderService.clearStudentList();
    }


}
