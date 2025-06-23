package utils;

import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TranslationUtil {

    private static final String API_URL = "https://google-translate113.p.rapidapi.com/api/v1/translator/text";
    private static final String API_KEY = "85d2662c7dmsh2d4c5cc6d7d2cf6p1ec83djsn157de02d08e2";
    private static final String API_HOST = "google-translate113.p.rapidapi.com";

    public static String translateToEnglish(String spanishText) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("X-RapidAPI-Key", API_KEY);
            conn.setRequestProperty("X-RapidAPI-Host", API_HOST);
            conn.setDoOutput(true);

            String data = "from=es&to=en&text=" + URLEncoder.encode(spanishText, "UTF-8");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(data.getBytes(StandardCharsets.UTF_8));
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }

            JsonNode jsonNode = new ObjectMapper().readTree(response.toString());
            return jsonNode.path("trans").asText();
        } catch (Exception e) {
            System.out.println("Translation failed: " + e.getMessage());
            return "[Translation error]";
        }
    }
}
