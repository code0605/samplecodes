import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ArtifactoryUserSearch {

    public static void main(String[] args) {
        String artifactoryURL = "https://your-artifactory-url.com"; // Replace with your Artifactory URL
        String apiKey = "your-api-key"; // Replace with your API key
        String username = "username-to-search"; // Replace with the username you want to search

        try {
            String searchURL = String.format("%s/api/security/users/%s", artifactoryURL, username);
            URL url = new URL(searchURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                System.out.println("User exists in Artifactory.");
                System.out.println("Response: " + response.toString());
            } else if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                System.out.println("User does not exist in Artifactory.");
            } else {
                System.out.println("Failed to search for user. Response code: " + connection.getResponseCode());
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
