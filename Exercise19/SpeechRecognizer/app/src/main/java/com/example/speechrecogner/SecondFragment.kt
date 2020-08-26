package com.example.speechrecogner


import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private lateinit var backButton: Button

    private lateinit var mRecyclerView : RecyclerView
    private lateinit var mTextList : ArrayList<FirebaseData>
    private lateinit var refresh : Button
    private lateinit var db : DatabaseReference
    private lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        v = inflater.inflate(R.layout.fragment_second, container, false)

        backButton = v.findViewById(R.id.button_second)

        mTextList = arrayListOf()

        mRecyclerView = v.findViewById(R.id.messageView)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)

        return v
    }

    private fun getData() {

        // Alustetaan tietokanta samalla tavalla kuin kirjoittamisessa
        db = FirebaseDatabase.getInstance().getReference("Messages")
        //val eventListener: ValueEventListener = object : ValueEventListener {
        mTextList = arrayListOf()
        val mAdapter = RecyclerAdapter(requireContext(), mTextList)

        // Lisätään listener tietokantaa
        db.addListenerForSingleValueEvent(object: ValueEventListener {
            // Jos ei voi kuunnella
            override fun onCancelled(error: DatabaseError) {
                Log.d("ERROR", "" + error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                // Tyhjennetään recyclerView, muuten uudet viestit vain lisätään uudestaan.
                mTextList.clear()
                // Loopataan jokaisen merkinnän läpi
                if (snapshot.exists()) {
                    for (i: DataSnapshot in snapshot.children) {
                        // Otetaan kaikki merkinnät, mitä tietty avain sisältää, eli "message" ja "time"
                        val data = i.getValue(FirebaseData::class.java)
                        // Lisätään adapteriin
                        mTextList.add(data!!)
                        Log.d(TAG, "Value = $data")
                    }
                }

                val dividerItemDecoration =
                    DividerItemDecoration(mRecyclerView.context, DividerItemDecoration.VERTICAL)
                dividerItemDecoration.setDrawable(
                    ContextCompat.getDrawable(context!!, R.drawable.divider)!!
                )
                mRecyclerView.addItemDecoration(dividerItemDecoration)

                // Kiinnitetään adapteri ja refreshataan view
                mRecyclerView.adapter = mAdapter
                mAdapter.notifyDataSetChanged()
            }
        })
    }

    // Metodi lyhyempään toast-ilmoitukseen... Koska... Miksei
    private fun showToastMessage(text: String?, duration: Int) {
        val toast = Toast.makeText(activity, text, Toast.LENGTH_SHORT)
        toast.show()
        val handler = Handler()
        handler.postDelayed(Runnable { toast.cancel() }, duration.toLong())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        refresh = v.findViewById(R.id.refreshButton)!!

        refresh.setOnClickListener {
            getData()
            showToastMessage("Syncing...", 500)
        }


        val buttonNext2: Button = view.findViewById(R.id.button_second)
        buttonNext2.setOnClickListener {
            fragmentManager
                ?.beginTransaction()
                ?.remove(SecondFragment())
                ?.replace(R.id.nav_host_fragment, FirstFragment())
                ?.attach(FirstFragment())
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                ?.commit()
        }
    }
}