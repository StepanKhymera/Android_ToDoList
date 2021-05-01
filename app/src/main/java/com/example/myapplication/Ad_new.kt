package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.myapplication.task_database.Task
import com.example.myapplication.task_database.TaskDatabaseHelper
import com.google.android.material.chip.ChipGroup
import java.text.SimpleDateFormat
import java.util.*

class Ad_new : AppCompatActivity() {

    var textview_date: TextView? = null
    var cal = Calendar.getInstance()
    var sdf: SimpleDateFormat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_new)
        val actionBar =  supportActionBar
        actionBar!!.title = "Add new task"
        actionBar.setDisplayHomeAsUpEnabled(true)
        textview_date = findViewById( R.id.text_view_date_1)
        val myFormat = "MM/dd/yyyy"
        sdf = SimpleDateFormat(myFormat, Locale.ITALIAN)

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
            var cmp_date:String? = null
            val date = java.util.Calendar.getInstance()
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
            var task = Task(0,
                    findViewById<EditText>(R.id.edit_task_name).text.toString(),
                    sdf!!.format(cal.getTime()),
                    cmp_date!!)
            val db = TaskDatabaseHelper(this)
            db.insert(task)

            if(findViewById<Switch>(R.id.googSwitch).isChecked){

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

            finish()
        }
    }


    private fun updateDateInView() {
        textview_date!!.setText("Due: ${sdf!!.format(cal.getTime())}")
    }

}