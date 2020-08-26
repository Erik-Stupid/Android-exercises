package com.example.employeesapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.employee_item.view.*
import org.json.JSONArray
import org.json.JSONObject

// Employees Adapter, used in RecyclerView in MainActivity
class EmployeesAdapter(private val employees: JSONArray)
    : RecyclerView.Adapter<EmployeesAdapter.ViewHolder>() {

    // Create UI View Holder from XML layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.employee_item, parent, false)
        return ViewHolder(view)
    }

    // View Holder class to hold UI views
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profImageView: ImageView = view.profImageView
        val nameTextView: TextView = view.nameTextView
        val emailTextView: TextView = view.emailTextView
        val phoneTextView: TextView = view.phoneTextView
        val titleTextView: TextView = view.titleTextView
        val departmentTextView: TextView = view.departmentTextView
        // add other Views here: title, email, ...
        // Add a item click listener
        init {
            itemView.setOnClickListener {
                // create an explicit intent
                val intent = Intent(view.context, EmployeeActivity::class.java)
                // add selected employee JSON as a string data
                intent.putExtra("employee",employees[adapterPosition].toString())
                // start a new activity
                view.context.startActivity(intent)
            }
        }
    }

    // Bind data to UI View Holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // employee to bind UI
        val employee: JSONObject = employees.getJSONObject(position)
        // employee lastname and firstname
        //val profImageView = ImageView
        //class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //    val imageView1: ImageView = view.profImageView
        //}
        val imageUrl = employee["image"]//.toString()
        Glide.with(holder.profImageView.context)
            .load(imageUrl)
            .into(holder.profImageView)
        //Glide.with(holder.profImageView.context)
        //    .load(imageUrl)
        //    .into(imageView1)
        //holder.imageView.context
        holder.nameTextView.text = employee["last_name"].toString() + " " + employee["first_name"].toString()
        holder.emailTextView.text = employee["email"].toString()
        holder.phoneTextView.text = employee["phone"].toString()
        holder.titleTextView.text = employee["title"].toString()
        holder.departmentTextView.text = employee["department"].toString()
        // title, email, phone, department, image
        // to get context in Glide, you can use holder.imageView.context
    }

    // Return item count in employees
    override fun getItemCount(): Int = employees.length()
}
