package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.StorageException;
import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

@Service
public class FileStorageService {

    private final FileMapper fileMapper;
    private final UserService userService;

    public FileStorageService(FileMapper fileMapper, UserService userService) {
        this.fileMapper = fileMapper;
        this.userService = userService;
    }

    public int store(MultipartFile uploadedFile, String username) {
        String filename = uploadedFile.getOriginalFilename();
        File file = new File();
        try {
            file.setContentType(uploadedFile.getContentType());
            file.setFileSize(String.valueOf(uploadedFile.getSize()));
            file.setFileData(uploadedFile.getBytes());
            file.setUserId(userService.getUser(username).getUserId());
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
        return fileMapper.insert(new File(
                null, filename, file.getContentType(), file.getFileSize(), file.getFileData(), file.getUserId()));
    }

    public List<File> loadAll(String username) {
        return fileMapper.find(userService.getUser(username).getUserId());
    }

    public File loadFile(Integer fileId) {
        return fileMapper.findById(fileId);
    }

    public void delete(Integer fileId) {
        fileMapper.delete(fileId);
    }

    public boolean checkIfFileExists(String filename, String username) {
        final boolean[] isTrue = new boolean[1];
        isTrue[0] = false;
        HashSet<String> filenames = new HashSet<>();

        loadAll(username).forEach(file -> {
            filenames.add(file.getFileName());
        });

        for (String e : filenames) {
            if (StringUtils.equals(filename, e)) {
                isTrue[0] = true;
                break;
            }
        }
        return isTrue[0];
    }
}
