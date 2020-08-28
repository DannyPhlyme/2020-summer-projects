package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;
    private final UserService userService;

    public NoteService(NoteMapper noteMapper, UserService userService) {
        this.noteMapper = noteMapper;
        this.userService = userService;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("NoteService is running");
    }

    public int createNote(Note note, String username) {
        note.setUserId(userService.getUser(username).getUserId());
        return noteMapper.insert(note);
    }

    public List<Note> getNotes(String username) {
        return noteMapper.find(userService.getUser(username).getUserId());
    }

    public void editNote(Note note) {
        noteMapper.update(note);
    }

    public void deleteNote(Integer noteId) {
        noteMapper.delete(noteId);
    }
}
