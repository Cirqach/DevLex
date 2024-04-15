package github.cirqach.devlex

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import github.cirqach.devlex.data.DevLexDBHelper
import github.cirqach.devlex.data.DevLexDatabaseContract


class MainActivity : AppCompatActivity() {
    private val dbh = DevLexDBHelper(this)
    private val REQUEST_CODE_STORAGE_PERMISSION = 123  // Choose a unique integer code


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonStart: Button = findViewById(R.id.start_button)
        buttonStart.setOnClickListener {
            val randomIntent = Intent(this, RealMainActivity::class.java)
            startActivity(randomIntent)

            val TAG = "checking database"
            Log.d(TAG, "onCreate: First check database + creating")
            checkDataBase()
            Log.d(TAG, "onCreate: Double check database")
            checkDataBase()
        }
        val buttonGithub: Button = findViewById(R.id.github_button)
        buttonGithub.setOnClickListener {
            val url = "https://github.com/Cirqach/DevLex.git"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }


    fun checkDataBase() {
        var checkDB: SQLiteDatabase? = null
        val DATABASE_NAME = "DevLex.db"
        val TAG = "checking database"
        val dbPath = applicationInfo.dataDir + DATABASE_NAME


        try {
            checkDB = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY)
            checkDB.close()
            Log.d(TAG, "checkDataBase: Fount database")
        } catch (e: SQLiteException) {
            Log.d(TAG, "checkDataBase: Database does not exist, creating")
            val SQL_CREATE_WORD_TABLE =
                "CREATE TABLE " + DevLexDatabaseContract.LexiconEntry.TABLE_NAME + " (" +
                        DevLexDatabaseContract.LexiconEntry.ID + " INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, " +
                        DevLexDatabaseContract.LexiconEntry.ENGLISH_NAME + " TEXT NOT NULL , " +
                        DevLexDatabaseContract.LexiconEntry.RUSSIAN_NAME + " TEXT NOT NULL , " +
                        DevLexDatabaseContract.LexiconEntry.WORD_DEFENITION + " TEXT);"
            dbh.writableDatabase.execSQL(SQL_CREATE_WORD_TABLE)

            dbh.fillDataBase(dbh.writableDatabase)
            // database doesn't exist yet.
        }
    }

}