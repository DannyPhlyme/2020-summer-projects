package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(id = "logout")
    private WebElement logoutButton;

    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id = "userTable")
    private WebElement notesTable;

    @FindBy(id = "credentialTable")
    private WebElement credentialsTable;

    @FindBy(xpath = "//button[@id='add-note']")
    private WebElement addNoteButton;

    @FindBy(xpath = "//button[@id='add-credential']")
    private WebElement addCredentialButton;

    @FindBy(id = "edit-note")
    private WebElement editNoteButton;

    @FindBy(id = "delete-note")
    private WebElement deleteNoteButton;

    @FindBy(id = "note-title")
    private WebElement noteTitleField;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionField;

    @FindBy(id = "credential-url")
    private WebElement credUrlField;

    @FindBy(id = "credential-username")
    private WebElement credUsernameField;

    @FindBy(id = "credential-password")
    private WebElement credPasswordField;

    @FindBy(id = "save-credential")
    private WebElement saveCredentialButton;

    @FindBy(id = "editCredential-url")
    private WebElement editCredUrlField;

    @FindBy(id = "editCredential-username")
    private WebElement editCredUsernameField;

    @FindBy(id = "editCredential-password")
    private WebElement editCredPasswordField;

    @FindBy(id = "close-edit-credential")
    private WebElement closeEditCredentialButton;

    @FindBy(id = "save-edit-credential")
    private WebElement saveEditCredentialButton;

    @FindBy(id = "edit-credential")
    private WebElement editCredentialButton;

    @FindBy(id = "delete-credential")
    private WebElement deleteCredentialButton;

    @FindBy(id = "save-note")
    private WebElement saveNoteButton;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public WebElement getLogoutButton() {
        return logoutButton;
    }

    public WebElement getNotesTab() {
        return notesTab;
    }

    public WebElement getCredentialsTab() {
        return credentialsTab;
    }

    public WebElement getSaveCredentialButton() {
        return saveCredentialButton;
    }

    public WebElement getNotesTable() {
        return notesTable;
    }

    public WebElement getCredentialsTable() {
        return credentialsTable;
    }

    public WebElement getAddNoteButton() {
        return addNoteButton;
    }

    public WebElement getCloseEditCredentialButton() {
        return closeEditCredentialButton;
    }

    public WebElement getEditCredUrlField() {
        return editCredUrlField;
    }

    public WebElement getEditCredUsernameField() {
        return editCredUsernameField;
    }

    public WebElement getEditCredPasswordField() {
        return editCredPasswordField;
    }

    public WebElement getSaveEditCredentialButton() {
        return saveEditCredentialButton;
    }

    public WebElement getEditCredentialButton() {
        return editCredentialButton;
    }

    public WebElement getDeleteCredentialButton() {
        return deleteCredentialButton;
    }

    public WebElement getAddCredentialButton() {
        return addCredentialButton;
    }

    public WebElement getEditNoteButton() {
        return editNoteButton;
    }

    public WebElement getDeleteNoteButton() {
        return deleteNoteButton;
    }

    public WebElement getNoteTitleField() {
        return noteTitleField;
    }

    public WebElement getNoteDescriptionField() {
        return noteDescriptionField;
    }

    public WebElement getCredUrlField() {
        return credUrlField;
    }

    public WebElement getCredUsernameField() {
        return credUsernameField;
    }

    public WebElement getCredPasswordField() {
        return credPasswordField;
    }

    public WebElement getSaveNoteButton() {
        return saveNoteButton;
    }

    public void logout() {
        logoutButton.click();
    }

    public void clearNoteFields() {
        noteTitleField.clear();
        noteDescriptionField.clear();
    }

    public void clearEditCredentialField() {
        editCredUrlField.clear();
        editCredUsernameField.clear();
        editCredPasswordField.clear();
    }
}
