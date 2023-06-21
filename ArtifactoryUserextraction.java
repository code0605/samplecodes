import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class ArtifactoryGroupUsersExtractor {
    private static final String ARTIFACTORY_URL = "https://your-artifactory-url";
    private static final String API_KEY = "your-api-key";

    public static void main(String[] args) {
        try {
            JSONArray groups = getAllGroups();
            if (groups != null) {
                for (int i = 0; i < groups.length(); i++) {
                    JSONObject group = groups.getJSONObject(i);
                    String groupName = group.getString("name");
                    JSONArray users = getUsersForGroup(groupName);
                    if (users != null) {
                        System.out.println("Group: " + groupName);
                        for (int j = 0; j < users.length(); j++) {
                            JSONObject user = users.getJSONObject(j);
                            String userName = user.getString("name");
                            String userEmail = user.getString("email");
                            System.out.println("User: " + userName);
                            System.out.println("Email: " + userEmail);
                            System.out.println();
                        }
                    }
                }
            } else {
                System.out.println("No groups found in Artifactory.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static JSONArray getAllGroups() throws Exception {
        String url = ARTIFACTORY_URL + "/api/security/groups";
        HttpUriRequest request = new HttpGet(url);
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + API_KEY);

        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            return new JSONArray(responseBody);
        }
        return null;
    }

    private static JSONArray getUsersForGroup(String groupName) throws Exception {
        String url = ARTIFACTORY_URL + "/api/security/users";
        HttpUriRequest request = new HttpGet(url);
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + API_KEY);

        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            JSONArray users = new JSONArray(responseBody);
            JSONArray usersInGroup = new JSONArray();
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                JSONArray groups = user.getJSONArray("groups");
                for (int j = 0; j < groups.length(); j++) {
                    String group = groups.getString(j);
                    if (group.equalsIgnoreCase(groupName)) {
                        usersInGroup.put(user);
                        break;
                    }
                }
            }
            return usersInGroup;
        }
        return null;
    }
}
