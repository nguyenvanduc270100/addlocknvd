package com.bienlongtuan.applocker.data.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.bienlongtuan.applocker.data.model.DBAppLock

class DbApp (private val context: Context,
factory: SQLiteDatabase.CursorFactory?)
: SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(p0: SQLiteDatabase?) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("hieu", Context.MODE_PRIVATE)
        val edit: SharedPreferences.Editor = sharedPreferences.edit()
        if (!sharedPreferences.getBoolean("saved", false)) {
            val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                    TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_PACKAGENAME + " TEXT," +
                    COLUMN_ISLOCK + " INTEGER," +
                    COLUMN_ISFAVORITE + " INTEGER" +")")

            p0!!.execSQL(CREATE_PRODUCTS_TABLE)
            edit.putBoolean("saved", true)
            edit.apply()
        } else {

        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "db.applock"
        val TABLE_NAME = "app"
        val COLUMN_ID = "_id"
        val COLUMN_NAME = "name"
        val COLUMN_PACKAGENAME = "package"
        val COLUMN_ISFAVORITE = "isFavorite"
        val COLUMN_ISLOCK = "isLock"

    }
    fun insertApp(name: String, packageName: String, isFavorite:Int, isLock: Int) {
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_PACKAGENAME, packageName)
        values.put(COLUMN_ISFAVORITE, isFavorite)
        values.put(COLUMN_ISLOCK, isLock)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
    fun getApp(): ArrayList<DBAppLock> {
        var arr: ArrayList<DBAppLock> = ArrayList()
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM app ", null)
        cursor.moveToFirst()

        while (!cursor.isAfterLast) {

            var app = DBAppLock(
                cursor.getString(cursor.getColumnIndex("name")),
                cursor.getString(cursor.getColumnIndex("package")),
                "",
                cursor.getInt(cursor.getColumnIndex("isLock")),
                cursor.getInt(cursor.getColumnIndex("isFavorite")),
            )
//
            arr.add(app)
            cursor.moveToNext()
        }

        return arr
    }
//    fun updateHistory(path: String, time: Long) {
//        val contentValues = ContentValues()
//        val db = this.readableDatabase
//        contentValues.put(COLUMN_HISTORY, time)
//        db.update(TABLE_NAME, contentValues, "path = '$path'", null)
//        return
//    }
    fun updateLock(packageName: String, isLock: Int) {
        val contentValues = ContentValues()
        val db = this.readableDatabase
        contentValues.put(COLUMN_ISLOCK, isLock)
        db.update(TABLE_NAME, contentValues, "package = '$packageName'", null)
        return
    }

    fun updateFavorite(packageName: String, isFavorite: Int) {
        val contentValues = ContentValues()
        val db = this.readableDatabase
        contentValues.put(COLUMN_ISFAVORITE, isFavorite)
        db.update(TABLE_NAME, contentValues, "package = '$packageName'", null)
        return
    }

    fun checkPath(packageName: String): Boolean {
        var arr: ArrayList<DBAppLock> = ArrayList()
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM app WHERE package = '$packageName' ", null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {

            var app = DBAppLock(
                cursor.getString(cursor.getColumnIndex("name")),
                cursor.getString(cursor.getColumnIndex("package")),
                "",
                cursor.getInt(cursor.getColumnIndex("isLock")),
                cursor.getInt(cursor.getColumnIndex("isFavorite")),
            )
//
            arr.add(app)
            cursor.moveToNext()
        }
        if (arr.size>0){
            return true
        }
        return false
    }
//    fun updateName(path: String, name: String) {
//        val contentValues = ContentValues()
//        val db = this.readableDatabase
//        contentValues.put(COLUMN_NAME, name)
//        db.update(TABLE_NAME, contentValues, "path = '$path'", null)
//        return
//    }
//    fun updatePath(path: String,pathOld: String) {
//        val contentValues = ContentValues()
//        val db = this.readableDatabase
//        contentValues.put(COLUMN_PATH, path)
//        db.update(TABLE_NAME, contentValues, "path = '$pathOld'", null)
//        return
//    }
//    fun getPDFRecently(): ArrayList<PDF> {
//        var arr: ArrayList<PDF> = ArrayList()
//        val db = this.writableDatabase
//        val cursor = db.rawQuery("SELECT * FROM pdf ORDER BY history DESC  ", null)
//        cursor.moveToFirst()
//        while (!cursor.isAfterLast) {
//
//            var pdf = PDF(cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("date")),
//                cursor.getString(cursor.getColumnIndex("size")),
//                cursor.getString(cursor.getColumnIndex("path")), cursor.getLong(cursor.getColumnIndex("history")),cursor.getLong(cursor.getColumnIndex("sort")),
//                cursor.getInt(cursor.getColumnIndex("favorite")))
////
//            if(pdf.history>0) {
//                arr.add(pdf)
//            }
//            cursor.moveToNext()
//        }
//        return arr
//    }
@SuppressLint("Range")
fun getLocked(): ArrayList<DBAppLock> {
        val arr = ArrayList<DBAppLock>()
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM app WHERE isLock = 1 ", null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val app = DBAppLock(
                cursor.getString(cursor.getColumnIndex("name")),
                cursor.getString(cursor.getColumnIndex("package")),
                "",
                cursor.getInt(cursor.getColumnIndex("isLock")),
                cursor.getInt(cursor.getColumnIndex("isFavorite")),
            )
//
            arr.add(app)
            cursor.moveToNext()
        }
        return arr
    }
    fun deleteApp(packageName: String): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase
        // Define 'where' part of query.
        val selection = COLUMN_PACKAGENAME + " LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(packageName)
        // Issue SQL statement.
        db.delete(TABLE_NAME, selection, selectionArgs)
        return true
    }
}