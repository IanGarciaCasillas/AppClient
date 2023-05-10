package cat.montilivi.appclient.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.montilivi.appclient.viewmodel.ComandesDetallsViewModel
import cat.montilivi.appclient.R

class ComandesDetallsFragment : Fragment() {

    lateinit var viewModel: ComandesDetallsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comandes_detalls, container, false)
    }


}