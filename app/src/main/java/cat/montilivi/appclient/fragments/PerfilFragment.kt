package cat.montilivi.appclient.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import cat.montilivi.appclient.ClientManager
import cat.montilivi.appclient.viewmodel.PerfilViewModel
import cat.montilivi.appclient.R
import cat.montilivi.appclient.databinding.FragmentPerfilBinding

class PerfilFragment : Fragment() {

    lateinit var viewModel: PerfilViewModel
    lateinit var binding:FragmentPerfilBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        binding = FragmentPerfilBinding.inflate(LayoutInflater.from(requireContext()),container,false)
        viewModel = PerfilViewModel()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.txtLog.observe(viewLifecycleOwner){newValue->
                Toast.makeText(requireContext(),newValue,Toast.LENGTH_LONG).show()
            }
        }


        var clientSelect = ClientManager.client

        binding.txtIdClient.text = clientSelect!!.IdClient.toString()
        binding.etNomClient.setText(clientSelect!!.NomClient)
        binding.etCognom1Client.setText(clientSelect!!.Cognom1Client)
        binding.etCognom2Client.setText(clientSelect!!.Cognom2Client)
        binding.etDniClient.setText(clientSelect!!.Dni)
        binding.etTelefonClient.setText(clientSelect!!.TelefonClient)
        binding.etDireccioClient.setText(clientSelect!!.DireccioClient)
        binding.etCodicPostalClient.setText(clientSelect!!.CodicPostal)
        binding.etCorreuClient.setText(clientSelect!!.CorreuClient)
        binding.etPasswordClient.setText(clientSelect!!.ContrasenyaClient)


        binding.btnActualitzar.setOnClickListener { it->

            clientSelect.NomClient = binding.etNomClient.text.toString()
            clientSelect.Cognom1Client = binding.etCognom1Client.text.toString()
            clientSelect.Cognom2Client = binding.etCognom2Client.text.toString()
            clientSelect.Dni = binding.etDniClient.text.toString()
            clientSelect.TelefonClient = binding.etTelefonClient.text.toString()
            clientSelect.DireccioClient = binding.etDireccioClient.text.toString()
            clientSelect.CodicPostal = binding.etCodicPostalClient.text.toString()
            clientSelect.CorreuClient = binding.etCorreuClient.text.toString()
            clientSelect.ContrasenyaClient = binding.etPasswordClient.text.toString()

            ClientManager.client = clientSelect

            viewModel.UpdateClient(clientSelect)

        }


        return binding.root
    }


}