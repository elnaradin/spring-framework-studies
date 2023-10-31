package com.example.studentrecorder.listener;

import com.example.studentrecorder.config.properties.InitDataProperties;
import com.example.studentrecorder.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty("sample-data.enabled")
@RequiredArgsConstructor
@Slf4j
public class AddSampleDataListener {
    private final StudentRepository studentRepository;
    private final InitDataProperties initDataProperties;

    @EventListener(ApplicationStartedEvent.class)
    public void loadData() {
        studentRepository.saveAll(initDataProperties.getStudents());
        log.info("Init data loaded");
    }

}
