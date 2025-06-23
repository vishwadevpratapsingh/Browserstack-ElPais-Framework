package tests;

import base.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.SkipException;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.OpinionPage;

import org.testng.annotations.AfterSuite;
import utils.WordFrequencyAnalyzer;

import java.util.List;

public class ElPaisScraperTest extends BaseTest {

    @Test
    public void runScraperTest() {
        WebDriver driver = getDriver();

        try {
            // Step 1: Navigate to homepage
            driver.get("https://elpais.com");
            System.out.println("Navigated to El País: " + driver.getCurrentUrl());

            // Step 2: Try navigating to Opinión section
            HomePage homePage = new HomePage(driver);
            if (!homePage.goToOpinionSection()) {
                throw new SkipException("Cannot proceed: Opinión section not reachable.");
            }

            // Step 3: Extract top 5 article links
            OpinionPage opinionPage = new OpinionPage(driver);
            List<String> articleLinks = opinionPage.extractArticleLinks();
            if (articleLinks.isEmpty()) {
                throw new SkipException("No opinion articles found.");
            }

            // Step 4: Extract and display content for each article
            opinionPage.extractContentFromArticles(articleLinks);

        } catch (SkipException skip) {
            System.out.println("Skipping test: " + skip.getMessage());
            throw skip;

        } catch (Exception e) {
            System.out.println("Test failed due to unexpected error: " + e.getMessage());
            e.printStackTrace();
            assert false : "Test failed with exception: " + e.getMessage();
        }
    }
    
    @AfterSuite
    public void analyzeTitles() {
        WordFrequencyAnalyzer.analyzeFrequency(".");
    }
}
