import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class JiraApiExample {
    public static void main(String[] args) {
        String username = "your_username";
        String password = "your_password"; // Your Jira account password
        String jiraUrl = "https://your-jira-instance/rest/api/2/search";
        String summary = "your_summary";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String credentials = username + ":" + password;
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

            String jql = "summary ~ \"" + summary + "\" AND status = \"Open\"";
            String urlWithParams = jiraUrl + "?jql=" + jql + "&maxResults=100";

            HttpGet httpGet = new HttpGet(urlWithParams);
            httpGet.setHeader("Authorization", "Basic " + encodedCredentials);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                JSONObject jsonResponse = new JSONObject(responseBody);
                JSONArray issuesArray = jsonResponse.getJSONArray("issues");

                for (int i = 0; i < issuesArray.length(); i++) {
                    JSONObject issueObject = issuesArray.getJSONObject(i);
                    String key = issueObject.getString("key");
                    String issueSummary = issueObject.getJSONObject("fields").getString("summary");
                    String status = issueObject.getJSONObject("fields").getJSONObject("status").getString("name");

                    System.out.println("Issue Key: " + key);
                    System.out.println("Summary: " + issueSummary);
                    System.out.println("Status: " + status);
                    System.out.println("-".repeat(50));
                }
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
