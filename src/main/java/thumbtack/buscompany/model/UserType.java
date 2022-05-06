package thumbtack.buscompany.model;

public enum UserType {
    ADMIN("ADMIN"), CLIENT("CLIENT");
    private final String value;

    UserType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
