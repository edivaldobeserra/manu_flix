package devandroid.edivaldo.manuflix.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import devandroid.edivaldo.manuflix.R;
import devandroid.edivaldo.manuflix.model.Categoria;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(bottomNavigationView,navController);

        salvarCategorias();
    }

    private void salvarCategorias(){
        new Categoria("Ação");
        new Categoria("Aventura");
        new Categoria("Animação");
        new Categoria("Comédia");
        new Categoria("Drama");
        new Categoria("Épico");
        new Categoria("Faroeste");
        new Categoria("Ficção");
        new Categoria("Guerra");
        new Categoria("Terror");
    }
}