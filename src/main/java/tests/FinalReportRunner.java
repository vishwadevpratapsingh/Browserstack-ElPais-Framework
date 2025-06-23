package tests;

import org.testng.annotations.AfterSuite;
import utils.WordFrequencyAnalyzer;

public class FinalReportRunner {

    private static boolean hasRun = false;

    @AfterSuite
    public void analyzeTitles() {
        if (!hasRun) {
            hasRun = true;
            WordFrequencyAnalyzer.analyzeFrequency(".");
        }
    }
}