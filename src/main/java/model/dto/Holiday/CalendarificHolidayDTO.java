package model.dto.Holiday;

public class CalendarificHolidayDTO {
    private String name;
    private DateInfo date;

    public String getName() { return name; }
    public DateInfo getDate() { return date; }

    public static class DateInfo {
        private String iso; // ex: "2026-12-25"
        public String getIso() { return iso; }
    }
}