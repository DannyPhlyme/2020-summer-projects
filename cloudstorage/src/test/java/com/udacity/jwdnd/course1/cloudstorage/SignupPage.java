package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {

    @FindBy(id = "inputFirstName")
    private WebElement firstnameField;

    @FindBy(id = "inputLastName")
    private WebElement lastnameField;

    @FindBy(id = "inputUsername")
    private WebElement usernameField;

    @FindBy(id = "inputPassword")
    private WebElement passwordField;

    @FindBy(id = "submit-button")
    private WebElement signupButton;

    @FindBy(id = "login-link")
    private WebElement loginLink;

    public SignupPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public WebElement getFirstnameField() {
        return firstnameField;
    }

    public WebElement getLastnameField() {
        return lastnameField;
    }

    public WebElement getUsernameField() {
        return usernameField;
    }

    public WebElement getPasswordField() {
        return passwordField;
    }

    public WebElement getSignupButton() {
        return signupButton;
    }

    public WebElement getLoginLink() {
        return loginLink;
    }

    public void signup() {
        signupButton.click();
    }

    public void gotoLogin() {
        loginLink.click();
    }

    public void resetInputFields() {
        firstnameField.clear();
        lastnameField.clear();
        usernameField.clear();
        passwordField.clear();
    }
}
