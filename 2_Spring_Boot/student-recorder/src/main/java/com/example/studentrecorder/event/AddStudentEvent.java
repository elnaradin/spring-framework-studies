package com.example.studentrecorder.event;

import com.example.studentrecorder.model.Student;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
@Getter
public class AddStudentEvent extends ApplicationEvent {
    private final Student student;
    public AddStudentEvent(Object source, Student student) {
        super(source);
        this.student = student;
    }
}
