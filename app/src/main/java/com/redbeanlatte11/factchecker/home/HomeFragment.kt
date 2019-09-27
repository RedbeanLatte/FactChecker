package com.redbeanlatte11.factchecker.home

import android.os.Bundle
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.garim.util.getViewModelFactory

class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel> { getViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.home_frag, container, false)

        //TODO: test code
        val webView: WebView = activity?.findViewById(R.id.web_view)!!
//        viewModel.reportVideo(webView,"https://www.youtube.com/watch?v=E7s4R7T-N40")
        viewModel.reportVideo(webView, "https://www.youtube.com/watch?v=KFWmvEPC3XI")

        setHasOptionsMenu(true)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_frag_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.menu_google_account -> {
                showAccountPopUpMenu()
                true
            }

            else -> false
        }

    private fun showAccountPopUpMenu() {
        val view = activity?.findViewById<View>(R.id.menu_google_account) ?: return
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.google_account_menu, menu)
            setOnMenuItemClickListener {
                findNavController().navigate(R.id.google_account_dest)
                true
            }
            show()
        }
    }
}