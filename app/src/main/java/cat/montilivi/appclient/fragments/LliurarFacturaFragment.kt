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
import cat.montilivi.appclient.viewmodel.LliurarFacturaViewModel
import cat.montilivi.appclient.R
import cat.montilivi.appclient.adapter.AdapterListViewHistorialTickets
import cat.montilivi.appclient.databinding.FragmentLliurarFacturaBinding

class LliurarFacturaFragment : Fragment() {



    lateinit var viewModel: LliurarFacturaViewModel
    lateinit var binding:FragmentLliurarFacturaBinding



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        binding = FragmentLliurarFacturaBinding.inflate(LayoutInflater.from(requireContext()),container,false)
        viewModel = LliurarFacturaViewModel()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        viewModel.CarregarHistorialTicket(ClientManager.client!!.IdClient!!)
        var adapter:AdapterListViewHistorialTickets

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.llistaHistorialTicket.observe(viewLifecycleOwner){newValue->
                var llista = viewModel.llistaHistorialTicket.value!!

                adapter = AdapterListViewHistorialTickets(requireContext(),llista,viewModel)
                binding.lstHistorialTickets.adapter = adapter

            }

            viewModel.txtLog.observe(viewLifecycleOwner){newValue->
                Toast.makeText(requireContext(),newValue,Toast.LENGTH_LONG).show()
            }

        }



        binding.btnLliurar.setOnClickListener { it->

            var numTicket = binding.etNumTicket.text.toString().toInt()
            var numDocument = binding.etNumDocumentTicket.text.toString().toInt()
            var client = ClientManager.client!!

            viewModel.LliurarFactura(numTicket,numDocument,client)


        }






        return binding.root
    }
}