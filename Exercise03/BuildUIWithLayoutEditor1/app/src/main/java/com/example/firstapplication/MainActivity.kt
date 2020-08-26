package com.example.firstapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
// import all id's from activity_main layout (now only textView is used)
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // layout will be drawn to the screen from activity_main.xml file
        setContentView(R.layout.activity_main)
    }
}
