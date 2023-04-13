package cat.montilivi.appclient.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.montilivi.appclient.viewmodel.ViewModel

import cat.montilivi.appclient.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    lateinit var viewModel:ViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentMainBinding.inflate(LayoutInflater.from(requireContext()),container,false)

        /*
        var clientWeb:OkHttp = OkHttp()
        //var result:String = clientWeb.TestGet()

        binding.btnEntrar.setOnClickListener { it ->
                /*
            var correuClient = binding.etCorreuClient.text.toString()
            var passwordClient = binding.etPasswordClient.text.toString()
            */

            viewModel.testJson.value

        }
*/

        viewModel = ViewModel()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        binding.btnEntrar.setOnClickListener { it ->
            var correuClient = binding.etCorreuClient.text.toString()
            var passwordClient = binding.etPasswordClient.text.toString()

            var result = viewModel.LoginClient(correuClient,passwordClient)

            if(result){
                //TODO NAVEGAR SEGON FRAG

            }else{
                //TODO INFORMAR CORREU O PASSWORD ERRONIA
            }


        }





        //return inflater.inflate(R.layout.fragment_main, container, false)
        return binding.root
    }
}