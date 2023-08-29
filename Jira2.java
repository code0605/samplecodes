import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class JiraQueryExample {
    public static void main(String[] args) {
        String jiraUrl = "https://your-jira-instance/rest/api/2/search?jql=your-query-here";
        String username = "your-username";
        String apiToken = "your-api-token"; // Or password if using Basic Auth

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(jiraUrl);
            request.addHeader("Authorization", "Basic " + Base64.getEncoder()
                                            .encodeToString((username + ":" + apiToken).getBytes()));

            HttpResponse response = httpClient.execute(request);
            String jsonResponse = EntityUtils.toString(response.getEntity());

            // Process jsonResponse here
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
