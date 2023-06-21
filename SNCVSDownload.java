import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServiceNowCSVDownloader {

    public static void main(String[] args) {
        String requestSysId = "YOUR_REQUEST_SYS_ID"; // Replace with the actual request sys_id
        String attachmentSysId = "YOUR_ATTACHMENT_SYS_ID"; // Replace with the actual attachment sys_id
        String username = "YOUR_USERNAME";
        String password = "YOUR_PASSWORD";

        String attachmentEndpoint = String.format(
            "https://YOUR_INSTANCE.service-now.com/api/now/table/sys_attachment/%s", attachmentSysId
        );

        try {
            URL url = new URL(attachmentEndpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/octet-stream");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", getBasicAuth(username, password));

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                String outputFilePath = String.format("%s.csv", requestSysId);
                OutputStream outputStream = new FileOutputStream(outputFilePath);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();

                System.out.println("CSV file downloaded successfully.");
            } else {
                System.out.println("Failed to download the CSV file. Response code: " + connection.getResponseCode());
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getBasicAuth(String username, String password) {
        String credentials = username + ":" + password;
        return "Basic " + java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}
