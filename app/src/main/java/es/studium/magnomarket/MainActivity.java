package es.studium.magnomarket;

import android.os.Bundle;
import android.view.View;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import es.studium.magnomarket.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    // variable estática con el idUsuario para diferenciar entre usuarios de la BD y solo mostrar los productos de él que está dentro del sesión
    public static int idUsuario;
    private BottomNavigationView navView;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_mi_despensa, R.id.navigation_lista_de_compra, R.id.navigation_notificaciones, R.id.navigation_mi_cuenta)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public void showBottomNavigationView() {
        navView.setVisibility(View.VISIBLE);
    }

    public void hideBottomNavigationView() {
        navView.setVisibility(View.GONE);
    }
}