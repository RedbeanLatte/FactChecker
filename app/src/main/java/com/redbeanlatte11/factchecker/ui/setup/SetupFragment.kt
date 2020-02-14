package com.redbeanlatte11.factchecker.ui.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.redbeanlatte11.factchecker.EventObserver
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.databinding.SetupFragBinding
import com.redbeanlatte11.factchecker.util.PreferenceUtils
import org.koin.android.viewmodel.ext.android.viewModel

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
        setupEventObserver()

        return viewDataBinding.root
    }

    private fun setupEventObserver() {
        viewModel.setupParamsSavedEvent.observe(this, EventObserver { setupParams ->
            PreferenceUtils.saveReportMessage(requireContext(), setupParams.reportMessage)
            PreferenceUtils.saveCommentMessage(requireContext(), setupParams.commentMessage)
            PreferenceUtils.saveAutoCommentEnabled(requireContext(), setupParams.autoCommentEnabled)
        })
    }

    private fun prepareSetup() {
        (activity as AppCompatActivity).supportActionBar?.hide()
        val navView: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navView.visibility = View.GONE
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
        viewModel.linkToGoogleAccountEvent.observe(this, EventObserver {
            val action = SetupFragmentDirections.actionSetupDestToGoogleAccountDest(SetupFragment::class.java.simpleName)
            findNavController().navigate(action)
        })
    }
}