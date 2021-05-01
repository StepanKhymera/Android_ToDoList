package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.task_database.Task
import com.example.myapplication.task_database.TaskDatabaseHelper
import com.google.android.material.chip.ChipGroup
import java.text.SimpleDateFormat
import java.util.*


class Edit_Task : AppCompatActivity() {

    var textview_date: TextView? = null
    var cal = Calendar.getInstance()
    var sdf: SimpleDateFormat? = null
    var task_id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit__task)
        val actionBar =  supportActionBar
        actionBar!!.title = "Edit task"
        actionBar.setDisplayHomeAsUpEnabled(true)
        textview_date = findViewById( R.id.text_view_date_1)

        val myFormat = "MM/dd/yyyy"
        sdf = SimpleDateFormat(myFormat, Locale.ITALIAN)

        val b = intent.extras
        var task: Task? = null
        if (b != null) {
            task = Task(b.getInt("id"), b.getString("name")!!, b.getString("due")!!, b.getString("cmp")!!)
            task_id = task.id
        }
        findViewById<EditText>(R.id.edit_task_name).setText(task!!.title)
        val chips =  findViewById<ChipGroup>(R.id.chipGroup)

        val date = java.util.Calendar.getInstance()
        when(task.cmp_day){
            "${date[Calendar.DATE]}/${date[Calendar.MONTH]}/${date[Calendar.YEAR]}" -> {
                chips.check(R.id.today)
            }
            "${date[Calendar.DATE+1]}/${date[Calendar.MONTH]}/${date[Calendar.YEAR]}" -> {
                chips.check(R.id.tomorrow)
            }
            "" -> {
                chips.check(R.id.all)
            }
        }

        val split = task.due.split("/")
        cal.set(Calendar.YEAR, split[2].toInt())
        cal.set(Calendar.MONTH, split[1].toInt())
        cal.set(Calendar.DAY_OF_MONTH, split[0].toInt())
        updateDateInView()


        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        textview_date!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(view.context,
                        dateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })

        findViewById<Button>(R.id.add_new_butt).setOnClickListener {
            val task = read_task_from_UI()
            val db = TaskDatabaseHelper(this)
            db.update(task)
            finish()
        }


        findViewById<Button>(R.id.delete_butt).setOnClickListener {
            val task = read_task_from_UI()
            val db = TaskDatabaseHelper(this)
            db.delete(task.id.toString())
            finish()
        }

        findViewById<Button>(R.id.googSwitch).setOnClickListener {
            val task = read_task_from_UI()
            val intent = Intent(Intent.ACTION_EDIT)
            intent.setType("vnd.android.cursor.item/event")
            intent.putExtra(CalendarContract.Events.TITLE, task.title);
            intent.putExtra(CalendarContract.Events.DESCRIPTION, "My To Do List :3");
            intent.putExtra(CalendarContract.Events.ALL_DAY, true);
            val split = task.due.split("/")
            val calDate = GregorianCalendar(split[2].toInt(), split[1].toInt(), split[0].toInt())
            intent.putExtra(
                CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                calDate.timeInMillis
            )
            intent.putExtra(
                CalendarContract.EXTRA_EVENT_END_TIME,
                calDate.timeInMillis
            )
            intent.putExtra(CalendarContract.Events.ALL_DAY, true);

            startActivity(intent)
        }

    }
    private fun read_task_from_UI(): Task{
        var cmp_date:String? = null
        val date = Calendar.getInstance()
        when(findViewById<ChipGroup>(R.id.chipGroup).checkedChipId){
            R.id.today -> {
                cmp_date = "${date[java.util.Calendar.DATE]}/${date[java.util.Calendar.MONTH]}/${date[java.util.Calendar.YEAR]}"
            }
            R.id.tomorrow -> {
                cmp_date = "${date[java.util.Calendar.DATE+1]}/${date[java.util.Calendar.MONTH]}/${date[java.util.Calendar.YEAR]}"
            }
            R.id.all -> {
                cmp_date = ""
            }
        }
        val task = Task(task_id!!,
            findViewById<EditText>(R.id.edit_task_name).text.toString(),
            sdf!!.format(cal.getTime()),
            cmp_date!!)
        return task
    }

    private fun updateDateInView() {
        textview_date!!.setText("Due: ${sdf!!.format(cal.getTime())}")
    }
}