package com.redbeanlatte11.factchecker.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.util.PreferenceUtils
import com.redbeanlatte11.factchecker.util.getViewModelFactory

class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel> { getViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.home_frag, container, false)

        //TODO: test code

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
            R.id.menu_report_all -> {
                val savedSignInResult = PreferenceUtils.loadSignInResult(requireContext())
                if (savedSignInResult) {
                    val dialog = ReportAllDialogFragment(viewModel)
                    dialog.show(activity?.supportFragmentManager!!, "ReportAllDialogFragment")
                } else {
                    val signInDialog = SignInDialogFragment()
                    signInDialog.show(activity?.supportFragmentManager!!, "SignInDialogFragment")
                }
                true
            }

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