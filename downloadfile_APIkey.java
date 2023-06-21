import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ServiceNowTaskAttachmentDownloader {
    public static void main(String[] args) {
        String serviceNowUrl = "https://your-instance.service-now.com/api/now/attachment";
        String apiKey = "your-api-key";
        String taskId = "task-id";
        String attachmentSysId = "attachment-sys-id";
        String destinationPath = "/path/to/save/file";

        try {
            URL url = new URL(serviceNowUrl + "?sysparm_query=table_name=task^table_sys_id=" + taskId +
                    "^sys_id=" + attachmentSysId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.setRequestProperty("Authorization", "Bearer " + apiKey);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(destinationPath);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();

                System.out.println("File downloaded successfully.");
            } else {
                System.out.println("Failed to download file. Response code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
