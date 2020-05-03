package es.upm.etsiinf.pmd.pmdproject1920.Task;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import es.upm.etsiinf.pmd.pmdproject1920.Fragments.ArticleDetailsFragmentArgs;
import es.upm.etsiinf.pmd.pmdproject1920.model.Article;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.exceptions.ServerCommunicationError;

public class DetailArticleTask  extends AsyncTask<String, Void, Article>{



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
}
