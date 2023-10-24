package org.example.service;

import org.example.exceptions.ContactNotFoundException;
import org.example.model.Contact;

public interface ContactService {
    String getAllContacts();
    void deleteContact(String email) throws ContactNotFoundException;
    void addContact(Contact contact);
}
