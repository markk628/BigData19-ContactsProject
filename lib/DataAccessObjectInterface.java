package contactsProject1.lib;

import java.util.HashMap;
import java.util.List;

public interface DataAccessObjectInterface {
	public HashMap<String, Contact>  getContacts();
	public void setContacts();
	public void insert();
	public List<Contact> select(String name, boolean shouldPrintContact);
	public List<Contact> select();
	public void update();
	public void delete();
}
