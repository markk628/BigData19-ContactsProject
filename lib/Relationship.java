package contactsProject1.lib;

public enum Relationship {
	FRIEND("friend"),
	FAMILY("family"),
	ETC("etc");

	private final String name;

	private Relationship(String name) {
		this.name = name;
	}
	
	public static Relationship relationship(String relationship) {
		switch (relationship.toLowerCase()) {
		case "family":
			return FAMILY;
		case "friend":
			return FRIEND;
		default:
			return ETC;
		}
	}

	@Override
	public String toString() {
		return this.name;
	}
}
