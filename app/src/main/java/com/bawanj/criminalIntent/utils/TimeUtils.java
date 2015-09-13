package com.bawanj.criminalIntent.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jeffreychou on 9/12/15.
 */
public class TimeUtils {

    public static String getCurDateYear(String srcTime){ // Sat Sep 12 22:56:37 GMT 2015

        Date date= null;
        SimpleDateFormat inFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT' yyyy", Locale.US);
        SimpleDateFormat outFormat= new SimpleDateFormat("EEEE',' MMM dd',' yyyy", Locale.US);

        try{
            date = inFormat.parse(srcTime);

        }catch (Exception e){

        }
        return outFormat.format(date);
    }
}
