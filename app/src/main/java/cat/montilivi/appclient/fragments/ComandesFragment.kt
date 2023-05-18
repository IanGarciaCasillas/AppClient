package cat.montilivi.appclient.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.montilivi.appclient.viewmodel.ComandesViewModel
import cat.montilivi.appclient.R

class ComandesFragment : Fragment() {

    companion object {
        fun newInstance() = ComandesFragment()
    }

    private lateinit var viewModel: ComandesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comandes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ComandesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}