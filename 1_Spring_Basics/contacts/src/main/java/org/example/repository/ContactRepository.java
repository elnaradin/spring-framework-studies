package org.example.repository;

import org.example.exceptions.ContactNotFoundException;
import org.example.model.Contact;

import java.util.List;

public interface ContactRepository {
    List<Contact> getAllContacts();
    void deleteByEmail(String email) throws ContactNotFoundException;

    void save(Contact contact);
    void saveAll(List<Contact> contacts);
    void deleteAll();
}
