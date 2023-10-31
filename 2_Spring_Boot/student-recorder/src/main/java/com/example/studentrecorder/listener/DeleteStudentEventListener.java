package com.example.studentrecorder.listener;

import com.example.studentrecorder.event.DeleteStudentEvent;
import com.example.studentrecorder.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class DeleteStudentEventListener {
    private final StudentRepository studentRepository;
    @EventListener
    public void listen(DeleteStudentEvent eventHolder) {
        studentRepository.deleteById(eventHolder.getStudentId());
    }

}
