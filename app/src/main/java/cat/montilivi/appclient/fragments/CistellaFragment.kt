package cat.montilivi.appclient.fragments

import android.opengl.Visibility
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import cat.montilivi.appclient.viewmodel.CistellaViewModel
import cat.montilivi.appclient.R
import cat.montilivi.appclient.adapter.AdapterListViewCistella
import cat.montilivi.appclient.databinding.FragmentCistellaBinding
import com.google.android.datatransport.runtime.dagger.BindsInstance

class CistellaFragment : Fragment() {

    lateinit var viewModel: CistellaViewModel
    lateinit var binding:FragmentCistellaBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        binding = FragmentCistellaBinding.inflate(LayoutInflater.from(requireContext()),container,false)
        viewModel = CistellaViewModel()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.GetLlista()
        var adapter:AdapterListViewCistella

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.llistaCistella.observe(viewLifecycleOwner){newValue->

                var llista = viewModel.llistaCistella.value

                if(newValue.size == 0){
                    adapter = AdapterListViewCistella(requireContext(),llista!!,viewModel)
                    binding.lstArticlesCistella.adapter = adapter
                    Toast.makeText(requireContext(),"NO HI HA ARTICLES EN LA CISTELLA",Toast.LENGTH_LONG).show()
                }
                else{
                    adapter = AdapterListViewCistella(requireContext(),llista!!,viewModel)
                    binding.lstArticlesCistella.adapter = adapter
                }
            }
            viewModel.canBuidarCistella.observe(viewLifecycleOwner){newValue->
                binding.btnCancelarCistella.isEnabled = newValue
            }
            viewModel.canPagar.observe(viewLifecycleOwner){newValue->
                binding.btnPagarCistella.isEnabled = newValue
            }

            viewModel.countTotalArticles.observe(viewLifecycleOwner){newValue->
                binding.txtTotalArticles.text = "Total articles: ${viewModel.countTotalArticles.value!!}"
            }
            viewModel.countTotalPagar.observe(viewLifecycleOwner){newValue->
                binding.txtTotalCistella.text = "Total: ${viewModel.countTotalPagar.value!!}€"
            }
            viewModel.stringStat.observe(viewLifecycleOwner){newValue ->
                Toast.makeText(requireContext(),newValue,Toast.LENGTH_LONG).show()
                if(newValue =="Comanda enviada. Gracies")
                    viewModel.ClearLlista()
            }
        }


        binding.btnCancelarCistella.setOnClickListener { it->
            viewModel.ClearLlista()
        }


        binding.btnPagarCistella.setOnClickListener { it->
            viewModel.EnviarComanda()
        }






        return binding.root
    }


}