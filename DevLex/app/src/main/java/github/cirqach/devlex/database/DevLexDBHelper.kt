package github.cirqach.devlex.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class DevLexDBHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object {
        val TAG: String = "DatabaseHelper"

        private const val DATABASE_NAME = "DevLex.db"

        private const val DATABASE_VERSION = 1

        fun getTableRowCount(TABLE_NAME: String, dbh: DevLexDBHelper): Int {
            val db = dbh.readableDatabase
            val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
            val count = cursor.count
            cursor.close()
            Log.d(TAG, "getTableRowCount: returning $count")
            return count
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d(TAG, "onCreate: called OnCreate")
    }


    @Throws(IOException::class)
    fun copyDatabase(context: Context?) {
        if (context == null) {
            Log.d(TAG, "copyDatabase: context null")
        } else {
            if (!checkdatabase(context)) {
                Log.d(TAG, "copyDatabase: database doesn't exist")
                Log.d(TAG, "copyDatabase: starting copying")

                // Open your local db as the input stream
                val myInput: InputStream = context.assets!!.open(DATABASE_NAME)

                val outputFile = context.getDatabasePath(DATABASE_NAME)

                // Open the empty db as the output stream (automatically goes to /databases/)
                val myOutput: OutputStream = FileOutputStream(outputFile)

                // Transfer byte to byte from input to output
                val buffer = ByteArray(1024)
                var length: Int
                while (myInput.read(buffer).also { length = it } > 0) {
                    myOutput.write(buffer, 0, length)
                }

                // Close streams
                myOutput.flush()
                myOutput.close()
                myInput.close()

                if (checkdatabase(context)) {
                    Log.d(TAG, "copyDatabase: database copying successful")
                } else {
                    Log.d(TAG, "copyDatabase: database copying failed")
                }
            } else {
                Log.d(TAG, "copyDatabase: database exist")
            }
        }
    }

    fun checkdatabase(context: Context?): Boolean {
        val DB_PATH: String? = context?.applicationInfo?.dataDir?.let { "$it/assets/" }
        var checkdb = false
        try {
            val myPath = DB_PATH + DATABASE_NAME
            val dbfile = File(myPath)
            checkdb = dbfile.exists()
        } catch (e: SQLiteException) {
            Log.d(TAG, "checkdatabase: ${e.stackTrace}")
        }
        return checkdb
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "onUpgrade: CALLED ON UPGRADE")
    }

    // add data to Lexicon
    fun addDataToLexicon(english_name: String, russian_name: String, defenition: String): Boolean {
        val p0 = this.writableDatabase
        val content_values = ContentValues()
        content_values.put(DevLexDatabaseContract.LexiconEntry.ENGLISH_NAME, english_name)
        content_values.put(DevLexDatabaseContract.LexiconEntry.RUSSIAN_NAME, russian_name)
        content_values.put(DevLexDatabaseContract.LexiconEntry.WORD_DEFENITION, defenition)
        val result = p0.insert(DevLexDatabaseContract.LexiconEntry.TABLE_NAME, null, content_values)
        return result != -1.toLong()
    }

    fun readAll(TABLE_NAME: String): Cursor? {
        val p0 = this.writableDatabase
        Log.d(TAG, "readAll: reading all data from $TABLE_NAME in ${p0.path}")
        val cursor =
            p0.rawQuery("select * from $TABLE_NAME", null)
        return cursor
    }

    fun deleteData(TABLE_NAME: String, ID: String) {
        val db = this.writableDatabase
        Log.d(TAG, "deleteData: trying deleting")
        // delete the data from the table
        db.delete(TABLE_NAME, "ID = ?", arrayOf(ID))
        Log.d(TAG, "deleteData: item with $ID delete, i perhaps")
        // close the database connection
        db.close()
    }

    fun saveDataToLexicon(
        englishName: String,
        russianName: String,
        definition: String,
        id: String
    ) {
        Log.d(TAG, "Start saving data to Lexicon")

        // 1. Check for valid database access:
        val db = writableDatabase
        if (db == null || !db.isOpen) {
            Log.d(TAG, "Database not open for update")
            return  // Exit the function if database is unavailable
        }

        try {
            // 2. Prepare content values:
            val contentValues = ContentValues().apply {
                put(DevLexDatabaseContract.LexiconEntry.ENGLISH_NAME, englishName)
                put(DevLexDatabaseContract.LexiconEntry.RUSSIAN_NAME, russianName)
                put(DevLexDatabaseContract.LexiconEntry.WORD_DEFENITION, definition)
            }
            Log.d("save data fun", "saveData: content values = $contentValues")

            // 3. Update the database:
            val whereClause = "${DevLexDatabaseContract.LexiconEntry.ID} = ?"
            val whereArgs = arrayOf(id)
            val rowsUpdated = db.update(
                DevLexDatabaseContract.LexiconEntry.TABLE_NAME,
                contentValues,
                whereClause,
                whereArgs
            )

            Log.d(TAG, "Rows updated: $rowsUpdated")
            if (rowsUpdated == 0) {
                Log.d(TAG, "No rows updated, data might be unchanged or entry not found")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error saving data: $e")
            // Handle potential exceptions during database operations (optional)
        } finally {
            // 4. Always close the database connection:
            db.close()
        }
    }

    fun saveDataToTest(
        TABLE_NAME: String,
        RESULT: String,
        RESULT_PROCENT: String,
        TIME_START: String,
        TIME_FINISH: String,
        PROGRESS: String,
        ID: String
    ) {
        Log.d(
            TAG,
            "saveDataToTest: saving data($RESULT, $RESULT_PROCENT, $TIME_START, $TIME_FINISH, $PROGRESS) to table $TABLE_NAME"
        )
        val db = writableDatabase
        if (db == null || !db.isOpen) {
            Log.d(TAG, "Database not open for update")
            return  // Exit the function if database is unavailable
        }

        try {
            // 2. Prepare content values:
            val contentValues = ContentValues().apply {
                put(DevLexDatabaseContract.Test.RESULT, RESULT)
                put(DevLexDatabaseContract.Test.RESULT_PROCENT, RESULT_PROCENT)
                put(DevLexDatabaseContract.Test.TIME_START, TIME_START)
                put(DevLexDatabaseContract.Test.TIME_FINISH, TIME_FINISH)
                put(DevLexDatabaseContract.Test.PROGRESS, PROGRESS)
            }
            Log.d(TAG, "saveDataToTest: content values = $contentValues")

            // 3. Update the database:
            val whereClause = "${DevLexDatabaseContract.LexiconEntry.ID} = ?"
            val whereArgs = arrayOf(ID)
            val rowsUpdated = db.update(
                DevLexDatabaseContract.LexiconEntry.TABLE_NAME,
                contentValues,
                whereClause,
                whereArgs
            )
            Log.d(TAG, "saveDataToTest: whereClause = $whereClause\nwhereArgs = $whereArgs")
            Log.d(TAG, "Rows updated: $rowsUpdated")
            if (rowsUpdated == 0) {
                Log.d(TAG, "No rows updated, data might be unchanged or entry not found")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error saving data: $e")
            // Handle potential exceptions during database operations (optional)
        } finally {
            // 4. Always close the database connection:
            db.close()
        }
    }

    fun getDataByIdFromLexicon(TABLE_NAME: String, ID: Int): DataList? {
        val db = readableDatabase
        val query =
            "SELECT * FROM $TABLE_NAME WHERE ${DevLexDatabaseContract.LexiconEntry.ID} = ${ID.toInt()}"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        if (ID != 0 && ID > 0) {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val id =
                        cursor.getInt(cursor.getColumnIndexOrThrow(DevLexDatabaseContract.LexiconEntry.ID))
                    val english_name =
                        cursor.getString(cursor.getColumnIndexOrThrow(DevLexDatabaseContract.LexiconEntry.ENGLISH_NAME))
                    val russian_name =
                        cursor.getString(cursor.getColumnIndexOrThrow(DevLexDatabaseContract.LexiconEntry.RUSSIAN_NAME))
                    val defenition =
                        cursor.getString(cursor.getColumnIndexOrThrow(DevLexDatabaseContract.LexiconEntry.WORD_DEFENITION))
                    val result: DataList =
                        DataList(english_name, russian_name, defenition, id.toString());
                    cursor.close()
                    return result
                } else {
                    Log.d(TAG, "getDataByIdFromLexicon: cursor is 0 rows")
                    cursor.close()
                }
            }
        } else {
            Log.d(TAG, "getDataByIdFromLexicon: ID is null $ID")
        }
        cursor.close()
        return null;
    }


}



