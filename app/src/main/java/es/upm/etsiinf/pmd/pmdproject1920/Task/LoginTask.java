package es.upm.etsiinf.pmd.pmdproject1920.Task;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.exceptions.AuthenticationError;

public class LoginTask extends AsyncTask<List<String>, Void, Boolean> {


    @Override
    protected Boolean doInBackground(List<String>... credentials) {
        try {
            ModelManager.login(credentials[0].get(0), credentials[0].get(1));
            return true;
        } catch (AuthenticationError e) {
            return false;
        }
    }
}
