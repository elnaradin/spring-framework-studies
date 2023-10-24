package org.example.console;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.ContactNotFoundException;
import org.example.model.Contact;
import org.example.model.Operations;
import org.example.service.ContactService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

import static java.lang.System.out;

@Component
@RequiredArgsConstructor
@Slf4j
public class Console {

    @Value("${app.profile}")
    private String activeProfile;
    private final ContactService contactService;
    private static final String CONTACT_FORMAT = "<full name>;<phone>;<email>";
    private static final String CONTACT_SAVE_INPUT_MESSAGE = "Enter the contact according to this format: " + CONTACT_FORMAT;
    private static final String CONTACT_DELETE_INPUT_MESSAGE = "Enter the email of the contact you want to delete:";
    private static final String ALL_CONTACTS_MESSAGE = "All saved contacts:";
    private static final String OPERATION_INPUT_MESSAGE = System.lineSeparator()+"Enter the operation. Supported operations: " + Arrays.toString(Operations.values());
    private static final String INCORRECT_CONTACT_FORMAT_ERROR = "-- The contact should be entered in the following format: " + CONTACT_FORMAT + "!";
    private static final String INCORRECT_NAME_FORMAT_ERROR = "-- A correct name should be entered!";
    private static final String INCORRECT_PHONE_FORMAT_ERROR = "-- A correct Russian phone number (starting with +7) should be entered!";
    private static final String INCORRECT_EMAIL_ERROR = "-- A correct email address should be entered!";
    private static final String OPERATION_DOES_NOT_EXIST = "-- This operation doesn't exists!";
    private static final String SAVE_SUCCESS_MESSAGE = "-- The contact is saved!";
    private static final String DELETE_SUCCESS_MESSAGE = "-- The contact is deleted!";
    private static final String CONTACT_NOT_FOUND_MESSAGE = "-- Failed to delete. Contact doesn't exist!";

    public void start() {
        log.info("Active profile: {}", activeProfile);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            out.println(OPERATION_INPUT_MESSAGE);
            Operations operation;
            try {
                operation = Operations.valueOf(scanner.nextLine().toUpperCase(Locale.ROOT));
                switch (operation) {
                    case SAVE -> {
                        out.println(CONTACT_SAVE_INPUT_MESSAGE);
                        String contact = scanner.nextLine();
                        validateAndSave(contact);
                    }
                    case LIST -> {
                        out.println(ALL_CONTACTS_MESSAGE);
                        out.println(contactService.getAllContacts());
                    }
                    case DELETE -> {
                        out.println(CONTACT_DELETE_INPUT_MESSAGE);
                        String email = scanner.nextLine();
                        validateAndDelete(email);
                    }
                    case EXIT -> {
                        return;
                    }
                }
            } catch (IllegalArgumentException e) {
                out.println(OPERATION_DOES_NOT_EXIST);
                log.error(e.getMessage());
            }
        }
    }

    private void validateAndDelete(String email) {
        if (!isValidEmail(email)) {
            out.println(INCORRECT_EMAIL_ERROR);
        } else {
            try {
                contactService.deleteContact(email);
                out.println(DELETE_SUCCESS_MESSAGE);
            } catch (ContactNotFoundException e) {
                out.println(CONTACT_NOT_FOUND_MESSAGE);
            }

        }
    }

    private void validateAndSave(String contact) {
        String[] data = contact.split(";");
        if (!isValidContact(contact)) {
            out.println(INCORRECT_CONTACT_FORMAT_ERROR);
        } else if (!isValidFullName(data[0])) {
            out.println(INCORRECT_NAME_FORMAT_ERROR);
        } else if (!isValidRussianPhone(data[1])) {
            out.println(INCORRECT_PHONE_FORMAT_ERROR);
        } else if (!isValidEmail(data[2])) {
            out.println(INCORRECT_EMAIL_ERROR);
        } else {
            contactService.addContact(new Contact(data[0], data[1], data[2]));
            out.println(SAVE_SUCCESS_MESSAGE);
        }
    }

    private static boolean isValidEmail(String data) {
        return data.matches("[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}");
    }

    private static boolean isValidRussianPhone(String data) {
        return data.matches("\\+7\\d{10}");
    }

    private static boolean isValidFullName(String data) {
        return data.matches("[\\p{L}- ]+");
    }

    private static boolean isValidContact(String contact) {
        return contact.matches("(.+;){2}.+");
    }
}
