package com.example.speechrecogner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.fragment_first.view.*
import java.util.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val Fragment.packageManager get() = activity?.packageManager

    interface Communicator {
        fun passDataCom(edittext_input: String?)
    }

    private lateinit var changingText: EditText
    private lateinit var buttonSave: Button
    private lateinit var listenButton: ImageView


    private lateinit var comm: Communicator


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_first, container, false)

        comm = activity as Communicator

        changingText = v.findViewById(R.id.textResult)
        buttonSave = v.findViewById(R.id.saveButton)
        listenButton = v.findViewById(R.id.buttonSpeak)

        listenButton.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            if (packageManager?.let { intent.resolveActivity(it) } != null) {
                startActivityForResult(intent, 10)
            } else {
                Toast.makeText(requireActivity(), "Your device doesn't support speech input.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        buttonSave.setOnClickListener {
            comm.passDataCom(v.textResult.text.toString())
        }

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonNext: Button = view.findViewById(R.id.button_first)

        buttonNext.setOnClickListener {
            fragmentManager
                ?.beginTransaction()
                ?.remove(FirstFragment())
                ?.replace(R.id.nav_host_fragment, SecondFragment())
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ?.commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            10 -> if (resultCode == Activity.RESULT_OK && null != data) {
                val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                textResult.setText(result!![0])            }
        }
    }
}