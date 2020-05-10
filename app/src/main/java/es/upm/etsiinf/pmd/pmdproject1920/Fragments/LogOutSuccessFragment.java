package es.upm.etsiinf.pmd.pmdproject1920.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import es.upm.etsiinf.pmd.pmdproject1920.MainActivity;
import es.upm.etsiinf.pmd.pmdproject1920.R;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;

import static androidx.navigation.Navigation.findNavController;

public class LogOutSuccessFragment extends Fragment {


    private Button btn_go_home;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).setVisibility(View.GONE, false);
        btn_go_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(v).navigate(LogOutSuccessFragmentDirections.actionLogOutSuccessToHome());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_out_success, container, false);
        btn_go_home = view.findViewById(R.id.btn_go_home);
        return view;
    }
}
