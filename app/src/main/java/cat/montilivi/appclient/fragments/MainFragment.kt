package cat.montilivi.appclient.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import cat.montilivi.appclient.viewmodel.ViewModel

import cat.montilivi.appclient.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    lateinit var viewModel:ViewModel

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

        /*
            viewModel.login.observe(viewLifecycleOwner){newValue ->
                if(newValue != null){
                    if(newValue){
                        var clientActual = viewModel.clientActual.value
                        Toast.makeText(it.context, "USUARI CORRECTE", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(it.context,"La contrasenya o el correu son erronis. TORNA",Toast.LENGTH_LONG).show()
                    }
                }
            }
 */
        /*
            viewModel.login.observeForever { value ->
                if (value) {
                    var clientActual = viewModel.clientActual.value
                    Toast.makeText(it.context, "USUARI CORRECTE", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(it.context,"La contrasenya o el correu son erronis. TORNA",Toast.LENGTH_LONG).show()
                }
            }
*/

        }





        //return inflater.inflate(R.layout.fragment_main, container, false)
        return binding.root
    }
}