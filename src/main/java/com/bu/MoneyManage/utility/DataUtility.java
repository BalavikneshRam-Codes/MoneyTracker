package com.bu.MoneyManage.utility;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@Component
public class DataUtility {
    public static String noteDisplayFormat = "MM dd,yyyy";
    public static String noteTimeDisplayFormat = "hh:mm a";
    public static String noteDateRequestFormat = "MMM dd, yyyy hh:mm a";
    public static String notesListDateFormat = "MMM dd, EEEE";
    public static String noteLocalDateFormat = "yyyy-MM-dd";

    public String getFormatedDateFromDate(Date date, String format) {
        if (date == null || format == null)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        String formattedDate = sdf.format(date);
        return formattedDate.replace("AM", "am").replace("PM", "pm");
    }

    public Date convertStringToDate(String date, String time, String format) {
        if (date == null || format == null) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);

            if (time == null || time.trim().isEmpty()) {
                return sdf.parse(date);
            } else {
                String dateTime = (date + " " + time).toUpperCase();
                return sdf.parse(dateTime);
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getFormatedDateFromStringDate(LocalDate date, String StringDateFormat,String returnStringFormat) throws ParseException {
        if (date == null || StringDateFormat == null)
            return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(StringDateFormat, Locale.ENGLISH);
        SimpleDateFormat sdfStringFormat = new SimpleDateFormat(StringDateFormat, Locale.ENGLISH);
        String formattedDate = date.format(formatter);
        Date date_ = sdfStringFormat.parse(formattedDate);
        SimpleDateFormat sdfReturnFormat = new SimpleDateFormat(returnStringFormat, Locale.ENGLISH);
        return sdfReturnFormat.format(date_);
    }

}
