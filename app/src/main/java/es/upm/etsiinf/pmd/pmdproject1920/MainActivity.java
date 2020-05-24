package es.upm.etsiinf.pmd.pmdproject1920;



import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import android.app.Notification;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import es.upm.etsiinf.pmd.pmdproject1920.bbdd.BBDDArticle;
import es.upm.etsiinf.pmd.pmdproject1920.model.Article;
import es.upm.etsiinf.pmd.pmdproject1920.utils.utils;

public class MainActivity extends AppCompatActivity {

    private  BottomNavigationView bottom_navigation;
    private List<Article> articles;
    private ConstraintLayout rl_fb_action;
    private FloatingActionButton fb_login, fb_create, fb_log_out, fb_action;
    private Animation fab_open, fab_close;
    private TextView tv_create, tv_log_out;
    private RelativeLayout loading;
    private RelativeLayout main_content;

    Boolean isOpen = false;

    static int counter = 0;
    private static NotificationHandler notificationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils.scheduleJob(this);
        setContentView(R.layout.activity_main);
        BBDDArticle.init(this);
        bottom_navigation = findViewById(R.id.bottom_navigation);
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottom_navigation, navController);
        notificationHandler = new NotificationHandler(this);
        fb_create = findViewById(R.id.fb_create);
        fb_log_out = findViewById(R.id.fb_log_out);
        fb_action = findViewById(R.id.fb_action);
        tv_create = findViewById(R.id.tv_create);
        tv_log_out = findViewById(R.id.tv_log_out);
        fb_login = findViewById(R.id.fb_login);
        rl_fb_action = findViewById(R.id.rl_fb_action);
        loading = findViewById(R.id.loading);
        main_content = findViewById(R.id.main_content);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fb_action.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if (isOpen){
                    tv_create.setVisibility(View.GONE);
                    tv_log_out.setVisibility(View.GONE);
                    fb_create.startAnimation(fab_close);
                    fb_log_out.startAnimation(fab_close);
                    fb_action.setImageResource(R.drawable.ic_menu_24dp);
                    fb_create.setClickable(false);
                    fb_log_out.setClickable(false);

                    isOpen = false;

                }else {

                    tv_create.setVisibility(View.VISIBLE);
                    tv_log_out.setVisibility(View.VISIBLE);
                    fb_create.setVisibility(View.VISIBLE);
                    fb_log_out.setVisibility(View.VISIBLE);
                    fb_create.startAnimation(fab_open);
                    fb_log_out.startAnimation(fab_open);
                    fb_action.setImageResource(R.drawable.ic_close_24dp);
                    fb_create.setClickable(true);
                    fb_log_out.setClickable(true);

                    isOpen = true;

                }
            }
        });

    }

    @SuppressLint("RestrictedApi")
    public void setVisibility(int visibility, boolean menuVisibility){
        bottom_navigation.setVisibility(visibility);
        fb_login.setVisibility(visibility);
        if (visibility == View.GONE){
            rl_fb_action.setVisibility(View.GONE);
        }else {
            setMenuVisibility(menuVisibility);
        }
    }

    @SuppressLint("RestrictedApi")
    private void setMenuVisibility(boolean menuVisibility){
        if (menuVisibility){
            rl_fb_action.setVisibility(View.VISIBLE);
            fb_create.setVisibility(View.GONE);
            fb_log_out.setVisibility(View.GONE);
            fb_login.setVisibility(View.GONE);
        }else {
            rl_fb_action.setVisibility(View.GONE);
            fb_create.setVisibility(View.GONE);
            fb_log_out.setVisibility(View.GONE);
            fb_login.setVisibility(View.VISIBLE);
        }
        isOpen = true;
        fb_action.callOnClick();
    }

    public void setArticles(List<Article> new_articles){
        articles = new_articles;
    }

    public List<Article> getArticles(){
        return articles;
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

    public void setLoading(boolean visibility) {
        if (visibility){
            loading.setVisibility(View.VISIBLE);
            main_content.setVisibility(View.GONE);
        }else {
            loading.setVisibility(View.GONE);
            main_content.setVisibility(View.VISIBLE);
        }
    }

    public static void sendNotification(String titulo, String mensaje) {
        String title = titulo;
        String msg = mensaje;
        Notification.Builder nb = notificationHandler.createNotification(title, msg);
        notificationHandler.getManager().notify(counter++, nb.build());
        notificationHandler.publishNotificationGroup();
    }


}