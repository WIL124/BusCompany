package thumbtack.buscompany.model;

import java.time.DayOfWeek;

public enum Weekday {
    Sun(DayOfWeek.SUNDAY),
    Mon(DayOfWeek.MONDAY),
    Tue(DayOfWeek.TUESDAY),
    Wed(DayOfWeek.WEDNESDAY),
    Thu(DayOfWeek.THURSDAY),
    Fri(DayOfWeek.FRIDAY),
    Sat(DayOfWeek.SATURDAY);
    private final DayOfWeek dayOfWeek;

    Weekday(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }
}
