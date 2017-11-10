package com.mtesitoo.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by eduardodiaz on 22/10/2017.
 */

public class DateHelper {

    public static Date parseDate(String stringDate) {
        try {
            return DateFormat.getDateInstance(DateFormat.LONG).parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateToString(Date date) {
        return DateFormat.getDateInstance().format(date);
    }
}
