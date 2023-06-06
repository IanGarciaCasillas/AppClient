package cat.montilivi.appclient.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.lifecycle.LifecycleOwner
import cat.montilivi.appclient.dades.Article
import cat.montilivi.appclient.dades.ComandaVenda
import cat.montilivi.appclient.dades.ComandaVendaDetalls
import cat.montilivi.appclient.databinding.ItemComandaDetallsListBinding
import cat.montilivi.appclient.viewmodel.ComandesDetallsViewModel

class AdapterListViewComandesDetalls(var context:Context, var llistaDetalls:MutableList<ComandaVendaDetalls>, var llistaArticles:MutableList<Article>, var viewModel:ComandesDetallsViewModel):BaseAdapter() {

    lateinit var binding: ItemComandaDetallsListBinding

    override fun getCount(): Int {
        return llistaDetalls.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {

        binding = ItemComandaDetallsListBinding.inflate(LayoutInflater.from(context), viewGroup, false)

        var detall = llistaDetalls[position]
        var articleSelect = llistaArticles[position]


        binding.txtNomArticle.text = articleSelect.NomArticle
        binding.txtUnitatsDemandes.text = detall.QuantitatDemanada.toString()
        binding.txtUnitatsServides.text = detall.QuantitatServida.toString()


        return binding.root
    }


    fun ActualitzarDetalls(detalls:List<ComandaVendaDetalls>){

    }
}