package contactsProject1.lib;

import java.util.List;
import java.util.Scanner;

public interface DataAccessObjectInterface {
	public void setContacts();
	public void setScanner(Scanner scanner);
	public void insert();
	public List<Contact> select(String name, boolean shouldPrintContact);
	public List<Contact> select();
	public void update();
	public void delete();
}
