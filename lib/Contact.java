package contactsProject1.lib;

import java.io.Serializable;

public class Contact implements Serializable {
	private static final long serialVersionUID = -2172188233643261933L;
	private String number;
	private String name;
	private String address;
	private Relationship relationship;
	
	public Contact(String number, String name, String address, Relationship relationship) {
		this.number = number;
		this.name = name;
		this.address = address;
		this.relationship = relationship;
	}
	
	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRelationship() {
		return this.relationship.toString();
	}

	public void setRelationship(Relationship relationship) {
		this.relationship = relationship;
	}
	
	@Override
	public String toString() {
		return "Name: " + this.name + ", Number: " + this.number + ", Address: " + this.address + ", Relationship: " + this.relationship;
	}
}
