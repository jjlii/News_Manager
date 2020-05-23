package es.upm.etsiinf.pmd.pmdproject1920.utils;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.upm.etsiinf.pmd.pmdproject1920.MainActivity;
import es.upm.etsiinf.pmd.pmdproject1920.Task.GetArticleDateTask;
import es.upm.etsiinf.pmd.pmdproject1920.bbdd.BBDDArticle;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;
import es.upm.etsiinf.pmd.pmdproject1920.model.Article;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.exceptions.ServerCommunicationError;

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
            timeBefore = new Date(lastPolling);
            preferences.edit().putLong("last_polling", Calendar.getInstance().getTimeInMillis()).apply();
        }
        List<Article> result = new ArrayList<Article>();
        try {
            result = new GetArticleDateTask().execute(sdfDate.format(timeBefore)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        for(Article article:  result){
            if (BBDDArticle.exist(article.getId())){
                BBDDArticle.updateArticulo(article);
                MainActivity.sendNotification("El artículo: "+ article.getTitleText() +" ha sido modificado.", article.getSubtitleText());
            }
            else{
                BBDDArticle.insertArticle(article);
                MainActivity.sendNotification("El artículo: "+ article.getTitleText() +" ha sido creado.", article.getSubtitleText());
            }

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
