import com.codeborne.selenide.*;
import com.codeborne.selenide.testng.SoftAsserts;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.FileNotFoundException;

import static com.codeborne.selenide.AssertionMode.SOFT;
import static com.codeborne.selenide.ClickOptions.usingDefaultMethod;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Configuration.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.ofSeconds;

@Listeners({SoftAsserts.class})
public class SelenideTests {
    public SelenideTests(){
        Configuration.timeout=20000;
        Configuration.browser = "chrome";
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("start-maximized");
        Configuration.browserSize="1920x1080";
        baseUrl="http://the-internet.herokuapp.com";
        reportsFolder = "src/main/resources/Reports";
        downloadsFolder="src/main/resources/images";
    }

    @Test
    public void screenshot() {
        open(baseUrl);
        $(byText("sfgdhs")).click(usingDefaultMethod().timeout(ofSeconds(20)));
        sleep(4000);
    }

    @Test
    public void selniumVsSelenide() {
        //selenium
//        driver.get("https://www.lambdatest.com/selenium-playground/checkbox-demo");
//        WebElement checkBox = driver.findElement(By.id("isAgeSelected"));
//        checkBox.click();
//        System.out.println("Clicked on the Checkbox");
//
//        WebElement successMessage = driver.findElement(By.id("txtAge"));
//        String expectedMessage = successMessage.getText();
//        System.out.println(expectedMessage);
//        driver.quit();

        //selenide
        open("https://www.lambdatest.com/selenium-playground/checkbox-demo");
        $("#isAgeSelected").click();
        SelenideElement successText = $("#txtAge");
        //successText.should(appear);
        successText.shouldHave(exactText("Success - Check box is checked"));
        //uccessText.shouldHave(text(" - Check box is checked"));
        sleep(4000);
    }
    @Test
    public void selectors() {
        open("https://www.lambdatest.com/selenium-playground/checkbox-demo");
        $("#isAgeSelected").click();
        $(byText("Success - Check box is checked")).should(appear);
        $(withText("Check box is checked")).should(appear);
        //  sleep(4000);

    }
    @Test
    public void className() {
        open("https://demo.guru99.com/test/selenium-xpath.html");
        $(".thick-heading").shouldHave(exactText("Tutorials Library"));
        $(by("title", "SQL")).shouldNotBe(visible); //negative
        SelenideElement agile = $(byTitle("Agile Testing"));
        agile.shouldBe(visible);
        $(byValue("LOGIN")).shouldNotHave(exactValue("test"));
        SelenideElement test = $(byName("btnReset"));
        test.click();
    }
    @Test
    public void chainableSelectors(){
        open("/tables");
        System.out.println($(byTagName("tr"),2).$(byTagName("td"),3).getText());
        //same
        System.out.println($(byXpath("//*[@id=\"table2\"]/tbody/tr[2]/td[4]")).getText());

        $("#table1").find("tbody").$(byTagName("tr"),2).findAll("td")
                .forEach(el -> System.out.println(el.getText()));

        System.out.println($("#table1").findAll("tbody tr").size());

    }

    @Test
    public void basicCommandsAndTimeout() {
        open("/dynamic_controls");
        SelenideElement enableButton= $(By.cssSelector("#input-example button"));
        SelenideElement message =  $("#message");
        enableButton.click();
        System.out.println(message.getText());
        System.out.println(enableButton.getText());
        enableButton.shouldHave(text("Disable")); //timeout works
    }
    @Test
    public void keyPressesExample() {
        open("/key_presses");
        actions().sendKeys(Keys.ESCAPE).perform();
        sleep(4000);
        open("https://www.google.com");
        SelenideElement search = $(By.xpath("//input[@class='gLFyf gsfi']"));
        search.setValue("test automation");
        search.sendKeys(Keys.CONTROL, "A");
        sleep(3000);
        search.sendKeys(Keys.DELETE);
        sleep(3000);
    }

    @Test
    public void elementsCollection() {
        open("/add_remove_elements/");
        for (int i = 0; i <4 ; i++) {
            $(byText("Add Element")).click();
        }
        ElementsCollection deleteButtons = $$(By.cssSelector("#elements button"));
        System.out.println(deleteButtons.size());
        System.out.println(deleteButtons.first().getText());
        sleep(3000);
        deleteButtons.last().click();
        sleep(3000);
        $$(byText("Delete")).forEach(el -> el.click());
        sleep(3000);
    }
    @Test
    public void checkConditions() {
        open("/inputs");
        $(".example input").setValue("3");
        $(".example input").shouldBe(not(empty));
        sleep(3000);
    }
    @Test
    public void handleDropDowns(){
        open("/dropdown");
        $("#dropdown").selectOption("Option 1");
        $("#dropdown").getSelectedOption().shouldHave(matchText("ption 1"),value("1"));
    }

    @Test
    public void fileDownload() throws FileNotFoundException {
        open("/download");
        $(byText("name.png")).download();
    }

    @Test
    public void softAssert() {
        Configuration.assertionMode = SOFT;
        SoftAssert softAssert = new SoftAssert();
        open("https://demo.guru99.com/test/selenium-xpath.html");
        softAssert.assertEquals($(".thick-heading").getText(), "incorrect text");
        SelenideElement agile = $(byTitle("Agile Testing"));
        System.out.println($(".thick-heading").getText());
        agile.shouldBe(visible);
        softAssert.assertAll();
    }
}