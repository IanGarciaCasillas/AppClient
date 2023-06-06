package cat.montilivi.appclient.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import cat.montilivi.appclient.R
import cat.montilivi.appclient.dades.ComandaVenda
import cat.montilivi.appclient.databinding.ItemComandesListBinding
import cat.montilivi.appclient.fragments.ComandesFragmentDirections

class AdapterListViewComandes(var context:Context, var llistaComanda:MutableList<ComandaVenda>):BaseAdapter() {

    lateinit var binding:ItemComandesListBinding

    override fun getCount(): Int {
        return llistaComanda.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        binding = ItemComandesListBinding.inflate(LayoutInflater.from(context),viewGroup,false)

        var comandaSelect = llistaComanda[position]

        binding.txtTitul.text = "COMANDA #${comandaSelect.IdComanda}"

        if(comandaSelect.EstatComandaVenda == "Pendent"){
            binding.txtTitul.setBackgroundColor(ContextCompat.getColor(context,R.color.Magenta))
        }
        else if (comandaSelect.EstatComandaVenda == "Preparant"){
            binding.txtTitul.setBackgroundColor(ContextCompat.getColor(context,R.color.yellow))
        }
        else if(comandaSelect.EstatComandaVenda == "Recollir"){
            binding.txtTitul.setBackgroundColor(ContextCompat.getColor(context,R.color.Orange))
        }
        else if(comandaSelect.EstatComandaVenda == "Cancelada"){
            binding.txtTitul.setBackgroundColor(ContextCompat.getColor(context,R.color.red))
        }
        else{
            binding.txtTitul.setBackgroundColor(ContextCompat.getColor(context,R.color.LimeGreen))
        }


        binding.txtTitul.setOnClickListener { it->
            var navController: NavController = Navigation.findNavController(it)
            navController.navigate(ComandesFragmentDirections.actionComandesFragmentToComandesDetallsFragment(comandaSelect))
        }


        return binding.root
    }
}