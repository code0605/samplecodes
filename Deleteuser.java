import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ArtifactoryUserDeletion {
    public static void main(String[] args) {
        String artifactoryUrl = "http://your-artifactory-url/api/security/users/{username}";
        String apiKey = "your-api-key";
        String usernameToDelete = "user-to-delete";

        try {
            URL url = new URL(artifactoryUrl.replace("{username}", usernameToDelete));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("X-JFrog-Art-Api", apiKey);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("Failed to delete user. Response code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
