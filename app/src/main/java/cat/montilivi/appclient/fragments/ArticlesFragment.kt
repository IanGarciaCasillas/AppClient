package cat.montilivi.appclient.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.databinding.adapters.SearchViewBindingAdapter
import androidx.databinding.adapters.SearchViewBindingAdapter.setOnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import cat.montilivi.appclient.R
import cat.montilivi.appclient.adapter.AdapterListViewArticles
import cat.montilivi.appclient.databinding.FragmentArticlesBinding
import cat.montilivi.appclient.viewmodel.ArticlesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class ArticlesFragment : Fragment() {

    lateinit var viewModel: ArticlesViewModel
    lateinit var binding:FragmentArticlesBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentArticlesBinding.inflate(LayoutInflater.from(requireContext()),container,false)
        viewModel = ArticlesViewModel()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        //val client = args.client


        var bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE

        viewModel.GetArticles()
        var adapter:AdapterListViewArticles


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.llistaCarregada.observe(viewLifecycleOwner){newValue ->
                if(newValue){
                    var llista = viewModel.llista.value
                    adapter = AdapterListViewArticles(requireContext(),llista!!,viewModel)
                    binding.lstLlistaArticles.adapter = adapter
                }
            }

            viewModel.textLog.observe(viewLifecycleOwner){ newValue ->
                Toast.makeText(requireContext(),newValue,Toast.LENGTH_LONG).show()
            }

            viewModel.articleAdd.observe(viewLifecycleOwner){newValue ->
                Toast.makeText(requireContext(),"$newValue afegides a la cistella",Toast.LENGTH_LONG).show()
            }

        }




        binding.svBuscadorArticles.setOnQueryTextListener(object :OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {

                if(newText.length == 0){
                    viewModel.GetArticles()
                }
                else if(newText.length >= 3){
                    viewModel.BuscarArticle(newText)
                }
                return false
            }

        })



        return binding.root
    }


}