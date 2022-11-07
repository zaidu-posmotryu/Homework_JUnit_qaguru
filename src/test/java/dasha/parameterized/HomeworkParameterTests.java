package dasha.parameterized;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selenide.*;


public class HomeworkParameterTests {
    @BeforeAll
    static void setUp() {
        Configuration.browserSize = "1920x1080";
        Configuration.holdBrowserOpen = true;
    }

    // Простой поиск с @ValueSource
    @ValueSource(strings = {"JavaScript", "Python"})
    @ParameterizedTest(name = "Простой поиск {0}")
    void simpleSearchValueS(String testData) {
        open("https://stepik.org/catalog");
        $(".search-form__input").setValue(testData);
        $(".search-form__submit").click();
        sleep(3000);
        $(".course-cards__item")
                .shouldHave(text(testData));
    }

    // Поиск со вводом данных с @CsvSource
    @CsvSource(value = {
            "Саратов, Москва, 30.11.2022, 15.12.2022, Этот билет можно купить по невозвратному тарифу"
    })
    @ParameterizedTest(name = "Поиск с вводом большего количества данных")
    void rzdSearchTickets(String from, String to, String departdate, String returndate, String comment) {
        open("https://www.rzd.ru/");
        $("#direction-from").setValue(from);
        sleep(4000);
        $("#direction-to").setValue(to);
        $("#datepicker-from").setValue(departdate);
        $("#datepicker-to").click();
        $("#datepicker-to").setValue(returndate);
        $(byClassName("rzd-go-to-result-button")).click();
        sleep(4000);
        $(".results-wrap").shouldHave(text(comment));
    }


    // Поиск со вводом данных с @MethodSource

    static Stream<Arguments> msuSiteMenuTextDataProvider() {
        return Stream.of(
                Arguments.of(List.of("Новости", "Объявления", "Пресс-служба", "Сайты МГУ", "Адреса", "Карта сайта", "Поиск"), Locale.Рус),
                Arguments.of(List.of("MSU Online", "Addresses", "Site map", "Search"), Locale.Eng)
        );
    }

    @MethodSource("msuSiteMenuTextDataProvider")
    @ParameterizedTest(name = "Проверка отображения пунктов меню для локали: {1}")
    void msuSiteButtonsText(List<String> menuTexts, Locale locale) {
        open("https://www.msu.ru/");
        $(byClassName(".lang-choose")).find(text(locale.name())).click();
        $$(".nav").filter(visible)
                .shouldHave(CollectionCondition.texts(menuTexts));
    }

}



