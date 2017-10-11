package com.integratorsb2b.ug.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import com.integratorsb2b.ug.R
import com.integratorsb2b.ug.databinding.ActivityMainBinding
import com.integratorsb2b.ug.resit.ResitActivity
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt

class MainActivity : AppCompatActivity(), HomeContract.View {

    private val tutorialsConfiguration = "tutorials_conf"

    private lateinit var localPresenter: HomeContract.Presenter

    override fun setPresenter(presenter: HomeContract.Presenter) {
        localPresenter = presenter
    }

    override fun showApplyResit() {
        startActivity(Intent(this, ResitActivity::class.java))
    }

    override fun showRequestTranscript() {
        TODO("not implemented")
    }

    private val tutorialShown: String = "tutorial_shown"

    override fun showNextTutorial() {
        val prefs: SharedPreferences = getSharedPreferences(tutorialsConfiguration, Context.MODE_PRIVATE)
        val nextTutorialShown = prefs.getBoolean(tutorialShown, false)
        if (!nextTutorialShown) {
            showTapTarget()

            prefs.edit().putBoolean(tutorialShown, true).apply()
        }
    }

    private fun showTapTarget() {
        MaterialTapTargetPrompt.Builder(this)
                .setTarget(findViewById<FloatingActionButton>(R.id.next))
                .setPrimaryText(R.string.tap_next)
                .create()
                .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setPresenter(HomePresenter(this, this))
        val binding: ActivityMainBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.presenter = localPresenter as HomePresenter
    }
}