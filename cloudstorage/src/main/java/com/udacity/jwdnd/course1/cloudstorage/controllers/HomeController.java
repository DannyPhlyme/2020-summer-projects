package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileStorageService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;
import java.util.List;

@Controller
public class HomeController {

    private final NoteService noteService;
    private final CredentialService credentialService;
    private final FileStorageService fileStorageService;

    public HomeController(NoteService noteService, CredentialService credentialService, FileStorageService fileStorageService) {
        this.noteService = noteService;
        this.fileStorageService = fileStorageService;
        this.credentialService = credentialService;
    }

    /* Home Controller Resource Handler */
    @GetMapping("/home")
    public String getHomePage(Authentication auth, @ModelAttribute("credential") Credential credential,
                              @ModelAttribute("note") Note note, Model model) {
        model.addAttribute("notes", noteService.getNotes(getAuthenticatedUser(auth))); // Notes
        model.addAttribute("credentials", credentialService.getCredentials(getAuthenticatedUser(auth))); // Credentials
        model.addAttribute("files", fileStorageService.loadAll(getAuthenticatedUser(auth))); // Files
        return "home";
    }

    /* Note Controller Resource Handler */
    @PostMapping("/notes")
    public RedirectView createOrEditNote(Authentication auth, Note note, Model model,
                                         RedirectAttributes redirectAttributes) {
        if (note.getNoteId() > 0) {
            noteService.editNote(note);
        } else {
            noteService.createNote(note, getAuthenticatedUser(auth));
        }
        redirectAttributes.addFlashAttribute("success", "Your changes were successfully saved.");
        return new RedirectView("/home");
    }

    @GetMapping("/notes/{id}")
    public RedirectView deleteNote(@PathVariable("id") Integer noteId, Model model,
                                   RedirectAttributes redirectAttributes) {
        if (noteId > 0) {
            noteService.deleteNote(noteId);
        }
        redirectAttributes.addFlashAttribute("success", "Your changes were successfully saved.");
        return new RedirectView("/home");
    }

    /* Credential Controller Resource Handler */
    @PostMapping("/credentials/add")
    public RedirectView createCredential(Authentication auth, Credential credential, Model model,
                                         RedirectAttributes redirectAttributes) {
        credentialService.addCredential(credential, getAuthenticatedUser(auth));
        redirectAttributes.addFlashAttribute("success", "Your changes were successfully saved.");
        return new RedirectView("/home");
    }

    @PostMapping("/credentials/edit")
    public RedirectView editCredential(Credential credential, Model model,
                                       RedirectAttributes redirectAttributes) {
        credentialService.editCredential(credential);
        redirectAttributes.addFlashAttribute("success", "Your changes were successfully saved.");
        return new RedirectView("/home");
    }

    @GetMapping("/credentials/{id}")
    public RedirectView deleteCredential(@PathVariable("id") Integer credentialId, Model model,
                                         RedirectAttributes redirectAttributes) {
        credentialService.deleteCredential(credentialId);
        redirectAttributes.addFlashAttribute("success", "Your changes were successfully saved.");
        return new RedirectView("/home");
    }

    @GetMapping("/decrypt-credential/{credentialId}")
    @ResponseBody
    public List<String> decryptCredential(@PathVariable("credentialId") Integer credentialId, Model model) {
        return Arrays.asList(credentialService.decryptCredential(credentialId));
    }

    /* File Controller Resource Handler */
    @RequestMapping("/files/{fileId}")
    public ResponseEntity<ByteArrayResource> serveFile(@PathVariable("fileId") Integer fileId) {
        File file = fileStorageService.loadFile(fileId);
        ByteArrayResource resource = new ByteArrayResource(file.getFileData());
        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFileName())
                // Content-Length
                .contentLength(Long.parseLong(file.getFileSize()))
                // Content-Type
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(resource);
    }

    @PostMapping("/files/upload")
    public RedirectView handleFileUpload(Authentication auth, @RequestParam("fileUpload") MultipartFile fileUpload,
                                         RedirectAttributes redirectAttributes) {
        boolean fileExists = fileStorageService.checkIfFileExists(
                fileUpload.getOriginalFilename(), getAuthenticatedUser(auth));

        if (fileUpload.isEmpty()) {
            redirectAttributes.addFlashAttribute("fileError",
                    "Cannot store empty file!");
        } else if (fileExists) {
            redirectAttributes.addFlashAttribute("fileError",
                    "File " + fileUpload.getOriginalFilename() + " already exists!");
        } else if (fileUpload.getOriginalFilename().contains("..")) {
            // Security check
            redirectAttributes.addFlashAttribute("fileError",
                    "Cannot store file. Please try again!");
        } else {
            fileStorageService.store(fileUpload, getAuthenticatedUser(auth));
            redirectAttributes.addFlashAttribute("fileSuccess",
                    "You successfully uploaded " + fileUpload.getOriginalFilename() + "!");
        }
        return new RedirectView("/home");
    }

    @GetMapping("/files/delete/{id}")
    public RedirectView deleteFile(@PathVariable("id") Integer fileId, RedirectAttributes redirectAttributes) {
        String filename = fileStorageService.loadFile(fileId).getFileName();
        fileStorageService.delete(fileId);
        redirectAttributes.addFlashAttribute("fileSuccess",
                "You successfully deleted " + filename + "!");
        return new RedirectView("/home");
    }

    private String getAuthenticatedUser(Authentication authentication) {
        return authentication.getName();
    }
}
