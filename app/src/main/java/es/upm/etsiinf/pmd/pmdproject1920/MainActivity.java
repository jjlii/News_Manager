package es.upm.etsiinf.pmd.pmdproject1920;



import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottom_navigation = findViewById(R.id.bottom_navigation);
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottom_navigation, navController);
    }

    @SuppressLint("RestrictedApi")
    public void setVisibility(int visibility){
        bottom_navigation = findViewById(R.id.bottom_navigation);
        FloatingActionButton fb_login = findViewById(R.id.fb_login);
        bottom_navigation.setVisibility(visibility);
        fb_login.setVisibility(visibility);

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
}