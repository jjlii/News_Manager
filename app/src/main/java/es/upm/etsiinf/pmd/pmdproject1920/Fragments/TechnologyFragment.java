package es.upm.etsiinf.pmd.pmdproject1920.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import es.upm.etsiinf.pmd.pmdproject1920.Adapter.NewsAdapter;
import es.upm.etsiinf.pmd.pmdproject1920.MainActivity;
import es.upm.etsiinf.pmd.pmdproject1920.R;
import es.upm.etsiinf.pmd.pmdproject1920.model.Article;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;

import static androidx.navigation.Navigation.findNavController;


public class TechnologyFragment extends Fragment {

    private List<Article> articles;
    private RecyclerView rv;
    private RecyclerView.LayoutManager layoutManager;
    private NewsAdapter adapter;
    private FloatingActionButton fb_login,fb_create, fb_log_out;
    private View fragmentView;



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)getActivity()).setVisibility(View.VISIBLE, ModelManager.isConnected());
        articles = ((MainActivity)getActivity()).filterArticles("Technology");
        fb_create = getActivity().findViewById(R.id.fb_create);
        fb_log_out = getActivity().findViewById(R.id.fb_log_out);
        fb_login = getActivity().findViewById(R.id.fb_login);
        fb_create = getActivity().findViewById(R.id.fb_create);
        fb_log_out = getActivity().findViewById(R.id.fb_log_out);
        fb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(fragmentView).navigate(TechnologyFragmentDirections.actionTechToLogIn());
            }
        });
        fb_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(fragmentView).navigate(TechnologyFragmentDirections.actionTechnologyToEditArticle(-1));
            }
        });
        fb_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Log out")
                        .setMessage("Are you sure you want to log out the account?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().getSharedPreferences("PrefsFile", Context.MODE_PRIVATE)
                                        .edit().clear().apply();
                                ModelManager.getRc().clear();
                                findNavController(fragmentView).navigate(TechnologyFragmentDirections.actionTechToLogOut());
                            }
                        }).setNegativeButton("No", null)
                        .show();
            }
        });
        showRecyclerView();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_technology, container, false);
        rv = fragmentView.findViewById(R.id.rv_tech);
        return fragmentView;
    }

    private void showRecyclerView(){
        layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        adapter = new NewsAdapter(articles, getActivity(), new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                findNavController(view).navigate(TechnologyFragmentDirections.actionTechToArticleDetail(articles.get(position).getId()));
            }

            @Override
            public void onEditItemClick(View view, int position) {
                findNavController(view).navigate(TechnologyFragmentDirections.actionTechnologyToEditArticle(articles.get(position).getId()));
            }
        });
        rv.setAdapter(adapter);
    }

}
