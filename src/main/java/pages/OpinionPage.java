package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.TranslationUtil;
import utils.WordFrequencyAnalyzer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.regex.Pattern;

public class OpinionPage {

	public final WebDriver driver;
	public final WebDriverWait wait;

	public OpinionPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	// Extracts top 5 article URLs from the opinion section
	public List<String> extractArticleLinks() {
		Set<String> uniqueLinks = new LinkedHashSet<>();
		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
					By.xpath("//a[contains(@href,'/opinion/')]")));

			List<WebElement> elements = driver.findElements(
					By.xpath("//a[contains(@href,'/opinion/')]"));

			Pattern articlePattern = Pattern.compile(".*/opinion/\\d{4}-\\d{2}-\\d{2}/.*");// Matches URLs like /opinion/2023-10-01/article-title

			for (WebElement el : elements) {
				try {
					String href = el.getAttribute("href");
					if (href != null && articlePattern.matcher(href).matches()) {
						uniqueLinks.add(href);
						if (uniqueLinks.size() == 5) break;
					}
				} catch (StaleElementReferenceException ignored) {}
			}
		} catch (Exception e) {
			System.out.println("Failed to extract links: " + e.getMessage());
		}

		List<String> result = new ArrayList<>(uniqueLinks);
		System.out.println("Found " + result.size() + " article URLs.");
		return result;
	}

	// Extracts translated title and preview for each article
	public void extractContentFromArticles(List<String> articleUrls) {
		List<String> translatedTitles = new ArrayList<>();

		for (String url : articleUrls) {
			System.out.println("\n Visiting Article: " + url);
			try {
				driver.navigate().to(url);

				if (pageIndicatesUnavailable()) {
					System.out.println("Skipping unavailable article.");
					continue;
				}

				String titleEs = getTitleWithRetries();
				String titleEn = TranslationUtil.translateToEnglish(titleEs);
				translatedTitles.add(titleEn);

				// Print titles in both languages
				System.out.println("Title (ES): " + titleEs);
				System.out.println("TITLE_EN: " + (titleEn.isBlank() ? "[Missing]" : titleEn));

				// Extract and print article content
				String preview = getArticlePreview();
				System.out.println("Content Preview: " + (preview.isBlank() ? "[No preview]" : preview));

				// Try to find and download cover image             
				try {
					WebElement metaImage = driver.findElement(By.xpath("//meta[@property='og:image']"));
					String imageUrl = metaImage.getAttribute("content");
					utils.ImageDownloader.downloadImage(imageUrl, titleEn);
				} catch (NoSuchElementException e) {
					System.out.println("No cover image found.");
				}


			} catch (Exception e) {
				System.out.println("Error processing article: " + e.getMessage());
			}
		}
		//To save titles to file for checking the frequency of words later
		WordFrequencyAnalyzer.saveTitlesToFile(translatedTitles);
	}

	// Retry logic for H1 title extraction
	public String getTitleWithRetries() {
		for (int attempt = 1; attempt <= 3; attempt++) {
			try {
				WebElement h1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));
				return h1.getText().trim();
			} catch (TimeoutException | NoSuchElementException e) {
				System.out.println("Title not found. Retrying... (" + attempt + ")");
			}
		}
		return "";
	}

	// Detects if the article content indicates 404/unavailable
	public boolean pageIndicatesUnavailable() {
		try {
			return driver.getPageSource().toLowerCase().contains("el contenido que busca no se encuentra disponible");
		} catch (Exception e) {
			return false;
		}
	}

	// Extracts a clean preview from elements, skipping short/cookie notices
	public String getArticlePreview() {
		try {
			List<WebElement> paragraphs = driver.findElements(By.tagName("p"));
			for (WebElement p : paragraphs) {
				String text = p.getText().trim();
				if (text.toLowerCase().contains("cookie") || text.length() < 50) continue;

				String[] words = text.split("\\s+");
				return words.length > 200
						? String.join(" ", Arrays.copyOfRange(words, 0, 200)) + "..."
								: text;// Return first 200 words or the full text if shorter
			}
		} catch (Exception ignored) {}
		return "";
	}

	// Writes translated titles to a temp file for post-run frequency analysis
	public void writeTitlesToFile(List<String> titles) {
		if (titles.isEmpty()) return;

		String fileName = "translated_titles_" + Thread.currentThread().getId() + ".txt";

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
			for (String title : titles) {
				writer.write(title);
				writer.newLine();
			}
			System.out.println("Titles written to " + fileName);
		} catch (IOException e) {
			System.out.println("Failed to write titles to file: " + e.getMessage());
		}
	}
}
