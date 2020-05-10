package es.upm.etsiinf.pmd.pmdproject1920.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.upm.etsiinf.pmd.pmdproject1920.Adapter.NewsAdapter;
import es.upm.etsiinf.pmd.pmdproject1920.MainActivity;
import es.upm.etsiinf.pmd.pmdproject1920.Task.AllArticlesTask;
import es.upm.etsiinf.pmd.pmdproject1920.R;
import es.upm.etsiinf.pmd.pmdproject1920.Task.LoginTask;
import es.upm.etsiinf.pmd.pmdproject1920.model.Article;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;

import static androidx.navigation.Navigation.findNavController;


public class HomeFragment extends Fragment {

    private SharedPreferences preferences;

    private RecyclerView rv;
    private List<Article> articles = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private NewsAdapter adapter;
    private FloatingActionButton fb_login, fb_create, fb_log_out;
    private String user, pwd;
    private View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        rv = fragmentView.findViewById(R.id.rv_news);
        fb_login = getActivity().findViewById(R.id.fb_login);
        fb_create = getActivity().findViewById(R.id.fb_create);
        fb_log_out = getActivity().findViewById(R.id.fb_log_out);
        return fragmentView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPreferencesData();
        fb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(fragmentView).navigate(HomeFragmentDirections.actionHomeToLogIn());
            }
        });
        fb_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(fragmentView).navigate(HomeFragmentDirections.actionHomeToEditArticle());
            }
        });
        fb_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSharedPreferences("PrefsFile", Context.MODE_PRIVATE)
                        .edit().clear().apply();
                ModelManager.getRc().clear();
                findNavController(fragmentView).navigate(HomeFragmentDirections.actionHomeToLogOut());
            }
        });
        ((MainActivity)getActivity()).setLoading(true);
        getArticles();
        showRecyclerView();
        ((MainActivity)getActivity()).setLoading(false);
    }

    private void showRecyclerView(){
        rv.setVisibility(View.VISIBLE);
        layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        adapter = new NewsAdapter(articles, getActivity(), new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                findNavController(view).navigate(HomeFragmentDirections.actionHomeToArticleDetail(articles.get(position).getId()));
            }
        });
        rv.setAdapter(adapter);
    }

    private void getArticles()  {
        boolean loginSuccess = false;
        if (!ModelManager.isConnected() &&
                null!=user
                && null!=pwd){
            List<String> credentials= new ArrayList<>();
            credentials.add(0,user);
            credentials.add(1,pwd);
            try {
                loginSuccess = new LoginTask(getActivity()).execute(credentials).get();
            } catch (ExecutionException | InterruptedException e) {
                loginSuccess = false;
            }
        }
        try {
            articles = new AllArticlesTask(getActivity()).execute().get();
        }catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        ((MainActivity)getActivity()).setArticles(articles);
        ((MainActivity)getActivity()).setVisibility(View.VISIBLE, ModelManager.isConnected());
    }

    private void getPreferencesData(){
        preferences = getActivity().getSharedPreferences("PrefsFile", Context.MODE_PRIVATE);
        user = preferences.getString("pref_user", null);
        pwd =  preferences.getString("pref_pwd", null);
    }


}
