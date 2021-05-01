package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import com.example.myapplication.task_database.Task
import com.example.myapplication.task_database.TaskDatabaseHelper
import java.util.*


class MyAdapter(private val context: Context, private val arrayList: ArrayList<Task>) : BaseAdapter() {
    private lateinit var title_View: TextView
    private lateinit var due_View: TextView
    private lateinit var id_View: TextView
    override fun getCount(): Int {
        return arrayList.size
    }
    override fun getItem(position: Int): Any {
        return position
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val convertView = LayoutInflater.from(context).inflate(R.layout.a_task, parent, false)

        id_View = convertView.findViewById(R.id.id)
        id_View.text = arrayList[position].id.toString()

        title_View = convertView.findViewById(R.id.title_task)
        title_View.text = arrayList[position].title

        due_View = convertView.findViewById(R.id.due_date)
        due_View.text = arrayList[position].due
        convertView.findViewById<Button>(R.id.done_butt).setOnClickListener { view ->
            val id =  ((view.parent as ConstraintLayout).getChildAt(0) as TextView).text.toString()
            val db = TaskDatabaseHelper(this.context)
            db.delete(id)
            this.arrayList.removeIf{ t -> t.id == id.toInt() }
            this.notifyDataSetChanged()
        }
        convertView.findViewById<Button>(R.id.edit_butt).setOnClickListener { view ->
            val id =  ((view.parent as ConstraintLayout).getChildAt(0) as TextView).text.toString().toInt()
            val db = TaskDatabaseHelper(this.context)
            val task = db.Tasks?.filter { t -> t.id == id }?.first()

            val intent = Intent(this.context, Edit_Task::class.java)
            val b = Bundle()
            b.putInt("id", task!!.id )
            b.putString("name", task.title)
            b.putString("due", task.due)
            b.putString("cmp", task.cmp_day)
            intent.putExtras(b)
            startActivity(this.context, intent,b)
        }
        return convertView
    }
}

