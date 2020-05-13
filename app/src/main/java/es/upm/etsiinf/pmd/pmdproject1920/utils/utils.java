package es.upm.etsiinf.pmd.pmdproject1920.utils;

import android.app.AlertDialog;
import android.content.Context;

import java.util.concurrent.ExecutionException;

import es.upm.etsiinf.pmd.pmdproject1920.Task.DeleteArticleTask;

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
}
