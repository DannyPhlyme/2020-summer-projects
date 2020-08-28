package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

    private static String firstname = "jane";
    private static String lastname = "doe";
    private static String username = "janedoe1234";
    private static String password = "password";

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private LoginPage loginPage;
    private SignupPage signupPage;
    private HomePage homePage;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void beforeEach() {
        this.driver = new ChromeDriver();
        loginPage = new LoginPage(driver);
        signupPage = new SignupPage(driver);
        homePage = new HomePage(driver);
    }

    @AfterEach
    void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        assertEquals("Login", driver.getTitle());
    }

    @Test
    @Order(10)
    void testLoginFailure() {
        WebDriverWait wait = new WebDriverWait(driver, 500);
        driver.get("http://localhost:" + this.port + "/login");
        loginPage.resetInputFields();
        loginPage.getUsernameField().sendKeys("mason");
        loginPage.getPasswordField().sendKeys("mount");
        loginPage.login();
        assertEquals("Invalid username or password", loginPage.getErrorMessageText());
    }

    @Test
    @Order(2)
    void testUnauthorizedAccess() {
        driver.get("http://localhost:" + this.port + "/login");
        assertEquals("Login", driver.getTitle());
        driver.get("http://localhost:" + this.port + "/signup");
        assertEquals("Sign Up", driver.getTitle());
        driver.get("http://localhost:" + this.port + "/home");
        assertEquals("Login", driver.getTitle());
    }

    @Test
    @Order(3)
    void testLoginLogoutPhase() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        driver.get("http://localhost:" + this.port + "/signup");

        signupPage.getFirstnameField().sendKeys(firstname);
        signupPage.getLastnameField().sendKeys(lastname);
        signupPage.getUsernameField().sendKeys(username);
        signupPage.getPasswordField().sendKeys(password);
        signupPage.getSignupButton().click();

        wait.until(ExpectedConditions.elementToBeClickable(signupPage.getLoginLink()));

        jse.executeScript("arguments[0].click()", signupPage.getLoginLink());

        wait.until(ExpectedConditions.elementToBeClickable(loginPage.getUsernameField()));
        wait.until(ExpectedConditions.elementToBeClickable(loginPage.getPasswordField()));

        loginPage.getUsernameField().sendKeys(username);
        loginPage.getPasswordField().sendKeys(password);
        jse.executeScript("arguments[0].click()", loginPage.getLoginButton());

        wait.until(ExpectedConditions.elementToBeClickable(homePage.getLogoutButton()));

        assertEquals("Home", driver.getTitle());
        jse.executeScript("arguments[0].click()", homePage.getLogoutButton());
        wait.until(ExpectedConditions.elementToBeClickable(loginPage.getLoginButton()));

        assertNotEquals("Home", driver.getTitle());
        Thread.sleep(5000);
    }

    @Test
    @Order(4)
    void testNoteCreation() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        driver.get("http://localhost:" + this.port + "/login");
        JavascriptExecutor jse = (JavascriptExecutor) driver;

        loginPage.getUsernameField().sendKeys(username);
        loginPage.getPasswordField().sendKeys(password);
        loginPage.getLoginButton().click();

        jse.executeScript("arguments[0].click()", homePage.getNotesTab());
        jse.executeScript("arguments[0].click()", homePage.getAddNoteButton());

        wait.until(ExpectedConditions.elementToBeClickable(homePage.getNoteTitleField()));
        wait.until(ExpectedConditions.elementToBeClickable(homePage.getNoteDescriptionField()));
        homePage.getNoteTitleField().sendKeys("Todo");
        homePage.getNoteDescriptionField().sendKeys("- Design models");
        homePage.getSaveNoteButton().click();

        jse.executeScript("arguments[0].click()", (homePage.getNotesTab()));

        WebElement notesTable = driver.findElement(By.id("userTable"));
        List<WebElement> noteList = notesTable.findElements(By.tagName("th"));
        boolean displayed = false;
        for (int i = 0; i < noteList.size(); i++) {
            WebElement row = noteList.get(i);
            if (!row.isDisplayed()) {
                jse.executeScript("arguments[0].scrollIntoView(true);", row);
            }
            if (row.getAttribute("innerHTML").equals("Todo")) {
                displayed = true;
                break;
            }
        }
        assertTrue(displayed);
        Thread.sleep(5000);
    }

    @Test
    @Order(5)
    void testNoteEditing() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        driver.get("http://localhost:" + this.port + "/login");
        JavascriptExecutor jse = (JavascriptExecutor) driver;

        loginPage.getUsernameField().sendKeys(username);
        loginPage.getPasswordField().sendKeys(password);
        loginPage.getLoginButton().click();

        jse.executeScript("arguments[0].click()", homePage.getNotesTab());
        jse.executeScript("arguments[0].click()", homePage.getEditNoteButton());

        wait.until(ExpectedConditions.elementToBeClickable(homePage.getNoteTitleField()));
        wait.until(ExpectedConditions.elementToBeClickable(homePage.getNoteDescriptionField()));

        homePage.clearNoteFields();
        homePage.getNoteTitleField().sendKeys("To-do");
        homePage.getNoteDescriptionField().sendKeys("- Implement services");
        homePage.getSaveNoteButton().click();

        jse.executeScript("arguments[0].click()", (homePage.getNotesTab()));

        List<WebElement> noteList = homePage.getNotesTable().findElements(By.tagName("th"));
        boolean displayed = false;
        for (int i = 0; i < noteList.size(); i++) {
            WebElement row = noteList.get(i);
            if (row.getAttribute("innerHTML").equals("To-do")) {
                displayed = true;
                break;
            }
        }
        assertTrue(displayed);
        Thread.sleep(5000);
    }

    @Test
    @Order(6)
    void testNoteDeletion() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        driver.get("http://localhost:" + this.port + "/login");
        JavascriptExecutor jse = (JavascriptExecutor) driver;

        loginPage.getUsernameField().sendKeys(username);
        loginPage.getPasswordField().sendKeys(password);
        loginPage.getLoginButton().click();

        wait.until(ExpectedConditions.elementToBeClickable(homePage.getNotesTab()));

        jse.executeScript("arguments[0].click()", homePage.getNotesTab());
        jse.executeScript("arguments[0].click()", homePage.getDeleteNoteButton());

        wait.until(ExpectedConditions.elementToBeClickable(homePage.getNotesTab()));

        jse.executeScript("arguments[0].click()", (homePage.getNotesTab()));

        boolean displayed = true;
        List<WebElement> noteList = homePage.getNotesTable().findElements(By.tagName("a"));
        for (int i = 0; i < noteList.size(); i++) {
            WebElement row = noteList.get(i);
            if (!ObjectUtils.isEmpty(row) && row.getAttribute("id").contains("delete-note")) {
                displayed = false;
            }
        }
        assertTrue(displayed);
        Thread.sleep(5000);
    }

    @Test
    @Order(7)
    void testCredentialCreation() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        driver.get("http://localhost:" + this.port + "/login");
        JavascriptExecutor jse = (JavascriptExecutor) driver;

        loginPage.getUsernameField().sendKeys(username);
        loginPage.getPasswordField().sendKeys(password);
        jse.executeScript("arguments[0].click()", loginPage.getLoginButton());

        wait.until(ExpectedConditions.elementToBeClickable(homePage.getCredentialsTab()));

        for (int i = 0; i < 3; i++) {
            jse.executeScript("arguments[0].click()", homePage.getCredentialsTab());
            jse.executeScript("arguments[0].click()", homePage.getAddCredentialButton());

            wait.until(ExpectedConditions.elementToBeClickable(homePage.getCredUrlField()));
            wait.until(ExpectedConditions.elementToBeClickable(homePage.getCredUsernameField()));
            wait.until(ExpectedConditions.elementToBeClickable(homePage.getCredPasswordField()));

            homePage.getCredUrlField().sendKeys("http://localhost:8080");
            homePage.getCredUsernameField().sendKeys(username);
            homePage.getCredPasswordField().sendKeys(password);
            homePage.getSaveCredentialButton().click();

            wait.until(ExpectedConditions.elementToBeClickable(homePage.getCredentialsTab()));

            jse.executeScript("arguments[0].click()", (homePage.getCredentialsTab()));
        }

        List<WebElement> credentialList = homePage.getCredentialsTable().findElements(By.tagName("th"));
        List<WebElement> credentialP = homePage.getCredentialsTable().findElements(By.tagName("td"));
        boolean displayed = false;
        boolean encrypted = false;
        for (int i = 0; i < credentialList.size(); i++) {
            WebElement row = credentialList.get(i);
            WebElement rowp = credentialP.get(i);
            if (!row.isDisplayed() && !rowp.isDisplayed()) {
                jse.executeScript("arguments[0].scrollIntoView(true);", row);
                jse.executeScript("arguments[0].scrollIntoView(true);", rowp);
            }
            if (row.getAttribute("innerHTML").equals("http://localhost:8080") && !rowp.getAttribute("innerHTML").equals(password)) {
                displayed = true;
                encrypted = true;
                break;
            }
        }
        assertTrue(displayed);
        assertTrue(encrypted);
        Thread.sleep(5000);
    }

    @Test
    @Order(8)
    void testCredentialEditing() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        driver.get("http://localhost:" + this.port + "/login");

        loginPage.getUsernameField().sendKeys(username);
        loginPage.getPasswordField().sendKeys(password);
        jse.executeScript("arguments[0].click()", loginPage.getLoginButton());

        wait.until(ExpectedConditions.elementToBeClickable(homePage.getCredentialsTab()));
        jse.executeScript("arguments[0].click()", (homePage.getCredentialsTab()));

        String editButton = "//*[@id=\"edit-credential\"]";
        List<WebElement> editButtons = driver.findElements(By.xpath(editButton));
        System.out.println(editButtons.size());

        for (int i = 0; i < editButtons.size(); i++) {
            wait.until(ExpectedConditions.elementToBeClickable(editButtons.get(0)));
            editButtons.get(i).click();
            assertEquals(password, homePage.getEditCredPasswordField().getAttribute("value"));
            wait.until(ExpectedConditions.elementToBeClickable(homePage.getCloseEditCredentialButton()));
            homePage.getCloseEditCredentialButton().click();
        }

        for (int i = 0; i < editButtons.size(); i++) {
            wait.until(ExpectedConditions.elementToBeClickable(homePage.getAddCredentialButton()));
            List<WebElement> editButtons1 = driver.findElements(By.xpath(editButton));
            editButtons1.get(i).click();
            wait.until(ExpectedConditions.elementToBeClickable(homePage.getEditCredUrlField()));
            homePage.clearEditCredentialField();
            homePage.getEditCredUrlField().sendKeys("http://localhost");
            homePage.getEditCredUsernameField().sendKeys("jannie");
            homePage.getEditCredPasswordField().sendKeys("password1234");
            homePage.getSaveEditCredentialButton().click();
            wait.until(ExpectedConditions.elementToBeClickable(homePage.getCredentialsTab()));
            jse.executeScript("arguments[0].click()", (homePage.getCredentialsTab()));
        }

        List<WebElement> credentialList = homePage.getCredentialsTable().findElements(By.tagName("th"));
        boolean displayed = false;
        for (int i = 0; i < credentialList.size(); i++) {
            WebElement row = credentialList.get(i);
            if (!row.isDisplayed()) {
                jse.executeScript("arguments[0].scrollIntoView(true);", row);
            }
            if (row.getAttribute("innerHTML").equals("http://localhost")) {
                displayed = true;
                break;
            }
        }
        assertTrue(displayed);
        Thread.sleep(5000);
    }

    @Test
    @Order(9)
    void testCredentialDeletion() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        driver.get("http://localhost:" + this.port + "/login");

        loginPage.getUsernameField().sendKeys(username);
        loginPage.getPasswordField().sendKeys(password);
        jse.executeScript("arguments[0].click()", loginPage.getLoginButton());

        wait.until(ExpectedConditions.elementToBeClickable(homePage.getCredentialsTab()));

        jse.executeScript("arguments[0].click()", (homePage.getCredentialsTab()));

        String editButton = "//*[@id=\"edit-credential\"]";
        List<WebElement> editButtons = driver.findElements(By.xpath(editButton));

        for (int i = 0; i < editButtons.size(); i++) {
            wait.until(ExpectedConditions.elementToBeClickable(homePage.getDeleteCredentialButton()));
            jse.executeScript("arguments[0].click()", (homePage.getDeleteCredentialButton()));
            wait.until(ExpectedConditions.elementToBeClickable(homePage.getCredentialsTab()));
            jse.executeScript("arguments[0].click()", (homePage.getCredentialsTab()));
        }

        boolean displayed = true;
        List<WebElement> credentialList = homePage.getCredentialsTable().findElements(By.tagName("a"));
        for (int i = 0; i < credentialList.size(); i++) {
            WebElement row = credentialList.get(i);
            if (!ObjectUtils.isEmpty(row) && row.getAttribute("id").contains("delete-credential")) {
                displayed = false;
            }
        }
        assertTrue(displayed);
        Thread.sleep(5000);
    }
}
