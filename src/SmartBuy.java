import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SmartBuy {
	public WebDriver driver;
	public double numberOfTry = 5;
	public int itemsInInventory = 1000;
	public SoftAssert softassert = new SoftAssert();

	@BeforeTest
	public void before_test() {

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://smartbuy-me.com/smartbuystore/en/");
		driver.manage().window().maximize();

	}

	@Test(priority = 1)
	public void add_SAMSUNG_50_inch_SMART() {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		for (int i = 0; i < numberOfTry; i++) {
			driver.findElement(By.xpath(
					"//*[@id=\"newtab-Featured\"]/div/div[1]/div/div/div/div[2]/div/div[3]/div[1]/div/div/form[1]/div[1]/button"))
					.click();
			driver.findElement(By.xpath("//*[@id=\"addToCartLayer\"]/a[2]")).click();

		}

	}

	@Test(priority = 2)
	public void check_the_price() throws InterruptedException {

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		Thread.sleep(1000);
		driver.findElement(By.className("mini-cart-link")).click();

		// --------------total Price----------
		String getTheTotalPrice = driver
				.findElement(By.xpath("//*[@id=\"cboxLoadedContent\"]/div/div/div[2]/div[1]/h4[2]")).getText();

		String the_updated_single_price = getTheTotalPrice.replace(".000 JOD", "");

		String updated = the_updated_single_price.trim();
		String the_updated_replace = updated.replace(",", ".");

		Double totalPrice = Double.parseDouble(the_updated_replace);
		System.out.println(totalPrice);

		// --------------total Price----------

		// -------------------total Quantity---------------------

		String getQuantity = driver.findElement(By.xpath("//*[@id=\"cboxLoadedContent\"]/div/div/ol/li/div[2]/div"))
				.getText();
		System.out.println(getQuantity);

		String replaceNumberOfQuantity = getQuantity.replace("Quantity: ", "");
		Double quantity = Double.parseDouble(replaceNumberOfQuantity);
		// -------------------total Quantity---------------------

		// ================== price per unit==================

		String pricePerUnit = driver.findElement(By.xpath("//*[@id=\"cboxLoadedContent\"]/div/div/ol/li/div[3]"))
				.getText();
		String split = pricePerUnit.replace("Total: ", "");
		String splitt = split.replace(" JOD", "");
		String updatPricePerUnit = splitt.replace(".000", "");
		Double updatedPricePerUnit = Double.parseDouble(updatPricePerUnit);
		double expectedResult = 1845.0;

		// ================== price per unit==================
		softassert.assertEquals(updatedPricePerUnit * quantity, expectedResult);

		softassert.assertAll();

	}

	@Test(priority = 3)
	public void add_all_khalatat_in_inventory() {

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(7));

		for (int i = 0; i < itemsInInventory; i++) {
			WebElement item = driver.findElement(By.xpath(
					"//*[@id=\"newtab-Featured\"]/div/div[1]/div/div/div/div[2]/div/div[3]/div[1]/div/div/form[1]/div[1]/button"));
			if (item.isDisplayed()) {
				item.click();
			}
			else {
				System.out.println("HI");
			}

			String sorryMsg = driver.findElement(By.xpath("//*[@id=\"addToCartLayer\"]/div[1]")).getText();

			if (sorryMsg.contains("Sorry")) {
				driver.findElement(By.xpath("//*[@id=\"addToCartLayer\"]/a[1]")).click();
			} else {
				driver.findElement(By.xpath("//*[@id=\"addToCartLayer\"]/a[2]")).click();
			}
		}

	}
}
