package es.upm.etsiinf.pmd.pmdproject1920.Task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.view.LayoutInflater;

import es.upm.etsiinf.pmd.pmdproject1920.R;
import es.upm.etsiinf.pmd.pmdproject1920.model.Article;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.exceptions.ServerCommunicationError;

public class DetailArticleTask  extends AsyncTask<String, Void, Article>{

    private AlertDialog alertDialog;
    @SuppressLint("StaticFieldLeak")
    private Activity activity;

    public DetailArticleTask(Activity myActivity){
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
    protected Article doInBackground(String... articleId) {
        Article res = null;
        int id = Integer.parseInt(articleId[0]);
        if (ModelManager.isConnected()) {
            try {
                res = ModelManager.getArticle(id);
            } catch (ServerCommunicationError serverCommunicationError) {
                serverCommunicationError.printStackTrace();
            }

        }
        return res;
    }

    @Override
    protected void onPostExecute(Article article) {
        super.onPostExecute(article);
        alertDialog.dismiss();
    }
}
