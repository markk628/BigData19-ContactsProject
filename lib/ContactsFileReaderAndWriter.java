package contactsProject1.lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class ContactsFileReaderAndWriter {
	private final String PATH = FilePath.path();
	private File contactsFile = new File(this.PATH);
	private DataAccessObjectInterface contacts;

	public ContactsFileReaderAndWriter() {
		this.contacts = this.readFileAndGetContacts();
	}

	public DataAccessObjectInterface getContacts(Scanner scanner) {
		if (this.contacts == null) {
			this.contacts = new Contacts(scanner);
		} else {
			this.contacts.setScanner(scanner);
		}
		this.contacts.setContacts();
		return this.contacts;
	}
	
	private DataAccessObjectInterface readFileAndGetContacts() {
		ObjectInputStream input = null;
		try {
			this.contactsFile.createNewFile();
			input = new ObjectInputStream(new FileInputStream(this.PATH));
			return (DataAccessObjectInterface)input.readObject();
		} catch (ClassCastException e) {
			System.out.println("ClassCastException caught while trying to cast object read from ObjectInputStream to HashMap<String, Contact> ContactsFileReaderAndWriter:readFile");
			System.out.println(e.getMessage());
		} catch (FileNotFoundException e) {
			System.out.println(this.PATH + " doesn't exist");
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("HashMap<String, Contact>) class doesn't exist");
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException caught in ContactsFileReaderAndWriter:readFile");
			System.out.println(e.getMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					System.out.println("IOException caught while trying to close ObjectInputStream stream ContactsFileReaderAndWriter:readFile");
					System.out.println(e.getMessage());
				}
			}
		}
		return null;
	}

	public void writeFile(DataAccessObjectInterface newContacts) {
		ObjectOutputStream output = null;
		try {
			output = new ObjectOutputStream(new FileOutputStream(this.PATH));
			output.writeObject(newContacts);
		} catch (FileNotFoundException e) {
			System.out.println(this.PATH + " doesn't exist");
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException caught in ContactsFileReaderAndWriter:writeFile");
			System.out.println(e.getMessage());
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					System.out.println("IOException caught while trying to close ObjectOutputStream stream ContactsFileReaderAndWriter:writeFile");
					System.out.println(e.getMessage());
				}
			}
		}
	}
}
