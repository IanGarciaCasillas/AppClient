package cat.montilivi.appclient.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cat.montilivi.appclient.R
import cat.montilivi.appclient.dades.Article
import cat.montilivi.appclient.databinding.ItemArticleListBinding
import cat.montilivi.appclient.viewmodel.ArticlesViewModel
import java.nio.charset.Charset
import java.util.*


class AdapterListViewArticles (var context: Context, var llistaArtilces:MutableList<Article>,var viewModel:ArticlesViewModel): BaseAdapter() {

    /*
    //ITEMS LAYOUT
    lateinit var imgFotoArticle:ImageView
    lateinit var txtNomArticle:TextView
    lateinit var txtPreuArticle:TextView
    lateinit var txtDispon:TextView
    lateinit var btnAfegir:Button
    */
    lateinit var binding:ItemArticleListBinding



    override fun getCount(): Int {
        return llistaArtilces.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {

        binding = ItemArticleListBinding.inflate(LayoutInflater.from(context),viewGroup,false)


        var articleSelect = llistaArtilces[position]

        var btm: Bitmap?=null
        if(articleSelect.FotoArticle != null){
            var fotoBytes = android.util.Base64.decode(articleSelect.FotoArticle, android.util.Base64.DEFAULT)
            btm = BitmapFactory.decodeByteArray(fotoBytes,0,fotoBytes.size)
        }

        binding.imgFotoArticle.setImageBitmap(btm)
       binding.txtNomArticle.text = articleSelect.NomArticle
       binding.txtPreuArticle.text = "${articleSelect.PreuVenta}€"
        binding.txtDispon.text = "Disponibles: ${articleSelect.Stock}"






        binding.btnAfegir.setOnClickListener { it ->
            viewModel.AfegirArticle(articleSelect)
        }


        return binding.root
    }



}