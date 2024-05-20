package github.cirqach.devlex.app_pages

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import github.cirqach.devlex.R
import github.cirqach.devlex.database.DevLexDBHelper

class AddWordActivity : AppCompatActivity() {

    private lateinit var englishName: EditText
    private lateinit var russianName: EditText
    private lateinit var definition: EditText
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_word)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val goBackButton = findViewById<Button>(R.id.go_back_button)
        goBackButton.setOnClickListener {

            onBackPressed()
        }

        englishName = findViewById(R.id.english_name_edit_text)
        russianName = findViewById(R.id.russian_name_edit_text)
        definition = findViewById(R.id.definition_edit_text)
        addButton = findViewById(R.id.add_button)

        val db = DevLexDBHelper(this)

        addButton.setOnClickListener {
            val englishNameText = englishName.text.toString()
            val russiaNameText = russianName.text.toString()
            val definitionText = definition.text.toString()
            val saveData = db.addDataToLexicon(englishNameText, russiaNameText, definitionText)

            if (TextUtils.isEmpty(englishNameText) || TextUtils.isEmpty(russiaNameText)) {
                Toast.makeText(this, "You can't leave empty field", Toast.LENGTH_SHORT).show()
            } else if (saveData) {
                Toast.makeText(this, "Word added to lexicon", Toast.LENGTH_SHORT).show()

                // Set result and finish activity
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this, "This words already exist", Toast.LENGTH_SHORT).show()
            }
        }


    }

}