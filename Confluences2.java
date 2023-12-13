import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SSOConfluenceTableExtractor {
    public static void main(String[] args) {
        try {
            // Provide credentials for SSO login
            String username = "your_username";
            String password = "your_password";

            // Set SSO login URL and Confluence page URL
            String ssoLoginUrl = "sso_login_page_url";
            String confluencePageUrl = "confluence_page_url";

            // Create a session to handle cookies and authentication
            Connection.Response loginForm = Jsoup.connect(ssoLoginUrl).method(Connection.Method.GET).execute();
            Document loginDoc = loginForm.parse();

            // Extract form data for login
            Element loginFormElement = loginDoc.select("form").first();
            Elements formElements = loginFormElement.select("input");

            // Prepare form data
            String csrfToken = loginForm.cookie("CSRF_TOKEN");
            String ltValue = formElements.select("[name=lt]").attr("value");
            String executionValue = formElements.select("[name=execution]").attr("value");

            // Simulate login
            Connection.Response loginResponse = Jsoup.connect(ssoLoginUrl)
                    .data("username", username)
                    .data("password", password)
                    .data("lt", ltValue)
                    .data("execution", executionValue)
                    .data("_eventId", "submit")
                    .data("submit", "Log In")
                    .cookie("CSRF_TOKEN", csrfToken)
                    .method(Connection.Method.POST)
                    .followRedirects(true)
                    .execute();

            // Use the authenticated session to access Confluence page
            Document confluencePage = Jsoup.connect(confluencePageUrl)
                    .cookies(loginResponse.cookies())
                    .get();

            // Extract table data
            Element table = confluencePage.select("table#your_table_id").first();
            if (table != null) {
                for (Element row : table.select("tr")) {
                    for (Element cell : row.select("td, th")) {
                        System.out.print(cell.text() + "\t");
                    }
                    System.out.println();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
