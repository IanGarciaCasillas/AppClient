package cat.montilivi.appclient.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.montilivi.appclient.viewmodel.CistellaViewModel
import cat.montilivi.appclient.R
import cat.montilivi.appclient.databinding.FragmentCistellaBinding

class CistellaFragment : Fragment() {

    lateinit var viewModel: CistellaViewModel
    lateinit var binding:FragmentCistellaBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        binding = FragmentCistellaBinding.inflate(LayoutInflater.from(requireContext()),container,false)
        viewModel = CistellaViewModel()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this






        return binding.root
    }


}