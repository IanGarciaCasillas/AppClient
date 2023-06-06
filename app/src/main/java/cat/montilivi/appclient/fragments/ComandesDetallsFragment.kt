package cat.montilivi.appclient.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import cat.montilivi.appclient.viewmodel.ComandesDetallsViewModel
import cat.montilivi.appclient.R
import cat.montilivi.appclient.adapter.AdapterListViewComandes
import cat.montilivi.appclient.adapter.AdapterListViewComandesDetalls
import cat.montilivi.appclient.databinding.FragmentComandesDetallsBinding

class ComandesDetallsFragment : Fragment() {


    lateinit var viewModel: ComandesDetallsViewModel
    lateinit var binding:FragmentComandesDetallsBinding

    val args:ComandesDetallsFragmentArgs by navArgs ()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentComandesDetallsBinding.inflate(LayoutInflater.from(requireContext()),container,false)
        viewModel = ComandesDetallsViewModel()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        var comanda = args.comanda

        viewModel.CarregarDetalls(comanda)

        for (item in viewModel.llistaDetalls.value!!){
            viewModel.NomArticle(item.IdArticle)
        }

        var adapter:AdapterListViewComandesDetalls


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            /*
            viewModel.llistaDetalls.observe(viewLifecycleOwner){newValue->

                var llista = viewModel.llistaDetalls.value!!

                adapter = AdapterListViewComandesDetalls(requireContext(),llista,viewModel)
                binding.lstLlistaDetalls.adapter = adapter

            }
           viewModel.llistaNomArticles.observe(viewLifecycleOwner){newValue->
               var llistaDetalls = viewModel.llistaDetalls.value!!
               adapter = AdapterListViewComandesDetalls(requireContext(),llistaDetalls,viewModel)

               binding.lstLlistaDetalls.adapter = adapter
           }
             */
            viewModel.totOk.observe(viewLifecycleOwner){newValue->
                viewModel.GetArticlesDetalls()
            }

            viewModel.totOk2.observe(viewLifecycleOwner){newValue->
                var llista = viewModel.llistaDetalls.value!!
                var llistaArticles = viewModel.llistaArticles.value!!

                adapter = AdapterListViewComandesDetalls(requireContext(),llista,llistaArticles,viewModel)

                binding.txtTotalArticles.text = "Total articles: ${viewModel.totalArticles.value}"
                binding.txtTotalCistella.text = "Total: ${viewModel.totalPreu.value}€"
                binding.lstLlistaDetalls.adapter = adapter

            }
        }


        binding.txtTitol.text = "COMANDA #${comanda.IdComanda}"





        return binding.root
    }


}