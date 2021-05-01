package com.example.myapplication.task_database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.icu.util.LocaleData
import android.text.TextUtils.split
import java.lang.reflect.Array
import java.time.LocalDate
import java.time.LocalDateTime

const val DB_NAME = "com.aziflaj.todolist.db"
const val DB_VERSION = 2
const val COL_TASK_ID = "id"
const val COL_TASK_TITLE = "title"
const val COL_TASK_CMP_DATE = "cmp_date"
const val COL_TASK_DUE = "due"

const val TABLE = "tasks"

class TaskDatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE " + TABLE + " ( " +
               COL_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TASK_TITLE + " TEXT NOT NULL, " +
                COL_TASK_CMP_DATE + " TEXT NOT NULL, " +
                COL_TASK_DUE + " TEXT NOT NULL);"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE)
        onCreate(db)
    }

    fun insert(task:Task){
       val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_TASK_TITLE, task.title)
        cv.put(COL_TASK_CMP_DATE, task.cmp_day)
        cv.put(COL_TASK_DUE, task.due)
        db.insert(TABLE, null, cv)
        db.close()
    }

    var Tasks: ArrayList<Task>? = null
        get(){
            val list_tasks = ArrayList<Task>()
            val selectQuery = "SELECT * FROM $TABLE"
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if(cursor.moveToFirst()){
                do{
                    list_tasks.add(Task(cursor.getInt(cursor.getColumnIndex(COL_TASK_ID)),
                                     cursor.getString(cursor.getColumnIndex(COL_TASK_TITLE)),
                                    cursor.getString(cursor.getColumnIndex(COL_TASK_DUE)),
                                     cursor.getString(cursor.getColumnIndex(COL_TASK_CMP_DATE))
                    ))
                }while (cursor.moveToNext())
            }
            db.close()
            return  list_tasks
        }

    fun update(task:Task){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_TASK_TITLE, task.title)
        cv.put(COL_TASK_CMP_DATE, task.cmp_day)
        cv.put(COL_TASK_DUE, task.due)
        val res = db.update(TABLE,  cv, "$COL_TASK_ID = ?", arrayOf(task.id.toString()) )
        db.close()
    }

    fun delete(id:String){
        val db = this.writableDatabase
        db.delete(TABLE,"$COL_TASK_ID = ?", arrayOf(id) )
        db.close()
    }

    fun Tasks_ByDate(date: Int, month: Int): ArrayList<Task>{
        val list_tasks = ArrayList<Task>()
        Tasks?.forEach {
            var date_split = it.cmp_day.split("/")
            if(date == date_split[0].toInt() && month == date_split[1].toInt() ){
                list_tasks.add(it)
            }
        }
        return list_tasks
    }
}
