package cat.montilivi.appclient.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import cat.montilivi.appclient.viewmodel.ComandesViewModel
import cat.montilivi.appclient.R
import cat.montilivi.appclient.adapter.AdapterListViewComandes
import cat.montilivi.appclient.databinding.FragmentComandesBinding

class ComandesFragment : Fragment() {

    lateinit var viewModel: ComandesViewModel
    lateinit var binding:FragmentComandesBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentComandesBinding.inflate(LayoutInflater.from(requireContext()),container,false)
        viewModel = ComandesViewModel()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.GetComandes()
        var adapter:AdapterListViewComandes

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.llistaComandes.observe(viewLifecycleOwner){newValue ->
                adapter = AdapterListViewComandes(requireContext(),viewModel.llistaComandes.value!!)
                binding.lstComandes.adapter = adapter
            }
            viewModel.stringLog.observe(viewLifecycleOwner){newValue->
                Toast.makeText(requireContext(),newValue,Toast.LENGTH_LONG).show()
            }
        }


        return  binding.root
    }


}