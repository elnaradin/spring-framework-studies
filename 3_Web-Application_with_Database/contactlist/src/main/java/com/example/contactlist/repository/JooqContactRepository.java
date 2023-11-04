package com.example.contactlist.repository;

import com.example.contactlist.exception.ContactNotFoundException;
import com.example.contactlist.jooq.db.Tables;
import com.example.contactlist.jooq.db.tables.records.ContactsRecord;
import com.example.contactlist.model.Contact;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(value = "repository-impl.type", havingValue = "jooq")
public class JooqContactRepository implements ContactRepository {

    private final DSLContext dslContext;

    @Override
    public List<Contact> findAll() {
        return dslContext
                .selectFrom(Tables.CONTACTS)
                .fetchInto(Contact.class);
    }

    @Override
    public Contact save(Contact contact) {
        contact.setId(System.currentTimeMillis());
        ContactsRecord contactsRecord = dslContext.newRecord(Tables.CONTACTS, contact);
        contactsRecord.store();
        return contactsRecord.into(Contact.class);
    }

    @Override
    public Contact update(Contact contact) {
        return dslContext
                .update(Tables.CONTACTS)
                .set(dslContext.newRecord(Tables.CONTACTS, contact))
                .where(Tables.CONTACTS.ID.eq(contact.getId()))
                .returning()
                .fetchOptional()
                .map(contactsRecord -> contactsRecord.into(Contact.class))
                .orElseThrow(() -> new ContactNotFoundException("Contact not found when updating. ID: " + contact.getId()));
    }

    @Override
    public Optional<Contact> findById(Long id) {
        return dslContext
                .selectFrom(Tables.CONTACTS)
                .where(Tables.CONTACTS.ID.eq(id))
                .fetchOptional()
                .map(contactsRecord -> contactsRecord.into(Contact.class));
    }

    @Override
    public void deleteById(Long id) {
        dslContext
                .deleteFrom(Tables.CONTACTS)
                .where(Tables.CONTACTS.ID.eq(id))
                .execute();
    }

    @Override
    public void batchInsert(List<Contact> contacts) {
        List<Query> insertQueries = new ArrayList<>();
        for (Contact contact : contacts) {
            insertQueries.add(
                    dslContext.insertInto(
                            Tables.CONTACTS,
                            Tables.CONTACTS.ID,
                            Tables.CONTACTS.FIRST_NAME,
                            Tables.CONTACTS.LAST_NAME,
                            Tables.CONTACTS.EMAIL,
                            Tables.CONTACTS.PHONE
                    ).values(
                            contact.getId(),
                            contact.getFirstName(),
                            contact.getLastName(),
                            contact.getEmail(),
                            contact.getPhone()
                    )
            );
        }
        dslContext
                .batch(insertQueries)
                .execute();

    }

    @Override
    public void truncateContacts() {
        dslContext
                .truncate(Tables.CONTACTS)
                .execute();
    }
}
