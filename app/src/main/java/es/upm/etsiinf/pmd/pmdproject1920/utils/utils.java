package es.upm.etsiinf.pmd.pmdproject1920.utils;

import android.app.AlertDialog;
import android.content.Context;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.upm.etsiinf.pmd.pmdproject1920.Task.DeleteArticleTask;
import es.upm.etsiinf.pmd.pmdproject1920.model.Article;

public class utils {

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


}
