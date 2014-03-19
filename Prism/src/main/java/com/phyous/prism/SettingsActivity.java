package com.phyous.prism;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;

import com.phyous.prism.service.ReminderService;

public class SettingsActivity extends Activity {
    private CheckBox mCheckBox;
    private TimePicker mTimePicker;
    private static final String PREFS_NAME = "PrisimPrefs";
    private static final String PREF_REMINDER_CHECKBOX = "reminderCheckbox";
    private static final String PREF_REMINDER_HOUR = "reminderhour";
    private static final String PREF_REMINDER_MINUTE = "reminderMinute";

    private static final boolean REMINDER_CHECKBOX_DEFAULT = true;
    private static final int REMINDER_HOUR_DEFAULT = 22;
    private static final int REMINDER_MINUTE_DEFAULT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mCheckBox = (CheckBox) findViewById(R.id.reminder_checkbox);
        mTimePicker = (TimePicker) findViewById(R.id.time_picker);

        if (savedInstanceState != null) {
            mCheckBox.setChecked(savedInstanceState.getBoolean(PREF_REMINDER_CHECKBOX, false));
            mTimePicker.setCurrentHour(savedInstanceState.getInt(PREF_REMINDER_HOUR));
            mTimePicker.setCurrentMinute(savedInstanceState.getInt(PREF_REMINDER_MINUTE));
        } else {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            mCheckBox.setChecked(
                    settings.getBoolean(PREF_REMINDER_CHECKBOX, REMINDER_CHECKBOX_DEFAULT));
            mTimePicker.setCurrentHour(
                    settings.getInt(PREF_REMINDER_HOUR, REMINDER_HOUR_DEFAULT));
            mTimePicker.setCurrentMinute(
                    settings.getInt(PREF_REMINDER_MINUTE, REMINDER_MINUTE_DEFAULT));
        }

        Button doneButton = (Button) findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save preferences
                savePreferences();

                if (mCheckBox.isChecked()) {
                    Intent intent = new Intent(getApplicationContext(), ReminderService.class);
                    intent.putExtra(ReminderService.HOUR_EXTRA, mTimePicker.getCurrentHour());
                    intent.putExtra(ReminderService.MINUTE_EXTRA, mTimePicker.getCurrentMinute());
                    intent.putExtra(ReminderService.SECOND_EXTRA, 0);

                    startService(intent);
                }

                finish();
            }
        });
    }

    private void savePreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(PREF_REMINDER_CHECKBOX, mCheckBox.isChecked());
        editor.putInt(PREF_REMINDER_HOUR, mTimePicker.getCurrentHour());
        editor.putInt(PREF_REMINDER_MINUTE, mTimePicker.getCurrentMinute());
        editor.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(PREF_REMINDER_CHECKBOX, mCheckBox.isChecked());
        outState.putInt(PREF_REMINDER_HOUR, mTimePicker.getCurrentHour());
        outState.putInt(PREF_REMINDER_MINUTE, mTimePicker.getCurrentMinute());
    }
}
