package es.upm.etsiinf.pmd.pmdproject1920.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)getActivity()).setVisibility(View.VISIBLE, ModelManager.isConnected());
        articles = ((MainActivity)getActivity()).filterArticles("Technology");
        showRecyclerView();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_technology, container, false);
        rv = view.findViewById(R.id.rv_tech);
        return view;
    }

    private void showRecyclerView(){
        layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        adapter = new NewsAdapter(articles, getActivity(), new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                findNavController(view).navigate(TechnologyFragmentDirections.actionTechToArticleDetail(articles.get(position).getId()));
            }
        });
        rv.setAdapter(adapter);
    }

}
