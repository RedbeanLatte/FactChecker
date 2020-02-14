package com.redbeanlatte11.factchecker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.redbeanlatte11.factchecker.EventObserver
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.databinding.GoogleAccountFragBinding
import com.redbeanlatte11.factchecker.ui.setup.SetupFragment
import kotlinx.android.synthetic.main.main_act.*
import org.koin.android.viewmodel.ext.android.viewModel

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
        viewModel.signIn(viewDataBinding.webView)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        setupNavigation()
    }

    private fun setupNavigation() {
        viewModel.signInCompletedEvent.observe(this, EventObserver {
            if (args.entryPointName == HomeFragment::class.java.simpleName) {
                findNavController().navigateUp()
            } else if (args.entryPointName == SetupFragment::class.java.simpleName) {

                changeStartDestination(R.id.home_dest)
                findNavController().navigateUp()
            }
        })

        viewModel.signOutCompletedEvent.observe(this, EventObserver {
            changeStartDestination(R.id.setup_dest)
        })
    }

    private fun changeStartDestination(destination: Int) {
        val navGraph = findNavController().navInflater.inflate(R.navigation.nav_graph)
        navGraph.startDestination = destination
        findNavController().graph = navGraph
    }
}