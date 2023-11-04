package com.example.contactlist.controller;

import com.example.contactlist.model.Contact;
import com.example.contactlist.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;
    @GetMapping("/")
    public String mainPage(Model model){
        model.addAttribute("contacts", contactService.getContacts());
        return "index";
    }
    @GetMapping("/contact/edit/{id}")
    public String editContactPage(@PathVariable Long id, Model model){
        model.addAttribute("contact", contactService.getContactById(id));
        return "contact";
    }

    @GetMapping("/contact/add")
    public String addContactPage(Model model){
        model.addAttribute("contact", new Contact());
        return "contact";
    }
    @PostMapping("/contact/save")
    public String saveContact(@ModelAttribute Contact contact){
        contactService.saveContact(contact);
        return "redirect:/";
    }

    @PostMapping("/contact/update")
    public String updateContact(@ModelAttribute Contact contact){
        contactService.updateContact(contact);
        return "redirect:/";
    }
    @PostMapping("/contact/delete/{id}")
    public String deleteContact(@PathVariable Long id){
        contactService.deleteContactById(id);
        return "redirect:/";
    }


}
