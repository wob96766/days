package com.mindspree.days.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTime 
{
	public int year = 2000;
	public int month = 1;
	public int day = 1;
	
	public int hour = 0;
	public int minute = 0;
	public int second = 0;
	
	
	public DateTime()
	{

	}
	
	public DateTime(Date date)
	{
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH) + 1;
		day = cal.get(Calendar.DAY_OF_MONTH);
		
		hour = cal.get(Calendar.HOUR_OF_DAY);
		minute = cal.get(Calendar.MINUTE);
		second = 0;
	}
	
	
	public DateTime(String date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar cal = Calendar.getInstance();
	    try {
			cal.setTime(sdf.parse(date));
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH) + 1;
			day = cal.get(Calendar.DAY_OF_MONTH);
			
			hour = 0;
			minute = 0;
			second = 0;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
	}
	
	public DateTime(String date, boolean timeset)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		
		Calendar cal = Calendar.getInstance();
	    try {
			cal.setTime(sdf.parse(date));
			hour = cal.get(Calendar.HOUR_OF_DAY);
			minute = cal.get(Calendar.MINUTE);
			second = 0;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
	}

	public DateTime(String date, String time)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String serverFormat = String.format("%s %s", date, time);

		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(serverFormat));

			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH) + 1;
			day = cal.get(Calendar.DAY_OF_MONTH);

			hour = cal.get(Calendar.HOUR_OF_DAY);
			minute = cal.get(Calendar.MINUTE);
			second = 0;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DateTime(boolean isSet)
	{
		year = 2000;
		month = 1;
		day = 1;
		
		hour = 0;
		minute = 0;
		second = 0;
	}
	
	public boolean before(DateTime date)
	{
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date source = sdf.parse(getServerDateTimeFormat());
			Date dest = sdf.parse(date.getServerDateTimeFormat());
			return source.before(dest);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

    public boolean beforeDate(DateTime date)
    {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date source = sdf.parse(getServerDateFormat());
            Date dest = sdf.parse(date.getServerDateFormat());
            return source.before(dest);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
	
	public long datediff(DateTime date)
	{
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date source = sdf.parse(getServerDateFormat());
			Date dest = sdf.parse(date.getServerDateFormat());
			long diff = source.getTime() - dest.getTime();
		    return diff / (24 * 60 * 60 * 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public String getAppDateFormat()
	{
		return String.format("%d년 %d월 %d일" , year, month, day);
	}
	
	public String getServerDateFormat()
	{
		return String.format("%04d-%02d-%02d" , year, month, day);
	}

    public String getServerDateTimeFormat()
    {
        return String.format("%04d-%02d-%02d %02d:%02d" , year, month, day, hour, minute);
    }
	
	public String getAppTimeFormat()
	{
		return String.format("%d시 %d분" , hour, minute, second);
	}
	
	public String getServerTimeFormat()
	{
		return String.format("%02d:%02d" , hour, minute);
	}
}
