package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import pages.RegistrationPage;

import java.util.Map;

import static io.qameta.allure.Allure.step;

@Tag("demoqa_m")
public class RegistrationTest {
    RegistrationPage registrationPage = new RegistrationPage();
    TestData testData = new TestData();

    @BeforeAll
    static void beforeAll() {
        Configuration.baseUrl = "https://demoqa.com";
        Configuration.browserSize = "1920x1080";
        Configuration.pageLoadStrategy = "eager";
        Configuration.remote = "https://user1:1234@selenoid.autotests.cloud/wd/hub";

        SelenideLogger.addListener("allure", new AllureSelenide());

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true
        ));
        Configuration.browserCapabilities = capabilities;

    }

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
    }

    @Test
    @Feature("Форма регистрации")
    @Story("Добавление информации о студенте")
    @Owner("a.moskotina")
    @Severity(SeverityLevel.BLOCKER)
    @Link(value = "Test case", url = "https://allure.autotests.cloud/")
    @DisplayName("Successful fill form registration")
    void successfulFillFormTest() {
        step("Открыть форму", () -> {
            registrationPage.openPage()
                    .deleteBanners();
        });
        step("Заполнить форму", () -> {
            registrationPage.setFirstName(testData.firstName)
                    .setLastName(testData.secondName)
                    .setUserEmail(testData.email)
                    .setGender(testData.gender)
                    .setUserNumber(testData.mobile)
                    .setDateOfBirth(testData.day, testData.month, testData.year)
                    .setSubjects(testData.symbol1, testData.subject1)
                    .setSubjects(testData.symbol2, testData.subject2)
                    .setHobbies(testData.hobbies1)
                    .setHobbies(testData.hobbies2)
                    .uploadPicture(testData.picture)
                    .setCurrentAddress(testData.address)
                    .setStateSelect(testData.state)
                    .setCitySelect(testData.city)
                    .submit();
        });
        step("Проверить результат", () -> {
            registrationPage.checkTitle()
                    .checkResult("Student Name", testData.firstName + " " + testData.secondName)
                    .checkResult("Student Email", testData.email)
                    .checkResult("Gender", testData.gender)
                    .checkResult("Mobile", testData.mobile)
                    .checkResult("Date of Birth", registrationPage.addZeroWithDateWithOneChar(testData.day) + " " + testData.month + "," + testData.year)
                    .checkResult("Subjects", testData.subject1 + ", " + testData.subject2)
                    .checkResult("Hobbies", testData.hobbies1 + ", " + testData.hobbies2)
                    .checkResult("Picture", testData.picture)
                    .checkResult("Address", testData.address)
                    .checkResult("Address", testData.address)
                    .checkResult("State and City", testData.state + " " + testData.city);
        });
    }
}
