package com.mainway.skinnerbox.Access.Date;

import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class GetDate {
    public static String date(Context context){
        String dateS=new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(new Date());
        return dateS;
    }
    public static String getTomorrow() {
        String strFormat="yyyy-MM-dd";
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, 1);
        return new SimpleDateFormat(strFormat).format(cal.getTime());
    }

    public static String getNextDay(int day){
        String strFormat="yyyy-MM-dd";
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, day);
        return new SimpleDateFormat(strFormat).format(cal.getTime());
    }
    public static String nextDay(String date) throws ParseException {
        String strFormat="yyyy-MM-dd";
        Date myDate=new SimpleDateFormat(strFormat).parse(date);
        Calendar c = Calendar.getInstance();
        c.setTime(myDate);
        c.add(Calendar.DATE, 1);
        Date nextDate = c.getTime();
        Log.i("dayDate", "nextDay : "+ new SimpleDateFormat(strFormat).format(nextDate.getTime()));
        return new SimpleDateFormat(strFormat).format(nextDate.getTime());
    }

    public static List<String> dateList(){
        List<String> dates=new ArrayList<>();

        for (int i = 1; i <=14; i++) {
            dates.add(getNextDay(i));
        }
        return dates;
    }

}
