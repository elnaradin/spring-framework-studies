package com.example.contactlist.service;

import com.example.contactlist.model.Contact;
import com.example.contactlist.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;

    @Override
    public List<Contact> getContacts() {
        return contactRepository.findAll();
    }

    @Override
    public void saveContact(Contact contact) {
        contactRepository.save(contact);
    }

    @Override
    public void updateContact(Contact contact) {
        contactRepository.update(contact);
    }

    @Override
    public Contact getContactById(Long id) {
        return contactRepository.findById(id).orElseThrow();
    }

    @Override
    public void saveAll(List<Contact> contacts) {
        contactRepository.batchInsert(contacts);
    }

    @Override
    public void deleteAllContacts() {
        contactRepository.truncateContacts();
    }

    @Override
    public void deleteContactById(Long id) {
        contactRepository.deleteById(id);
    }
}
