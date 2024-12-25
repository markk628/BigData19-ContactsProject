package contactsProject1.lib;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Contacts {
	private HashMap<String, Contact> contacts;
	private Scanner scanner; 

	// 무엇을 입력받을지 여부하는 enum
	private enum Input {
		NAME("Name"),
		NUMBER("Number"),
		ADDRESS("Address"),
		RELATIONSHIP("Relationship"),
		OPTION("Option"),
		YESORNO("Yes or no");

		private final String name;

		private Input(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}

	// 생성자
	public Contacts(HashMap<String, Contact> contacts, Scanner scanner) {
		this.contacts = contacts != null ? contacts : new HashMap<>();
		this.scanner = scanner;
	}

	// 연락처 hashmap을 반환하느 method
	public HashMap<String, Contact> getContacts() {
		return this.contacts;
	}
	
	// 입력받은 문자열이 비어있으면 Exceptiond을 던지고 아니면 반환하는 method
	private String returnInputIfValid(Input option) throws Exception {
		String field = this.scanner.nextLine();
		if (field.isBlank() || field.isEmpty()) {
			throw new Exception(option.toString() + " can't be empty");
		}
		return field;
	}

	// 입력받은 번호의 길이가 11이 않이거나 문자가 포함되있으면 Exception을 던지고 아니면 반환하는 method
	private String returnNumberIfValid(String number) throws Exception {
		if ((number.length() != 11) || (!number.matches("\\d+"))) {
			throw new Exception("Enter 11 digits");
		}

		return number;
	}

	// 입력받은 관계가 "friend", "family", "etc"중 하나가 아니면 Exception을 던지고, 그중 하나이면 반환하는 method
	private String returnRelationshipIfValid(String relationship) throws Exception {
		switch (relationship.toLowerCase()) {
		case "friend", "family", "etc":
			break;
		default:
			throw new Exception("Relationship must be either family, friend, or etc");
		}
		return relationship;
	}

	// 입력받은 답장이 "yes", "no", "y", "n"중 하나가 아니면 Exception을 던지고, 그중 하나이면 반환하는 method
	private String returnYesOrNoIfValid(String answer) throws Exception {
		switch (answer.toLowerCase()) {
		case "yes", "no", "y", "n":
			return answer;
		default:
			throw new Exception("Enter yes/no/y/n");
		}
	}

	// Input enum case option을 이용해 어떤 method를 호출한지 정하고 호출한 method의 값을 반환하는 method
	private String handleInputException(Input option) {
		String input;
		try {
			input = this.returnInputIfValid(option);
			if (input.toLowerCase().equals("back")) {
				return input;
			}
			switch (option) {
			case NAME, ADDRESS, OPTION:
				return input;
			case NUMBER:
				return this.returnNumberIfValid(input);
			case RELATIONSHIP:
				return this.returnRelationshipIfValid(input);
			default:
				return this.returnYesOrNoIfValid(input);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.print("Enter " + option.toString().toLowerCase() + ": ");
			return this.handleInputException(option);
		}
	}
	
	// 입력받은 연락처의 관계 문자열에 해당되는 Relationship enum case를 반환하는 method
	private Relationship handleRelationshipException() {
		String relationship = this.handleInputException(Input.RELATIONSHIP);
		if (relationship.equals("back")) {
			return null;
		}
		return Relationship.relationship(relationship);
	}

	// 입력받은 정수 문자열을 정수로 변환하고 반환하는 method
	private int handleParseIntException(String number) {
		try {
			return Integer.parseInt(number != null ? number : this.scanner.nextLine());
		} catch (NumberFormatException e) {
			System.out.print("enter a NUMBER: ");
			return this.handleParseIntException(null);
		}
	}

	// 이름이 name인 연락처들을 뫃아 관계순으로 정렬시킨후 그중에서 사용자가 선택한 연락처를 반환하는 method
	private Contact getContact(String name) {
		List<Contact> contactsList = this.contacts
				.values()
				.stream()
				.filter(contact -> contact.getName().equals(name))
				.sorted(Comparator.comparing(Contact::getRelationship))
				.toList();		
		int nameCount = contactsList.size();
		int selection;
		String option;
		
		if (nameCount == 1) {
			return contactsList.get(0);
		} else if (contactsList.isEmpty()) {
			System.out.println(name + " is not in your contacts");
			return null;
		}
		System.out.println("There are " + nameCount + " people with the name " + name);
		System.out.println("Select the contact to update");
		for (int i = 0; i < nameCount; i++) {
			System.out.println((i + 1) + ". " + contactsList.get(i));
		}
		while (true) {
			System.out.print("Contact: ");
			option = handleInputException(Input.OPTION);
			if (option.equals("back")) {
				return null;
			}
			selection = this.handleParseIntException(option);
			if ((selection > 0) && (selection <= nameCount)) {
				return contactsList.get(selection - 1);
			} else {
				System.out.println(selection + " is not an option");
			}
		}
	}

	// 연락처 생성후 저장하는 method
	public void createContact() {
		String number;
		String name;
		String address;
		Relationship relationship;

		System.out.println("----------------------------------");
		System.out.println("          Create contact");
		System.out.println("----------------------------------");
		System.out.print("Number (ex. 01011112222): ");
		number = this.handleInputException(Input.NUMBER);
		if (number.equals("back")) {
			return;
		}
		// 이미 저장되어있는 번호면 저장되어있는 연락처를 수정하고싶냐고 물어본다
		if (this.contacts.containsKey(number)) {
			System.out.println("Contact with number already exists. Would you like to update the contact? (y/n): ");
			if (this.handleInputException(Input.YESORNO).equals("y")) {
				this.updateContact(number);
			}
			return;
		}
		System.out.print("Name: ");
		name = this.handleInputException(Input.NAME);
		if (name.equals("back")) {
			return;
		}
		System.out.print("Address: ");
		address = this.handleInputException(Input.ADDRESS);
		if (address.equals("back")) {
			return;
		}
		System.out.print("Relationship (family/friend/etc): ");
		relationship = this.handleRelationshipException();
		if (relationship == null) {
			return;
		}
		this.contacts.put(number, new Contact(number, name, address, relationship));
		System.out.println("********** Contact created successfully **********");
		System.out.println("----------------------------------");
	}

	// number에 해당되는 연라처의 정보를 출력하는 method
	private void readContact(String number) {
		Contact contact = this.contacts.get(number);

		System.out.println("Name: " + contact.getName());
		System.out.println("Number: " + contact.getNumber());
		System.out.println("Address: " + contact.getAddress());
		System.out.println("Relationship: " + contact.getRelationship());
	}

	// 입력받은 이름에 해당되는 연라처의 정보를 출력하는 method
	// 입력받은 이름을 이용해 getContact(String name)를 호출해서 Contact instance를 받고
	// 받은 Contact instance의 번호를 이용해 readContact(String number)를 호출
	public void readContact() {
		String name;
		Contact contact;
		
		System.out.print("Name of the contact to see: ");
		name = this.handleInputException(Input.NAME);
		if (name.equals("back")) {
			return;
		}
		contact = this.getContact(name);
		if (contact == null) {
			return;
		}
		this.readContact(contact.getNumber());
	}

	// 이름순으로 정렬한 연락처들의 정보를 호출하는 method
	public void readContacts() {
		List<String> numbers = this.contacts
				.values()
				.stream()
				.sorted(Comparator.comparing(Contact::getName))
				.map(contact -> contact.getNumber())
				.toList();
		
		System.out.println("You have " + contacts.size() + (contacts.size() != 1 ? " contacts" : " contact"));
		System.out.println("--------------------------------");
		System.out.println("            Contacts");
		System.out.println("--------------------------------");
		for (String number: numbers) {
			this.readContact(number);
			System.out.println("--------------------------------");
		}
	}

	// contactToUpdate의 이름을 수정하는 method
	private String updateConactName(Contact contactToUpdate) {
		String name;

		System.out.print("New name for " + contactToUpdate.getName() + ": ");
		name = this.handleInputException(Input.NAME);
		if (name.equals("back")) {
			return "back";
		}
		contactToUpdate.setName(name);
		return name;
	}

	// contactToUpdate의 번호를 수정하는 method
	private String updateConactNumber(Contact contactToUpdate) {
		String newNumber;
		String number = contactToUpdate.getNumber();

		System.out.print("New number for " + contactToUpdate.getName() + ": ");
		newNumber = this.handleInputException(Input.NUMBER);
		if (newNumber.equals("back")) {
			return "back";
		}
		contactToUpdate.setNumber(newNumber);
		this.contacts.put(newNumber, contactToUpdate);
		this.contacts.remove(number);
		return newNumber;
	}

	// contactToUpdate의 주소를 수정하는 method
	private String updateContactAddress(Contact contactToUpdate) {
		String address;

		System.out.print("New address for " + contactToUpdate.getName() + ": ");
		address = this.handleInputException(Input.ADDRESS);
		if (address.equals("back")) {
			return "back";
		}
		contactToUpdate.setAddress(address);
		return address;
	}

	// contactToUpdate의 관계를 수정하는 method	
	private String updateConactRelationship2(Contact contactToUpdate) {
		Relationship relationship;

		System.out.print("New relationship for " + contactToUpdate.getName() + ": ");
		relationship = this.handleRelationshipException();
		if (relationship == null) {
			return "back";
		}
		contactToUpdate.setRelationship(relationship);
		return relationship.toString();
	}

	// 입력받은 이름에 해당되는 연락처를 수정하는 method
	// 입력받은 이름을 이용해 getContact(String name)를 호출해서 Contact instance를 받고
	// 받은 Contact instance의 정보 수정
	public void updateContact() {
		Contact contactToUpdate;
		String name;
		String number;
		String address;
		String relationship;
		String[] fields;

		System.out.println("--------------------------------");
		System.out.println("             Update");
		System.out.println("--------------------------------");
		System.out.print("Name of the contact to update: ");
		name = this.handleInputException(Input.NAME);
		if (name.equals("back")) {
			return;
		} 
		contactToUpdate = this.getContact(name);
		if (contactToUpdate == null) {
			return;
		}
		System.out.println("Enter fields(s) to update (ex. 1, 3, 4)");
		System.out.println("1. Name");
		System.out.println("2. Number");
		System.out.println("3. Address");
		System.out.println("4. Relationship");
		System.out.print("Field(s): ");
		fields = this.handleInputException(Input.OPTION).replace(" ", "").split(",");
		for (String field: fields) {
			switch (field) {
			case "1":
				name = this.updateConactName(contactToUpdate);
				if (name.equals("back")) {
					return;
				}
				System.out.println("********** " + name + "'s name updated successfully **********");
				break;
			case "2":
				number = this.updateConactNumber(contactToUpdate);
				if (number.equals("back")) {
					return;
				}
				System.out.println("********** " + name + "'s number updated successfully **********");
				break;
			case "3":
				address = this.updateContactAddress(contactToUpdate);
				if (address.equals("back")) {
					return;
				}
				System.out.println("********** " + name + "'s address updated successfully **********");
				break;
			case "4":
				relationship = this.updateConactRelationship2(contactToUpdate);
				if (relationship.equals("back")) {
					return;
				}
				System.out.println("********** " + name + "'s relationship updated successfully **********");
				break;
			case "back":
				return;
			default:
				System.out.println("********** " + field + " is not an option **********");
			}
		}
		System.out.println("--------------------------------");
	}

	// 입력받은 번호에 해당되는 연락처의 이름, 주소, 그리고 관계를 수정하는 method
	// createContact()에 사용
	private void updateContact(String number) {
		Contact contactToUpdate = this.contacts.get(number);
		String name;
		String address;
		Relationship relationship;

		System.out.println("--------------------------------");
		System.out.println("             Update");
		System.out.println("--------------------------------");
		System.out.print("Name: ");
		name = this.handleInputException(Input.NAME);
		if (name.equals("back")) {
			return;
		}
		System.out.print("Address: ");
		address = this.handleInputException(Input.ADDRESS);
		if (address.equals("back")) {
			return;
		}
		System.out.print("Relationship (family/friend/etc): ");
		relationship = this.handleRelationshipException();
		if (relationship == null) {
			return;
		}
		contactToUpdate.setNumber(number);
		contactToUpdate.setName(name);
		contactToUpdate.setAddress(address);
		contactToUpdate.setRelationship(relationship);
		System.out.println("********** Contact updated successfully **********");
		System.out.println("--------------------------------");
	}

	// 연락처를 삭제하는 method
	public void deleteContact() {
		Contact contactToDelete;
		String name;

		System.out.println("--------------------------------");
		System.out.println("             Delete");
		System.out.println("--------------------------------");
		System.out.print("Name of the contact to delete: ");
		name = this.handleInputException(Input.NAME);
		if (name.equals("back")) {
			return;
		}
		contactToDelete = this.getContact(name);
		if (contactToDelete == null) {
			return;
		}
		this.contacts.remove(contactToDelete.getNumber());
		System.out.println("********* " + contactToDelete.getName() + " has been deleted **********");
		System.out.println("--------------------------------");
	}
}
