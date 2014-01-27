package com.phyous.prism.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.phyous.prism.service.task.AlarmTask;

import java.util.Calendar;

public class ScheduleService extends Service {
	public class ServiceBinder extends Binder {
		ScheduleService getService() {
			return ScheduleService.this;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private final IBinder mBinder = new ServiceBinder();

	public void setAlarm(Calendar c) {
		new AlarmTask(this, c).run();
	}
}