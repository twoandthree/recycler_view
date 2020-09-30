package com.iuni.myrecyclerview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.view.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myList = listOf(
                Person("Recker", "555-4664"),
                Person("Pat", "553-4774"),
                Person("Irish", "255-6644"),
                Person("Dom", "515-4544"),
                Person("Recker", "555-4664"),
                Person("Pat", "553-4774"),
                Person("Irish", "255-6644"),
                Person("Dom", "515-4544"),
                Person("Recker", "555-4664"),
                Person("Pat", "553-4774"),
                Person("Irish", "255-6644"),
                Person("Dom", "515-4544")
        )

        my_rv.layoutManager = LinearLayoutManager(this)
        my_rv.setHasFixedSize(true)

        var adapter = MyAdapter(myList, this)
        my_rv.adapter = adapter

        var tracker: SelectionTracker<Long>? = null

        tracker = SelectionTracker.Builder<Long>(
            "selection-1",
            my_rv,
            StableIdKeyProvider(my_rv),
            MyLookup(my_rv),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()


//        fun onSaveInstanceState(outState: Bundle?) {
//            if (outState != null) {
//                super.onSaveInstanceState(outState)
//            }
//
//            if(outState != null)
//                tracker?.onSaveInstanceState(outState)
//        }

        if(savedInstanceState != null) {
            tracker?.onRestoreInstanceState(savedInstanceState)
        }

        adapter.setTracker(tracker)
    }
}

data class Person(val name:String,
        val phone:String)


class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val name: TextView = view.list_item_name
    val phone: TextView = view.list_item_phone

    fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> = object: ItemDetailsLookup.ItemDetails<Long>() {
        override fun getPosition(): Int = adapterPosition

        override fun getSelectionKey(): Long? = itemId
    }
}


class MyLookup(private val rv: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = rv.findChildViewUnder(e.x, e.y)
        if(view != null) {
            return (rv.getChildViewHolder(view) as MyViewHolder).getItemDetails()
        }
        return null
    }
}


class MyAdapter(private val listItems:List<Person>,
private val context: Context) : RecyclerView.Adapter<MyViewHolder>() {
    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int = listItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
            MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false))

    override fun onBindViewHolder(vh: MyViewHolder, position: Int) {
        vh.name.text = listItems[position].name
        vh.phone.text = listItems[position].phone

        val parent = vh.name.parent as LinearLayout

        if(tracker!!.isSelected(position.toLong())) {
            parent.setBackgroundColor(Color.parseColor("#45B97A"))
        } else {
            // Reset color to white if not selected
            parent.setBackgroundColor(Color.parseColor("#1B211E"))
        }
    }

    private var tracker: SelectionTracker<Long>? = null

    fun setTracker(tracker: SelectionTracker<Long>?) {
        this.tracker = tracker
    }

}