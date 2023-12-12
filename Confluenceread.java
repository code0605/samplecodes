import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConfluencePageReader {

    public static void main(String[] args) {
        // Confluence page URL
        String confluenceUrl = "YOUR_CONFLUENCE_PAGE_URL";

        try {
            // Fetch Confluence page content
            Document doc = Jsoup.connect(confluenceUrl).get();

            // Find the table containing dates (adjust this based on your Confluence page structure)
            Element table = doc.select("table").first();

            if (table != null) {
                // Extract dates from the table
                List<TaskEntry> taskEntries = extractDatesFromTable(table);

                // Print or process the extracted dates
                for (TaskEntry entry : taskEntries) {
                    System.out.println("Task: " + entry.getTaskName() + ", Due Date: " + entry.getDueDate());
                }
            } else {
                System.out.println("Table not found on the Confluence page.");
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static List<TaskEntry> extractDatesFromTable(Element table) throws ParseException {
        List<TaskEntry> taskEntries = new ArrayList<>();

        // Skip header row
        Elements rows = table.select("tr:gt(0)");

        for (Element row : rows) {
            Elements columns = row.select("td");

            String taskName = columns.get(0).text().trim();
            String dueDateStr = columns.get(1).text().trim();

            // Convert due date string to Date object (adjust date format if needed)
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dueDate = dateFormat.parse(dueDateStr);

            taskEntries.add(new TaskEntry(taskName, dueDate));
        }

        return taskEntries;
    }

    private static class TaskEntry {
        private final String taskName;
        private final Date dueDate;

        public TaskEntry(String taskName, Date dueDate) {
            this.taskName = taskName;
            this.dueDate = dueDate;
        }

        public String getTaskName() {
            return taskName;
        }

        public Date getDueDate() {
            return dueDate;
        }
    }
}
