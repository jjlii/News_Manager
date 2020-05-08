package es.upm.etsiinf.pmd.pmdproject1920;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import es.upm.etsiinf.pmd.pmdproject1920.model.Article;

public class MainActivity extends AppCompatActivity {

    private  BottomNavigationView bottom_navigation;
    private List<Article> articles;
    private ConstraintLayout rl_fb_action;
    private FloatingActionButton fb_login, fb_create, fb_edit, fb_log_out, fb_action;
    private Animation fab_open, fab_close;
    private TextView tv_create, tv_edit, tv_log_out;

    Boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottom_navigation = findViewById(R.id.bottom_navigation);
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottom_navigation, navController);
        // galeria
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.imageView);
        button = (Button)findViewById(R.id.ly_buttonLoadPicture);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        //
        fb_create = findViewById(R.id.fb_create);
        fb_edit = findViewById(R.id.fb_edit);
        fb_log_out = findViewById(R.id.fb_log_out);
        fb_action = findViewById(R.id.fb_action);
        tv_create = findViewById(R.id.tv_create);
        tv_edit = findViewById(R.id.tv_edit);
        tv_log_out = findViewById(R.id.tv_log_out);
        bottom_navigation = findViewById(R.id.bottom_navigation);
        fb_login = findViewById(R.id.fb_login);
        rl_fb_action = findViewById(R.id.rl_fb_action);

        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);

        fb_action.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if (isOpen){

                    tv_create.setVisibility(View.GONE);
                    tv_edit.setVisibility(View.GONE);
                    tv_log_out.setVisibility(View.GONE);
                    fb_create.startAnimation(fab_close);
                    fb_edit.startAnimation(fab_close);
                    fb_log_out.startAnimation(fab_close);
                    fb_action.setImageResource(R.drawable.ic_menu_24dp);
                    fb_create.setClickable(false);
                    fb_edit.setClickable(false);
                    fb_log_out.setClickable(false);

                    isOpen = false;

                }else {

                    tv_create.setVisibility(View.VISIBLE);
                    tv_edit.setVisibility(View.VISIBLE);
                    tv_log_out.setVisibility(View.VISIBLE);
                    fb_create.setVisibility(View.VISIBLE);
                    fb_edit.setVisibility(View.VISIBLE);
                    fb_log_out.setVisibility(View.VISIBLE);
                    fb_create.startAnimation(fab_open);
                    fb_edit.startAnimation(fab_open);
                    fb_log_out.startAnimation(fab_open);
                    fb_action.setImageResource(R.drawable.ic_close_24dp);
                    fb_create.setClickable(true);
                    fb_edit.setClickable(true);
                    fb_log_out.setClickable(true);

                    isOpen = true;

                }
            }
        });

        fb_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Create News", Toast.LENGTH_SHORT).show();

            }
        });

        fb_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Edit News", Toast.LENGTH_SHORT).show();

            }
        });

        fb_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Log Out", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @SuppressLint("RestrictedApi")
    public void setVisibility(int visibility, Boolean menuVisibility){
        bottom_navigation.setVisibility(visibility);
        if (menuVisibility){
            rl_fb_action.setVisibility(View.VISIBLE);
            fb_login.setVisibility(View.GONE);
        }else {
            rl_fb_action.setVisibility(visibility);
            fb_login.setVisibility(View.VISIBLE);
        }
    }

    public List<Article> getArticles(){
        return articles;
    }

    public void setArticles(List<Article> new_articles){
        articles = new_articles;
    }

    public List<Article> filterArticles(String category){
        List<Article> res = new ArrayList<>();
        for(Article article:articles){
            if(article.getCategory().equals(category)){
                res.add(article);
            }
        }
        return res;
    }
    // Galeria
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
    }
}