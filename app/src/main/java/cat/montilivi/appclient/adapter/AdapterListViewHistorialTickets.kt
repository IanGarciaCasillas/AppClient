package cat.montilivi.appclient.adapter

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import cat.montilivi.appclient.dades.Ticket
import cat.montilivi.appclient.databinding.ItemComandesListBinding
import cat.montilivi.appclient.viewmodel.LliurarFacturaViewModel

class AdapterListViewHistorialTickets(var context:Context, var llistaTicket:MutableList<Ticket>, var viewModel:LliurarFacturaViewModel):BaseAdapter() {

    lateinit var binding:ItemComandesListBinding


    override fun getCount(): Int {
        return llistaTicket.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        binding = ItemComandesListBinding.inflate(LayoutInflater.from(context),viewGroup,false)

        var ticketSelect = llistaTicket[position]

        var data = ticketSelect.DataTicket


        binding.txtTitul.text = "TICKET ${ticketSelect.IdTicket}-${ticketSelect.NumDocument}"

        binding.txtTitul.textSize = 20f


        return binding.root
    }
}