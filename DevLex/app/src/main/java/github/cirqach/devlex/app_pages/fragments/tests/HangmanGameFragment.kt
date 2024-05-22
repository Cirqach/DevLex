package github.cirqach.devlex.app_pages.fragments.tests

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import github.cirqach.devlex.R
import github.cirqach.devlex.app_pages.tests.HangmanGame
import github.cirqach.devlex.database.DevLexDBHelper
import github.cirqach.devlex.database.HangmanGame.HangmanHelper
import java.util.Random

class HangmanGameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hangman_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val play = view.findViewById<Button>(R.id.play)
        play.setOnClickListener {
            val intent = Intent(activity, HangmanGame::class.java)
            val dbHelper = DevLexDBHelper(view.context)
            val db = HangmanHelper()
            val guessWordsArray: MutableList<String?> = db.getQuestionsList(dbHelper, 10)

            val rand = Random().nextInt(guessWordsArray.size)
            val secretWord = guessWordsArray[rand]

            intent.putExtra("secretWord", secretWord)
            activity?.startActivity(intent)
        }
    }
}