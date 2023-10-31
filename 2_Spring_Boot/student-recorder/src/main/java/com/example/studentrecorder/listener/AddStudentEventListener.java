package com.example.studentrecorder.listener;

import com.example.studentrecorder.event.AddStudentEvent;
import com.example.studentrecorder.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddStudentEventListener {
    private final StudentRepository studentRepository;
    @EventListener
    public void listen(AddStudentEvent eventHolder){
        studentRepository.save(eventHolder.getStudent());
    }
}
