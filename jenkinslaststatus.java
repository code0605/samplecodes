import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JenkinsStatusFetcher {
    public static void main(String[] args) {
        String jiraStoryUrl = "https://your-jira-instance/rest/api/2/issue/PROJECT-123";
        String jiraUsername = System.getenv("JIRA_USERNAME");
        String jiraPassword = readEncryptedPassword();

        String jenkinsUrl = fetchJenkinsUrlFromJira(jiraStoryUrl, jiraUsername, jiraPassword);
        String jenkinsStatus = fetchJenkinsStatus(jenkinsUrl);

        System.out.println("Jenkins Last Run Status: " + jenkinsStatus);
    }

    private static String fetchJenkinsUrlFromJira(String jiraStoryUrl, String username, String password) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(jiraStoryUrl);
            request.addHeader("Accept", "application/json");
            request.setHeader("Authorization", "Basic " + getEncodedCredentials(username, password));

            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);

            // Extract Jenkins URL from the Jira story description using regex
            Pattern pattern = Pattern.compile("(https?://[^\\s]+)");
            Matcher matcher = pattern.matcher(responseBody);
            if (matcher.find()) {
                return matcher.group(0);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String fetchJenkinsStatus(String jenkinsUrl) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(jenkinsUrl);
            request.addHeader("Accept", "application/json");

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();

            return String.valueOf(statusCode);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getEncodedCredentials(String username, String password) {
        String credentials = username + ":" + password;
        return java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    private static String readEncryptedPassword() {
        try {
            // Read the encrypted password from a configuration file
            byte[] encryptedPasswordBytes = Files.readAllBytes(Paths.get("encrypted_password.txt"));
            // Decrypt the password (implementation omitted)

            return "decrypted_password"; // Replace with your decryption logic

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
