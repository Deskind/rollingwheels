package deskind.com.rollingwheels.utils;

import java.text.DecimalFormat;

public class MyDateFormatter {
    public static String makeTwoNumbersDate(String d){
        String date = "";
        DecimalFormat formatter = new DecimalFormat("00");

        //split sDate value with "-"
        String[] dateParts = d.split("-");

        String month = formatter.format(Double.valueOf(dateParts[1]));
        String day = formatter.format(Double.valueOf(dateParts[2]));

        date = dateParts[0]+"-"+month+"-"+day;

        return date;
    }
}
