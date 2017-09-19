package fr.masciulli.drinks.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import fr.masciulli.drinks.BuildConfig
import fr.masciulli.drinks.R

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_about)

        val versionView: TextView = findViewById(R.id.version)
        versionView.text = getString(R.string.about_version, BuildConfig.VERSION_NAME)
    }
}
