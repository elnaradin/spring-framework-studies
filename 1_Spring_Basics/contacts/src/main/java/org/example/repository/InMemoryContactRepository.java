package org.example.repository;

import org.example.exceptions.ContactNotFoundException;
import org.example.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class InMemoryContactRepository implements ContactRepository {
    private final List<Contact> contacts = new ArrayList<>();

    @Override
    public List<Contact> getAllContacts() {
        return contacts;
    }

    @Override
    public void deleteByEmail(String email) throws ContactNotFoundException {
        boolean isDeleted = false;
        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);
            if (contact.getEmail().equals(email)) {
                contacts.remove(contact);
                isDeleted = true;
            }
        }
        if (!isDeleted) {
            throw new ContactNotFoundException();
        }
    }

    @Override
    public void save(Contact contact) {
        contacts.add(contact);
    }

    @Override
    public void saveAll(List<Contact> contacts) {
        this.contacts.addAll(contacts);
    }

    @Override
    public void deleteAll() {
        contacts.clear();
    }
}
