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
        const val TAG: String = "DatabaseHelper"

        private const val DATABASE_NAME = "DevLex.db"

        private const val DATABASE_VERSION = 1

        fun getTableRowCount(tableName: String, dbh: DevLexDBHelper): Int {
            val db = dbh.readableDatabase
            val cursor = db.query(tableName, null, null, null, null, null, null)
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
            if (!checkDataBase(context)) {
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

                if (checkDataBase(context)) {
                    Log.d(TAG, "copyDatabase: database copying successful")
                } else {
                    Log.d(TAG, "copyDatabase: database copying failed")
                }
            } else {
                Log.d(TAG, "copyDatabase: database exist")
            }
        }
    }

    private fun checkDataBase(context: Context?): Boolean {
        val dataBasePath: String? = context?.applicationInfo?.dataDir?.let { "$it/assets/" }
        var checkDataBase = false
        try {
            val myPath = dataBasePath + DATABASE_NAME
            val dataBaseFile = File(myPath)
            checkDataBase = dataBaseFile.exists()
        } catch (e: SQLiteException) {
            Log.d(TAG, "checkdatabase: ${e.stackTrace}")
        }
        return checkDataBase
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "onUpgrade: CALLED ON UPGRADE")
    }

    // add data to Lexicon
    fun addDataToLexicon(englishName: String, russianName: String, definition: String): Boolean {
        val p0 = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DevLexDatabaseContract.LexiconEntry.ENGLISH_NAME, englishName)
        contentValues.put(DevLexDatabaseContract.LexiconEntry.RUSSIAN_NAME, russianName)
        contentValues.put(DevLexDatabaseContract.LexiconEntry.WORD_DEFENITION, definition)
        val result = p0.insert(DevLexDatabaseContract.LexiconEntry.TABLE_NAME, null, contentValues)
        return result != (-1).toLong()
    }

    fun readAll(tableName: String): Cursor? {
        val p0 = this.writableDatabase
        Log.d(TAG, "readAll: reading all data from $tableName in ${p0.path}")
        val cursor =
            p0.rawQuery("select * from $tableName", null)
        return cursor
    }

    fun deleteData(tableName: String, id: String) {
        val db = this.writableDatabase
        Log.d(TAG, "deleteData: trying deleting")
        // delete the data from the table
        db.delete(tableName, "ID = ?", arrayOf(id))
        Log.d(TAG, "deleteData: item with $id delete, i perhaps")
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
        tableName: String,
        result: Int,
        resultProcent: Int,
        countOfQuestions:Int
    ): Boolean {
        Log.d(TAG, "saveDataToTest: saving $tableName + $result + $resultProcent")
        val p0 = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DevLexDatabaseContract.Test.RESULT, result)
        contentValues.put(DevLexDatabaseContract.Test.RESULT_PROCENT, resultProcent)
        contentValues.put(DevLexDatabaseContract.Test.QUESTION_COUNT, countOfQuestions)
        val returnResult = p0.insert(tableName, null, contentValues)
        return returnResult != (-1).toLong()
    }

    fun getDataByIdFromLexicon(tableName: String, id: Int): DataList? {
        val db = readableDatabase
        val query =
            "SELECT * FROM $tableName WHERE ${DevLexDatabaseContract.LexiconEntry.ID} = $id"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        if (id != 0) { // Handle both null and non-existent IDs
            if (cursor.moveToFirst()) {
                val cursorId =
                    cursor.getInt(cursor.getColumnIndexOrThrow(DevLexDatabaseContract.LexiconEntry.ID))
                val englishName =
                    cursor.getString(cursor.getColumnIndexOrThrow(DevLexDatabaseContract.LexiconEntry.ENGLISH_NAME))
                val russianName =
                    cursor.getString(cursor.getColumnIndexOrThrow(DevLexDatabaseContract.LexiconEntry.RUSSIAN_NAME))
                val definition =
                    cursor.getString(cursor.getColumnIndexOrThrow(DevLexDatabaseContract.LexiconEntry.WORD_DEFENITION))
                val result =
                    DataList(englishName, russianName, definition, id.toString())
                cursor.close()
                return result
            } else {
                Log.d(
                    TAG,
                    "getDataByIdFromLexicon: Entry with ID $id not found"
                ) // More specific message
                cursor.close()
                return null // Or an empty DataList if preferred
            }
        } else {
            Log.d(TAG, "getDataByIdFromLexicon: ID is null")
        }
        cursor.close()
        return null
    }

    fun doesIdExist(tableName: String, id: Int): Boolean {
        val db = readableDatabase
        val query =
            "SELECT 1 FROM $tableName WHERE ${DevLexDatabaseContract.LexiconEntry.ID} = $id"
        val cursor = db.rawQuery(query, null)
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }


}



