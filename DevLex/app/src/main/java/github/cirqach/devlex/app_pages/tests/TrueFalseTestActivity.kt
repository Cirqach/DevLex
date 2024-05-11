package github.cirqach.devlex.app_pages.tests

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import github.cirqach.devlex.R
import github.cirqach.devlex.database.DevLexDBHelper
import github.cirqach.devlex.database.FindTranslationTest.QuestionBaseHelper
import github.cirqach.devlex.databinding.ActivityFindTranlationTestBinding

class TrueFalseTestActivity : AppCompatActivity() {

    private var mCurrentPosition: Int = 1 // Default and the first question position
    private var mQuestionsList: MutableList<QuestionBaseHelper.Question?> = mutableListOf()
    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAnswers: Int = 0


    private lateinit var binding: ActivityFindTranlationTestBinding
    private var dbh: DevLexDBHelper? = null
    private var questionHelper: QuestionBaseHelper = QuestionBaseHelper()
    private var questionsList: MutableList<QuestionBaseHelper.Question?> =
        mutableListOf() // Set to hold all questions
    private var questionsCount: Int = 0
    private val TAG: String = "true false test"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_true_false_test)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}