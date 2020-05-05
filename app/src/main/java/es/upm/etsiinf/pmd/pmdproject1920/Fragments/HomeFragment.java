package es.upm.etsiinf.pmd.pmdproject1920.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.upm.etsiinf.pmd.pmdproject1920.Adapter.NewsAdapter;
import es.upm.etsiinf.pmd.pmdproject1920.Task.AllArticlesTask;
import es.upm.etsiinf.pmd.pmdproject1920.R;
import es.upm.etsiinf.pmd.pmdproject1920.Task.LoadArticlesTask;
import es.upm.etsiinf.pmd.pmdproject1920.model.Article;

import static androidx.navigation.Navigation.findNavController;


public class HomeFragment extends Fragment {

    private SharedPreferences preferences;

    private RecyclerView rv;
    private ProgressBar progressBar;
    private List<Article> articles = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private NewsAdapter adapter;
    private FloatingActionButton fb_login;
    private String user, pwd;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rv = view.findViewById(R.id.rv_news);
        progressBar = view.findViewById(R.id.progress);
        fb_login = view.findViewById(R.id.fb_login);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPreferencesData();
        try {
            if(null==user || null==pwd ){
                articles = new AllArticlesTask().execute().get();
            }else {
                List<String> credentials = new ArrayList<>();
                credentials.add(0,user);
                credentials.add(1,pwd);
                articles = new LoadArticlesTask().execute(credentials).get();
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        fb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(v).navigate(HomeFragmentDirections.actionHomeToLogIn());
            }
        });
        setVisible();
        showRecyclerView();
    }

    private void setVisible(){
        progressBar.setVisibility(View.GONE);
        rv.setVisibility(View.VISIBLE);
    }

    private void showRecyclerView(){
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

    private void getPreferencesData(){
        preferences = getActivity().getSharedPreferences("PrefsFile", Context.MODE_PRIVATE);
        if (preferences.contains("pref_pwd")){
            user = preferences.getString("pref_user", null);
        }
        if (preferences.contains("pref_pwd")){
            pwd =  preferences.getString("pref_pwd", null);
        }

    }
}
