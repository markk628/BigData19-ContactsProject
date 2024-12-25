package contactsProject1.app;

import java.util.Scanner;

import contactsProject1.lib.Contacts;
import contactsProject1.lib.ContactsFileReaderAndWriter;

public class ContactsApp {
	public static void main(String[] args) {
		ContactsFileReaderAndWriter contactsEditor = new ContactsFileReaderAndWriter();
		Scanner scanner = new Scanner(System.in);
		Contacts contacts = new Contacts(contactsEditor.getContacts(), scanner);
		String option;

		while (true) {
			System.out.println();
			System.out.println("============================");
			System.out.println("      Select an option");
			System.out.println("============================");
			System.out.println("1. create a contact");
			System.out.println("2. look up a contact");
			System.out.println("3. look up all contacts");
			System.out.println("4. update a contact");
			System.out.println("5. delete a contact");
			System.out.println("6. terminate the program");
			System.out.print("Option: ");
			option = scanner.nextLine();
			if (option.isEmpty() || option.isBlank()) {
				continue;
			}
			System.out.println();
			switch (option.charAt(0)) {
			case '1':
				contacts.createContact();
				break;
			case '2':
				contacts.readContact();
				break;
			case '3':
				contacts.readContacts();
				break;
			case '4':
				contacts.updateContact();
				break;
			case '5':
				contacts.deleteContact();
				break;
			case '6':
				System.out.println("Closing contacts app");
				contactsEditor.writeFile(contacts.getContacts());
				scanner.close();
				return;
			default:
				System.out.println(option + " is not an option");
			}
		}
	}

}
