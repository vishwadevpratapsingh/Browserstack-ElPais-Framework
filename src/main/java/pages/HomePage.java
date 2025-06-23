package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    public final WebDriver driver;
    public final WebDriverWait wait;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public boolean goToOpinionSection() {
        try {
            dismissCookieBanners();

            // Attempt desktop navigation
            if (tryClick(By.xpath("//a[contains(@href,'/opinion') or contains(text(),'Opinión')]"))) {
                return true;
            }

            System.out.println("Desktop navigation failed. Trying mobile menu...");

            // Attempt mobile fallback
            if (tryClick(By.cssSelector(".hamburger, .menu-icon, .nav-toggle"))) {
                Thread.sleep(1000); // allow menu to expand
                return tryClick(By.xpath("//a[contains(@href,'/opinion') or contains(text(),'Opinión')]"));
            }

        } catch (Exception e) {
            System.out.println("Navigation to 'Opinión' failed: " + e.getMessage());
        }

        System.out.println("Mobile fallback also failed.");
        return false;
    }

    public boolean tryClick(By locator) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
                scrollToView(element);
                element.click();
                return true;
            } catch (ElementClickInterceptedException | TimeoutException | StaleElementReferenceException e) {
                System.out.println("Click failed. Retrying (" + attempt + ")...");
            } catch (Exception ignored) {}
        }

        // JS fallback click
        try {
            WebElement element = driver.findElement(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            System.out.println("JS force click succeeded.");
            return true;
        } catch (Exception e) {
            System.out.println("JS force click failed: " + e.getMessage());
        }

        return false;
    }
    //Scrolls the page to bring the element into view
    public void scrollToView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        try {
            Thread.sleep(500); // allow time for scroll
        } catch (InterruptedException ignored) {}
    }
    // to dismiss cookie banners on the homepage
    public void dismissCookieBanners() {
        try {
            Thread.sleep(2000); // wait for modal to load
            String[] dismissXPaths = {
                "//button[contains(text(),'Rechazar') or contains(text(),'Reject')]",
                "//button[contains(text(),'Continuar sin aceptar')]",
                "//button[@id='didomi-notice-disagree-button']",
                "//span[contains(text(),'Rechazar')]"
            };// array of XPaths to try for dismissing cookie banners

            for (String xpath : dismissXPaths) {
                try {
                    WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
                    System.out.println("Cookie modal dismissed.");
                    return;
                } catch (TimeoutException ignored) {}
            }

            System.out.println("No cookie modal to dismiss.");
        } catch (Exception e) {
            System.out.println("Error dismissing cookie modal: " + e.getMessage());
        }
    }
}
