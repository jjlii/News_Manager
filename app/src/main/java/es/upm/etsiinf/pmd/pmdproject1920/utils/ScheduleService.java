package es.upm.etsiinf.pmd.pmdproject1920.utils;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import java.util.Date;

public class ScheduleService extends JobService {

    private static String TAG = "ScheduleService";
    private SharedPreferences preferences;
    private Long lastPolling;
    private Date timeBefore;
    private Date timeAfter;
    private static final String DATE_FORMAT_MYSQL = "yyyy-MM-dd hh:mm:ss";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "Job executed !!");
        preferences = getSharedPreferences("PrefsFileDate", Context.MODE_PRIVATE);
        lastPolling = preferences.getLong("last_polling", 0);
        SimpleDateFormat sdfDate = new SimpleDateFormat(DATE_FORMAT_MYSQL);

        if (lastPolling == 0){
            timeBefore = new Date();
            timeBefore.setTime(timeBefore.getTime()-900000);
        }else {


        }


        //reprogramar la tarea
        utils.scheduleJob(getApplicationContext());
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

}
