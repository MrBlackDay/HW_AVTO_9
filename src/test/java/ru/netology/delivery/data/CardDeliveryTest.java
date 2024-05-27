package ru.netology.delivery.data;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class CardDeliveryTest {

    @BeforeAll
    static void setUpAll(){
        SelenideLogger.addListener("allure", new AllureSelenide());
        //AllureSelenide());
    }

    @AfterAll
    static void teaDownAll(){
        SelenideLogger.removeListener( "allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlaningMeeting() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        int addDaysForFirstMeeting = 4;
        String firstMeetDay = DataGenerator.generateDate(addDaysForFirstMeeting);
        int addDaysForSecondMeeting = 7;
        String secondMeetDay = DataGenerator.generateDate(addDaysForSecondMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME),Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetDay);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText ("Запланировать")).click();
        $(byText ("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id= success-notification] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + firstMeetDay))
                .shouldBe(visible);
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME),Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(secondMeetDay);
        $(byText ("Запланировать")).click();
        $("[data-test-id= 'replan-notification'] .notification__content")
                .shouldHave(exactText("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(visible);

        $("[data-test-id= 'replan-notification'] button").click();
        $("[data-test-id= 'success-notification'] . notification__content")// ошибка
                .shouldHave(exactText("Встреча успешно запланирована на " + secondMeetDay))
                .shouldBe(visible);

    }
}


