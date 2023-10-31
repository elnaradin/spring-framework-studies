package com.example.studentrecorder.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeleteStudentEvent extends ApplicationEvent {
    private final Integer studentId;
    public DeleteStudentEvent(Object source, Integer studentId) {
        super(source);
        this.studentId = studentId;
    }
}
