package com.example.terravive

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.terravive.databinding.ActivityMainBinding
import com.example.terravive.databinding.DashboardMain2Binding


class Dashboard : AppCompatActivity() {

    private lateinit var binding: DashboardMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DashboardMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())


        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.activities -> replaceFragment(ActivityFragment())
                R.id.charity -> replaceFragment(CharityFragment())
                R.id.notification -> replaceFragment(NotificationFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
            }
            true
        }

        val mainView: View = findViewById(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.home_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun enableEdgeToEdge() {
        // Implement edge-to-edge if needed, or leave it blank if handled in themes
    }
}

/*
class Dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.dashboard_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}


public class Dashboard extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        enableEdgeToEdge(); // Note: This method may need to be defined or replaced depending on your setup
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.home:
                        replaceFragment(new HomeFragment());
                        break;
                case R.id.activity:
                        replaceFragment(new ActivityFragment());
                        break;
                case R.id.charity:
                        replaceFragment(new CharityFragment());
                        break;
                case R.id.notification:
                        replaceFragment(new NotificationFragment());
                        break;
                case R.id.profile:
                        replaceFragment(new ProfileFragment());
                        break;

            }


            return true;
    })

        View mainView = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
        WindowInsetsCompat systemBars = insets.getInsets(Type.systemBars());
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        return insets;
    });
    }
    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_layout, fragment);
        fragmentTransaction.commit();


    }

    private void enableEdgeToEdge() {
        // If you're using edge-to-edge manually, you can define any custom setup here.
        // Or, if it's from a library or specific setup, replace this with the appropriate method call.
    }
}*/