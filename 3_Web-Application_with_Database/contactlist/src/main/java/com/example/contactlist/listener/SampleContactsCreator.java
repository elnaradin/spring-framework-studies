package com.example.contactlist.listener;

import com.example.contactlist.model.Contact;
import com.example.contactlist.service.ContactService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@ConditionalOnProperty("sample-data.enabled")
@RequiredArgsConstructor
@Slf4j
public class SampleContactsCreator {
    private final ContactService contactService;

    @EventListener(ApplicationReadyEvent.class)
    public void createTestContacts() {
        List<Contact> contacts = IntStream.range(1, 6)
                .mapToObj((i) -> new Contact(
                        (long) i,
                        "TestName " + i,
                        "TestLastName " + i,
                        "test-email" + i + "@mail.ru",
                        "+7999999999" + i
                ))
                .collect(Collectors.toList());
        contactService.saveAll(contacts);
        log.info("Test data loaded");
    }

    @PreDestroy
    public void clearTable() {
        contactService.deleteAllContacts();
        log.info("Table 'contacts' truncated");
    }
}
