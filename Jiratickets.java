import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class JiraTicketSearchExample {
    public static void main(String[] args) {
        String jiraServerUrl = "https://your-jira-server-url.com";
        String searchQuery = "summary ~ \"Specific Title\""; // Modify the search query as per your requirements

        String apiUrl = jiraServerUrl + "/rest/api/2/search?jql=" + searchQuery;

        // Create HTTP client
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // Create HTTP GET request
        HttpGet httpGet = new HttpGet(apiUrl);

        try {
            // Execute the request
            HttpResponse response = httpClient.execute(httpGet);

            // Get the response entity
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // Parse the response as JSON
                String jsonString = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(jsonString);

                // Get the array of issues
                JSONArray issues = json.getJSONArray("issues");

                // Process each issue
                for (int i = 0; i < issues.length(); i++) {
                    JSONObject issue = issues.getJSONObject(i);

                    // Extract the issue key and summary
                    String issueKey = issue.getString("key");
                    String summary = issue.getJSONObject("fields").getString("summary");

                    // Print the issue key and summary
                    System.out.println("Issue Key: " + issueKey);
                    System.out.println("Summary: " + summary);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the HTTP client
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
