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
    private val DB_PATH: String? = context?.applicationInfo?.dataDir?.let {
        "$it/assets/"
    }


    override fun onCreate(db: SQLiteDatabase) {
        Log.d(TAG, "onCreate: called OnCreate")
    }

    @Throws(IOException::class)
    fun copyDatabase(context: Context?) {
        if (context == null) {
            Log.d(TAG, "copyDatabase: context null")
        } else {
            if (!checkdatabase()) {
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

                if (checkdatabase()) {
                    Log.d(TAG, "copyDatabase: database copying successful")
                } else {
                    Log.d(TAG, "copyDatabase: database copying failed")
                }
            } else {
                Log.d(TAG, "copyDatabase: database exist")
            }
        }
    }


    fun checkdatabase(): Boolean {
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

    companion object {
        val TAG: String = "DatabaseHelper"

        private const val DATABASE_NAME = "DevLex.db"

        private const val DATABASE_VERSION = 1
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

    // read all data from lexicon
    // TODO: read from any table
    fun readAllDataFromLexicon(): Cursor? {
        val p0 = this.writableDatabase
        Log.d(TAG, "readAllDataFromLexicon: ${this.writableDatabase.path}")
        val cursor =
            p0.rawQuery("select * from " + DevLexDatabaseContract.LexiconEntry.TABLE_NAME, null)
        return cursor
    }


    // TODO: delete by ID from any table
    fun deleteData(TABLE_NAME: String, ENGLISH_NAME: String) {
        val db = this.writableDatabase

        // delete the data from the table
        db.delete(TABLE_NAME, "ENGLISH_NAME = ?", arrayOf(ENGLISH_NAME))

        // close the database connection
        db.close()
    }

    fun saveDataToLexicon(
        englishName: String,
        russianName: String,
        definition: String,
        id: String
    ) {
        Log.d(TAG, "Start saveData")

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

    /*

        fun getDataByID(TABLE_NAME: String, ID:String): DataList{
            val db = readableDatabase
            val query = "SELECT * FROM $TABLE_NAME WHERE ${DevLexDatabaseContract.LexiconEntry.ID} = ${ID.toInt()}"
            val cursor = db.rawQuery(query, null)
            cursor.moveToFirst()

            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DevLexDatabaseContract.LexiconEntry.ID))
            val english_name = cursor.getInt(cursor.getColumnIndexOrThrow(DevLexDatabaseContract.LexiconEntry.ID))
            val russian_name = cursor.getInt(cursor.getColumnIndexOrThrow(DevLexDatabaseContract.LexiconEntry.ID))
            val defenition = cursor.getInt(cursor.getColumnIndexOrThrow(DevLexDatabaseContract.LexiconEntry.ID))

        }
    */


}