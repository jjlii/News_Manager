package es.upm.etsiinf.pmd.pmdproject1920.Task;

import android.os.AsyncTask;

import java.util.List;

import es.upm.etsiinf.pmd.pmdproject1920.model.Article;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.exceptions.ServerCommunicationError;
import es.upm.etsiinf.pmd.pmdproject1920.utils.utils;

public class GetArticleDateTask extends AsyncTask<String, Void, List<Article>> {

    public AsyncResponse delegate = null;

    public GetArticleDateTask(AsyncResponse delegate){
        this.delegate =  delegate;
    }

    @Override
    protected List<Article> doInBackground(String... strings) {
        List<Article> res = null;
        try {
            res = ModelManager.getArticlesDate(strings[0]);
            res = utils.sortArticlesByDates(res);
        } catch (ServerCommunicationError serverCommunicationError) {
            serverCommunicationError.printStackTrace();
        }
        return res;
    }

    @Override
    protected void onPostExecute(List<Article> articles) {
        delegate.processFinish(articles);
    }
}
