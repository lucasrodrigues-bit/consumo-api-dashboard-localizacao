package model.dto.Holiday;

import java.util.List;

// Espelha a estrutura aninhada da Calendarific: { "response": { "holidays": [...] } }
public class CalendarificResponseDTO {
    private ResponseWrapper response;

    public ResponseWrapper getResponse() { return response; }

    public static class ResponseWrapper {
        private List<CalendarificHolidayDTO> holidays;
        public List<CalendarificHolidayDTO> getHolidays() { return holidays; }
    }
}