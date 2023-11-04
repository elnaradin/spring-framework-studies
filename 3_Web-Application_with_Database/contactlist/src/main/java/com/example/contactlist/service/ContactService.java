package com.example.contactlist.service;
import com.example.contactlist.model.Contact;

import java.util.List;

public interface ContactService {
    List<Contact> getContacts();

    void saveContact(Contact contact);

    void updateContact(Contact contact);

    Contact getContactById(Long id);

    void saveAll(List<Contact> contacts);

    void deleteAllContacts();

    void deleteContactById(Long id);
}