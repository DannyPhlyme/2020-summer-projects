package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.utils.StringGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;
    private final UserService userService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService, UserService userService) {
        this.encryptionService = encryptionService;
        this.userService = userService;
        this.credentialMapper = credentialMapper;
    }

    public int addCredential(Credential credential, String username) {
        credential.setUserId(userService.getUser(username).getUserId());
        String key = StringGenerator.random(16);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), key);
        return credentialMapper.insert(new Credential(
                null, credential.getUrl(), credential.getUsername(), key, encryptedPassword, credential.getUserId()));
    }

    public List<Credential> getCredentials(String username) {
        return credentialMapper.find(userService.getUser(username).getUserId());
    }

    public void editCredential(Credential credential) {
        credential.setKey(StringGenerator.random(16));
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), credential.getKey()));
        credentialMapper.update(credential);
    }

    public void deleteCredential(Integer credentialId) {
        credentialMapper.delete(credentialId);
    }

    public Credential findCredentialById(Integer credentialId) {
        return credentialMapper.findById(credentialId);
    }

    public String decryptCredential(Integer credentialId) {
        Credential credential = findCredentialById(credentialId);
        return encryptionService.decryptValue(credential.getPassword(), credential.getKey());
    }
}
