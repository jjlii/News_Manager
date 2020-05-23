package es.upm.etsiinf.pmd.pmdproject1920.utils;

import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.upm.etsiinf.pmd.pmdproject1920.Task.DeleteArticleTask;
import es.upm.etsiinf.pmd.pmdproject1920.model.Article;

public class utils {

    public static final int SCHEDULE_SERVICE_ID = 0;

    public static void showInfoDialog(Context context, String msg){
        new AlertDialog.Builder(context)
                .setTitle("Warning")
                .setMessage(msg).setNegativeButton("Ok", null)
                .show();
    }


    public static boolean deleteAction(String articleId){
        boolean deleteResult;
        try {
            deleteResult = new DeleteArticleTask().execute(articleId).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            deleteResult=false;
        }
        return deleteResult;
    }

    public static List<Article> sortArticlesByDates(List<Article> articles){
        Collections.sort(articles, new Comparator<Article>() {
            @Override
            public int compare(Article o1, Article o2) {
                return o2.getLastUpdate().compareTo(o1.getLastUpdate());
            }
        });
        return articles;
    }

    public static void scheduleJob(Context context){
        ComponentName serviceComponent = new ComponentName(context, ScheduleService.class);



        JobInfo.Builder builder = new JobInfo.Builder(SCHEDULE_SERVICE_ID, serviceComponent);

        builder.setMinimumLatency(900000);
        builder.setOverrideDeadline(960000);
        builder.setPersisted(true);
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());

    }


}
