import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ArtifactoryUploader {

    private static final String ARTIFACTORY_URL = "https://your-artifactory-url/artifactory";
    private static final String API_KEY = "your-api-key";
    private static final String REPO_KEY = "your-repo-key";
    private static final String ARTIFACT_PATH = "/path/to/artifact/file";
    private static final String TARGET_FILENAME = "uploaded_artifact.jar";

    public static void main(String[] args) {
        try {
            uploadArtifact();
            System.out.println("Artifact uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to upload artifact.");
        }
    }

    private static void uploadArtifact() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // Create the file entity to upload
        Path artifactPath = Paths.get(ARTIFACT_PATH);
        File artifactFile = artifactPath.toFile();
        HttpEntity fileEntity = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.STRICT)
                .addBinaryBody("file", artifactFile, ContentType.DEFAULT_BINARY, TARGET_FILENAME)
                .build();

        // Create the HTTP POST request
        String uploadUrl = ARTIFACTORY_URL + "/" + REPO_KEY + "/" + TARGET_FILENAME;
        HttpPost httpPost = new HttpPost(uploadUrl);
        httpPost.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + API_KEY);
        httpPost.setEntity(fileEntity);

        // Execute the request
        HttpResponse response = httpClient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        HttpEntity responseEntity = response.getEntity();

        if (statusCode == 201) {
            // Artifact uploaded successfully
            EntityUtils.consumeQuietly(responseEntity);
        } else {
            // Error occurred
            String responseString = EntityUtils.toString(responseEntity);
            throw new IOException("Failed to upload artifact. Status code: " + statusCode + ", Response: " + responseString);
        }

        httpClient.close();
    }
}
