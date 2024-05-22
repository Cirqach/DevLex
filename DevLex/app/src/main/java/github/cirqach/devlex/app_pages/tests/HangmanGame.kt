package github.cirqach.devlex.app_pages.tests

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import github.cirqach.devlex.R
import github.cirqach.devlex.database.DevLexDBHelper
import github.cirqach.devlex.database.HangmanGame.HangmanHelper
import java.util.Locale
import java.util.Random

class HangmanGame : AppCompatActivity() {
    private var kill = 0
    private var secretWord = ""
    private var secretDisplay = ""
    private val correctGuesses = mutableListOf<String>()
    private lateinit var toBeGuessed: TextView
    private lateinit var hangmanDrawing: ImageView
    private lateinit var playerGuess: EditText
    private lateinit var tryButton: Button
    private lateinit var copySecretWord: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hangman_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val extras = intent.extras
        if (extras != null) {
            secretWord = extras.getString("secretWord").toString()
        }
        copySecretWord = secretWord
        toBeGuessed = findViewById(R.id.toBeGuessed)
        hangmanDrawing = findViewById(R.id.hangmanDrawing)
        tryButton = findViewById(R.id.tryButton)
        playerGuess = findViewById(R.id.playerGuess)
        hangmanDrawing.setImageResource(R.drawable.hangman10)
        prepGame()
    }

    fun guessTry(click: View) {
        val tryButton = findViewById<Button>(R.id.tryButton)
        if (click === tryButton) {
            val pGuess = playerGuess.text.toString().lowercase(Locale.ROOT)
            playerGuess.text = null

            // Player asks for a letter
            if (pGuess.length == 1) {
                if (pGuess in secretWord.lowercase(Locale.ROOT)) {

                    correctGuesses.add(pGuess)
                    refactorSecret()

                    Toast.makeText(applicationContext, "Good guess!", Toast.LENGTH_SHORT).show()
                    checkWin()
                    return
                }
            }

            // Player tries to guess
            if (pGuess.length > 1) {
                if (pGuess.equals(secretWord, ignoreCase = true)) {
                    winDialogPopUp(true)
                    return
                }
            }

            // Player fails
            kill++
            when (kill) {
                1 -> hangmanDrawing.setImageResource(R.drawable.hangman9)
                2 -> hangmanDrawing.setImageResource(R.drawable.hangman8)
                3 -> hangmanDrawing.setImageResource(R.drawable.hangman7)
                4 -> hangmanDrawing.setImageResource(R.drawable.hangman6)
                5 -> hangmanDrawing.setImageResource(R.drawable.hangman5)
                6 -> hangmanDrawing.setImageResource(R.drawable.hangman4)
                7 -> hangmanDrawing.setImageResource(R.drawable.hangman3)
                8 -> hangmanDrawing.setImageResource(R.drawable.hangman2)
                9 -> hangmanDrawing.setImageResource(R.drawable.hangman1)
                10 -> {
                    hangmanDrawing.setImageResource(R.drawable.hangman0)
                    winDialogPopUp(false)
                    val dbh = DevLexDBHelper(this)
                    dbh.saveHangmanResult("lose", copySecretWord)
                }
            }
        }
    }

    // Recreate display of the secret word based on progress
    private fun refactorSecret() {
        secretDisplay = ""
        secretWord.forEach { s ->
            secretDisplay += (checkIfGuessed(s.toString()))
        }
        toBeGuessed.text = secretDisplay
    }

    // Reveal correctly guessed letters
    private fun checkIfGuessed(s: String): String {
        return when (correctGuesses.contains(s.lowercase(Locale.ROOT))) {
            true -> s
            false -> "_"
        }
    }

    // If a char from secretWord isn't in guess chars then player didn't guess everything yet
    private fun checkWin() {
        var everythingGuessed = true
        secretWord.lowercase(Locale.ROOT).forEach { c ->
            if (!correctGuesses.contains(c.toString()))
                everythingGuessed = false
        }
        if (everythingGuessed) {
            winDialogPopUp(true)
            val dbh = DevLexDBHelper(this)
            dbh.saveHangmanResult("win", secretWord)
        }
    }

    // Win/Lose alert
    private fun winDialogPopUp(won: Boolean) {
        val builder = AlertDialog.Builder(this@HangmanGame)
        if (won) {
            builder.setTitle("Congratulations!")
        } else {
            builder.setTitle("Boo! You hanged a man!")
        }
        builder.setMessage("Do you want to play again?")

        builder.setPositiveButton("Let's go") { _, _ ->

            // This one below is in main view more readable
            val dbHelper = DevLexDBHelper(this)
            val db = HangmanHelper()
            secretWord = db.getQuestionsList(dbHelper, 10)[Random().nextInt(10) + 1].toString()
            prepGame()

            Toast.makeText(applicationContext, "New game started!", Toast.LENGTH_SHORT).show()

        }
        builder.setNegativeButton("No") { _, _ ->
            finish()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()

        // Save the word to the database, regardless of whether the player wins or loses
        val dbh = DevLexDBHelper(this)
        dbh.saveHangmanResult(if (won) "win" else "lose", secretWord)
    }

    private fun prepGame() {
        hangmanDrawing.setImageResource(R.drawable.hangman10)
        kill = 0
        secretDisplay = ""
        correctGuesses.clear()

        repeat(secretWord.length) {
            secretDisplay += "_"
        }

        toBeGuessed.text = secretDisplay
    }
}