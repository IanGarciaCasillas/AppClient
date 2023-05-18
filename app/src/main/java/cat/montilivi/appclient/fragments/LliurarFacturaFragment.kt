package cat.montilivi.appclient.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.montilivi.appclient.viewmodel.LliurarFacturaViewModel
import cat.montilivi.appclient.R

class LliurarFacturaFragment : Fragment() {

    companion object {
        fun newInstance() = LliurarFacturaFragment()
    }

    private lateinit var viewModel: LliurarFacturaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lliurar_factura, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LliurarFacturaViewModel::class.java)
        // TODO: Use the ViewModel
    }

}