package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ArticlePage extends BasePage {

	public By titleLocator = By.cssSelector("h1");

	//Adding Multiple locators in case the structure changes in the future (Array of locators)
	public By[] contentLocators = new By[]{
			By.cssSelector("section.a_c p"),
			By.cssSelector("div.a_c p"),
			By.cssSelector("div.article_body p"),
			By.cssSelector("article p")
	};

	public ArticlePage(WebDriver driver) {
		super(driver);
	}
	// getArticleTitle method to extract the article title
	public String getArticleTitle() {
		try {
			return wait.until(driver -> driver.findElement(titleLocator)).getText();
		} catch (Exception e) {
			System.out.println("Failed to extract title.");
			return "";
		}
	}
	// getArticleContent method to extract the article content
	public String getArticleContent() {
		try {
			for (By locator : contentLocators) {
				List<WebElement> paragraphs = driver.findElements(locator);

				if (paragraphs != null && !paragraphs.isEmpty()) {
					StringBuilder contentBuilder = new StringBuilder();
					for (WebElement para : paragraphs) {
						contentBuilder.append(para.getText()).append("\n");
					}
					return contentBuilder.toString().trim();
				}
			}

			System.out.println("No content found with added locators.");
			return "";

		} catch (Exception e) {
			System.out.println("Failed to extract content due to error.");
			return "";
		}
	}
	// getCoverImageUrl method to extract the cover image URL
	public String getCoverImageUrl() {
		try {
			WebElement img = driver.findElement(By.cssSelector("figure img"));
			return img.getAttribute("src");
		} catch (Exception e) {
			System.out.println("No cover image found.");
			return null;
		}
	}
}
