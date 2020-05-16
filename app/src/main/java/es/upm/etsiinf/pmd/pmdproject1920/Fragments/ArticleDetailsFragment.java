package es.upm.etsiinf.pmd.pmdproject1920.Fragments;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import es.upm.etsiinf.pmd.pmdproject1920.MainActivity;
import es.upm.etsiinf.pmd.pmdproject1920.R;
import es.upm.etsiinf.pmd.pmdproject1920.Task.DetailArticleTask;
import es.upm.etsiinf.pmd.pmdproject1920.Task.LoadArticlesTask;
import es.upm.etsiinf.pmd.pmdproject1920.model.Article;
import es.upm.etsiinf.pmd.pmdproject1920.utils.SerializationUtils;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.exceptions.ServerCommunicationError;

public class ArticleDetailsFragment extends Fragment {

    private ImageView iv_detail_img;
    private TextView tv_category_detail, tv_title_detail, tv_subtitle_detail, tv_abstract_detail,
            tv_body_detail, tv_author_value, tv_date_value;
    private Article article;
    private ProgressBar progressBar;
    private ScrollView article_detail_container;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int articleId = getArguments().getInt("articleId");
        Bitmap img = null;
        try {
            article = new DetailArticleTask().execute(Integer.toString(articleId)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            img = SerializationUtils.base64StringToImg(article.getImage().getImage());
        } catch (ServerCommunicationError serverCommunicationError) {
            serverCommunicationError.printStackTrace();
        }
        iv_detail_img.setImageBitmap(img);
        tv_category_detail.setText(article.getCategory());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_title_detail.setText(Html.fromHtml("<h2>"+article.getTitleText()+"</h2>", Html.FROM_HTML_MODE_COMPACT));
            tv_subtitle_detail.setText(Html.fromHtml("<h2>"+article.getSubtitleText()+"</h2>", Html.FROM_HTML_MODE_COMPACT));
            tv_abstract_detail.setText(Html.fromHtml("<h2>"+article.getAbstractText()+"</h2>", Html.FROM_HTML_MODE_COMPACT));
            tv_body_detail.setText(Html.fromHtml("<h2>"+article.getBodyText()+"</h2>", Html.FROM_HTML_MODE_COMPACT));
        }else {
            tv_title_detail.setText(Html.fromHtml("<h2>"+article.getTitleText()+"</h2>"));
            tv_subtitle_detail.setText(Html.fromHtml("<h2>"+article.getSubtitleText()+"</h2>"));
            tv_abstract_detail.setText(Html.fromHtml("<h2>"+article.getAbstractText()+"</h2>"));
            tv_body_detail.setText(Html.fromHtml("<h2>"+article.getBodyText()+"</h2>"));
        }
        tv_author_value.setText(Integer.toString(article.getIdUser()));
        tv_date_value.setText(article.getLastUpdate().toString());
        setVisible();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setVisibility(View.GONE, ModelManager.isConnected());
        View view = inflater.inflate(R.layout.fragment_article_details, container, false);
        iv_detail_img = view.findViewById(R.id.iv_detail_img);
        tv_category_detail = view.findViewById(R.id.tv_category_detail);
        tv_title_detail = view.findViewById(R.id.tv_title_detail);
        tv_subtitle_detail = view.findViewById(R.id.tv_subtitle_detail);
        tv_abstract_detail = view.findViewById(R.id.tv_abstract_detail);
        tv_body_detail = view.findViewById(R.id.tv_body_detail);
        tv_author_value = view.findViewById(R.id.tv_author_value);
        tv_date_value = view.findViewById(R.id.tv_date_value);
        progressBar = view.findViewById(R.id.pb_article_detail);
        article_detail_container = view.findViewById(R.id.sv);
        return view;
    }

    private void setVisible(){
        progressBar.setVisibility(View.GONE);
        article_detail_container.setVisibility(View.VISIBLE);
    }
}
