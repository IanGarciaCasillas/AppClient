package cat.montilivi.appclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cat.montilivi.appclient.databinding.ActivityMainBinding
import cat.montilivi.appclient.fragments.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    public lateinit var binding: ActivityMainBinding


    companion object {
        private const val TAG = "MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
       // binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.bottomNavigationView.visibility = View.GONE

        var navController = findNavController(R.id.navegacion)
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.menu_articles -> {
                    navController.navigate(R.id.articlesFragment)
                    true
                }
                R.id.menu_cistella ->{
                    navController.navigate(R.id.cistellaFragment)
                    true
                }
                R.id.menu_factura ->{
                    navController.navigate(R.id.lliurarFacturaFragment2)
                    true
                }
                R.id.menu_comandes -> {
                    navController.navigate(R.id.comandesFragment)
                    true
                }
                R.id.menu_perfil ->{
                    navController.navigate(R.id.perfilFragment)
                    true
                }

                else -> false
            }
        }


    }
}