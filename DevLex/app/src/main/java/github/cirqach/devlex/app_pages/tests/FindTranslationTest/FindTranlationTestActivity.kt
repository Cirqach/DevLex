package github.cirqach.devlex.app_pages.tests.FindTranslationTest

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import github.cirqach.devlex.database.DevLexDBHelper
import github.cirqach.devlex.database.DevLexDatabaseContract
import github.cirqach.devlex.database.FindTranslationTest.QuestionBaseHelper
import kotlin.random.Random

class FindTranlationTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFindTranlationTestBinding
    private var score = 0
    private var currentQuestionIndex = 0
    private var dbh: DevLexDBHelper? = null
    private var questionHelper: QuestionBaseHelper? = null
    private var questions: QuestionBaseHelper.QuestionsBase? = null // Set to hold all questions
    private var currentQuestion: QuestionBaseHelper.Question? = null // Current question object
    private var questionsCount: Int = 0
    private val TAG: String = "find translation test"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_find_tranlation_test)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        questionHelper = QuestionBaseHelper()
        dbh = DevLexDBHelper(this)
        if (dbh != null) {
            questionsCount = intent.getIntExtra(
                "count_of_test",
                DevLexDBHelper.getTableRowCount(
                    DevLexDatabaseContract.LexiconEntry.TABLE_NAME,
                    dbh!!
                )
            )
        }

        if (questionHelper != null) {
            questions =
                questionHelper!!.getQuestionsBase(questionsCount, dbh) // Fetch all questions
        }
        binding = ActivityFindTranlationTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayQuestion()

        binding.activityFindTranslationVariantButton1.setOnClickListener { checkAnswer(0) }
        binding.activityFindTranslationVariantButton2.setOnClickListener { checkAnswer(1) }
        binding.activityFindTranslationVariantButton3.setOnClickListener { checkAnswer(2) }
        binding.restartButton.setOnClickListener { restartQuiz() }
    }


    fun showResult() {
        Log.d(TAG, "showResult: showing result $score")
        Toast.makeText(this, "Your result: $score", Toast.LENGTH_SHORT).show()
        binding.restartButton.isEnabled = true
    }

    fun displayQuestion() {
        if (questions != null) {
            // Randomly select a question from the list (excluding answered ones)
            if (questions!!.questionBase != null) {
                val currentQuestion = questions!!.questionBase?.randomOrNull()

                if (currentQuestion != null) {
                    binding.activityFindTranslationRandomWordTextView.text =
                        currentQuestion.questions?.firstOrNull()?.english_name ?: "ERROR"

                    val shuffledAnswers = currentQuestion.questions?.toMutableList()?.apply {
                        shuffle(Random) // Shuffle answer order
                    }

                    binding.activityFindTranslationVariantButton1.text =
                        shuffledAnswers?.get(0)?.russian_name ?: "ERROR"
                    binding.activityFindTranslationVariantButton2.text =
                        shuffledAnswers?.get(1)?.russian_name ?: "ERROR"
                    binding.activityFindTranslationVariantButton3.text =
                        shuffledAnswers?.get(2)?.russian_name ?: "ERROR"

                    binding.activityFindTranslationTestQuestionCountTextView.text =
                        (currentQuestionIndex + 1).toString() // Update question count
                    currentQuestion.answered = true // Mark question as answered

                    resetButtonColors()
                } else {
                    // Handle no more questions scenario (e.g., show completion message)
                    showResult()
                }
            }
        }
    }

    fun checkAnswer(selectedAnswer: Int) {
        if (currentQuestion != null) {
            // Always check against the first element (index 0) of the shuffled questions
            val correctAnswer = currentQuestion!!.questions?.firstOrNull()

            if (selectedAnswer == 0 && correctAnswer != null) {
                score++
                correctButtonColors(selectedAnswer)
            } else {
                wrongButtonColors(selectedAnswer)
                correctButtonColors(0) // Highlight the actual correct answer (index 0)
            }

            if (currentQuestionIndex < questionsCount - 1) {
                currentQuestionIndex++
                binding.activityFindTranslationRandomWordTextView.postDelayed(
                    { displayQuestion() },
                    1000
                )
            } else {
                showResult()
            }
        }
    }

    fun restartQuiz() {
        currentQuestionIndex = 0
        score = 0
        questions =
            questionHelper?.getQuestionsBase(questionsCount, dbh) // Fetch all questions again
        displayQuestion()
        binding.restartButton.isEnabled = false
    }


    fun correctButtonColors(buttonIndex: Int) {
        resetButtonColors() // Ensure all buttons are initially reset
        when (buttonIndex) {
            0 -> binding.activityFindTranslationVariantButton1.setBackgroundColor(Color.GREEN)
            1 -> binding.activityFindTranslationVariantButton2.setBackgroundColor(Color.GREEN)
            2 -> binding.activityFindTranslationVariantButton3.setBackgroundColor(Color.GREEN)
        }
    }

    fun wrongButtonColors(buttonIndex: Int) {
        resetButtonColors() // Ensure all buttons are initially reset
        when (buttonIndex) {
            0 -> binding.activityFindTranslationVariantButton1.setBackgroundColor(Color.RED)
            1 -> binding.activityFindTranslationVariantButton2.setBackgroundColor(Color.RED)
            2 -> binding.activityFindTranslationVariantButton3.setBackgroundColor(Color.RED)
        }
    }

    fun resetButtonColors() {
        binding.activityFindTranslationVariantButton1.setBackgroundColor(Color.parseColor("#7287fd"))
        binding.activityFindTranslationVariantButton2.setBackgroundColor(Color.parseColor("#7287fd"))
        binding.activityFindTranslationVariantButton3.setBackgroundColor(Color.parseColor("#7287fd"))
    }

}

