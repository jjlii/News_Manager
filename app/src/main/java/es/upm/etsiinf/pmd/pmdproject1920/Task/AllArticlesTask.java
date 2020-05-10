package es.upm.etsiinf.pmd.pmdproject1920.Task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.view.LayoutInflater;

import java.util.List;

import es.upm.etsiinf.pmd.pmdproject1920.R;
import es.upm.etsiinf.pmd.pmdproject1920.model.Article;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.exceptions.ServerCommunicationError;

public class AllArticlesTask extends AsyncTask<Void, Void, List<Article>> {
    private AlertDialog alertDialog;
    @SuppressLint("StaticFieldLeak")
    private Activity activity;

    public AllArticlesTask(Activity myActivity){
        activity = myActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        LayoutInflater i = activity.getLayoutInflater();
        alertDialog = new AlertDialog.Builder(activity)
                .setView(i.inflate(R.layout.fullscreen_loading_dialog,null))
                .setCancelable(false)
                .create();
        alertDialog.show();
    }

    @Override
    protected List<Article> doInBackground(Void... voids) {
        List<Article> res = null;
        try {
            res = ModelManager.getArticles();
        } catch (ServerCommunicationError serverCommunicationError) {
            serverCommunicationError.printStackTrace();
        }
        return res;
    }

    @Override
    protected void onPostExecute(List<Article> articles) {
        super.onPostExecute(articles);
        alertDialog.dismiss();
    }
}
