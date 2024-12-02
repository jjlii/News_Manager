package es.upm.etsiinf.pmd.pmdproject1920.Task;

import android.os.AsyncTask;

import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.exceptions.ServerCommunicationError;

public class DeleteImageTask extends AsyncTask<Integer, Void, Boolean> {
    @Override
    protected Boolean doInBackground(Integer... integers) {
        boolean res= false;
        if (ModelManager.isConnected()){
            try {
                ModelManager.deleteImage(integers[0]);
                res = true;
            } catch (ServerCommunicationError serverCommunicationError) {
                serverCommunicationError.printStackTrace();
                res = false;
            }
        }
        return res;
    }
}
