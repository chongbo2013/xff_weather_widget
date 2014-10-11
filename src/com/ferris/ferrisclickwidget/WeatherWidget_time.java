package com.ferris.ferrisclickwidget;

import java.util.Calendar;
import java.util.Date;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.RemoteViews;

public class WeatherWidget_time extends AppWidgetProvider {

	private static RemoteViews updateViews;
	private static Bitmap hour1;
	private static Bitmap hour2;
	private static Bitmap minute1;
	private static Bitmap minute2;
	private static Bitmap solarBitmap;
	private static Bitmap lunarBitmap;
	private static Bitmap weekBitmap;

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		for (int i = 0; i < appWidgetIds.length; i++) {
			if (updateViews != null) {
				updateViews = null;
			}
			updateViews = lewaBuildUpdate(context, appWidgetIds[i], false,
					true, true);
			appWidgetManager.updateAppWidget(appWidgetIds[i], updateViews);
			System.gc();
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onEnabled(Context context) {

		updateTimeAlarm(context);
		super.onEnabled(context);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

	@Override
	public void onReceive(final Context context, Intent intent) {

		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)
				|| intent.getAction().equals(WeatherControl.WIDGETTIMESERVICE)) {// 开机启动
			// 启动后台服务
			if (startservice == null) {
				startservice = new Intent(context, WidgetTimeService.class);
			}

			context.startService(startservice);
			updateTimeAlarm(context);
		} else if (intent.getAction().equals(
				WeatherControl.FERRIS_UPDATE_WIDGET)) { // 监听到时间变化，更新wiget
			updateTimeAlarm(context);
		}

		super.onReceive(context, intent);
	}

	private static Intent launchIntent, startservice = null;
	private static PendingIntent weatherPendingIntent;
	private static Date date = null;
	private static int[] appWidgetIds = null;

	private static synchronized RemoteViews lewaBuildUpdate(Context context,
			int i, boolean isRefresh, boolean isTimeUpdate, boolean isDateUpdate) {
		// TODO Auto-generated method stub
		if (date != null) {
			date = null;
		}
		date = new Date();
		boolean bool = DateFormat.is24HourFormat(context);
		if (!isStartWithone(context, date, bool)) {
			if (updateViews != null) {
				updateViews = null;
			}
			updateViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_v5_time);
		} else {
			if (updateViews != null) {
				updateViews = null;
			}
			updateViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_v5_time_1);
		}

		updateTime(updateViews, context, date, bool, isTimeUpdate);
		updateDate(updateViews, context, isDateUpdate);

		if (launchIntent != null) {
			launchIntent = null;
		}

		if (weatherPendingIntent != null) {
			weatherPendingIntent = null;
		}
		launchIntent = new Intent(Intent.ACTION_MAIN, null);
		launchIntent.addCategory(Intent.CATEGORY_DESK_DOCK);
		launchIntent.setClassName("com.android.deskclock",
				"com.android.deskclock.AlarmClock");
		launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		weatherPendingIntent = PendingIntent.getActivity(context, 0,
				launchIntent, 0);

		updateViews.setOnClickPendingIntent(R.id.v5_widget_time_container,
				weatherPendingIntent);


		updateViews.setOnClickPendingIntent(R.id.v5_widget_date,
				weatherPendingIntent);
		updateViews.setOnClickPendingIntent(R.id.v5_widget_week_ll,
				weatherPendingIntent);
		return updateViews;
	}

	private static void drawHour(Context context, RemoteViews remoteViews,
			Date date, boolean b24Hour, boolean isTimeUpdate) {
		int i = date.getHours();
		int j = 0;
		int k = 0;
		if (b24Hour) {
			j = i / 10;
			k = i % 10;
			remoteViews.setViewVisibility(R.id.lewa_widget_moment, View.GONE);
		} else {
			remoteViews
					.setViewVisibility(R.id.lewa_widget_moment, View.VISIBLE);
			if (i >= 0 && i < 12) {
				remoteViews.setTextViewText(R.id.lewa_widget_moment, context
						.getResources().getString(R.string.AM));
			} else if (i >= 12) {
				remoteViews.setTextViewText(R.id.lewa_widget_moment, context
						.getResources().getString(R.string.PM));
			}
			if ((i == 0) || (i == 12)) {
				j = 1;
				k = 2;
			} else {
				if ((i > 0) && (i < 12)) {
					j = i / 10;
					k = i % 10;
				} else {
					j = (i - 12) / 10;
					k = (i - 12) % 10;
				}
			}
		}
		if (hour1 == null || isTimeUpdate) {
			WeatherControl.recyleBitmap(hour1);
			hour1 = getHourAndMinuteBitmap(context, String.valueOf(j));
		}
		remoteViews.setImageViewBitmap(R.id.v5_widget_time_hour1, hour1);
		if (hour2 == null || isTimeUpdate) {
			WeatherControl.recyleBitmap(hour2);
			hour2 = getHourAndMinuteBitmap(context, String.valueOf(k));
		}
		remoteViews.setImageViewBitmap(R.id.v5_widget_time_hour2, hour2);
	}

	private static boolean isStartWithone(Context context, Date date,
			boolean b24Hour) {
		int i = date.getHours();
		int j = 0;
		if (b24Hour) {
			j = i / 10;
		} else {
			if ((i == 0) || (i == 12)) {
				j = 1;
			} else {
				if ((i > 0) && (i < 12)) {
					j = i / 10;
				} else {
					j = (i - 12) / 10;
				}
			}
		}
		if (j == 1) {
			return true;
		} else {
			return false;
		}
	}

	private static int getClockNumberResourceId(int resID) {
		return R.drawable.num_0 + resID;
	}

	private static int getDateNumberResourceId(int resID) {
		return R.drawable.num_0 + resID;
	}

	private static void drawMinute(Context context, RemoteViews remoteViews,
			Date date, boolean isTimeUpdate) {
		int i = date.getMinutes();
		int j = i / 10;
		int k = i % 10;
		if (minute1 == null || isTimeUpdate) {
			WeatherControl.recyleBitmap(minute1);
			minute1 = getHourAndMinuteBitmap(context, String.valueOf(j));
		}
		remoteViews.setImageViewBitmap(R.id.v5_widget_time_minute1, minute1);
		if (minute2 == null || isTimeUpdate) {
			WeatherControl.recyleBitmap(minute2);
			minute2 = getHourAndMinuteBitmap(context, String.valueOf(k));
		}
		remoteViews.setImageViewBitmap(R.id.v5_widget_time_minute2, minute2);
	}

	private static Bitmap getConditionFrom(String condition, Context context) {
		final float scale = context.getApplicationContext().getResources()
				.getDisplayMetrics().density;
		Paint paint = new Paint();
		paint.setTextSize(25 * scale);
		paint.setColor(Color.parseColor("#3a3a3a"));
		paint.setAntiAlias(true);
		float tempratureRangeWidth = paint.measureText(condition);
		int translateY = (int) (32 * scale + 0.5f);
		Bitmap bitmap = Bitmap.createBitmap(
				(int) (tempratureRangeWidth + 0.5f), (int) (40 * scale + 0.5f),
				Config.ARGB_8888);
		Canvas canvasTemp = new Canvas(bitmap);

		canvasTemp.drawText(condition, 0, translateY, paint);
		return bitmap;
	}

	public static void updateAppWidget(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			boolean isTimeUpdate, boolean isDateUpdate) {

		RemoteViews updateView = lewaBuildUpdate(context, appWidgetId, false,
				isTimeUpdate, isDateUpdate);
		appWidgetManager.updateAppWidget(appWidgetId, updateView);
	}

	public static boolean updateWidgets(Context context, boolean isTimeUpdate,
			boolean isDateUpdate) {

		if (componentName != null) {
			componentName = null;
		}
		componentName = new ComponentName(context, context.getPackageName()
				+ ".WeatherWidget_time");

		if (appWidgetManger == null) {
			appWidgetManger = AppWidgetManager.getInstance(context);
		}

		if (appWidgetIds != null) {
			appWidgetIds = null;
		}

		appWidgetIds = appWidgetManger.getAppWidgetIds(componentName);
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			WeatherWidget_time.updateAppWidget(context, appWidgetManger,
					appWidgetIds[i], isTimeUpdate, isDateUpdate);
		}
		return N > 0;
	}

	private static ComponentName componentName = null;;
	private static RemoteViews updateView2;
	private static AppWidgetManager appWidgetManger = null;

	public synchronized void updateTimeAlarm(Context context) {

		if (appWidgetManger == null) {
			appWidgetManger = AppWidgetManager.getInstance(context);
		}

		if (componentName != null) {
			componentName = null;
		}
		componentName = new ComponentName(context, context.getPackageName()
				+ ".WeatherWidget_time");
		if (appWidgetIds != null) {
			appWidgetIds = null;
		}
		appWidgetIds = appWidgetManger.getAppWidgetIds(componentName);

		if (appWidgetIds.length <= 0) {
			return;
		}

		for (int i = 0; i < appWidgetIds.length; i++) {
			if (updateView2 != null) {
				updateView2 = null;
			}
			updateView2 = lewaBuildUpdate(context, appWidgetIds[i], false,
					true, true);
			appWidgetManger.updateAppWidget(appWidgetIds[i], updateView2);

			System.gc();
		}
	}

	private static Bitmap getHourAndMinuteBitmap(Context context, String text) {
		int shadow_radius = context.getResources().getInteger(
				R.integer.v5_widget_time_shadow_radius);
		int shadow_dx = context.getResources().getInteger(
				R.integer.v5_widget_time_shadow_dx);
		int shadow_dy = context.getResources().getInteger(
				R.integer.v5_widget_time_shadow_dy);
		final float scale = context.getApplicationContext().getResources()
				.getDisplayMetrics().density;
		Paint paint = new Paint();
		paint.setTextSize(context.getResources().getInteger(
				R.integer.v5_widget_hour_text_size));
		paint.setColor(Color.WHITE);
		Typeface face = Typeface.createFromAsset(context.getAssets(),
				"NeoSans-Light.otf");
		paint.setTypeface(face);
		paint.setAntiAlias(true);
		paint.setFakeBoldText(false);
		paint.setShadowLayer(shadow_radius, shadow_dx, shadow_dy, Color.DKGRAY);
		paint.setShadowLayer(shadow_radius, shadow_dx, shadow_dy, Color.DKGRAY);
		paint.setFilterBitmap(true);
		int translateY = (int) (63 * scale + 0.5f);
		float tempratureRangeWidth = paint.measureText(text);
		Bitmap bitmap = Bitmap.createBitmap(
				(int) (tempratureRangeWidth + 0.5f), (int) (67 * scale + 0.5f),
				Config.ARGB_8888);
		Canvas canvasTemp = new Canvas(bitmap);
		canvasTemp.drawText(text, 0, translateY, paint);
		return bitmap;
	}

	private static Bitmap createTextBitmap(Context context, String text,
			int size, int transy, boolean isSetShadow) {
		final float scale = context.getApplicationContext().getResources()
				.getDisplayMetrics().density;
		Paint paint = new Paint();
		Rect rect = new Rect();
		paint.getTextBounds(text, 0, text.length(), rect);
		paint.setTextSize(size);
		paint.setColor(Color.WHITE);
		Typeface face = Typeface.createFromAsset(context.getAssets(),
				"NeoSans-Light.otf");
		paint.setTypeface(face);
		paint.setAntiAlias(true);

		if (isSetShadow)
			paint.setShadowLayer(5, 1, 2, Color.DKGRAY);
		int translateY = (int) (transy * scale + 0.5f);

		float tempratureRangeWidth = paint.measureText(text);
		Bitmap bitmap = Bitmap.createBitmap(
				(int) (tempratureRangeWidth + 0.5f), (int) (20 * scale + 0.5f),
				Config.ARGB_8888);
		Canvas canvasTemp = new Canvas(bitmap);
		canvasTemp.drawText(text, 0, translateY, paint);
		return bitmap;
	}

	public static int getFontHeight(float fontSize) {
		Paint paint = new Paint();
		paint.setTextSize(fontSize);
		FontMetrics fm = paint.getFontMetrics();
		return (int) Math.ceil(fm.descent - fm.top) + 2;
	}

	public static void updateTime(RemoteViews updateViews, Context context,
			Date date, boolean bool, boolean isTimeUpdate) {
		drawHour(context, updateViews, date, bool, isTimeUpdate);
		drawMinute(context, updateViews, date, isTimeUpdate);
	}

	public static void updateDate(RemoteViews updateViews, Context context,
			boolean isDateUpdate) {
		Calendar calendar = Calendar.getInstance();
		int mounth = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
		int v5_widget_solar_text_size = context.getResources().getInteger(
				R.integer.v5_widget_solar_text_size);
		int v5_widget_lunar_text_size = context.getResources().getInteger(
				R.integer.v5_widget_lunar_text_size);
		int transy = context.getResources().getInteger(
				R.integer.v5_widget_transy);
		int solar_transy = context.getResources().getInteger(
				R.integer.v5_widget_solar_transy);
		String weekdayString = DataFormatControl
				.dayOfWeekV5(context, dayofweek);
		LunarItem item = new LunarItem(calendar);
		String lunarString = item.getChinaCurrentMonthAndDayString();
		StringBuilder builder = new StringBuilder();
		if (String.valueOf(mounth).length() <= 1)
			builder.append(0);
		String dayString = String.valueOf(day);
		if (dayString.length() <= 1)
			dayString = "0" + day;
		builder.append(mounth + "." + dayString);
		if (solarBitmap == null || isDateUpdate) {
			WeatherControl.recyleBitmap(solarBitmap);
			solarBitmap = WeatherControl.createTextBitmap(context,
					builder.toString(), v5_widget_solar_text_size,
					solar_transy, true);
		}
		updateViews.setImageViewBitmap(R.id.v5_widget_date_solar, solarBitmap);
		if (WeatherControl.isLanguageZhCn() || WeatherControl.isLanguageZhTw()) {
			if (lunarBitmap == null || isDateUpdate) {
				WeatherControl.recyleBitmap(lunarBitmap);
				lunarBitmap = WeatherControl.createTextBitmap(context,
						context.getString(R.string.lunar) + lunarString,
						v5_widget_lunar_text_size, transy, true);
			}
			updateViews.setImageViewBitmap(R.id.v5_widget_date_lunar,
					lunarBitmap);
		}
		if (weekBitmap == null || isDateUpdate) {
			WeatherControl.recyleBitmap(weekBitmap);
			weekBitmap = WeatherControl.createTextBitmap(context,
					weekdayString, v5_widget_lunar_text_size, transy, true);
		}
		updateViews.setImageViewBitmap(R.id.v5_widget_week, weekBitmap);
	}

}
