package es.upm.etsiinf.pmd.pmdproject1920.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import es.upm.etsiinf.pmd.pmdproject1920.MainActivity;
import es.upm.etsiinf.pmd.pmdproject1920.R;
import es.upm.etsiinf.pmd.pmdproject1920.Task.DetailArticleTask;
import es.upm.etsiinf.pmd.pmdproject1920.model.Article;
import es.upm.etsiinf.pmd.pmdproject1920.model.Image;
import es.upm.etsiinf.pmd.pmdproject1920.utils.SerializationUtils;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.exceptions.ServerCommunicationError;
import es.upm.etsiinf.pmd.pmdproject1920.utils.utils;

import static android.app.Activity.RESULT_OK;
import static androidx.core.app.ActivityCompat.startActivityForResult;
import static es.upm.etsiinf.pmd.pmdproject1920.utils.SerializationUtils.imgToBase64String;

public class EditArticleFragment extends Fragment {

    private static final int PICK_IMAGE = 100;
    private static final String DATE_FORMAT_MYSQL = "yyyy-MM-dd hh:mm:ss";
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



    private void checkMandatory(){
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
            if (article != null){
                updateAction(now);
            }else {
                createAction(now);
            }

        }
    }

    private void createAction(Date now){
        Article a = new Article(
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
                a.addImage(thumbnail, "");
            } catch (ServerCommunicationError serverCommunicationError) {
                Log.e("Add Image Error", serverCommunicationError.getMessage());
                utils.dialogDeleteRes(getContext(), "Error adding the image");
            }
        }
        a.setLastUpdate(now);
    }

    private void updateAction(Date now){
        article.setCategory(ly_category.getSelectedItem().toString());
        article.setTitleText(et_title.getText().toString());
        article.setAbstractText(et_abstract.getText().toString());
        article.setBodyText(et_body.getText().toString());
        article.setSubtitleText(et_subtitle.getText().toString());
        article.setLastUpdate(now);
    }


}
