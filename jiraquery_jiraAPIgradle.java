import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

import java.net.URI;
import java.util.concurrent.ExecutionException;

public class JiraApiExample {
    public static void main(String[] args) {
        String jiraUrl = "https://your-jira-instance";
        String username = "your_username";
        String password = "your_password"; // Your Jira account password
        String summary = "your_summary";

        JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
        URI serverUri = URI.create(jiraUrl);
        JiraRestClient restClient = factory.createWithBasicHttpAuthentication(serverUri, username, password);

        SearchRestClient searchClient = restClient.getSearchClient();
        String jql = "summary ~ \"" + summary + "\" AND status = \"Open\"";

        try {
            SearchResult searchResult = searchClient.searchJql(jql, 100, 0, null).claim();
            for (BasicIssue basicIssue : searchResult.getIssues()) {
                Issue issue = restClient.getIssueClient().getIssue(basicIssue.getKey()).claim();
                System.out.println("Issue Key: " + issue.getKey());
                System.out.println("Summary: " + issue.getSummary());
                System.out.println("Status: " + issue.getStatus().getName());
                System.out.println("-".repeat(50));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            restClient.close();
        }
    }
}




//dependencies {
    // Other dependencies...

//    implementation group: 'com.atlassian.jira', name: 'jira-rest-java-client-core', version: '5.2.0' // Use the latest version
//    implementation group: 'com.atlassian.jira', name: 'jira-rest-java-client-plugin', version: '5.2.0' // Use the same version as above
//    implementation group: 'com.atlassian.util.concurrent', name: 'atlassian-util-concurrent', version: '2.4.1'
//}
