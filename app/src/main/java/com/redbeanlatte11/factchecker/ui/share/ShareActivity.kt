package com.redbeanlatte11.factchecker.ui.share

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.redbeanlatte11.factchecker.EventObserver
import com.redbeanlatte11.factchecker.R
import com.redbeanlatte11.factchecker.util.showToast
import kotlinx.android.synthetic.main.share_act.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class ShareActivity : AppCompatActivity() {

    lateinit var url: String

    private val viewModel: ShareViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.share_act)

        setupButton()
        handleIntent()
        setupEventObserver()
    }

    private fun setupEventObserver() {
        viewModel.videoUrlConfirmedEvent.observe(this, EventObserver {
            viewModel.addBlacklistVideo(url)
        })

        viewModel.blacklistAddedEvent.observe(this, EventObserver {
            showToast(getString(R.string.adding_blacklist_success))
            finish()
        })

        viewModel.duplicatedUrlEvent.observe(this, EventObserver {
            showToast(getString(R.string.confirm_url_already_registered))
            finish()
        })

        viewModel.channelUrlConfirmedEvent.observe(this, EventObserver {
            viewModel.addBlacklistChannel(url)
        })

        viewModel.addFailedEvent.observe(this, EventObserver {
            showToast(getString(R.string.adding_blacklist_error))
            finish()
        })
    }

    private fun setupButton() {
        text_view_cancel.setOnClickListener {
            finish()
        }

        text_view_ok.setOnClickListener {
            viewModel.confirmUrl(url)
        }
    }

    private fun handleIntent() {
        val action = intent.action
        val type = intent.type

        if (action == Intent.ACTION_SEND && type == "text/plain") {
            url = intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""
            Timber.d("url: $url")
        }
    }
}