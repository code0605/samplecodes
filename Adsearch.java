import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class LocalOutlookContactSearch {
    public static void main(String[] args) {
        // Create an Outlook Application instance
        ActiveXComponent outlookApp = new ActiveXComponent("Outlook.Application");

        try {
            // Get the NameSpace
            Dispatch namespace = Dispatch.get(outlookApp, "GetNamespace", "MAPI").toDispatch();

            // Specify the contact's name to search for
            String contactName = "John Doe"; // Replace with the contact's name

            // Get the Contacts folder
            Dispatch contactsFolder = Dispatch.call(namespace, "GetDefaultFolder", 10 /* OlDefaultFolders.olFolderContacts */).toDispatch();

            // Get the items (contacts) in the Contacts folder
            Dispatch items = Dispatch.get(contactsFolder, "Items").toDispatch();

            // Get the count of contacts
            int count = Dispatch.get(items, "Count").getInt();

            // Iterate through the contacts and search for the specified contact
            for (int i = 1; i <= count; i++) {
                Dispatch contact = Dispatch.call(items, "Item", i).toDispatch();
                String displayName = Dispatch.get(contact, "FullName").toString();

                if (displayName.equalsIgnoreCase(contactName)) {
                    System.out.println("Contact found: " + displayName);

                    // You can retrieve more details about the contact as needed
                    // For example: String email = Dispatch.get(contact, "Email1Address").toString();
                    // System.out.println("Email: " + email);

                    break; // Exit the loop if the contact is found
                }
            }

            // Release COM objects
            outlookApp.safeRelease();
            Dispatch.call(items, "Release");
            Dispatch.call(contactsFolder, "Release");
            Dispatch.call(namespace, "Release");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

