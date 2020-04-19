package es.upm.etsiinf.pmd.pmdproject1920;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import static androidx.navigation.Navigation.findNavController;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return findNavController(this, R.id.nav_host_fragment ).navigateUp();
    }
}