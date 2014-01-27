package com.phyous.prism.service.task;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.phyous.prism.service.NotifyService;

import java.util.Calendar;

public class AlarmTask implements Runnable{
	private final Calendar date;
	private final AlarmManager am;
	private final Context context;

	public AlarmTask(Context context, Calendar date) {
		this.context = context;
		this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		this.date = date;
	}
	
	@Override
	public void run() {
		final Intent intent = new Intent(context, NotifyService.class);
		intent.putExtra(NotifyService.INTENT_NOTIFY, true);
		final PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
		
		am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                date.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent);
	}
}
