package com.ferris.ferrisclickwidget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import lewa.bi.BIAgent;

public class WeatherControl {
	
	public static final String FERRIS_UPDATE_WIDGET = "com.android.FERRIS_UPDATE_WIDGET";
	public static final String WIDGETTIMESERVICE = "com.ferris.ferrisclickwidget.widgettimeservice";
	public static final String LAST_UPDATE_TIME = "lastUpdateTime";
	public static final String WEATHER_AUTO_UPDATE = "weatherAutoUpdate";
	public static final String WEATHER_PROVINCE_CODE = "weatherProvinceCode";
	public static final String WEATHER_CITY = "weatherCity";
	public static final String WEATHER_CITY_CODE = "weatherCityCode";
	public static final String WEATHER_CITY_DEFAULT = "weatherCity_default";
	public static final String WEATHER_CITY_CODE_DEFAULT = "weatherCityCode_default";
	public static final String WEATHER_UPDATE_ROUND = "weatherUpdateRound";
	public static final String WEATHER_SHOW = "showInMain";
	public static final String ALREADY_SET = "alreadySet";
	public static final String WEATHER_PROVINCE = "weatherProvince";
	public static final String WEATHER_PROVINCE_DEFAULT = "weatherProvince_default";
	public static final String UPDATE_ROUND_SELECTED = "updateRoundSelected";
	public static final String CITY_SELECTED = "citySelected";
	public static final String PROVINCE_SELECTED = "provinceSelected";

	public static final String WEATHER_LOCATION_SETTING = "weatherLocation";
	public static final String WEATHER_LOCATION_SETTING1 = "weatherLocation1";
	public static final String LOCAL_CITY = "localCity";
	public static final String WEATHER_CURRENT_TEMPERATURE = "weatherCurrentTemperature";
	public static final String WEATHER_CURRENT_CONDITION = "weatherCurrentCondition";
	public static final String WEATHER_CURRENT_WIND_CONDITION = "weatherCurrentWindCondition";
	public static final String WEATHER_CURRENT_ICON_NAME = "weatherIconName";
	public static final String WEATHER_CURRENT_CITY = "weatherCurrentCity";

	public static final String WEATHER_CURRENT = "weatherCurrent";
	public static final String WEATHER_CUR_CITY_CODE = "weatherCurrentCityCode";

	private static final String SHORT_LONGITUDE = "ShortLongitude";
	private static final String SHORT_LATITUDE = "ShortLatitude";
	private static final String LONGITUDE = "Longitude";
	private static final String LATITUDE = "Latitude";
	public static final String WEATHER_UPDATE_ACTION = "com.lfan.action.WEATHER_UPDATE";
	public static final int WEATHER_SUNSHINE = 0;
	public static final int WEATHER_SHADE = 2;
	public static final int WEATHER_CLOUDY = 1;
	public static final int WEATHER_FOG = 3;
	public static final int WEATHER_HAZE = 4;
	public static final int WEATHER_SHOWER = 5;
	public static final int WEATHER_LIGHT_RAIN = 6;
	public static final int WEATHER_MODE_RAIN = 7;
	public static final int WEATHER_HEAVRY_RAIN = 8;
	public static final int WEATHER_THUNDER_RAIN = 9;
	public static final int WEATHER_TORRENT_RAIN = 10;
	public static final int WEATHER_RAIN_SNOW = 11;
	public static final int WEATHER_SNOW_SHOWER = 12;
	public static final int WEATHER_LIGHT_SNOW = 13;
	public static final int WEATHER_MODE_SNOW = 14;
	public static final int WEATHER_HEAVRY_SNOW = 15;
	public static final int WEATHER_TORRENT_SNOW = 16;
	public static final int WEATHER_RAIN_HAIL = 17;
	public static final int WEATHER_HAIL = 18;

	public static final int PM_GOOD = 0;
	public static final int PM_LIGHT = 1;
	public static final int PM_SERIOUS = 2;
	public static final String WEATHER_UPDATE = "weather_update";
	public static final String HOT_CITIES_UPDATE = "hot_cities";
	public static final String ALL_CITIES_UPDATE = "all_cities";

	private Context context;
	private Spinner spiProvince = null;
	private Spinner spiCity = null;
	private Spinner spiHour = null;
	private LinearLayout chkAutoUpdate = null;
	private TextView offTextView = null;
	private TextView onTextView = null;

	private LinearLayout chkAutoUpdateShow = null;
	private TextView offTextViewShow = null;
	private TextView onTextViewShow = null;

	private ArrayAdapter<String> provinceAdpter = null;
	private ArrayAdapter<String> cityAdpter = null;
	private int provinceSelected = 0;
	private int citySelected = 0;
	private int updateRoundSelected = 0;
	private Boolean autoUpdate = true;
	private Boolean isAutoUpdateSelected = false;
	private Boolean isShowSelected = false;

	private static LocationManager mgr;

	private static final String BASE_URL = "http://api.lewaos.com/thinkpage/trends"; // ÁùÌìÔ¤±¨
	private static final String BASE_NOW_URL = "http://api.lewaos.com/thinkpage/day"; // ÊµÊ±ÌìÆø
	private static final String WEATHERAPI_WEATHER_URL = "http://weather.365rili.com/weatherapi/weather_get_cn.php?citycode=";
	private static final String EXTENSION = ".html";
	public static final String WEATHER_TAG = "RiliWeather";
	private static final String WEATHER_DATA_FILE = "weather.dat";
	private static final String SHANGHAI_WEATHER_DATA_FILE = "shanghaiweather.dat";
	private static String bestMethod;
	public static final String ALL_CITIES_URL = "http://api.lewaos.com/thinkpage/get_all_city";
	public static final String HOT_CITIES_URL = "http://api.lewaos.com/thinkpage/get_hot_city";

	private static Map<String, Integer> locationMap = new HashMap<String, Integer>();

	private String language = Locale.getDefault().getCountry();
	public String[] PROVINCE;
	public String[] PROVINCE_CODE;
	public String[][] CITY;
	public String[][] CITY_CODE;
	public static final String[] UPDATE_ROUND = { "0.5", "1", "2", "3", "4",
			"6", "8", "12", "24" };
	private final static String MAIN_PACKAGE_NAME = "com.when.android.calendar365";

	public WeatherControl(Context context) {
		this.context = context;
		// getCityData();
	}

	/*
	 * »ñÈ¡ÓÃ»§Î»ÖÃ
	 */
	public static Boolean getUserLocation(Context context) {
		mgr = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		Location location;
		try {
			// »ñÈ¡×î¼ÑÊý¾ÝÔ´
			Criteria criteria = new Criteria();
			bestMethod = mgr.getBestProvider(criteria, true);
			location = mgr.getLastKnownLocation(bestMethod);

			if (location != null) {
				locationMap.put(LATITUDE, Integer.parseInt((new BigDecimal(
						location.getLatitude() * 1000000).setScale(0,
						BigDecimal.ROUND_HALF_UP)).toString()));
				locationMap.put(LONGITUDE, Integer.parseInt((new BigDecimal(
						location.getLongitude() * 1000000).setScale(0,
						BigDecimal.ROUND_HALF_UP)).toString()));
				locationMap.put(SHORT_LATITUDE, Integer
						.parseInt((new BigDecimal(location.getLatitude())
								.setScale(1, BigDecimal.ROUND_HALF_UP))
								.toString()));
				locationMap.put(SHORT_LONGITUDE, Integer
						.parseInt((new BigDecimal(location.getLongitude())
								.setScale(1, BigDecimal.ROUND_HALF_UP))
								.toString()));

				return true;
			}
		} catch (NumberFormatException e) {
		} catch (Exception e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		return false;
	}

	public static String getCityTimeIconNameFormatted(String iconName,
			String preName, int tag) {

		if (iconName.equals("")) {
			iconName = preName + "weather_default";
		} else {
			if (tag == 0) {// µ±Ìì

				if (iconName.indexOf(".") > 0) {
					iconName = preName
							+ "weather_city_time_"
							+ new String(iconName.substring(0,
									iconName.lastIndexOf(".")));
				} else {
					iconName = preName + "weather_city_time_" + iconName;
				}

			} else {// Ô¤²â
				if (iconName.indexOf(".") > 0) {
					iconName = preName
							+ "weather_"
							+ new String(iconName.substring(0,
									iconName.lastIndexOf(".")));
				} else {
					iconName = preName + "weather_" + iconName;
				}
			}
		}
		return iconName;
	}

	public static String getCityTimeIconNameFormatted(String iconName) {
		return getCityTimeIconNameFormatted(iconName, "", 0);
	}

	public static int getCityTimeIconId(Context context, String iconName,
			String preName, int tag) {
		int id = 0;
		try {
			iconName = getCityTimeIconNameFormatted(iconName, preName, tag);
			id = context.getResources().getIdentifier(iconName, "drawable",
					context.getPackageName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public static int getCityTimeIconId(Context context, String iconName,
			int tag) {
		return getCityTimeIconId(context, iconName, "", tag);
	}

	public static String getIconNameFormatted(String iconName, String preName) {
		if (iconName.equals("")) {
			iconName = preName + "weather_default";
		} else {
			if (iconName.indexOf(".") > 0) {
				iconName = preName
						+ "weather_weather_"
						+ new String(iconName.substring(0,
								iconName.lastIndexOf(".")));
			} else {
				iconName = preName + "weather_weather_" + iconName;
			}
		}
		return iconName;
	}

	public static int getIconId(Context context, String iconName, String preName) {
		int id = 0;
		try {
			iconName = getIconNameFormatted(iconName, preName);
			id = context.getResources().getIdentifier(iconName, "drawable",
					context.getPackageName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public static int getIconId(Context context, String iconName) {
		return getIconId(context, iconName, "");
	}

	public static boolean isWiFiActive(Context inContext) {
		WifiManager mWifiManager = (WifiManager) inContext
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
		int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
		if (mWifiManager.isWifiEnabled() && ipAddress != 0) {
			return true;
		} else {
			return false;
		}
	}

	public static Boolean IsConnection(Context inContext) {
		ConnectivityManager connec = (ConnectivityManager) inContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED
				|| connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING) {
			return true;
		} else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED
				|| connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
			return false;
		}
		return false;
	}

	public void releaseLocationManager() {
		if (locationManager != null && locationListener != null)
			locationManager.removeUpdates(locationListener);
	}

	public static String getImageString(String condition) {
		if (condition == null)
			return "";
		if (condition.contains("×ª")) {
			// condition=condition.substring(0,condition.indexOf("×ª"));
			String firstCondition = condition.substring(0,
					condition.indexOf("×ª"));
			String secCondition = condition
					.substring(condition.indexOf("×ª") + 1);
			int firIndex = WeatherControl.getWeather(firstCondition);
			int secIndex = WeatherControl.getWeather(secCondition);
			if (firIndex > secIndex) {
				condition = firstCondition;
			} else {
				condition = secCondition;
			}
		}
		if (condition.contains("ÔÆ")) {
			return "cloudy";
		} else if (condition.contains("À×")) {
			return "thunderstorm";
		} else if (condition.contains("Óê") || condition.contains("±¢")) {
			if (condition.contains("Óê") && condition.contains("±¢")) {
				return "rainandhailstone";
			} else if (condition.contains("Óê")) {
				if (condition.contains("´óÓê")) {
					return "heavyrain";
				} else if (condition.contains("Ð¡Óê")) {
					return "lightrain";
				} else if (condition.contains("ÕóÓê")) {
					return "shower";
				} else if (condition.contains("Ñ©")) {
					return "rainandsnow";
				} else {
					return "heavyrain";
				}
			} else {
				return "hailstone";
			}
		} else if (condition.contains("Çç")) {
			return "sunshine";
		} else if (condition.contains("Îí") || condition.contains("ö²")) {
			return "fog";
		} else if (condition.contains("Òõ")) {
			return "shade";
		} else if (condition.contains("Ñ©")) {
			if (condition.contains("´óÑ©")) {
				return "heavysnow";
			} else if (condition.contains("Ð¡Ñ©")) {
				return "lightsnow";
			} else if (condition.contains("ÕóÑ©")) {
				return "lightsnow";
			} else {
				return "heavysnow";
			}
		} else {
			return "";
		}
	}

	public static String getBgImageName(String condition, String lastCondition) {
		if (condition != null && condition.contains("×ª")) {
			String firstCondition = condition.substring(0,
					condition.indexOf("×ª"));
			String secCondition = condition
					.substring(condition.indexOf("×ª") + 1);
			int firIndex = WeatherControl.getWeather(firstCondition);
			int secIndex = WeatherControl.getWeather(secCondition);
			if (firIndex > secIndex) {
				condition = firstCondition;
			} else {
				condition = secCondition;
			}
		} else if (condition == null) {
			return "";
		}
		if (lastCondition != null && lastCondition.contains("×ª")) {
			String firstCondition = lastCondition.substring(0,
					lastCondition.indexOf("×ª"));
			String secCondition = lastCondition.substring(lastCondition
					.indexOf("×ª") + 1);
			int firIndex = WeatherControl.getWeather(firstCondition);
			int secIndex = WeatherControl.getWeather(secCondition);
			if (firIndex > secIndex) {
				lastCondition = firstCondition;
			} else {
				lastCondition = secCondition;
			}
			// lastCondition=lastCondition.substring(0,lastCondition.indexOf("×ª"));
		}
		if (condition.contains("ÔÆ")) {
			if (lastCondition == null || lastCondition != null
					&& !lastCondition.contains("ÔÆ")) {
				return "cloudy";
			} else {
				return "";
			}
		} else if (condition.contains("À×")) {
			if (lastCondition == null || lastCondition != null
					&& !lastCondition.contains("À×")) {
				return "thunder";
			} else {
				return "";
			}
		} else if (condition.contains("Óê") || condition.contains("±¢")) {
			if (lastCondition == null || lastCondition != null
					&& !lastCondition.contains("Óê")
					&& !lastCondition.contains("±¢") || lastCondition != null
					&& lastCondition.contains("À×")) {
				return "rain";
			} else {
				return "";
			}
		} else if (condition.contains("Çç")) {
			if (lastCondition == null || lastCondition != null
					&& !lastCondition.contains("Çç")) {
				return "sunshine";
			} else {
				return "";
			}
		} else if (condition.contains("Îí") || condition.contains("ö²")) {
			if (lastCondition == null || lastCondition != null
					&& !lastCondition.contains("Îí")
					&& !lastCondition.contains("ö²")) {
				return "fog";
			} else {
				return "";
			}
		} else if (condition.contains("Òõ")) {
			if (lastCondition == null || lastCondition != null
					&& !lastCondition.contains("Òõ")) {
				return "shade";
			} else {
				return "";
			}
		} else if (condition.contains("Ñ©")) {
			if (lastCondition == null || lastCondition != null
					&& !lastCondition.contains("Ñ©")) {
				return "snow";
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	public static int getWeather(String condition) {
		if (condition == null)
			return WEATHER_SUNSHINE;
		if (condition.contains("Çç")) {
			return WEATHER_SUNSHINE;
		} else if (condition.contains("Òõ")) {
			return WEATHER_SHADE;
		} else if (condition.contains("ÔÆ")) {
			return WEATHER_CLOUDY;
		} else if (condition.contains("Îí")) {
			return WEATHER_FOG;
		} else if (condition.contains("ö²")) {
			return WEATHER_HAZE;
		} else if (condition.contains("À×")) {
			return WEATHER_THUNDER_RAIN;
		} else if (condition.contains("Óê")) {
			if (condition.contains("ÕóÓê")) {
				return WEATHER_SHOWER;
			} else if (condition.contains("Ð¡Óê")) {
				return WEATHER_LIGHT_RAIN;
			} else if (condition.contains("ÖÐÓê")) {
				return WEATHER_MODE_RAIN;
			} else if (condition.contains("´óÓê")) {
				return WEATHER_HEAVRY_RAIN;
			} else if (condition.contains("±©Óê")) {
				return WEATHER_TORRENT_RAIN;
			} else if (condition.contains("Ñ©")) {
				return WEATHER_RAIN_SNOW;
			} else if (condition.contains("±¢")) {
				return WEATHER_RAIN_HAIL;
			} else {
				return WEATHER_MODE_RAIN;
			}
		} else if (condition.contains("Ñ©")) {
			if (condition.contains("ÕóÑ©")) {
				return WEATHER_SNOW_SHOWER;
			} else if (condition.contains("Ð¡Ñ©")) {
				return WEATHER_LIGHT_SNOW;
			} else if (condition.contains("ÖÐÑ©")) {
				return WEATHER_MODE_SNOW;
			} else if (condition.contains("´óÑ©")) {
				return WEATHER_HEAVRY_SNOW;
			} else if (condition.contains("±©Ñ©")) {
				return WEATHER_TORRENT_SNOW;
			} else {
				return WEATHER_MODE_SNOW;
			}
		} else if (condition.contains("±¢")) {
			return WEATHER_HAIL;
		} else {
			return WEATHER_SUNSHINE;
		}
	}

	public static Date getDate(String expires) {
		SimpleDateFormat format = new SimpleDateFormat(
				"EEE dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date date = null;

		try {
			date = format.parse(expires);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	public static long parseGmtTime(String gmtTime) {
		try {
			SimpleDateFormat GMT_FORMAT = new SimpleDateFormat(
					"EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
			return GMT_FORMAT.parse(gmtTime).getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static Date getGMT() {
		Date gmt8 = null;
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
				Locale.ENGLISH);
		Calendar day = Calendar.getInstance();
		day.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		day.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		day.set(Calendar.DATE, cal.get(Calendar.DATE));
		day.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
		day.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
		day.set(Calendar.SECOND, cal.get(Calendar.SECOND));
		gmt8 = day.getTime();
		return gmt8;
	}

	public static int getTimeDif(Context context, String time) {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String getMonth = time.substring(0,
				time.indexOf(context.getString(R.string.yue)));
		String getDay = time.substring(
				time.indexOf(context.getString(R.string.yue)) + 1,
				time.indexOf(context.getString(R.string.ri)));
		if (month + 1 == Integer.valueOf(getMonth)) {
			return day - Integer.valueOf(getDay);
		} else {
			return 2;
		}
	}

	public static int getPmStatus(String pm) {
		if (pm == null)
			return PM_LIGHT;
		int pmValue = Integer.parseInt(pm);
		if (pmValue < 100) {
			return PM_GOOD;
		} else if (pmValue >= 100 && pmValue < 200) {
			return PM_LIGHT;
		} else if (pmValue >= 200) {
			return PM_SERIOUS;
		}
		return PM_LIGHT;
	}

	public static String buildUserAgent(Context context) {
		if (!initUserAgent && context != null) {
			initUserAgent = true;
			StringBuilder s = new StringBuilder(256);
			s.append(USEER_AGENT_PREFIX);
			s.append(" (Android ").append(Build.VERSION.RELEASE);
			String model = Build.MODEL;
			s.append("; Model ").append(model.replace(" ", "_"));
			if (!initLewaVersion) {
				getLewaVersion();
			}
			if (lewaVersion != null && lewaVersion.length() > 0) {
				s.append("; ").append(lewaVersion);
			}
			s.append(") ");
			if (!initAppVersionCode) {
				getAppVersionCode(context);
			}
			if (appVersionCode != null) {
				s.append(context.getPackageName()).append("/")
						.append(appVersionCode);
			}
			if (!initBiClientId) {
				getBiClientId(context);
			}
			if (biClientId != null && biClientId.length() > 0) {
				s.append(" ClientID/").append(biClientId);
			}
			return (userAgent = s.toString());
		}
		return userAgent;
	}

	public static String getBiClientId(Context context) {
		if (!initBiClientId && context != null) {
			initBiClientId = true;
			// get biclient id by lewa.bi.BIAgent.getBIClientId(Context context)
			// method
			Class<?> demo = null;
			try {
				demo = Class.forName(CLASS_NAME_BIAGENT);
				Method method = demo.getMethod(METHOD_NAME_GET_CLIENT_ID,
						Context.class);
				return (biClientId = (String) method.invoke(demo.newInstance(),
						context));
			} catch (Exception e) {
				/**
				 * accept all exception, include ClassNotFoundException,
				 * NoSuchMethodException, InvocationTargetException,
				 * NullPointException
				 */
				e.printStackTrace();
			}
		}
		return biClientId;
	}

	public static String getAppVersionCode(Context context) {
		if (!initAppVersionCode && context != null) {
			initAppVersionCode = true;
			PackageManager pm = context.getPackageManager();
			if (pm != null) {
				PackageInfo pi;
				try {
					pi = pm.getPackageInfo(context.getPackageName(), 0);
					if (pi != null) {
						return (appVersionCode = Integer
								.toString(pi.versionCode));
					}
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return appVersionCode;
	}

	public static String getLewaVersion() {
		if (!initLewaVersion) {
			initLewaVersion = true;
			// get lewa os version by lewa.os.Build.LEWA_VERSION
			Class<?> demo = null;
			try {
				demo = Class.forName(CLASS_NAME_LEWA_BUILD);
				Field field = demo.getField(FIELD_NAME_LEWA_BUILD_VERSION);
				return lewaVersion = (String) field.get(demo.newInstance());
			} catch (Exception e) {
				/**
				 * accept all exception, include ClassNotFoundException,
				 * NoSuchFieldException, InstantiationException,
				 * IllegalArgumentException, IllegalAccessException,
				 * NullPointException
				 */
				e.printStackTrace();
			}
		}
		return lewaVersion;
	}

	class CellIDInfo {
		public int cellId;
		public String mobileCountryCode;
		public String mobileNetworkCode;
		public int locationAreaCode;
		public String radioType;

		public CellIDInfo() {
		}
	}

	public static String getCellInfo(Context context) {
		// if(cellInfo==null){
		cellInfo = "";
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		CellLocation loc = tm.getCellLocation();
		if (loc instanceof GsmCellLocation) {
			GsmCellLocation gsmCellLocation = (GsmCellLocation) loc;
			if (gsmCellLocation != null) {
				int cellId = gsmCellLocation.getCid();
				int areaCode = gsmCellLocation.getLac();
				if (cellId == -1 && areaCode == -1) {
					cellInfo = "";
				} else {
					cellInfo = cellId + "_" + areaCode;
				}
			}
		} else {
			CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) loc;
			if (cdmaCellLocation != null) {
				int cellId = cdmaCellLocation.getBaseStationId();
				int areaCode = cdmaCellLocation.getNetworkId();
				if (cellId == -1 && areaCode == -1) {
					cellInfo = "";
				} else {
					cellInfo = cellId + "_" + areaCode;
				}
			}
		}
		// }
		return cellInfo;
	}

	public static String buildCityCode(String citycode) {
		if (citycode != null && citycode.contains("|"))
			citycode = citycode.substring(0, citycode.lastIndexOf("|"));
		return citycode;
	}

	public static boolean checkDataBase() {
		SQLiteDatabase db = null;
		try {
			String path = DB_PATH + DB_NAME;
			db = SQLiteDatabase.openDatabase(path, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (db != null)
			db.close();
		return db != null ? true : false;
	}

	public static boolean copyDataBase(Context context) {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = context.getAssets().open("com.lewa.weather");
			String outFileName = DB_PATH + DB_NAME;
			os = new FileOutputStream(outFileName);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
			os.flush();
			os.close();
			is.close();
			SharedPreferences sp = context.getSharedPreferences(
					WEATHER_SHAREDPREFS_COMMON, Context.MODE_PRIVATE);
			sp.edit()
					.putLong(ALL_CITIES_LAST_UPDATE_TIME,
							System.currentTimeMillis()).commit();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		} finally {
			try {
				if (is != null)
					is.close();
				if (os != null)
					os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * È¥³ýstringÀ¨ºÅÖÐµÄ×Ö·û
	 * 
	 * @param string
	 * @return
	 */
	public static String removeBrackets(String string) {
		if (string != null && string.contains("£¨")) {
			int index = string.indexOf("£¨");
			string = string.substring(0, index);
		} else if (string != null && string.contains("(")) {
			int index = string.indexOf("(");
			string = string.substring(0, index);
		}
		return string;
	}

	public static boolean correctCellInfo(Context context) {
		getCellInfo(context);
		if (!TextUtils.isEmpty(cellInfo)) {
			mLocation = null;
			if (locationManager == null)
				locationManager = (LocationManager) context
						.getSystemService(Context.LOCATION_SERVICE);
			boolean gpsEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			if (gpsEnabled) {
				if (correctListener == null)
					correctListener = new LocationListener() {

						@Override
						public void onStatusChanged(String provider,
								int status, Bundle extras) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onProviderEnabled(String provider) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onProviderDisabled(String provider) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onLocationChanged(Location location) {
							// TODO Auto-generated method stub
							mLocation = location;
						}
					};
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 1000, 0, correctListener);
				return true;
			}
		}
		return false;
	}

	public static void releaseCorrectListener() {
		if (locationManager != null && correctListener != null)
			locationManager.removeUpdates(correctListener);
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static void recyleBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled())
			bitmap.recycle();
	}

	public static Bitmap createTextBitmap(Context context, String text,
			float size, int transy, boolean isSetShadow) {
		int shadow_radius = context.getResources().getInteger(
				R.integer.v5_widget_shadow_radius);
		int shadow_dx = context.getResources().getInteger(
				R.integer.v5_widget_shadow_dx);
		int shadow_dy = context.getResources().getInteger(
				R.integer.v5_widget_shadow_dy);

		final float scale = context.getApplicationContext().getResources()
				.getDisplayMetrics().density;
		Paint paint = new Paint();
		Rect rect = new Rect();
		paint.getTextBounds(text, 0, text.length(), rect);
		paint.setTextSize(size);
		paint.setColor(Color.WHITE);
		// Typeface face = Typeface.createFromAsset (context.getAssets() ,
		// "NeoSans-Light.otf");
		Typeface face = Typeface.createFromFile(ANDROID_ROBOTO_FONT_FILE);
		paint.setTypeface(face);
		paint.setAntiAlias(true);

		if (isSetShadow)
			paint.setShadowLayer(shadow_radius, shadow_dx, shadow_dy,
					Color.DKGRAY);
		int translateY = (int) (transy * scale + 0.5f);

		float tempratureRangeWidth = paint.measureText(text);
		Bitmap bitmap = Bitmap.createBitmap(
				(int) (tempratureRangeWidth + 0.5f), (int) (20 * scale + 0.5f),
				Config.ARGB_8888);
		Canvas canvasTemp = new Canvas(bitmap);
		canvasTemp.drawText(text, 0, translateY, paint);
		return bitmap;
	}

	public static boolean isBitmapNull(Bitmap bitmap) {
		if (bitmap == null || bitmap.isRecycled())
			return true;
		return false;
	}

	public static String buildPmCondition(String pmCondition, Context context) {
		if (pmCondition != null && pmCondition.length() == 1) {
			if (pmCondition.equals(context.getString(R.string.v5_excellent))) {
				return context.getString(R.string.v5_excellent_build);
			} else if (pmCondition.equals(context.getString(R.string.v5_good))) {
				return context.getString(R.string.v5_good_build);
			}
		}
		return pmCondition;
	}

	public static String buildCityName(String name) {
		if (TextUtils.isEmpty(name)) {
			return name;
		}
		if (WeatherControl.isLanguageEnUs()) {
			if (name.length() > 9) {
				name = name.substring(0, 9) + "...";
			}
		} else {
			if (name.length() > 4) {
				name = name.substring(0, 3);
				name = name.concat("...");
			} else if (name.length() == 2) {
				name = " " + name.substring(0, 1) + "  " + name.substring(1)
						+ " ";
			}
		}
		return name;
	}

	public static String getLanguageHeader() {
		StringBuilder builder = new StringBuilder();
		builder.append(Locale.getDefault().getLanguage());
		builder.append("-");
		builder.append(Locale.getDefault().getCountry());
		return builder.toString().toLowerCase();
	}

	public static boolean isLanguageZhCn() {
		String defaultLanguage = getLanguageHeader();
		return defaultLanguage.equalsIgnoreCase("zh-cn");
	}

	public static boolean isLanguageZhTw() {
		String defaultLanguage = getLanguageHeader();
		return defaultLanguage.equalsIgnoreCase("zh-tw");
	}

	public static boolean isLanguageEnUs() {
		String defaultLanguage = getLanguageHeader();
		return defaultLanguage.equalsIgnoreCase("en-us");
	}

	public static boolean isCountryCN(String country) {
		if (country != null && country.equalsIgnoreCase("china")
				|| country != null && country.equals("ÖÐ¹ú"))
			return true;
		return false;
	}

	public static String removeBlank(String string) {
		if (!TextUtils.isEmpty(string)) {
			string = string.replaceAll(" ", "");
		}
		return string;
	}

	public static void writeLogToSDCard(String str, String filename) {
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			File file = new File(path + File.separator + filename);
			FileOutputStream outputStream = null;
			OutputStreamWriter writer = null;
			try {
				if (!file.exists())
					file.createNewFile();
				outputStream = new FileOutputStream(file, true);
				writer = new OutputStreamWriter(outputStream);
				SimpleDateFormat simpleDateFormat = DataFormatControl
						.getFormatter();
				String time = simpleDateFormat.format(new Date());
				str = str + " " + time;
				writer.write(str);
				writer.close();
				outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (writer != null)
						writer.close();
					if (outputStream != null)
						outputStream.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static final String MOCK_GPS="mock_gps";
    public static Location mLocation=null;
    private static final long TWO_HOURS = 1000 * 60 * 60 * 2;
    private  static LocationManager locationManager;
    private  static LocationListener locationListener;
    private static boolean               initUserAgent                 = false;
    private static boolean               initBiClientId                = false;
    private static boolean               initAppVersionCode            = false;
    private static boolean               initLewaVersion               = false;
    public static final String           USEER_AGENT_PREFIX            = "LewaApi/1.0-1";
    public static final String           CLASS_NAME_BIAGENT            = "lewa.bi.BIAgent";
    public static final String           METHOD_NAME_GET_CLIENT_ID     = "getBIClientId";
    public static final String           CLASS_NAME_LEWA_BUILD         = "lewa.os.Build";
    public static final String           FIELD_NAME_LEWA_BUILD_VERSION = "LEWA_VERSION";
    private static String                userAgent                     = null;
    /** biclient id defined by lewa.bi.BIAgent.getBIClientId(Context context) method **/
    private static String                biClientId                    = null;
    /** android:versionCode in AndroidManifest.xml **/
    private static String                appVersionCode                = null;
    /** lewa os version defined by lewa.os.Build.LEWA_VERSION **/
    private static String                lewaVersion                   = null;
    private static String                cellInfo                      = null;
    private static String DB_PATH="/data/data/com.lewa.weather/databases/";
    private static String DB_NAME="com.lewa.weather";
    private static LocationListener correctListener;
    private static String HOT_CITIES_LAST_UPDATE_TIME="hot_cities_last_update_time";
    private static String ALL_CITIES_LAST_UPDATE_TIME="all_cities_last_update_time";
    private static String HOT_CITIES_EXPIRES="hot_cities_expires";
    private static String ALL_CITIES_EXPIRES="ALL_cities_expires";
    public static String WEATHER_SHAREDPREFS_COMMON="weatherLocation";
    private static final long ONE_DAY = 1000 * 60 * 60 * 24;
    public static String LOCATION_COUNTRY="location_country";
    public static String UPDATE="update";
    public static String WEATHER_LOG="weather.txt";
    public static boolean isWriteLogToSD=false;
    private static final String ANDROID_ROBOTO_FONT_FILE = "/system/fonts/Roboto-Light.ttf";
}
