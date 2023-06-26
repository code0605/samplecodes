import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class JiraIssueLinkExample {
    public static void main(String[] args) {
        String jiraServerUrl = "https://your-jira-server-url.com";
        String ticketKey = "JIRA-123";

        String apiUrl = jiraServerUrl + "/rest/api/2/issue/" + ticketKey + "/link";

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

                // Get the array of issue links
                JSONArray issueLinks = json.getJSONArray("issueLinks");

                // Process each issue link
                for (int i = 0; i < issueLinks.length(); i++) {
                    JSONObject issueLink = issueLinks.getJSONObject(i);

                    // Extract the linked issue key
                    String linkedIssueKey = issueLink.getJSONObject("inwardIssue").getString("key");

                    // Print the linked issue key
                    System.out.println("Linked Issue Key: " + linkedIssueKey);
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
