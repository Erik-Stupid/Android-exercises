package com.example.roomshoppinglist

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), AskShoppingListItemDialogFragment.AddDialogListener {
    // Shopping List Room database
    private lateinit var db: ShoppingListRoomDatabase
    // Shopping List items
    private var shoppingList: MutableList<ShoppingListItem> = ArrayList()
    // Shopping List adapter
    private lateinit var adapter: ShoppingListAdapter


    // onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // Use LinearManager as a layout manager for recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Create Adapter for recyclerView
        adapter = ShoppingListAdapter(shoppingList)
        recyclerView.adapter = adapter

        // Create database and get instance
        db = Room.databaseBuilder(applicationContext, ShoppingListRoomDatabase::class.java, "hs_db").build()
        // load all shopping list items
        loadShoppingListItems()

        // add a new item to shopping list
        fab.setOnClickListener() { view ->
            // create and show dialog
            val dialog = AskShoppingListItemDialogFragment()
            dialog.show(supportFragmentManager, "AskNewItemDialogFragment")
        }
        // Initialize swipe in recyclerView
        fun initSwipe() {
            // Create Touch Callback
            val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                // Swiped
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    // adapter delete item position
                    val position = viewHolder.adapterPosition
                    // Create a Handler Object
                    val handler = Handler(Handler.Callback {
                        // Toast message
                        Toast.makeText(applicationContext,it.data.getString("message"), Toast.LENGTH_SHORT).show()
                        // Notify adapter data change
                        adapter.update(shoppingList)
                        true
                    })
                    // Get remove item id
                    var id = shoppingList[position].id
                    // Remove from UI list
                    shoppingList.removeAt(position)
                    // Remove from db
                    Thread(Runnable {
                        db.shoppingListDao().delete(id)
                        val message = Message.obtain()
                        message.data.putString("message","Item deleted from db!")
                        handler.sendMessage(message)
                    }).start()
                }

                // Moved
                override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
                    return true
                }

            }
            // Attach Item Touch Helper to recyclerView
            val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }
    }


    // Load shopping list items from db
    private fun loadShoppingListItems() {
        // Question: How to create a database operation, if UI thread can't be used
        // -> Handler
        // -> AsyncTask
        // -> Thread
        // How about insert, query, delete, update, etc...
        // -> So you might need to do a multiple Handlers or AsyncTask to handle all situations...
        // => Not GOOD!
        // Other options: Android Architecture Components, RXJava, RXAndroid, RXKotlin
        // - You will learn these in other exercises
        // OK - Now, we will use own Thread and Handler for a learning purpose

        // Create a Handler Object
        val handler = Handler(Handler.Callback {
            // Toast message
            Toast.makeText(applicationContext,it.data.getString("message"), Toast.LENGTH_SHORT).show()
            // Notify adapter data change
            adapter.update(shoppingList)
            true
        })
        // Create a new Thread to insert data to database
        Thread(Runnable {
            shoppingList = db.shoppingListDao().getAll()
            val message = Message.obtain()
            if (shoppingList.size > 0)
                message.data.putString("message","All shopping list items read from db!")
            else
                message.data.putString("message","Shopping list is empty!")
            handler.sendMessage(message)
        }).start()
    }
    // Add a new shopping list item to db
    override fun onDialogPositiveClick(item: ShoppingListItem) {
        // Create a Handler Object
        val handler = Handler(Handler.Callback {
            // Toast message
            Toast.makeText(applicationContext,it.data.getString("message"), Toast.LENGTH_SHORT).show()
            // Notify adapter data change
            adapter.update(shoppingList)
            true
        })
        // Create a new Thread to insert data to database
        Thread(Runnable {
            // insert and get autoincrement id of the item
            val id = db.shoppingListDao().insert(item)
            // add to view
            item.id = id.toInt()
            shoppingList.add(item)
            val message = Message.obtain()
            message.data.putString("message","Item added to db!")
            handler.sendMessage(message)
        }).start()
    }
}