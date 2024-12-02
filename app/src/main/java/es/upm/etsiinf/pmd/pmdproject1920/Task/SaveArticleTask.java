package es.upm.etsiinf.pmd.pmdproject1920.Task;

import android.os.AsyncTask;

import es.upm.etsiinf.pmd.pmdproject1920.model.Article;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.exceptions.ServerCommunicationError;

public class SaveArticleTask extends AsyncTask<Article, Void, Integer> {

    @Override
    protected Integer doInBackground(Article... articles) {
        int res= -1;
        if (ModelManager.isConnected()){
            try {
                res = ModelManager.saveArticle(articles[0]);
            } catch (ServerCommunicationError serverCommunicationError) {
                serverCommunicationError.printStackTrace();
            }
        }
        return res;
    }

}
