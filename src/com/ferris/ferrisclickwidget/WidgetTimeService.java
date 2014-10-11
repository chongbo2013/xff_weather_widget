package com.ferris.ferrisclickwidget;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;


public class WidgetTimeService extends Service{
	UpdateReceiver widgetsUpdateReceiver = new UpdateReceiver();
	public static Intent FERRIS_UPDATE_WIDGET =null;
	public static IntentFilter intentFilte=null;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if(intentFilte==null){
			intentFilte = new IntentFilter();
		}
		intentFilte.addAction(Intent.ACTION_TIME_TICK);
		intentFilte.addAction(Intent.ACTION_TIME_CHANGED);
		intentFilte.addAction(Intent.ACTION_DATE_CHANGED);
		intentFilte.addAction(Intent.ACTION_TIMEZONE_CHANGED);
		this.getBaseContext().registerReceiver(widgetsUpdateReceiver, intentFilte);
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
//		sendBroadcast(new Intent("com.ferris.ferrisclickwidget.widgettimeservice"));
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	class UpdateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			updateWidget();
		}
		
	}
	
	public synchronized void updateWidget() {
		if(FERRIS_UPDATE_WIDGET!=null){
			FERRIS_UPDATE_WIDGET=null;
		}
		FERRIS_UPDATE_WIDGET=new Intent(WeatherControl.FERRIS_UPDATE_WIDGET);
		sendBroadcast(FERRIS_UPDATE_WIDGET);
		System.gc();
	}
}
