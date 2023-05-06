package cat.montilivi.appclient.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import cat.montilivi.appclient.R
import cat.montilivi.appclient.dades.Client
import cat.montilivi.appclient.databinding.FragmentRegistreBinding
import cat.montilivi.appclient.viewmodel.RegistreViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class RegistreFragment : Fragment() {

    val args:RegistreFragmentArgs by navArgs()

    lateinit var viewModel: RegistreViewModel
    lateinit var binding:FragmentRegistreBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val tokenMobil = args.token
        binding = FragmentRegistreBinding.inflate(LayoutInflater.from(requireContext()),container,false)
        viewModel = RegistreViewModel()
        binding.viewModelRegistre = viewModel
        binding.lifecycleOwner = this



        binding.btnRegistre.setOnClickListener { it ->

            var nomClient = binding.etNomClient.text.toString()
            var congom1Client = binding.etCognom1Client.text.toString()
            var cognom2Client = binding.etCognom2Client.text.toString()
            var dniClient = binding.etDniClient.text.toString()
            var telefonClient = binding.etTelefonClient.text.toString()
            var direccioClient = binding.etDireccioClient.text.toString()
            var codicPostalClient = binding.etCodicPostalClient.text.toString()
            var correuClient = binding.etCorreuClient.text.toString()
            var passwordClient = binding.etPasswordClient.text.toString()


            var client:Client = Client(null,dniClient,nomClient,congom1Client,cognom2Client,correuClient,passwordClient,telefonClient,direccioClient,codicPostalClient,tokenMobil)

            viewModel.CrearCompte(client)
        }





        return binding.root
    }
}