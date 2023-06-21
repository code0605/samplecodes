import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

import java.net.URI;
import java.util.concurrent.ExecutionException;

public class JiraTicketExtractor {
    public static void main(String[] args) {
        // Jira server URL and credentials
        String jiraUrl = "https://your-jira-instance.com";
        String username = "your-username";
        String apiToken = "your-api-token";

        // Create JiraRestClient instance
        JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
        URI serverUri = URI.create(jiraUrl);
        JiraRestClient restClient = factory.createWithBasicHttpAuthentication(serverUri, username, apiToken);

        try {
            // JQL query to search for issues
            String jqlQuery = "summary ~ 'your-summary' AND status = 'your-status'";

            // Perform the search
            SearchResult searchResult = restClient.getSearchClient().searchJql(jqlQuery).get();

            // Process the search result
            for (Issue issue : searchResult.getIssues()) {
                String key = issue.getKey();
                String summary = issue.getSummary();
                String description = issue.getDescription();

                System.out.println("Key: " + key);
                System.out.println("Summary: " + summary);
                System.out.println("Description: " + description);
                System.out.println("--------------------------");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            // Close the JiraRestClient
            restClient.close();
        }
    }
}
