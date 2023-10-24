package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.ContactNotFoundException;
import org.example.model.Contact;
import org.example.repository.ContactRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ContactsServiceImpl implements ContactService {
    private final ContactRepository contactRepository;

    @Override
    public String getAllContacts() {
        List<Contact> allContacts = contactRepository.getAllContacts();
        String contacts = allContacts
                .stream()
                .map(Contact::toString)
                .collect(Collectors.joining(System.lineSeparator()));
        if (contacts.isEmpty()) {
            contacts = "-- List is empty";
        }
        return contacts;
    }

    @Override
    public void deleteContact(String email) throws ContactNotFoundException {
        contactRepository.deleteByEmail(email);
    }

    @Override
    public void addContact(Contact contact) {
        contactRepository.save(contact);
    }
}
