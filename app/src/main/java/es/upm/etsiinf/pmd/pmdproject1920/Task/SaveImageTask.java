package es.upm.etsiinf.pmd.pmdproject1920.Task;

import android.os.AsyncTask;

import es.upm.etsiinf.pmd.pmdproject1920.model.Image;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.exceptions.ServerCommunicationError;

public class SaveImageTask extends AsyncTask<Image, Void, Integer> {
    @Override
    protected Integer doInBackground(Image... images) {
        int res= -1;
        if (ModelManager.isConnected()){
            try {
                res = ModelManager.saveImage(images[0]);
            } catch (ServerCommunicationError serverCommunicationError) {
                serverCommunicationError.printStackTrace();
            }
        }
        return res;
    }
}
