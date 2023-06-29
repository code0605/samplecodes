import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JenkinsLastBuildStatusExample {
    public static void main(String[] args) {
        String jenkinsUrl = "https://your-jenkins-instance";
        String apiToken = "your-api-token";

        try {
            // Create the URL object for the last build API endpoint
            URL url = new URL(jenkinsUrl + "/job/your-job-name/lastBuild/api/json");

            // Create the HTTP connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the authorization header with the API token
            String auth = "Bearer " + apiToken;
            connection.setRequestProperty("Authorization", auth);

            // Send the GET request
            connection.setRequestMethod("GET");

            // Read the response
            int responseCode = connection.getResponseCode();
            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            // Parse the JSON response
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Extract the build status from the response
            String buildStatus = ""; // Initialize the build status variable
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Parse the JSON response and extract the build result
                // Here, we are assuming the JSON response contains a field named "result" that holds the build status
                JSONObject jsonResponse = new JSONObject(response.toString());
                buildStatus = jsonResponse.getString("result");
            }

            System.out.println("Response Code: " + responseCode);
            System.out.println("Build Status: " + buildStatus);

            // Close the connection
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
