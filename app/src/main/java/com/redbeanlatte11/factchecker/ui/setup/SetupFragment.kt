package com.redbeanlatte11.factchecker.ui.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.databinding.SetupFragBinding
import com.redbeanlatte11.factchecker.util.PreferenceUtils
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class SetupFragment : Fragment() {

    private val viewModel: SetupViewModel by viewModel()

    private lateinit var viewDataBinding: SetupFragBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = SetupFragBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        prepareSetup()

        setupObserver()

        return viewDataBinding.root
    }

    private fun prepareSetup() {
        (activity as AppCompatActivity).supportActionBar?.hide()
        val navView: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navView.visibility = View.GONE
    }

    private fun clearPreparingSetup() {
        (activity as AppCompatActivity).supportActionBar?.show()

        val navView: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navView.visibility = View.VISIBLE
    }

    private fun setupObserver() {
        viewModel.reportMessage.observe(this, Observer { reportMessage ->
            Timber.d("reportMessage: $reportMessage")
            PreferenceUtils.saveReportMessage(requireContext(), reportMessage)
        })

        viewModel.commentMessage.observe(this, Observer { commentMessage ->
            PreferenceUtils.saveCommentMessage(requireContext(), commentMessage)
        })

        viewModel.autoCommentEnabled.observe(this, Observer { autoCommentEnabled ->
            PreferenceUtils.saveAutoCommentEnabled(requireContext(), autoCommentEnabled)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupNavigation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.setReportMessage(PreferenceUtils.loadReportMessage(requireContext()))
        viewModel.setCommentMessage(PreferenceUtils.loadCommentMessage(requireContext()))
        viewModel.setAutoCommentEnabled(PreferenceUtils.loadAutoCommentEnabled(requireContext()))
    }

    private fun setupNavigation() {
        viewDataBinding.buttonLinkGoogleAccount.setOnClickListener {
            clearPreparingSetup()

            val action = SetupFragmentDirections.actionSetupDestToGoogleAccountDest(SetupFragment::class.java.simpleName)
            findNavController().navigate(action)
        }
    }
}