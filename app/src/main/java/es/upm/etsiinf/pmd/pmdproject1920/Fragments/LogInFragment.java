package es.upm.etsiinf.pmd.pmdproject1920.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.material.textfield.TextInputEditText;

import es.upm.etsiinf.pmd.pmdproject1920.MainActivity;
import es.upm.etsiinf.pmd.pmdproject1920.R;

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
        ((MainActivity)getActivity()).setVisibility(View.GONE);
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        et_user = view.findViewById(R.id.et_user);
        et_pwd = view.findViewById(R.id.et_pwd);
        cb_remember = view.findViewById(R.id.cb_remember);
        btn_log_in = view.findViewById(R.id.btn_log_in);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = getActivity().getSharedPreferences("PrefsFile", Context.MODE_PRIVATE);
        btn_log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_remember.isChecked()){
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("pref_user", et_user.getText().toString());
                    editor.putString("pref_pwd", et_pwd.getText().toString());
                    editor.apply();
                }else {
                    preferences.edit().clear().apply();
                }
            }
        });
    }
}
