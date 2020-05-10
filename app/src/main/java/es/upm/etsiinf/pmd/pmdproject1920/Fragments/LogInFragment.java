package es.upm.etsiinf.pmd.pmdproject1920.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.upm.etsiinf.pmd.pmdproject1920.MainActivity;
import es.upm.etsiinf.pmd.pmdproject1920.R;
import es.upm.etsiinf.pmd.pmdproject1920.Task.LoadArticlesTask;
import es.upm.etsiinf.pmd.pmdproject1920.Task.LoginTask;
import es.upm.etsiinf.pmd.pmdproject1920.model.Article;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.exceptions.AuthenticationError;

import static androidx.navigation.Navigation.findNavController;

public class LogInFragment extends Fragment {

    private SharedPreferences preferences;

    private TextInputEditText et_user, et_pwd;
    private CheckBox cb_remember;
    private Button btn_log_in;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setVisibility(View.GONE, ModelManager.isConnected());
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        et_user = view.findViewById(R.id.et_user);
        et_pwd = view.findViewById(R.id.et_pwd);
        cb_remember = view.findViewById(R.id.cb_remember);
        btn_log_in = view.findViewById(R.id.btn_log_in);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = getActivity().getSharedPreferences("PrefsFile", Context.MODE_PRIVATE);
        btn_log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String user = et_user.getText().toString();
                String pwd =et_pwd.getText().toString();
                List<String> credentials = new ArrayList<>();
                credentials.add(0,user);
                credentials.add(1, pwd);
                Boolean loginSuccess = false;
                try {
                    loginSuccess = new LoginTask().execute(credentials).get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    loginSuccess = false;
                }
                if (loginSuccess){
                    if (cb_remember.isChecked()){
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("pref_user", et_user.getText().toString());
                        editor.putString("pref_pwd", et_pwd.getText().toString());
                        editor.apply();
                    }else {
                        preferences.edit().clear().apply();
                    }
                    findNavController(v).navigate(LogInFragmentDirections.actionLogInToHome());
                }else {
                    Toast.makeText(getContext(),"Authentification Error, try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
