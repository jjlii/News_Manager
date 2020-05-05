package es.upm.etsiinf.pmd.pmdproject1920.Task;

import android.os.AsyncTask;

import java.util.List;

import es.upm.etsiinf.pmd.pmdproject1920.model.Article;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.exceptions.ServerCommunicationError;

public class AllArticlesTask extends AsyncTask<Void, Void, List<Article>> {

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
}
