package github.cirqach.devlex.app_pages

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
import github.cirqach.devlex.data.DevLexDBHelper

class add_word_activity : AppCompatActivity() {

    private lateinit var english_name: EditText
    private lateinit var russian_name: EditText
    private lateinit var definition: EditText
    private lateinit var add_button: Button

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

        english_name = findViewById(R.id.english_name_edit_text)
        russian_name = findViewById(R.id.russian_name_edit_text)
        definition = findViewById(R.id.defenition_edit_text)
        add_button = findViewById(R.id.add_button)

        val db: DevLexDBHelper = DevLexDBHelper(this)

        add_button.setOnClickListener {
            val englishName_text = english_name.text.toString()
            val russianName_text = russian_name.text.toString()
            val definition_text = definition.text.toString()
            val savedata = db.addDataToLexicon(englishName_text, russianName_text, definition_text)
            if (TextUtils.isEmpty(englishName_text) || TextUtils.isEmpty(russianName_text)) {
                Toast.makeText(this, "You can't leave empty field", Toast.LENGTH_SHORT).show()
            } else if (savedata == true) {
                Toast.makeText(this, "Word added to lexicon", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "This words already exist", Toast.LENGTH_SHORT).show()
            }
            onBackPressed()

        }


    }

}