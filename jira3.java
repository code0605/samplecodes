import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class JiraQueryExample {
    public static void main(String[] args) {
        // Jira credentials
        String username = "your_username";
        String password = "your_password";
        
        // Construct the Jira API URL with the JQL query
        String jqlQuery = "project=TLC AND Summary ~\"jesd\" AND Status not in (close,open)";
        String apiUrl = "https://your-jira-instance.com/rest/api/2/search?jql=" + jqlQuery;
        
        try {
            // Create HttpClient with basic authentication
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setDefaultCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                    .build();
            
            // Create HTTP GET request
            HttpGet httpGet = new HttpGet(apiUrl);
            
            // Execute the request
            HttpResponse response = httpClient.execute(httpGet);
            
            // Get the response entity
            HttpEntity entity = response.getEntity();
            
            // Check if response is successful
            if (response.getStatusLine().getStatusCode() == 200 && entity != null) {
                // Convert response entity to string
                String responseBody = EntityUtils.toString(entity);
                System.out.println(responseBody);
            } else {
                System.out.println("Request failed with status code: " + response.getStatusLine().getStatusCode());
            }
            
            // Close the HttpClient
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
