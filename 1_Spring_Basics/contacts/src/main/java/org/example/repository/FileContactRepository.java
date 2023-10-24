package org.example.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.example.exceptions.ContactNotFoundException;
import org.example.model.Contact;
import org.example.model.Headers;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FileContactRepository implements ContactRepository {
    @Value("${app.contacts.file.path}")
    private String filePath;
    private static final char DELIMITER = ';';
    private String headers;
    private CSVFormat csvFormat;

    @PostConstruct
    public void setUp() {
        try {
            csvFormat = CSVFormat
                    .newFormat(DELIMITER)
                    .builder()
                    .setHeader(Headers.class)
                    .setSkipHeaderRecord(true)
                    .setRecordSeparator(System.lineSeparator())
                    .setIgnoreEmptyLines(true)
                    .build();
            headers = Arrays.stream(Headers.values())
                    .map(Headers::toString)
                    .map(String::toLowerCase)
                    .collect(Collectors.joining(String.valueOf(DELIMITER)))
                    .concat(System.lineSeparator());
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                Files.createFile(path);
                Files.writeString(path, headers);
                log.info("new file created at {}", filePath);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    @Override
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        try (Reader in = new FileReader(filePath, StandardCharsets.UTF_8);) {
            Iterable<CSVRecord> records = csvFormat.parse(in);
            records.forEach(r -> {
                contacts.add(Contact
                        .builder()
                        .fullName(r.get(Headers.fullName))
                        .email(r.get(Headers.email))
                        .phone(r.get(Headers.phone))
                        .build());
            });
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return contacts;
    }

    @Override
    public void deleteByEmail(String email) throws ContactNotFoundException {
        List<Contact> contacts = getAllContacts();
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
        deleteAll();
        saveAll(contacts);
    }

    @Override
    public void save(Contact contact) {
        try (Writer out = new FileWriter(filePath, StandardCharsets.UTF_8, true)) {
            CSVPrinter printer = csvFormat.print(out);
            printer.printRecord(contact.getFullName(), contact.getPhone(), contact.getEmail());
            printer.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    @Override
    public void saveAll(List<Contact> contacts) {
        contacts.forEach(this::save);
    }

    @Override
    public void deleteAll() {
        try (Writer out = new FileWriter(filePath)) {
            out.write("");
            CSVPrinter printer = csvFormat.print(out);
            printer.printRecord(headers);
            printer.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
