package cat.montilivi.appclient.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Path.Direction
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import cat.montilivi.appclient.R
import cat.montilivi.appclient.viewmodel.ViewModel

import cat.montilivi.appclient.databinding.FragmentMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    lateinit var viewModel:ViewModel
    lateinit var tokenValue:String
    companion object{
        private const val TAG = "MainFragment"
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentMainBinding.inflate(LayoutInflater.from(requireContext()),container,false)

        viewModel = ViewModel()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        var valueString:String = ""

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.txtLogLogin.observe(viewLifecycleOwner){newValue ->
                valueString = newValue
            }
            viewModel.login.observe(viewLifecycleOwner){ newValue ->
                if(newValue){
                    var clientActual = viewModel.clientActual.value
                    Toast.makeText(requireContext(), valueString, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(),valueString,Toast.LENGTH_LONG).show()
                }
            }
        }


        binding.btnEntrar.setOnClickListener { it ->
            var correuClient = binding.etCorreuClient.text.toString()
            var passwordClient = binding.etPasswordClient.text.toString()

            viewModel.LoginClient(correuClient,passwordClient)

        }

        binding.btnRegistre.setOnClickListener { it ->
            var navController:NavController = Navigation.findNavController(it)

            //navController.navigate(MainFragmentDirections.actionMainFragmentToRegistreFragment())
           navController.navigate(MainFragmentDirections.actionMainFragmentToRegistreFragment(tokenValue))


        }


        //NOTIFICACIO
        FirebaseApp.initializeApp(requireContext())

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Error al obtener el token de registro", task.exception)
                    return@OnCompleteListener
                }

                // Obtener el token de registro
                val token = task.result
                tokenValue = token!!

                // Enviar el token de registro al servidor web API ASP.NET Core C#
                // Log and toast
                val msg = getString(R.string.msg_token_fmt, token)
                Log.d(TAG, msg)
                //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()

                var nada = 1
                var myTokenDispo = token
            })

        this.askNotificationPermission()


        //return inflater.inflate(R.layout.fragment_main, container, false)
        return binding.root
    }

    //FUNCIONS PER LA NOTIFICACIO
    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }
    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}