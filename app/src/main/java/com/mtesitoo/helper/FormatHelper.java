package com.mtesitoo.helper;

import android.text.Html;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by naily on 24/07/16.
 */
public class FormatHelper {

    private static final String dateFormat = "dd MMM yyyy";

    public static String formatDate(Date date)
    {
        return new SimpleDateFormat(dateFormat).format(date);
    }

    public static String formatPrice(String currency, double price)
    {
        return String.format("%s%,.2f", currency, price);
    }

    public static String formatDescription(String desc){
        return Html.fromHtml(desc).toString();
    }
}
