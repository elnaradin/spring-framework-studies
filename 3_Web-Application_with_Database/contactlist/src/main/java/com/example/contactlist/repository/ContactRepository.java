package com.example.contactlist.repository;

import com.example.contactlist.model.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactRepository {
    List<Contact> findAll();

    Contact save(Contact contact);

    Contact update(Contact contact);

    Optional<Contact> findById(Long id);

    void deleteById(Long id);
    void batchInsert(List<Contact> contacts);

    void truncateContacts();
}
