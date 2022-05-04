package com.lgi.applock.data.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.lgi.applock.models.DBAppLock

class DbApp(
    private val context: Context,
    factory: SQLiteDatabase.CursorFactory?
) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(p0: SQLiteDatabase?) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val edit: SharedPreferences.Editor = sharedPreferences.edit()
        if (!sharedPreferences.getBoolean("saved", false)) {
            val createTable = ("CREATE TABLE " +
                    TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_PACKAGE_NAME + " TEXT," +
                    COLUMN_IS_LOCK + " INTEGER," +
                    COLUMN_FLAGS + " INTEGER," +
                    COLUMN_IS_FAVORITE + " INTEGER" + ")")

            p0!!.execSQL(createTable)
            edit.putBoolean("saved", true)
            edit.apply()
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "db.applock"
        const val TABLE_NAME = "app"
        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PACKAGE_NAME = "package"
        const val COLUMN_IS_FAVORITE = "isFavorite"
        const val COLUMN_IS_LOCK = "isLock"
        const val COLUMN_FLAGS = "flag"

    }

    fun insertApp(name: String, packageName: String, isFavorite: Int, isLock: Int,flag: Int) {
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_PACKAGE_NAME, packageName)
        values.put(COLUMN_IS_FAVORITE, isFavorite)
        values.put(COLUMN_IS_LOCK, isLock)
        values.put(COLUMN_FLAGS, flag)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getApp(): ArrayList<DBAppLock> {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        return handleArrayDB(cursor, db)
    }

    fun getFavorite(isSizeLock: Boolean = true): ArrayList<DBAppLock> {
        val sql: String = if (isSizeLock) {
            "SELECT * FROM $TABLE_NAME Where $COLUMN_IS_FAVORITE = 1"
        } else {
            "SELECT * FROM $TABLE_NAME Where $COLUMN_IS_FAVORITE = 1 AND $COLUMN_IS_LOCK = 1"
        }
        val db = this.writableDatabase
        val cursor = db.rawQuery(sql, null)
        return handleArrayDB(cursor, db)
    }

    @SuppressLint("Range")
    private fun handleArrayDB(cursor: Cursor, db: SQLiteDatabase): ArrayList<DBAppLock> {
        val arr: ArrayList<DBAppLock> = ArrayList()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val app = DBAppLock(
                cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_PACKAGE_NAME)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_FLAGS)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_IS_LOCK)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_IS_FAVORITE)),
            )
            arr.add(app)
            cursor.moveToNext()
        }
        db.close()

        return arr
    }

    fun updateAllFavoriteLock(isLock: Int) {
        val contentValues = ContentValues()
        val db = this.readableDatabase
        contentValues.put(COLUMN_IS_LOCK, isLock)
        db.update(TABLE_NAME, contentValues, "$COLUMN_IS_FAVORITE = 1", null)
        db.close()

        return
    }


    fun updateLock(packageName: String, isLock: Int) {
        val contentValues = ContentValues()
        val db = this.readableDatabase
        contentValues.put(COLUMN_IS_LOCK, isLock)
        db.update(TABLE_NAME, contentValues, "package = '$packageName'", null)
        db.close()

        return
    }

    fun updateFavorite(packageName: String, isFavorite: Int) {
        val contentValues = ContentValues()
        val db = this.readableDatabase
        contentValues.put(COLUMN_IS_FAVORITE, isFavorite)
        db.update(TABLE_NAME, contentValues, "package = '$packageName'", null)
        db.close()

        return
    }

    @SuppressLint("Range")
    fun checkPath(packageName: String): Boolean {
        val arr: ArrayList<DBAppLock> = ArrayList()
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE package = '$packageName' ", null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {

            val app = DBAppLock(
                cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_PACKAGE_NAME)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_FLAGS)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_IS_LOCK)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_IS_FAVORITE)),
            )
//
            arr.add(app)
            cursor.moveToNext()
        }
        if (arr.size > 0) {
            return true
        }
        db.close()

        return false
    }

    @SuppressLint("Range")
    fun getLocked(): ArrayList<DBAppLock> {
        val arr = ArrayList<DBAppLock>()
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_IS_LOCK = 1 ", null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val app = DBAppLock(
                cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_PACKAGE_NAME)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_FLAGS)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_IS_LOCK)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_IS_FAVORITE)),
            )
//
            arr.add(app)
            cursor.moveToNext()
        }
        db.close()
        return arr
    }

    fun deleteApp(packageName: String): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase
        // Define 'where' part of query.
        val selection = "$COLUMN_PACKAGE_NAME LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(packageName)
        // Issue SQL statement.
        db.delete(TABLE_NAME, selection, selectionArgs)
        db.close()

        return true
    }
}