import org.jfrog.artifactory.client.Artifactory;
import org.jfrog.artifactory.client.ArtifactoryClientBuilder;

public class ArtifactoryConnection {

    public static void main(String[] args) {
        String artifactoryURL = "https://your-artifactory-url.com"; // Replace with your Artifactory URL
        String apiKey = "your-api-key"; // Replace with your API key

        // Create Artifactory connection object
        Artifactory artifactory = ArtifactoryClientBuilder.create()
                .setUrl(artifactoryURL)
                .setApiKey(apiKey)
                .build();

        // Use the artifactory object for further interactions with Artifactory
        // For example, you can list repositories, upload artifacts, etc.
    }
}
