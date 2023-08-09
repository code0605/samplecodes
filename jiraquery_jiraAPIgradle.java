import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Status;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;

import java.net.URI;

public class JiraApiExample {
    public static void main(String[] args) {
        String jiraUrl = "https://your-jira-instance";
        String username = "your_username";
        String password = "your_password"; // Your Jira account password
        String summary = "your_summary";

        JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
        URI serverUri = URI.create(jiraUrl);
        JiraRestClient restClient = factory.createWithBasicHttpAuthentication(serverUri, username, password);

        String jql = "summary ~ \"" + summary + "\" AND status = \"Open\"";

        try {
            Promise<Iterable<Issue>> searchJqlPromise = restClient.getSearchClient().searchJql(jql, 100, 0, null);
            Iterable<Issue> issues = searchJqlPromise.claim();

            for (Issue issue : issues) {
                System.out.println("Issue Key: " + issue.getKey());
                System.out.println("Summary: " + issue.getSummary());
                Status status = issue.getStatus();
                System.out.println("Status: " + status.getName());
                System.out.println("-".repeat(50));
            }
        } catch (Exception e) {
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
//}
