package com.example.employeesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.employee_item.*
import kotlinx.android.synthetic.main.employee_item.view.*
import kotlinx.android.synthetic.main.employee_item.view.profImageView
import org.json.JSONObject

class EmployeeActivity : AppCompatActivity() {

    //private var il: ImageView = profImageView;
    // EmployeeActivity's onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee)

       //il = findViewById(R.id.profImageView)

        // get data from intent
        val bundle: Bundle? = intent.extras;
        if (bundle != null) {
            val employeeString = bundle!!.getString("employee")
            val employee = JSONObject(employeeString)
            //il.setImageResource(resId)

            val imageUrl = employee["image"]

            //var resId = bundle.getInt("profImageView")
            //resId = findViewById(R.id.profImageView)


            Glide.with(profImageView)
                .load(imageUrl)
                .into(profImageView)
            val firstname = employee["first_name"]
            val lastaname = employee["last_name"]
            val email = employee["email"]
            val phone = employee["phone"]
            val title = employee["title"]
            val department = employee["department"]
            nameTextView.text = employee["last_name"].toString() + " " + employee["first_name"].toString()
            emailTextView.text = employee["email"].toString()
            phoneTextView.text = employee["phone"].toString()
            titleTextView.text = employee["title"].toString()
            departmentTextView.text = employee["department"].toString()
            // modify here to display other employee's data too!
            /*
            Toast.makeText(
                this,
                "Name is $firstname $lastaname\nEmail: $email\nPhone: $phone\nTitle: $title\nDepartment: $department",
                Toast.LENGTH_LONG
            ).show()
             */
        }
    }
}
