package thumbtack.buscompany.model;

import java.time.DayOfWeek;

public enum Weekday {
    Sun("Sun", DayOfWeek.SUNDAY),
    Mon("Mon", DayOfWeek.MONDAY),
    Tue("Tue", DayOfWeek.TUESDAY),
    Wed("Wed", DayOfWeek.WEDNESDAY),
    Thu("Thu", DayOfWeek.THURSDAY),
    Fri("Fri", DayOfWeek.FRIDAY),
    Sat("Sat", DayOfWeek.SATURDAY);
    private final String value;
    private final DayOfWeek dayOfWeek;

    Weekday(String value, DayOfWeek dayOfWeek) {
        this.value = value;
        this.dayOfWeek = dayOfWeek;
    }

    public String getValue() {
        return value;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }
}
