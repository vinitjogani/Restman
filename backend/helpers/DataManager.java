package backend.helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class DataManager {

    // Constants
    private static final String FILENAME = "log.txt";
    private final String[] WEEK_DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    // Instance variables
    private SimpleDateFormat formatter;
    private SimpleDateFormat weekdayFormatter;
    private Date start, end;

    /**
     * Constructs a DataManager with the given date range.
     *
     * @param start The start date for fetching data.
     * @param end   The end date for fetching data.
     */
    public DataManager(LocalDate start, LocalDate end) {
        formatter = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
        weekdayFormatter = new SimpleDateFormat("EEEE");
        this.start = asDate(start);
        this.end = asDate(end);
    }

    /**
     * Gets a LocalDate as a date.
     *
     * @param localDate The local date object to convert.
     * @return Returns the converted date object.
     */
    private Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Reads the file into a Map of dates to line parts.
     *
     * @return Returns a map of dates to line parts array.
     */
    private Map<Date, String[]> readLines() {
        Map<Date, String[]> lines = new HashMap<>();
        try {
            FileReader input = new FileReader(FILENAME);
            BufferedReader bufferedReader = new BufferedReader(input);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split("\\|");
                Date date;
                try {
                    if (data.length < 2) continue;
                    String stringDate = data[0].trim() + "/" + data[1].trim();
                    date = formatter.parse(stringDate);
                } catch (ParseException ignored) {
                    continue;
                }
                if (date.after(start) && date.before(end)) {
                    lines.put(date, data);
                }
            }
        } catch (IOException ignored) {
        }
        return lines;
    }

    /**
     * Parses consumed menu items in the date range and gives their numbers.
     *
     * @return Returns a map of menu item names, and their order frequencies.
     */
    public HashMap<String, Number> getConsumptionData() {
        HashMap<String, Integer> map = new HashMap<>();

        Map<Date, String[]> lines = readLines();
        for (Date date : lines.keySet()) {
            if (lines.get(date).length >= 6 && lines.get(date)[2].trim().equals("ORDER PLACED")) {
                String menuItem = lines.get(date)[5].replace("Ordered Item: ", "").trim();
                map.put(menuItem, map.getOrDefault(menuItem, 0) + 1);
            }
        }
        return new HashMap<>(map);
    }

    /**
     * Gets Hour to Average Customers at specified weekday.
     *
     * @param weekday The weekday to get statistics for.
     * @return Returns a map of hours to number of average customers received.
     */
    public HashMap<String, Number> getHourToCustomerAverage(String weekday) {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        SimpleDateFormat hourFormatter = new SimpleDateFormat("HH:00");
        for (int i = 0; i < 24; i++) map.put(String.format("%02d:00", i), 0);

        int numDays = calcNumDays(weekday);
        Map<Date, String[]> lines = readLines();
        for (Date date : lines.keySet()) {
            if (lines.get(date).length >= 4 && lines.get(date)[2].trim().equals("CUSTOMERS SEATED")) {
                if (weekdayFormatter.format(date).equals(weekday) || weekday.equals("ALL")) {
                    String hourString = hourFormatter.format(date);
                    int numCustomers = Integer.valueOf(lines.get(date)[3].trim().replace("Number of Customers: ",
                            ""));
                    map.put(hourString, map.getOrDefault(hourString, 0) +
                            numCustomers);
                }
            }
        }
        if (numDays > 0) {
            for (String k : map.keySet()) {
                map.put(k, map.get(k) / numDays);
            }
        }
        return new LinkedHashMap<>(map);
    }

    /**
     * Gets the Weekday to Customers count.
     *
     * @return Returns a map of weekdays to number of customers received.
     */
    public HashMap<String, Number> getWeekdayToCustomers() {
        HashMap<String, Integer> map = new HashMap<>();
        for (String WEEK_DAY : WEEK_DAYS) map.put(WEEK_DAY, 0);

        Map<Date, String[]> lines = readLines();
        for (Date date : lines.keySet()) {
            if (lines.get(date).length >= 4 && lines.get(date)[2].trim().equals("CUSTOMERS SEATED")) {
                String weekday = weekdayFormatter.format(date);
                String amountString = lines.get(date)[3].trim().replace("Number of Customers: ", "");
                Integer amount = Integer.valueOf(amountString);
                map.put(weekday, map.getOrDefault(weekday, 0) + amount);
            }
        }
        return new HashMap<>(map);
    }

    /**
     * Gets Weekday to Value of sales.
     *
     * @return Returns a map of weekdays to total sales.
     */
    public HashMap<String, Number> getWeekdayToSales() {
        HashMap<String, Double> map = new HashMap<>();
        for (String WEEK_DAY : WEEK_DAYS) map.put(WEEK_DAY, 0.0);

        Map<Date, String[]> lines = readLines();
        for (Date date : lines.keySet()) {
            if (lines.get(date).length >= 7 && lines.get(date)[2].trim().equals("ORDER DELIVERED")) {
                String weekday = weekdayFormatter.format(date);
                String amountString = lines.get(date)[6].trim().replace("Order Price: ", "");
                Double amount = Double.valueOf(amountString);
                map.put(weekday, map.getOrDefault(weekday, 0.0) + amount);
            }
        }

        return new HashMap<>(map);
    }

    /**
     * Helper method to calculate number of times a weekday occurs in the date range.
     *
     * @param weekDay The weekday to count.
     * @return Returns the number of times the weekday occurs in the date range.
     */
    private int calcNumDays(String weekDay) {
        Calendar cStart = Calendar.getInstance();
        Calendar cEnd = Calendar.getInstance();
        cStart.setTime(start);
        cEnd.setTime(end);

        int count = 0;
        if (weekDay.equals("ALL")) {
            for (Date date = cStart.getTime(); cStart.before(cEnd); cStart.add(Calendar.DATE, 1),
                    date = cStart.getTime()) {
                count += 1;
            }
        } else {
            for (Date date = cStart.getTime(); cStart.before(cEnd); cStart.add(Calendar.DATE, 1),
                    date = cStart.getTime()) {
                if (weekdayFormatter.format(date).equals(weekDay)) {
                    count += 1;
                }
            }
        }
        return count;
    }
}

