package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.example.myapplication.task_database.Task
import com.example.myapplication.task_database.TaskDatabaseHelper

/**
 * A simple [Fragment] subclass.
 * Use the [all_List.newInstance] factory method to
 * create an instance of this fragment.
 */
class all_List : Fragment() {
    var listView: ListView? = null
    var adapter: MyAdapter? = null
    var arrayList: ArrayList<Task> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_first, container, false)

        listView = view.findViewById(R.id.list_todo)

        var db = TaskDatabaseHelper(view.context)

        arrayList = db.Tasks ?: ArrayList()

        adapter = MyAdapter(view.context, arrayList)

        listView?.adapter = adapter

        return view
    }
}