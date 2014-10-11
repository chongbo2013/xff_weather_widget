package com.ferris.ferrisclickwidget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LunarItem {
	private int year;
	private int month;
	private int day;
	private int dyear;
	private int myear;
	private int mmonth;
	private int mday;
	private int dmonth;
	private int dday;
	private boolean leap;
	final static String chineseNumber[] = { "һ", "��", "��", "��", "��", "��", "��",
			"��", "��", "ʮ", "ʮһ", "ʮ��" };
	final static String chineseMonth[] = { "��", "��", "��", "��", "��", "��", "��",
		"��", "��", "ʮ", "��", "��" };
	static SimpleDateFormat chineseDateFormat = new SimpleDateFormat(
			"yyyy��MM��dd��");
	final static long[] lunarInfo = new long[] { 0x04bd8, 0x04ae0, 0x0a570,
			0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
			0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0,
			0x0ada2, 0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5, 0x06a50,
			0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566,
			0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0,
			0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4,
			0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0, 0x0b550,
			0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950,
			0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260,
			0x0f263, 0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0,
			0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0ba60, 0x195a6,
			0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40,
			0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0, 0x074a3,
			0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, 0x0c960,
			0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0,
			0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9,
			0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954, 0x06aa0,
			0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65,
			0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0,
			0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0, 0x055b2,
			0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0 };

	//======   ����ũ��   y���������  
	final private static int yearDays(int y) {
		int i, sum = 348;
		for (i = 0x8000; i > 0x8; i >>= 1) {
			if ((lunarInfo[y - 1900] & i) != 0)
				sum += 1;
		}
		return (sum + leapDays(y));
	}

	//======   ����ũ��   y�����µ�����  
	final public static int leapDays(int y) {
		if (leapMonth(y) != 0) {
			if ((lunarInfo[y - 1900] & 0x10000) != 0)
				return 30;
			else
				return 29;
		} else
			return 0;
	}

	//======   ����ũ��   y�����ĸ���   1-12   ,   û�򴫻�   0  
	final public static int leapMonth(int y) {
		if (y>2049 || y<1900)
			return 0;
		return (int) (lunarInfo[y - 1900] & 0xf);
	}

	//======   ����ũ��   y��m�µ�������  
	final public static int monthDays(int y, int m) {
		if ((lunarInfo[y - 1900] & (0x10000 >> m)) == 0)
			return 29;
		else
			return 30;
	}

	//======   ����ũ��   y�����Ф  
	final public String animalsYear() {
		final String[] Animals = new String[] { "��", "ţ", "��", "��", "��", "��",
				"��", "��", "��", "��", "��", "��" };
		return Animals[(year - 4) % 12];
	}

	//======   ����   ���յ�offset   ���ظ�֧,   0=����  
	final private static String cyclicalm(int num) {
		final String[] Gan = new String[] { "��", "��", "��", "��", "��", "��", "��",
				"��", "��", "��" };
		final String[] Zhi = new String[] { "��", "��", "��", "î", "��", "��", "��",
				"δ", "��", "��", "��", "��" };
		return (Gan[num % 10] + Zhi[num % 12]);
	}
	
	final public String cyclicalmd() {
		int i;
		if(dmonth == 1|| dmonth == 2){
			dyear = dyear-1;
			dmonth = dmonth + 12;
			
		}
//		String  string = String.valueOf(dyear);
//		String string1 = string.substring(0,2);
//		String string2 = string.substring(2);
		int c = dyear/100;
		int y1 = dyear%100;
		
		if(dmonth % 2 == 0) i = 6;
		else  i = 0;
		int g=(int) (4*c+Math.floor(c/4)+5*y1+Math.floor(y1/4)+Math.floor(3*(dmonth+1)/5)+dday-3);
		int z=(int) (8*c+Math.floor(c/4)+5*y1+Math.floor(y1/4)+Math.floor(3*(dmonth+1)/5)+dday+7+i);
		final String[] Gan = new String[] {"��", "��", "��", "��", "��", "��", "��", "��",
				"��", "��"  };
		final String[] Zhi = new String[] { "��" ,"��", "��", "��", "î", "��", "��", "��",
				"δ", "��", "��", "��" };
		
		return (Gan[g%10] + Zhi[z%12]);
		
		
	}
	
	
	/**  
	 * 
	 *   ����y��m��d�ն�Ӧ��ũ��.  
	 *   yearCyl3:ũ������1864�������                             ?  
	 *   monCyl4:��1900��1��31������,������  
	 *   dayCyl5:��1900��1��31����������,�ټ�40             ?  
	 *   @param   cal    
	 *   @return    
	 */
	public LunarItem(Calendar cal) {
		Date baseDate = null;
		try {
			baseDate = chineseDateFormat.parse("1900��1��31��");
		} catch (ParseException e) {
			e.printStackTrace(); //To   change   body   of   catch   statement   use   Options   |   File   Templates.  
		}
		myear = dyear = cal.get(Calendar.YEAR);
		mmonth = dmonth = cal.get(Calendar.MONTH)+1;

		mday = dday = cal.get(Calendar.DAY_OF_MONTH);

		//�����1900��1��31����������  
		int offset = (int) ((cal.getTime().getTime() - baseDate.getTime()) / 86400000L);
		calculate(offset);	
	}
	
	public void setCalendar(Calendar cal) {
		Date baseDate = null;
		try {
			baseDate = chineseDateFormat.parse("1900��1��31��");
		} catch (ParseException e) {
			e.printStackTrace(); //To   change   body   of   catch   statement   use   Options   |   File   Templates.  
		}
		myear = dyear = cal.get(Calendar.YEAR);
		mmonth = dmonth = cal.get(Calendar.MONTH)+1;

		mday = dday = cal.get(Calendar.DAY_OF_MONTH);

		//�����1900��1��31����������  
		int offset = (int) ((cal.getTime().getTime() - baseDate.getTime()) / 86400000L);
		calculate(offset);	
	}
	
//	private static HashMap<Integer, String> map = new HashMap<Integer, String>();
	
	//private static String[] hash = new String[60000];
	
	
	
	private void calculate(int offset) {
		
//		int key = offset;
//		
//		if (hash[key] != null && hash[key].length() > 0) {
//			parse(hash[key]);
//			return;
//		}
//		
//		
//		if (map.containsKey(offset)) {
//			parse(map.get(offset));
//			return;
//		}
//		
		
		
		int leapMonth;
		//��offset��ȥÿũ���������  
		//   ���㵱����ũ���ڼ���  
		//i���ս����ũ�������  
		//offset�ǵ���ĵڼ���  
		int iYear, daysOfYear = 0;
		for (iYear = 1900; iYear < 2050 && offset > 0; iYear++) {
			daysOfYear = yearDays(iYear);
			offset -= daysOfYear;
		}
		if (offset < 0) {
			offset += daysOfYear;
			iYear--;
		}
		//ũ�����  
		year = iYear;

		leapMonth = leapMonth(iYear); //���ĸ���,1-12  
		leap = false;

		//�õ��������offset,�����ȥÿ�£�ũ��������������������Ǳ��µĵڼ���  
		int iMonth, daysOfMonth = 0;
		for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
			//����  
			if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
				--iMonth;
				leap = true;
				daysOfMonth = leapDays(year);
			} else
				daysOfMonth = monthDays(year, iMonth);

			offset -= daysOfMonth;
			//�������  
			if (leap && iMonth == (leapMonth + 1))
				leap = false;
			if (!leap) {
			}
		}
		//offsetΪ0ʱ�����Ҹղż�����·������£�ҪУ��  
		if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
			if (leap) {
				leap = false;
			} else {
				leap = true;
				--iMonth;
			}
		}
		//offsetС��0ʱ��ҲҪУ��  
		if (offset < 0) {
			offset += daysOfMonth;
			--iMonth;
		}
		month = iMonth;
		day = offset + 1;
		
//		sb.append(year + " ");
//		sb.append(month + " ");
//		sb.append(day + " ");
//		sb.append(dyear + " ");
//		sb.append(myear + " ");
//		sb.append(mmonth + " ");
//		sb.append(mday + " ");
//		sb.append(dmonth + " ");
//		sb.append(dday + " ");
//		sb.append(leap ? "1" : "0");
//	
//		hash[key] = sb.toString();
//		map.put(key, sb.toString());
	}

	public String getChinaCurrentMonthAndDayString() {
		return getChinaMonthString(month)+getChinaDayString(day);
	}
	/**
	 * ����ũ�����·ݸ��գ��޸�ũ����11��12,1 �·ݷֱ�Ϊ�� �� ����
	 */
	public String getCurrentLunarMonthAndDay() {
		return getChinaMonth(month)+getChinaDayString(day);
	}
	
	public String getChinaCurrentYearAndMonthAndDayString() {
		return "ũ��" + year + "��" + getChinaMonthString(month)+ getChinaDayString(day);	
	}
	
	public String getChinaYearAndMonthAndDayString() {
		return "ũ�� " + getChinaYearString(year) + " " + getChinaMonthString(month)+ getChinaDayString(day);
	}
	
	public static String getChinaDayString(int day) {
		String chineseTen[] = { "��", "ʮ", "إ", "ئ" };
		int n = day % 10 == 0 ? 9 : day % 10 - 1;
		if (day > 30)
			return "";
		if (day == 20)
			return "��ʮ";
		if (day == 10)
			return "��ʮ";
		if (day == 30)
			return "��ʮ";
		else
			return chineseTen[day / 10] + chineseNumber[n];
	}
	
	public static String getChinaYearString(int year) {
		String[] numbers = new String[]{"��","һ","��","��","��","��","��","��","��","��"};
		return numbers[year/1000] + numbers[year/100%10]
		       + numbers[year/10%10] + numbers[year%10] + "��";
	}
	
	/**
	 * ����ũ����11��12,1 �·ݷֱ�Ϊ�� �� ��
	 */
	public String getChinaMonth(int month) {
		if (leap)
			return "��" + chineseMonth[month - 1] + "��";
		else
			return chineseMonth[month - 1] + "��";
	}
	
	public String getChinaMonthString(int month) {
		if (leap)
			return "��" + chineseNumber[month - 1] + "��";
		else
			return chineseNumber[month - 1] + "��";
	}
	
	public static String getChineseNumber(int num) {
		return chineseNumber[num-1];
	}

	@Override
	public String toString() {
		if (day == 1) {
			switch (month) {
			case 1:
				return "����";
			case 11:
				return "����";
			case 12:
				return "����";
			default:
				return chineseNumber[month - 1] + "��";
			}
		}
		else
			return getChinaDayString(day);
	}
	
	public String getFormatDate() {
		return year + "-" + pad(month) + "-" + pad(day) + " 00:00:00";
	}
	
	public int getYear() {
		return year;
	}
	
	/**
	 * 
	 * @return month between 0-11
	 */
	public int getMonth() {
		return month-1;
	}
	
	public int getDay() {
		return day;
	}

	public boolean isLeep() {
		return leap;
	}
	/*public static void main(String[] args) throws ParseException {
		Calendar today = Calendar.getInstance();
		today.setTime(chineseDateFormat.parse("2004��1��22��"));
		LunarItem lunar = new LunarItem(today);

		//System.out.println("����ʱ�䣺" + chineseDateFormat.format(today.getTime())
				+ "��ũ��" + lunar);
	}
	*/
	private String pad(int num) {
		if (num < 10){
			return "0" + num;
		}
		else{
			return "" + num;
		}
	}
}
