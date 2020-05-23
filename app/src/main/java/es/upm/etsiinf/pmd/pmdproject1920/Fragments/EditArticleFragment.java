package es.upm.etsiinf.pmd.pmdproject1920.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.upm.etsiinf.pmd.pmdproject1920.MainActivity;
import es.upm.etsiinf.pmd.pmdproject1920.R;
import es.upm.etsiinf.pmd.pmdproject1920.Task.DetailArticleTask;
import es.upm.etsiinf.pmd.pmdproject1920.Task.SaveArticleTask;
import es.upm.etsiinf.pmd.pmdproject1920.Task.SaveImageTask;
import es.upm.etsiinf.pmd.pmdproject1920.bbdd.BBDDArticle;
import es.upm.etsiinf.pmd.pmdproject1920.model.Article;
import es.upm.etsiinf.pmd.pmdproject1920.model.Image;
import es.upm.etsiinf.pmd.pmdproject1920.utils.SerializationUtils;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.exceptions.ServerCommunicationError;
import es.upm.etsiinf.pmd.pmdproject1920.utils.utils;

import static android.app.Activity.RESULT_OK;

public class EditArticleFragment extends Fragment {

    private static final int PICK_IMAGE = 100;
    private static final String DATE_FORMAT_MYSQL = "yyyy-MM-dd hh:mm:ss";
    private static final String EMPTY_IMG_DES = "";

    private Article article;
    private TextInputEditText et_title, et_subtitle, et_abstract, et_body;
    private Spinner ly_category;
    private ImageView iv_image;
    private Button btn_cancel, btn_load_picture, btn_save;
    private String userId;
    private int oldArticleId = -1;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userId = ModelManager.getIdUser();
        ArrayList<String> items = new ArrayList<String>(Arrays.asList(
                "Economy",
                "National",
                "Sports",
                "Technology"));
        Bitmap img = null;
        if (article != null){
            btn_save.setText(R.string.save);
            userId = String.valueOf(article.getIdUser());
            oldArticleId = article.getId();
            et_title.setText(article.getTitleText());
            et_subtitle.setText(article.getSubtitleText());
            et_abstract.setText(article.getAbstractText());
            et_body.setText(article.getBodyText());
            ly_category.setSelection(items.indexOf(article.getCategory()));
            try {
                img = SerializationUtils.base64StringToImg(article.getImage().getImage());
            } catch (ServerCommunicationError serverCommunicationError) {
                serverCommunicationError.printStackTrace();
            }
            iv_image.setImageBitmap(img);
        }

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle(btn_save.getText().toString())
                        .setMessage("Do you want to save the changes?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                publishProcess();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });



        btn_load_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    openGallery();
            }
        });
    }


    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view;
        ((MainActivity)getActivity()).setVisibility(View.GONE, ModelManager.isConnected());
        view = inflater.inflate(R.layout.fragment_edit_article, container, false);
        int articleId = getArguments().getInt("articleId");
        if (articleId!=-1){
            try {
                article = new DetailArticleTask().execute(Integer.toString(articleId)).get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        et_title= view.findViewById(R.id.et_title);
        et_subtitle= view.findViewById(R.id.et_subtitle);
        et_abstract= view.findViewById(R.id.et_abstract);
        et_body= view.findViewById(R.id.et_body);
        ly_category = view.findViewById(R.id.ly_category);
        iv_image = view.findViewById(R.id.iv_image);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_load_picture = view.findViewById(R.id.btn_load_picture);
        btn_save = view.findViewById(R.id.btn_save);
        return view;
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            Uri imageUri;
            imageUri = data.getData();
            iv_image.setImageURI(imageUri);
        }
    }



    private void publishProcess(){
        SimpleDateFormat sdfDate = new SimpleDateFormat(DATE_FORMAT_MYSQL);
        Date now = new Date();
        String nowStr = sdfDate.format(now);
        try {
            now = SerializationUtils.dateFromString(nowStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (et_title.getText().toString().equals(""))
            et_title.setError("The Title is mandatory");
        else if (et_subtitle.getText().toString().equals(""))
            et_subtitle.setError("The Subtitle is mandatory");
        else if (et_abstract.getText().toString().equals(""))
            et_abstract.setError("The Abstract is mandatory");
        else if (et_body.getText().toString().equals(""))
            et_body.setError("The Body is mandatory");
        else {
            publishAction(now);
        }
    }

    private void publishAction(Date now){
        int newArticleId;
        if (!checkNotChangeArticle()){
            if(article != null){
                article.setCategory(ly_category.getSelectedItem().toString());
                article.setTitleText(et_title.getText().toString());
                article.setAbstractText(et_abstract.getText().toString());
                article.setBodyText(et_body.getText().toString());
                article.setSubtitleText(et_subtitle.getText().toString());
            }else {
                article = new Article(
                        ly_category.getSelectedItem().toString(),
                        et_title.getText().toString(),
                        et_abstract.getText().toString(),
                        et_body.getText().toString(),
                        et_subtitle.getText().toString(),
                        userId
                );
            }
            if (null!=iv_image.getDrawable()){
                String thumbnail = SerializationUtils.imgToBase64String(
                        ((BitmapDrawable)iv_image.getDrawable()).getBitmap());
                try {
                    article.addImage(thumbnail, EMPTY_IMG_DES);
                    article.setThumbnail(thumbnail);
                } catch (ServerCommunicationError serverCommunicationError) {
                    Log.e("Add Image Error", serverCommunicationError.getMessage());
                    utils.showInfoDialog(getContext(), "Error adding the image");
                }
            }
            article.setLastUpdate(now);
            try {
                if (btn_save.getText().toString().equals(R.string.create)){
                    BBDDArticle.insertArticle(article);
                }else {
                    BBDDArticle.updateArticulo(article);
                }
                newArticleId = new SaveArticleTask().execute(article).get();
                if (oldArticleId ==-1){
                    article.setId(newArticleId);
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            updateFromTheMainList();
        }else {
            utils.showInfoDialog(getContext(),"Not content to update!");
        }
    }



    private boolean checkNotChangeArticle(){
        boolean res;
        if(article==null){
            res = false;
        }else {
            res =  article.getCategory().equals(ly_category.getSelectedItem().toString())&&
                    article.getTitleText().equals(et_title.getText().toString())&&
                    article.getAbstractText().equals(et_abstract.getText().toString())&&
                    article.getBodyText().equals(et_body.getText().toString())&&
                    article.getSubtitleText().equals(et_subtitle.getText().toString());
        }
        return res;
    }


    private void updateFromTheMainList(){
        List<Article> articles = ((MainActivity)getActivity()).getArticles();
        if(oldArticleId != -1){
            List<Article> newArticles = new ArrayList<>();
            for(Article auxArticle : articles){
                if (auxArticle.getId() != oldArticleId){
                    newArticles.add(auxArticle);
                }
            }
            newArticles.add(article);
            newArticles = utils.sortArticlesByDates(newArticles);
            ((MainActivity)getActivity()).setArticles(newArticles);
        }else {
            articles.add(article);
            articles = utils.sortArticlesByDates(articles);
            ((MainActivity)getActivity()).setArticles(articles);
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Success")
                .setMessage("Article is "+btn_save.getText()).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().onBackPressed();
            }
        })
                .show();
    }

}
