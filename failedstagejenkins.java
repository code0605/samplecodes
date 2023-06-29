import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class JenkinsPipelineStatus {
    public static void main(String[] args) {
        String jenkinsUrl = "http://your-jenkins-url"; // Replace with your Jenkins URL
        String pipelineName = "your-pipeline-name"; // Replace with your pipeline name
        String buildNumber = "your-build-number"; // Replace with the build number of the failed pipeline run

        try {
            URL url = new URL(jenkinsUrl + "/job/" + pipelineName + "/" + buildNumber + "/wfapi/describe");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                Scanner scanner = new Scanner(conn.getInputStream());
                String jsonResponse = scanner.useDelimiter("\\A").next();
                scanner.close();

                // Parse the JSON response
                String failedStage = extractFailedStage(jsonResponse);
                System.out.println("Failed Stage: " + failedStage);
            } else {
                System.out.println("Failed to retrieve pipeline status. Response code: " + responseCode);
            }

            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String extractFailedStage(String jsonResponse) {
        Gson gson = new Gson();
        JsonObject root = gson.fromJson(jsonResponse, JsonObject.class);
        JsonArray stages = root.getAsJsonArray("stages");

        for (JsonElement stageElement : stages) {
            JsonObject stage = stageElement.getAsJsonObject();
            String status = stage.get("status").getAsString();

            if (status.equals("FAILED")) {
                String stageName = stage.get("name").getAsString();
                return stageName;
            }
        }

        return "Failed Stage Name not found";
    }
}
