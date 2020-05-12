package es.upm.etsiinf.pmd.pmdproject1920.Task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ProgressBar;

import java.util.List;

import es.upm.etsiinf.pmd.pmdproject1920.MainActivity;
import es.upm.etsiinf.pmd.pmdproject1920.R;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.exceptions.AuthenticationError;

public class LoginTask extends AsyncTask<List<String>, Void, Boolean> {

    private AlertDialog alertDialog;
    @SuppressLint("StaticFieldLeak")
    private Activity activity;
    public LoginTask(Activity myActivity){
        activity=myActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        LayoutInflater i = activity.getLayoutInflater();
        alertDialog = new AlertDialog.Builder(activity)
                .setView(i.inflate(R.layout.fullscreen_loading_dialog,null))
                .setCancelable(true)
                .create();
        alertDialog.show();
    }

    @Override
    protected Boolean doInBackground(List<String>... credentials) {
        try {
            ModelManager.login(credentials[0].get(0), credentials[0].get(1));
            return true;
        } catch (AuthenticationError e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (alertDialog.isShowing()){
            alertDialog.dismiss();
        }
    }
}
