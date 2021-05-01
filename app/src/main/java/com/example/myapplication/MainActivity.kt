package com.example.myapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.karn.notify.Notify
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnNavigationItemSelectedListener {it: MenuItem ->
            when(it.itemId){
                R.id.Item_today-> {
                    Navigation.findNavController(findViewById<TextView>(R.id.nav_host_fragment)).navigate(R.id.FirstFragment);
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.Item_tomorrow-> {
                    Navigation.findNavController(findViewById<TextView>(R.id.nav_host_fragment)).navigate(R.id.SecondFragment);
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.Item_All-> {
                    Navigation.findNavController(findViewById<TextView>(R.id.nav_host_fragment)).navigate(R.id.all_List);
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 59)

        }
        Notify
            .with(this)
            .content {
                this.title
                text = "I got triggered at - ass"
            }
            .show()
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val alrmIntent = Intent(this,AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 101, alrmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager?.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        )

        findViewById<FloatingActionButton>( R.id.fab).setOnClickListener { view ->
           startActivity(Intent(this, Ad_new::class.java))
        }
    }

}