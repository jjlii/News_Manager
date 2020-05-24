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
import es.upm.etsiinf.pmd.pmdproject1920.Task.AsyncResponse;
import es.upm.etsiinf.pmd.pmdproject1920.Task.GetArticleDateTask;
import es.upm.etsiinf.pmd.pmdproject1920.bbdd.BBDDArticle;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;
import es.upm.etsiinf.pmd.pmdproject1920.model.Article;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.exceptions.ServerCommunicationError;

public class ScheduleService extends JobService {

    private static String TAG = "ScheduleService";
    private SharedPreferences preferences;
    private String lastPolling;
    private static final String DATE_FORMAT_MYSQL = "yyyy-MM-dd HH:mm:ss";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "Job executed !!");
        preferences = getSharedPreferences("PrefsFileDate", Context.MODE_PRIVATE);
        lastPolling = preferences.getString("last_polling", null);
        SimpleDateFormat sdfDate = new SimpleDateFormat(DATE_FORMAT_MYSQL);
        if (lastPolling == null){
            //TODO MAl
            List<Article> articles = BBDDArticle.loadAllArticles();
            articles =  utils.sortArticlesByDates(articles);
            lastPolling = SerializationUtils.dateToString(articles.get(0).getLastUpdate());
        }
        new GetArticleDateTask(new AsyncResponse() {
            @Override
            public void processFinish(List<Article> articles) {
                if(!articles.isEmpty())preferences.edit().putString("last_polling", SerializationUtils.dateToString(articles.get(0).getLastUpdate())).apply();
                for(Article article:  articles){
                    if (BBDDArticle.exist(article.getId())){
                        BBDDArticle.updateArticulo(article);
                        MainActivity.sendNotification("El artículo: \""+ article.getTitleText() +"\" ha sido modificado.", article.getSubtitleText());
                    }
                    else{
                        BBDDArticle.insertArticle(article);
                        MainActivity.sendNotification("El artículo: \""+ article.getTitleText() +"\" ha sido creado.", article.getSubtitleText());
                    }
                }
            }
        }).execute(lastPolling);
        utils.scheduleJob(getApplicationContext());
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

}
