import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class JenkinsLastBuildNumber {
    public static void main(String[] args) {
        String jenkinsUrl = "http://your-jenkins-url"; // Replace with your Jenkins URL
        String pipelineName = "your-pipeline-name"; // Replace with your pipeline name

        try {
            URL url = new URL(jenkinsUrl + "/job/" + pipelineName + "/lastBuild/buildNumber");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                Scanner scanner = new Scanner(conn.getInputStream());
                String buildNumber = scanner.useDelimiter("\\A").next();
                scanner.close();
                
                System.out.println("Last Build Number: " + buildNumber);
            } else {
                System.out.println("Failed to retrieve last build number. Response code: " + responseCode);
            }

            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
