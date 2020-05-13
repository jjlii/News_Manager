package es.upm.etsiinf.pmd.pmdproject1920.Fragments;

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
import java.util.concurrent.ExecutionException;

import es.upm.etsiinf.pmd.pmdproject1920.MainActivity;
import es.upm.etsiinf.pmd.pmdproject1920.R;
import es.upm.etsiinf.pmd.pmdproject1920.Task.DetailArticleTask;
import es.upm.etsiinf.pmd.pmdproject1920.Task.SaveArticleTask;
import es.upm.etsiinf.pmd.pmdproject1920.Task.SaveImageTask;
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
    private Image image = null;

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
            et_title.setText(article.getTitleText());
            et_subtitle.setText(article.getSubtitleText());
            et_abstract.setText(article.getAbstractText());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                et_body.setText(Html.fromHtml("<h2>"+article.getBodyText()+"</h2>", Html.FROM_HTML_MODE_COMPACT));
            }else {
                et_body.setText(Html.fromHtml("<h2>"+article.getBodyText()+"</h2>"));
            }
            ly_category.setSelection(items.indexOf(article.getCategory()));
            try {
                img = SerializationUtils.base64StringToImg(article.getImage().getImage());
            } catch (ServerCommunicationError serverCommunicationError) {
                serverCommunicationError.printStackTrace();
            }
            iv_image.setImageBitmap(img);
            try {
                image = article.getImage();
            } catch (ServerCommunicationError serverCommunicationError) {
                serverCommunicationError.printStackTrace();
            }
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
                publishProcess();
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
                article = new DetailArticleTask(getActivity()).execute(Integer.toString(articleId)).get();
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
        if (et_title.getText().equals(""))
            et_title.setError("The Title is mandatory");
        else if (et_subtitle.getText().equals(""))
            et_subtitle.setError("The Subtitle is mandatory");
        else if (et_abstract.getText().equals(""))
            et_abstract.setError("The Abstract is mandatory");
        else if (et_body.getText().equals(""))
            et_body.setError("The Body is mandatory");
        else {
            publishAction(now);
        }
    }

    private void publishAction(Date now){
        Article publishArticle;
        int saveArticleTask;
        if (!checkNotChangeArticle() || !sameImg()){
            publishArticle = new Article(
                    ly_category.getSelectedItem().toString(),
                    et_title.getText().toString(),
                    et_abstract.getText().toString(),
                    et_body.getText().toString(),
                    et_subtitle.getText().toString(),
                    userId
            );
            if (((BitmapDrawable)iv_image.getDrawable()).getBitmap()!=null){
                String thumbnail = SerializationUtils.imgToBase64String(
                        ((BitmapDrawable)iv_image.getDrawable()).getBitmap());
                try {
                    publishArticle.addImage(thumbnail, EMPTY_IMG_DES);
                } catch (ServerCommunicationError serverCommunicationError) {
                    Log.e("Add Image Error", serverCommunicationError.getMessage());
                    utils.showInfoDialog(getContext(), "Error adding the image");
                }
            }
            publishArticle.setLastUpdate(now);

            try {
                saveArticleTask = new SaveArticleTask().execute(publishArticle).get();
                if (saveArticleTask==-1){
                    utils.showInfoDialog(getContext(), "Error publishing the article, try again");
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                utils.showInfoDialog(getContext(), "Error publishing the article, try again");
            }
        }else {
            utils.showInfoDialog(getContext(),"Not content to update!");
        }
    }



    private boolean checkNotChangeArticle(){
        return article.getCategory().equals(ly_category.getSelectedItem().toString())&&
                article.getTitleText().equals(et_title.getText().toString())&&
                article.getAbstractText().equals(et_abstract.getText().toString())&&
                article.getBodyText().equals(et_body.getText().toString())&&
                article.getSubtitleText().equals(et_subtitle.getText().toString());
    }

    private boolean sameImg(){
        try {
            Bitmap originalImg = SerializationUtils.base64StringToImg(article.getImage().getImage());
            Bitmap view_image = ((BitmapDrawable)iv_image.getDrawable()).getBitmap();
            return originalImg.equals(view_image);
        } catch (ServerCommunicationError serverCommunicationError) {
            serverCommunicationError.printStackTrace();
        }
        return false;
    }


}
