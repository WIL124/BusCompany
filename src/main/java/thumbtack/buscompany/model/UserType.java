package thumbtack.buscompany.model;

public enum UserType {
    ADMIN("ADMIN"), CLIENT("CLIENT");
    // REVU лишнее. Это просто toString
    // вот если бы нужно ыбло пояснение, тогда да. Но тут этого нет
    private final String value;

    UserType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
