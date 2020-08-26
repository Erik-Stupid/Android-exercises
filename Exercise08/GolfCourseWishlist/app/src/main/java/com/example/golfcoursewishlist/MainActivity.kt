package com.example.golfcoursewishlist

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row_places.view.*


private var isListView = true
private var mStaggeredLayoutManager: StaggeredGridLayoutManager? = null

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Use StaggeredGridLayoutManager as a layout manager for recyclerView
        mStaggeredLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = mStaggeredLayoutManager

        // Use GolfCourseWishlistAdapter as a adapter for recyclerView
        recyclerView.adapter = GolfCourseWishlistAdapter(Places.placeList())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_toggle -> {
                if (isListView) {
                    item.setIcon(R.drawable.ic_view_stream_white_24dp)
                    item.setTitle("Show as list")
                    isListView = false
                    mStaggeredLayoutManager?.setSpanCount(2)
                } else {
                    item.setIcon(R.drawable.ic_view_column_white_24dp)
                    item.setTitle("Show as grid")
                    isListView = true
                    mStaggeredLayoutManager?.setSpanCount(1)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    class Place {
        var name: String? = null
        var image: String? = null

        fun getImageResourceId(context: Context): Int {
            return context.resources.getIdentifier(this.image,"drawable", context.packageName)
        }
    }

    class Places {
        // like "static" in other OOP languages
        companion object {
            // hard code a few places
            var placeNameArray = arrayOf(
                    "Black Mountain",
                    "Chambers Bay",
                    "Clear Water",
                    "Harbour Town",
                    "Kajaani",
                    "Muirfield",
                    "Old Course",
                    "Pebble Beach",
                    "Spy Class"
            )

            // return places
            fun placeList(): ArrayList<Place> {
                val list = ArrayList<Place>()
                for (i in placeNameArray.indices) {
                    val place = Place()
                    place.name = placeNameArray[i]
                    place.image = placeNameArray[i].replace("\\s+".toRegex(), "").toLowerCase()
                    list.add(place)
                }
                return list
            }
        }
    }

    class GolfCourseWishlistAdapter(private val places: ArrayList<Place>)
        : RecyclerView.Adapter<GolfCourseWishlistAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nameTextView: TextView = view.placeName
            val imageView: ImageView = view.placeImage
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GolfCourseWishlistAdapter.ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.row_places, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = places.size

        // bind data to UI View Holder
        override fun onBindViewHolder(holder: GolfCourseWishlistAdapter.ViewHolder, position: Int) {
            // place to bind UI
            val place: Place = places[position]
            // name
            holder.nameTextView.text = place.name
            // image
            Glide.with(holder.imageView.context).load(place.getImageResourceId(holder.imageView.context)).into(holder.imageView)
        }
    }
}
