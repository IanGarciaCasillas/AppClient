package cat.montilivi.appclient.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation

import cat.montilivi.appclient.R
import cat.montilivi.appclient.databinding.FragmentMainBinding


import cat.montilivi.appclient.OKHTTP.OkHttp

class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentMainBinding.inflate(LayoutInflater.from(requireContext()),container,false)

        binding.btnSeguent.setOnClickListener { it ->

            var navController:NavController = Navigation.findNavController(it)
            navController.navigate(R.id.action_mainFragment_to_segonFragment)

        }

        var clientWeb:OkHttp = OkHttp()

        var result:String = clientWeb.TestGet()

        var nada = 1
        binding.txtTitol.text = result





        //return inflater.inflate(R.layout.fragment_main, container, false)
        return binding.root
    }
}