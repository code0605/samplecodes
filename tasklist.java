import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ServiceNowTaskList {
    public static void main(String[] args) {
        String serviceNowUrl = "https://your-instance.service-now.com/api/now/table/task";
        String username = "your-username";
        String password = "your-password";
        String shortDescription = "Your short description"; // Change this to the specific short description you want to search

        try {
            URL url = new URL(serviceNowUrl + "?sysparm_query=short_description=" + shortDescription);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            String credentials = username + ":" + password;
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
            connection.setRequestProperty("Authorization", "Basic " + encodedCredentials);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                System.out.println(response.toString()); // Process the response JSON as needed
            } else {
                System.out.println("Failed to retrieve tasks. Response code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
