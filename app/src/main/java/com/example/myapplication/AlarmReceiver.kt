package com.example.myapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import com.example.myapplication.task_database.Task
import com.example.myapplication.task_database.TaskDatabaseHelper
import io.karn.notify.Notify
import java.util.*

class AlarmReceiver: BroadcastReceiver() {
    val CH_ID = "chanell_id"

    override fun onReceive(context: Context?, intent: Intent?) {
        configChannel(context)

        val due_tasks = getTasksToAlert(context)
        if(due_tasks.isNotEmpty()){
            due_tasks.forEach{t: Task? ->
                sendNotification(context,t!!.id , "Task: \"${t.title}\" is due." )
            }
        }

    }
    private fun getTasksToAlert(context: Context?): List<Task>{
        val db =TaskDatabaseHelper(context)
        val AllTasks = db.Tasks
        val date = java.util.Calendar.getInstance()
        val res =  AllTasks!!.filter { t ->
            t.due.split("/")[0].toInt() ==  (date[Calendar.DAY_OF_MONTH]+1) &&
                    t.due.split("/")[1].toInt() ==  (date[Calendar.MONTH]+1)
        }
        return res
    }

    private  fun sendNotification(context: Context?,id:Int , text:String){
        val notifBuilder = NotificationCompat.Builder(context!!, CH_ID)
            .setContentText(text)
            .setContentTitle("You have a task due tomorrow!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.ic_app_icon)
        with(NotificationManagerCompat.from(context)){
            notify(id, notifBuilder.build())
        }
    }

    private  fun configChannel(context: Context?){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CH_ID, "To Do List",NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "A task is due"
            }
            val notificationManager: NotificationManager =  context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}