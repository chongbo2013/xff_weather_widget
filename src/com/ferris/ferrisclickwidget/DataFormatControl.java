package com.ferris.ferrisclickwidget;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
public class DataFormatControl {

	/**
	 * �����ʽΪhh:m ��h:mm ��h:m ��ʱ�䣬����hh:mm��ʽ��ʱ��
	 * @param time
	 * @return hh:mm��ʽ��ʱ��
	 */
	public final static String Pad(String time) {
		if (time.length() > 5) {
			time = time.substring(time.indexOf(" ", 0) + 1, time
					.lastIndexOf(":"));
		}
		if (time.indexOf(":", 0) == 1)
			time = "0" + time;
		if (time.length() - time.indexOf(":", 0) == 2)
			time = time.substring(0, time.indexOf(":", 0) + 1) + "0"
					+ time.substring(time.indexOf(":", 0) + 1);
		return time;
	}
	
	/**
	 * �����ֱ�Ϊ��λ�����ϣ���С��10��ǰ�油0
	 * @param num 
	 * @return ��λ���λ������
	 */
	public final static String Pad(int num) {
		if (num < 10)
			return "0" + num;
		else
			return "" + num;
	}
	
	/**
	 * ȡ������ĳһ��Ŀ�ʼʱ�䣬��Calendar��ʽʱ��תΪ"yyyy:MM:dd 00:00:00"��ʽ
	 * @param now Calendar��ʽ��ʱ��
	 * @return "yyyy:MM:dd 00:00:00"��ʽ��ʱ��
	 */
	public final static String GetDateAtBeginString(Calendar now) {
    	return now.get(Calendar.YEAR)+"-"+Pad(now.get(Calendar.MONTH)+1)+"-"+Pad(now.get(Calendar.DAY_OF_MONTH))+" "+"00:00:00";
	}
	
	/**
	 * ȡ��ũ��ĳһ��Ŀ�ʼʱ�䣬��LunarItem��ʽʱ��תΪ"yyyy:MM:dd 00:00:00"��ʽ
	 * @param now Calendar��ʽ��ʱ��
	 * @return "yyyy:MM:dd 00:00:00"��ʽ��ʱ��
	 */
	public final static String GetDateAtBeginString(LunarItem now) {
    	return now.getYear()+"-"+Pad(now.getMonth()+1)+"-"+Pad(now.getDay())+" "+"00:00:00";
	}
	
//	public final static String GetDateString(Calendar now) {
//    	return now.get(Calendar.YEAR)+"-"+Pad(now.get(Calendar.MONTH)+1)+"-"+Pad(now.get(Calendar.DAY_OF_MONTH))+" "+Pad(now.get(Calendar.HOUR))+":"+Pad(now.get(Calendar.MINUTE))+":00";
//	}
//	
//	public final static String GetDateString(LunarItem now) {
//    	return now.getYear()+"-"+Pad(now.getMonth()+1)+"-"+Pad(now.getDay())+" "+"00:00:00";
//	}
//	
	
	/**
	 * ȡ���������ڼ����ַ���
	 * @param now ���ܵĵ�һ��Ϊ������ĵĸ�ʽ
	 * @return ����"�ܼ�"
	 */
	public final static String DayOfWeekDisplay(int date) {
		String[] chineseNum = new String[]{"","��","һ","��","��","��","��","��"};
		return "��"+chineseNum[date];
	}
	
    public final static String DayOfWeekDisplayInternation(Context context,String date) {
        String[] chineseNum = new String[]{context.getString(R.string.zhouri),context.getString(R.string.zhouyi),context.getString(R.string.zhouer),context.getString(R.string.zhousan),context.getString(R.string.zhousi),context.getString(R.string.zhouwu),context.getString(R.string.zhouliu)};
        Integer[] resourceIds=new Integer[]{R.string.sun,R.string.mon,R.string.tue,R.string.wed,R.string.thu,R.string.fri,R.string.sat};
        for(int i=0;i<chineseNum.length;i++){
            if(date!=null&&chineseNum[i].equals(date)){
                return context.getString(resourceIds[i]);
            }
        }
        return "";
    }
    public final static String DayOfWeekDisplayInternationFull(Context context,String date) {
        String[] chineseNum = new String[]{context.getString(R.string.zhouri),context.getString(R.string.zhouyi),context.getString(R.string.zhouer),context.getString(R.string.zhousan),context.getString(R.string.zhousi),context.getString(R.string.zhouwu),context.getString(R.string.zhouliu)};
        Integer[] resourceIds=new Integer[]{R.string.sunday,R.string.monday,R.string.tuesday,R.string.wednesday,R.string.thursday,R.string.friday,R.string.saturday};
        for(int i=0;i<chineseNum.length;i++){
            if(date!=null&&chineseNum[i].equals(date)){
                return context.getString(resourceIds[i]);
            }
        }
        return "";
    }
	
	/**
	 * ȡ���������ڼ����ַ���
	 * @param now ���ܵĵ�һ��Ϊ������ĵĸ�ʽ
	 * @return ����"���ڼ�"
	 */
	public final static String DayOfXingqiDisplay(int date) {
		String[] chineseNum = new String[]{"","��","һ","��","��","��","��","��"};
		return "����"+chineseNum[date];
	}
	
	/**
	 * ������ܼ��������ַ���ת��Ϊ�����ַ���
	 * @param str �����ܼ����ַ��������ܵĵ�һ��Ϊ������ĵĸ�ʽ�����ŷָ�
	 * @return �����ַ��������ŷָ�
	 */
	public static String WeekOfDayDisplay(String str) {
		if (str == null)
			return "";
		String[] chineseNum = new String[] { "", "��", "һ", "��", "��", "��", "��",
				"��" };
		String value = "";

		for (int i = 0; i < str.length(); i += 2) {
			if (i > 0)
				value += ",";
			value += chineseNum[str.charAt(i) - '0'];
		}
		return value;
	}
	
	/**
	 * ��������Ϊ��һ����ַ�����Ϊ��һΪ��һ����ַ���  1,2,3 -> 7,1,2
	 * @param str 
	 * @return
	 */
	public final static String padDayOfWeekCtoS(String str) {
		if (str == null)
			return "";
    	String value="";
    	for (int i=0; i<str.length(); i++){
    		if (str.charAt(i)==',')
    			value+=",";
    		else
    			if (str.charAt(i)=='1')
    				value+="7";
    			else
    				value+=str.charAt(i)-'1';
    	}
    	return value;
    }
	
	/**
	 * ���"yyyy:MM:dd DD:mm:ss"��ʽ��ʱ�䴮�е����ֵ
	 * @param time
	 * @return
	 */
	public final static int getYear(String time) {
    	return Integer.parseInt(time.substring(0,4));
    }
    
	/**
	 * ���"yyyy:MM:dd DD:mm:ss"��ʽ��ʱ�䴮�е��·�ֵ
	 * @param time
	 * @return 1-12���·�ֵ
	 */
	public final static int getMonth(String time) {
    	return Integer.parseInt(time.substring(5,7));
    }
    
	/**
	 * ���"yyyy:MM:dd DD:mm:ss"��ʽ��ʱ�䴮�е�����ֵ
	 * @param time
	 * @return
	 */
	public final static int getDay(String time) {
		return Integer.parseInt(time.substring(8,10));
	}
	
	/**
	 * ���"yyyy:MM:dd DD:mm:ss"��ʽ��ʱ�䴮�е�Сʱֵ
	 * @param time
	 * @return 24Сʱֵ
	 */
	public final static int getHour(String time) {
		return Integer.parseInt(time.substring(11,13));
	}
	
	/**
	 * ���"yyyy:MM:dd DD:mm:ss"��ʽ��ʱ�䴮�еķ���ֵ
	 * @param time
	 * @return 
	 */
	public final static int getMinute(String time) {
		return Integer.parseInt(time.substring(14,16));
	}
    
	/**
	 * ����ʱ��time���ж����Ƿ����ڴ�������£������ڴ�������£�����0�������ڴ�������£�����32�������ڴ�������£���������
	 * @param time
	 * @param month
	 * @param year
	 * @return
	 */
	public final static int getDayInMonth (String time, int month, int year){
    	if (getYear(time)*100+getMonth(time) < year*100+month)
    		return 0;
    	else if (getYear(time)*100+getMonth(time) > year*100+month)
    		return 32;
    	else	
    		return Integer.parseInt(time.substring(8,10));
    }
    
	/**
	 * ����ũ��ʱ��time���ж����Ƿ����ڴ���Ĺ�������
	 * @param time
	 * @param month
	 * @param year
	 * @return �����ڴ�������£�����0�������ڴ�������£�����32�������ڴ�������£���������
	 */
	public final static int getLunarBeginDay (String time, LunarItem begin, LunarItem end, int max){
    	long timeInt = getYear(time)*10000+getMonth(time)*100+getDay(time);
    	//System.out.println("The time is: " + timeInt + " " + begin.getYear()*10000+(begin.getMonth()+1)*100+begin.getDay());
    	if (timeInt < begin.getYear()*10000+(begin.getMonth()+1)*100+begin.getDay())
    		return 0;
    	else if (timeInt > end.getYear()*10000+(end.getMonth()+1)*100+end.getDay())
    		return 32;
    	else if (time.contains(begin.getYear()+"-"+Pad(begin.getMonth()+1)+"-")){
    		return getDay(time) - begin.getDay() + 1;
    	}
    	else if (time.contains(end.getYear()+"-"+Pad(end.getMonth()+1)+"-")){
    		return max - (end.getDay() - getDay(time));
    	}
    	else
			return -1;
    }
    
	public final static int getLunarBeginDayForYearRepeat (String time, LunarItem begin, LunarItem end){
    	long timeInt = ((begin.getMonth()==11&&getMonth(time)!=12)?10000:0)+getMonth(time)*100+getDay(time);
    	if (timeInt < (begin.getMonth()+1)*100+begin.getDay())
    		return 0;
    	else if (timeInt > (begin.getMonth()==11?10000:0)+(end.getMonth()+1)*100+end.getDay())
    		return 32;
    	else if (time.contains("-"+Pad(begin.getMonth()+1)+"-")){
    		return getDay(time) - begin.getDay() + 1;
    	}
    	else
			return -1;
    }
    
	public final static int getLunarEndDay (String time, LunarItem begin, LunarItem end, int max){
    	long timeInt = getYear(time)*10000+getMonth(time)*100+getDay(time);
    	if (timeInt > end.getYear()*10000+(end.getMonth()+1)*100+end.getDay())
    		return 32;
    	else if (timeInt < begin.getYear()*10000+(begin.getMonth()+1)*100+begin.getDay())
    		return 0;
    	else if (time.contains(begin.getYear()+"-"+Pad(begin.getMonth()+1)+"-")){
    		return getDay(time) - begin.getDay() + 1;
    	}
    	else if (time.contains(end.getYear()+"-"+Pad(end.getMonth()+1)+"-")){
    		return max - (end.getDay() - getDay(time));
    	}
    	else
			return -1;
    }
    
	public final static int getLunarEndDayForYearRepeat (String time, LunarItem begin, LunarItem end, int max){
		long timeInt = ((begin.getMonth()==11&&getMonth(time)!=12)?10000:0)+getMonth(time)*100+getDay(time);
    	if (timeInt > (begin.getMonth()==11?10000:0)+(end.getMonth()+1)*100+end.getDay())
    		return 32;
    	else if (timeInt < (begin.getMonth()+1)*100+begin.getDay())
    		return 0;
    	else if (time.contains("-"+Pad(end.getMonth()+1)+"-")){
    		return max - (end.getDay() - getDay(time));
    	}
    	else
			return -1;
    }
	
	public static SimpleDateFormat getFormatter() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		formatter.setTimeZone(Calendar.getInstance().getTimeZone());
		
		return formatter;
	}
	
	public static String getDate(Long time){
		if(time!=null&&time!=-1){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(time);
			return formatter.format(c.getTime());
		}else return "";
	}
	public static String dayOfWeekV5(Context context,int date){
		String[] week=context.getResources().getStringArray(R.array.week);
		return week[date];
	}
}

