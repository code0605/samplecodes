import java.util.Iterator;
import org.apache.commons.lang3.ArrayUtils;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.SafeArray;
import com.jacob.com.Variant;

public class OutlookEmailAddressRetriever {
    public static void main(String[] args) {
        try {
            // Initialize Outlook Application
            ActiveXComponent outlook = new ActiveXComponent("Outlook.Application");
            Dispatch namespace = Dispatch.get(outlook, "GetNamespace").invoke("MAPI");

            // Get the Inbox folder
            Dispatch inbox = Dispatch.get(namespace, "GetDefaultFolder").invoke(6);

            // Get the items (emails) in the Inbox
            Dispatch items = Dispatch.get(inbox, "Items");

            // Iterate through the emails
            Iterator<Variant> emailIterator = new Variant(items).toDispatch().iterator();
            while (emailIterator.hasNext()) {
                Dispatch email = emailIterator.next().toDispatch();

                // Get the email sender's address
                String senderAddress = Dispatch.get(email, "SenderEmailAddress").toString();

                // Print the sender's email address
                System.out.println("Sender Email Address: " + senderAddress);
            }

            // Release COM objects
            emailIterator = null;
            inbox.safeRelease();
            namespace.safeRelease();
            outlook.safeRelease();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


dependencies {
    implementation group: 'net.sf.jacob-project', name: 'jacob', version: '1.20'
    implementation group: 'commons-lang', name: 'commons-lang', version: '2.6'
}
