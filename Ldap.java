import java.io.FileInputStream;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUserChecker {

    public static void main(String[] args) {
        try {
            // Load Excel file
            FileInputStream excelFile = new FileInputStream("path/to/your/file.xlsx"); // Replace with your Excel file path
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(0); // Assuming the data is in the first sheet

            // Initialize LDAP context for Active Directory
            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldap://your-ldap-server:389"); // Replace with your LDAP server URL
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, "your-ldap-username"); // Replace with your LDAP username
            env.put(Context.SECURITY_CREDENTIALS, "your-ldap-password"); // Replace with your LDAP password

            DirContext context = new InitialDirContext(env);

            // Iterate through Excel rows
            for (Row row : sheet) {
                if (row.getCell(0) != null) {
                    String username = row.getCell(0).getStringCellValue(); // Assuming usernames are in the first column

                    // Check if the user exists in Active Directory
                    try {
                        Attributes attributes = context.getAttributes("CN=" + username); // Replace with your LDAP query
                        System.out.println(username + " exists in Active Directory");
                    } catch (Exception e) {
                        System.out.println(username + " does not exist in Active Directory");
                    }
                }
            }

            // Close resources
            context.close();
            workbook.close();
            excelFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
