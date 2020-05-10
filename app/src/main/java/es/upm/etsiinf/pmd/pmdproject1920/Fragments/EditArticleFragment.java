package es.upm.etsiinf.pmd.pmdproject1920.Fragments;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Html;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import es.upm.etsiinf.pmd.pmdproject1920.MainActivity;
import es.upm.etsiinf.pmd.pmdproject1920.R;
import es.upm.etsiinf.pmd.pmdproject1920.Task.DetailArticleTask;
import es.upm.etsiinf.pmd.pmdproject1920.model.Article;
import es.upm.etsiinf.pmd.pmdproject1920.utils.network.ModelManager;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class EditArticleFragment extends Fragment {

    private Article article;
    private TextInputEditText et_title, et_subtitle, et_abstract, et_body;
    private Spinner ly_category;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<String> items = new ArrayList<String>(Arrays.asList(
                "Economy",
                "National",
                "Sports",
                "Technology"));
        if (article != null){
            et_title.setText(article.getTitleText());
            et_subtitle.setText(article.getSubtitleText());
            et_abstract.setText(article.getAbstractText());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                et_body.setText(Html.fromHtml("<h2>"+article.getBodyText()+"</h2>", Html.FROM_HTML_MODE_COMPACT));
            }else {
                et_body.setText(Html.fromHtml("<h2>"+article.getBodyText()+"</h2>"));
            }

            ly_category.setSelection(items.indexOf(article.getCategory()));
        }
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

        /*
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.imageView);
        button = (Button)findViewById(R.id.ly_buttonLoadPicture);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        */
        return view;
    }
    /* Galeria
    ImageView imageView;
    Button button;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }*/
}
