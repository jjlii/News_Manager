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

    @Override
    protected Article doInBackground(String... articleId) {
        Article res = null;
        int id = Integer.parseInt(articleId[0]);
        try {
            res = ModelManager.getArticle(id);
        } catch (ServerCommunicationError serverCommunicationError) {
            serverCommunicationError.printStackTrace();
        }
        return res;
    }
}
