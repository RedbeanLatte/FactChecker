package com.redbeanlatte11.factchecker.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.redbeanlatte11.factchecker.EventObserver
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.databinding.GoogleAccountFragBinding
import com.redbeanlatte11.factchecker.ui.setup.SetupFragment
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class GoogleAccountFragment : Fragment() {

    private val viewModel: GoogleAccountViewModel by viewModel()

    private lateinit var viewDataBinding: GoogleAccountFragBinding

    private val args: GoogleAccountFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.google_account_frag, container, false)

        viewDataBinding = GoogleAccountFragBinding.bind(view)

        setupWebView()
        viewModel.linkGoogleAccount(viewDataBinding.webView)

        return view
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        viewDataBinding.webView.apply {
            settings.javaScriptEnabled = true
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.setAppCacheEnabled(false)
            webViewClient = WebViewClient()
            clearHistory()
            clearCache(true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        setupEventObserver()
    }

    private fun setupEventObserver() {
        viewModel.signInCompletedEvent.observe(this, EventObserver {
            Timber.d("signInCompletedEvent")
            if (args.entryPointName == HomeFragment::class.java.simpleName) {
                findNavController().navigateUp()
            } else if (args.entryPointName == SetupFragment::class.java.simpleName) {
                clearPreparingSetup()
                changeStartDestination(R.id.home_dest)
                findNavController().navigateUp()
            }
        })

        viewModel.signOutCompletedEvent.observe(this, EventObserver {
            Timber.d("signOutCompletedEvent")
            changeStartDestination(R.id.setup_dest)
            findNavController().navigateUp()
        })
    }

    private fun changeStartDestination(destination: Int) {
        val navGraph = findNavController().navInflater.inflate(R.navigation.nav_graph)
        navGraph.startDestination = destination
        findNavController().graph = navGraph
    }

    private fun clearPreparingSetup() {
        (activity as AppCompatActivity).supportActionBar?.show()

        val navView: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navView.visibility = View.VISIBLE
    }
}