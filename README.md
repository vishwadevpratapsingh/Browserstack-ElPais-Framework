This is a Java-based automation framework built to *scrape articles* from the *Opinión* section of [El País](https://elpais.com/), translate titles to English, and analyze repeated words. It supports cross-browser/device execution via *BrowserStack* and runs tests in parallel using *TestNG*.

---

## Features

- Cross-browser testing with BrowserStack
- Mobile & desktop support (Android, iOS, Windows, macOS)
- Parallel test execution with TestNG
- Translates Spanish article titles to English using Google Translate API
- Logs top article links and previews
- Extracts and analyzes *frequently used words* in article titles
- Downloads cover image of article (if present)
- Automatically generates word frequency report after suite ends
- Saves translated titles to file for offline analysis

---

##  Tech Stack

| Tool / Language        | Purpose                              |
|------------------------|--------------------------------------|
| Java                   | Programming language                 |
| Selenium WebDriver     | Browser automation                   |
| TestNG                 | Test orchestration                   |
| BrowserStack           | Cross-browser & cross-device testing |
| Maven                  | Build & dependency management        |
| Git                    | Version control                      |
| RapidAPI               | Translation (ES → EN)                |

---

## Project Structure
BrowserStackSelenium/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ ├── base/
│ │ │ │ └── BaseTest.java
│ │ │ ├── pages/
│ │ │ │ ├── HomePage.java
│ │ │ │ ├── OpinionPage.java
│ │ │ │ └── ArticlePage.java
│ │ │ ├── tests/
│ │ │ │ ├── ElPaisScraperTest.java
│ │ │ │ └── FinalReportRunner.java
│ │ │ └── utils/
│ │ │ ├── ImageDownloader.java
│ │ │ ├── TranslationUtil.java
│ │ │ ├── WordFrequencyAnalyzer.java
│ │ │ ├── TextAnalysisUtil.java
│ │ │ └── PropertyReader.java
│ ├── resources/
│ │ ├── TranslatedTitles/
│ │ └── driver/
│ │ └── chromedriver.exe
│
├── testng-parallel.xml # Parallel test config
├── config.properties # BrowserStack credentials & config
├── images/ # Downloaded article images
└── pom.xml # Maven dependencies

HOW TO RUN
1. Clone the repository**  
2. Install dependencies**  
3. Set your BrowserStack credentials** in `config.properties`:
properties | browserstack.username=YOUR_USERNAME | browserstack.accesskey=YOUR_ACCESS_KEY
4. Right click on the testng-parallel.xml and run as TestNGSuite


