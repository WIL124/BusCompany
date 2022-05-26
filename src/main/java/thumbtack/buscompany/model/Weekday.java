package thumbtack.buscompany.model;

public enum Weekday {
    Sun("Sun"),
    Mon("Mon"),
    Tue("Tue"),
    Wed("Wed"),
    Thu("Thu"),
    Fri("Fri"),
    Sat("Sat");
    private final String value;

    Weekday(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
