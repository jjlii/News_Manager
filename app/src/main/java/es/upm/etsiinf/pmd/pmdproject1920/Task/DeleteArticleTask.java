package es.upm.etsiinf.pmd.pmdproject1920.Task;

import android.os.AsyncTask;
import android.util.Log;

import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.exceptions.ServerCommunicationError;

public class DeleteArticleTask extends AsyncTask<String,Void,Boolean> {
    @Override
    protected Boolean doInBackground(String... articleId) {
        if (ModelManager.isConnected()) {
            int id = Integer.parseInt(articleId[0]);
            try {
                ModelManager.deleteArticle(id);
                return true;
            } catch (ServerCommunicationError serverCommunicationError) {
                Log.e("DeleteArticleTaskError","Error deleting article");
                return false;
            }
        }
        return false;
    }
}
