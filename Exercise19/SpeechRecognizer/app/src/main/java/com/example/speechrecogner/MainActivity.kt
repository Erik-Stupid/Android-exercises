package com.example.speechrecogner

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentTransaction
import androidx.preference.PreferenceManager
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_first.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() , FirstFragment.Communicator { //FirstFragment.FragmentFirstListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        checkTheme()
    }

    // FragmentFirst sisältää EditTextin, jonka sisältö siirretään tänne
    override fun passDataCom(edittext_input: String?) {

        // Alustaa Firebasedatabasen otsikolla: Message
        val db = FirebaseDatabase.getInstance().getReference("Messages")

        // Alustetaan muuttuja ja formatteri, joka ottaa nykyisen ajan tässä muodossa: "M d, yyyy 00:00:00 AM/PM"
        val current = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateTimeInstance()
        val formatted = formatter.format(current)

        // Työntö komento "Messages" otsikon alle.
        val ref = db.push()
        // Jokainen uusi viesti saa uuden random "avaimen", jonka sisään tulee "message" ja "time" kohdat
        ref.child("time").setValue(formatted)
        ref.child("message").setValue(edittext_input.toString())


        Toast.makeText(this,"Text saved to History!", Toast.LENGTH_SHORT).show()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun settingsMenu() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment, SettingsFragment())
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                settingsMenu()
                return true
            }
            // "About" dialogi
            R.id.action_about ->{
                //AlertDialog("Click the microphone icon to start recording speech.")
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("About this app")
                builder.setMessage("The app uses Firebase realtime Database to save data which then will be shown in history page. \nGoogle Speech-to-text API is used for speech recognition. \n \nClick the microphone icon to start recording speech.\n \nClick the text field to type your message. \n \nHistory will have a list of the saved messages.")
                builder.setNeutralButton("OK"){_,_ ->
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
                //Toast.makeText(applicationContext, "click on about", Toast.LENGTH_LONG).show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun getSpeechInput(view: View?) {
        // Intent, joka avaa puheentunnistus-dialogin
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        // Alustaa asetuksia
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        // Tarkistaa tukeeko laita puheentunnistusta. Pääasiassa emulaattoria varten
        if (packageManager?.let { intent.resolveActivity(it) } != null) {
            startActivityForResult(intent, 10)
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            10 -> if (resultCode == Activity.RESULT_OK && data != null) {
                // Ota tuloksen teksti
                val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                textResult.setText(result!![0])
            }
        }
    }

    // Seurasin tätä tutoriaali teeman vaihtamiseen: https://proandroiddev.com/dark-mode-on-android-app-with-kotlin-dc759fc5f0e1
    class MyPreferences(context: Context?) {

        companion object {
            private const val DARK_STATUS = ""
        }

        private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

        var darkMode = preferences.getInt(DARK_STATUS, 0)
            set(value) = preferences.edit().putInt(DARK_STATUS, value).apply()

    }

    fun changeTheme() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.choose_theme_text))
        val styles = arrayOf("Light", "Dark", "System default")
        val checkedItem = MyPreferences(this).darkMode

        builder.setSingleChoiceItems(styles, checkedItem) { dialog, which ->

            when (which) {
                0 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    MyPreferences(this).darkMode = 0
                    delegate.applyDayNight()
                    dialog.dismiss()
                }
                1 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    MyPreferences(this).darkMode = 1
                    delegate.applyDayNight()
                    dialog.dismiss()
                }
                2 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    MyPreferences(this).darkMode = 2
                    delegate.applyDayNight()
                    dialog.dismiss()
                }

            }
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun checkTheme() {
        when (MyPreferences(this).darkMode) {
            0 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                delegate.applyDayNight()
            }
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                delegate.applyDayNight()
            }
            2 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                delegate.applyDayNight()
            }
        }
    }
}

