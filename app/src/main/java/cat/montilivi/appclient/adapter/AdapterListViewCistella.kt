package cat.montilivi.appclient.adapter

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import cat.montilivi.appclient.ClientManager
import cat.montilivi.appclient.R
import cat.montilivi.appclient.dades.ArticleCistella
import cat.montilivi.appclient.databinding.ItemCistellaArticleBinding
import cat.montilivi.appclient.viewmodel.CistellaViewModel

class AdapterListViewCistella(var context: Context, var llista:MutableList<ArticleCistella>, var viewModel:CistellaViewModel):BaseAdapter() {

    lateinit var binding:ItemCistellaArticleBinding


    override fun getCount(): Int {
        return llista.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        binding = ItemCistellaArticleBinding.inflate(LayoutInflater.from(context),viewGroup,false)


        var articleCistellaSelect = llista[position]


        binding.txtNomArticle.text = articleCistellaSelect.Article!!.NomArticle
        binding.txtPreuArticle.text = "${articleCistellaSelect.Article!!.PreuVenta} €/u"
        binding.txtTotalArticles.text = "Total: ${articleCistellaSelect.Article!!.PreuVenta * articleCistellaSelect.Quantitat!!}"
        binding.txtCountArticle.text = articleCistellaSelect.Quantitat.toString()


        binding.btnSumarCount.setOnClickListener { it->
            if(articleCistellaSelect.Article!!.Stock.toInt()>0)
            {
                articleCistellaSelect.Article!!.Stock--
                articleCistellaSelect.Quantitat = articleCistellaSelect.Quantitat!! +1
                viewModel.UpdateItem(articleCistellaSelect)
            }
        }

        binding.btnRestaCount.setOnClickListener { it ->
            articleCistellaSelect.Quantitat = articleCistellaSelect.Quantitat!! -1
            if(articleCistellaSelect.Article!!.Stock.toInt()>0)
            {
                articleCistellaSelect.Article!!.Stock++
                if(articleCistellaSelect.Quantitat!! <= 0){
                    viewModel.DeleteItem(articleCistellaSelect)
                }
                else{
                    viewModel.UpdateItem(articleCistellaSelect)
                }
            }
        }

        binding.btnDeleterArticle.setOnClickListener{it->
            viewModel.DeleteItem(articleCistellaSelect)
        }


        return binding.root
    }

}