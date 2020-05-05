package es.upm.etsiinf.pmd.pmdproject1920.Task;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import es.upm.etsiinf.pmd.pmdproject1920.model.Article;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.exceptions.AuthenticationError;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.exceptions.ServerCommunicationError;
public class LoadArticlesTask extends AsyncTask<List<String>, Void, List<Article>> {
    
	private static final String TAG = "LoadArticlesTask";
    
	@Override
    protected List<Article> doInBackground(List<String>... credentials) {
        List<Article> res = null;
		//ModelManager uses singleton pattern, connecting once per app execution in enough
        String strIdUser = ModelManager.getLoggedIdUSer();
        String strApiKey = ModelManager.getLoggedApiKey();
        String strIdAuthUser = ModelManager.getLoggedAuthType();
        if (!ModelManager.isConnected()){
			// if it is the first login
            if (strIdUser==null || strIdUser.equals("")) {
                try {
                    ModelManager.login(credentials[0].get(0),credentials[0].get(1));
                } catch (AuthenticationError e) {
                    Log.e(TAG, e.getMessage());
                }
            }
			// if we have saved user credentials from previous connections
			else{
                ModelManager.stayloggedin(strIdUser,strApiKey,strIdAuthUser);
            }
        }
		//If connection has been successful
        if (ModelManager.isConnected()) {
            try {
				// obtain 6 articles from offset 0
                res = ModelManager.getArticles(20, 0);
                for (Article article : res) {
					// We print articles in Log
                    Log.i(TAG, String.valueOf(article));
                }
            } catch (ServerCommunicationError e) {
                Log.e(TAG,e.getMessage());
            }
        }
        return res;
    }
}
