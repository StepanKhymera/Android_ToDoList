package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.myapplication.task_database.Task
import com.example.myapplication.task_database.TaskDatabaseHelper

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    var listView: ListView? = null
    var adapter: MyAdapter? = null
    var arrayList: ArrayList<Task> = ArrayList()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_first, container, false)
        listView = view.findViewById(R.id.list_todo)



        return view
    }

    override fun onStart() {
        super.onStart()

        var db = TaskDatabaseHelper(this.context)
        val date = java.util.Calendar.getInstance()
        arrayList = db.Tasks_ByDate(date[java.util.Calendar.DATE], date[java.util.Calendar.MONTH])
        adapter = this.context?.let { MyAdapter(it, arrayList) }
        listView?.adapter = adapter
    }
}