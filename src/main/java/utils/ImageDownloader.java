package utils;

import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageDownloader {

    public static final String IMAGE_FOLDER = "images";

    public static void downloadImage(String imageUrl, String fileNameBase) {
        try {
            if (imageUrl == null || imageUrl.isEmpty()) {
                System.out.println("No image URL provided.");
                return;
            }

            // Remove query parameters from image URL
            String cleanedUrl = imageUrl.split("\\?")[0];

            // Extract extension safely
            String extension = cleanedUrl.contains(".")
                    ? cleanedUrl.substring(cleanedUrl.lastIndexOf("."))
                    : ".jpg";

            // Sanitize file name (Windows-safe)
            String safeFileName = sanitize(fileNameBase) + extension;
            Files.createDirectories(Paths.get(IMAGE_FOLDER));

            String filePath = IMAGE_FOLDER + File.separator + safeFileName;

            // Download
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            try (InputStream in = connection.getInputStream();
                 OutputStream out = new FileOutputStream(filePath)) {

                byte[] buffer = new byte[2048];
                int bytesRead;

                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }

                System.out.println("Image saved: " + filePath);
            }

        } catch (Exception e) {
            System.out.println("Failed to download image: " + e.getMessage());
        }
    }
    // sanitize method to ensure file names are safe
    public static String sanitize(String input) {
        return input.replaceAll("[^a-zA-Z0-9-_]", "_");
    }
}
