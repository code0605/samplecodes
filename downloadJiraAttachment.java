import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class JiraAttachmentDownloader {
    public static void main(String[] args) {
        // Jira server URL and credentials
        String jiraUrl = "https://your-jira-instance.com";
        String username = "your-username";
        String password = "your-password";

        // Jira ticket key and attachment ID
        String ticketKey = "TICKET-123";
        String attachmentId = "10001";

        // Output file path for the attachment
        String outputFilePath = "path/to/save/attachment.txt";

        // Create HttpClient instance
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build();

        try {
            // Create attachment download URL
            String attachmentUrl = jiraUrl + "/rest/api/latest/issue/" + ticketKey + "/attachments/" + attachmentId;

            // Create HTTP GET request
            HttpGet httpGet = new HttpGet(attachmentUrl);

            // Execute the request
            HttpResponse response = httpClient.execute(httpGet);

            // Check if the request was successful
            if (response.getStatusLine().getStatusCode() == 200) {
                // Get the attachment content as an InputStream
                try (InputStream inputStream = response.getEntity().getContent()) {
                    // Save the attachment to a file
                    try (FileOutputStream outputStream = new FileOutputStream(new File(outputFilePath))) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                    System.out.println("Attachment downloaded successfully.");
                }
            } else {
                System.out.println("Failed to download attachment. Status code: " + response.getStatusLine().getStatusCode());
                System.out.println("Response: " + EntityUtils.toString(response.getEntity()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the HttpClient
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
