package github.cirqach.devlex.app_pages.tests

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import github.cirqach.devlex.R
import github.cirqach.devlex.app_pages.ActivityResult
import github.cirqach.devlex.database.DevLexDBHelper
import github.cirqach.devlex.database.DevLexDatabaseContract
import github.cirqach.devlex.database.FindTranslationTest.QuestionBaseHelper
import github.cirqach.devlex.databinding.ActivityFindTranlationTestBinding

class FindTranslationTestActivity : AppCompatActivity(), View.OnClickListener {


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
    private val tag: String = "find translation test"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_find_tranlation_test)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tag = "find translation test"
        Log.d(tag, "onCreate: starting find translation test ")

        dbh = DevLexDBHelper(this)
        questionsCount = intent.getIntExtra(
            "count_of_test",
            DevLexDBHelper.getTableRowCount(
                DevLexDatabaseContract.LexiconEntry.TABLE_NAME,
                dbh!!
            )
        )

        mQuestionsList = questionHelper.getQuestionsList(dbh, questionsCount) // Fetch all questions
        Log.d(this.tag, "onCreate: question base is $questionsList")
        binding = ActivityFindTranlationTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(this.tag, "onCreate: ${questionHelper.getQuestionsList(dbh, questionsCount)}")

        setQuestion()
        binding.progressBar.max = questionsCount
        binding.tvOptionOne.setOnClickListener(this)
        binding.tvOptionTwo.setOnClickListener(this)
        binding.tvOptionThree.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
    }

    private fun setQuestion() {
        val question = mQuestionsList[mCurrentPosition - 1]

        defaultOptionsView()

        if (mCurrentPosition == mQuestionsList.size) {
            binding.btnSubmit.text = getString(R.string.finish)
        } else {
            binding.btnSubmit.text = getString(R.string.submit)
        }

        // Update the progress bar's current value
        binding.progressBar.progress = mCurrentPosition
        // Update the tvProgress text
        binding.tvProgress.text = getString(
            R.string.putting_result_to_database,
            mCurrentPosition.toString(),
            mQuestionsList.size.toString()
        )


        if (question != null) {
            binding.tvQuestion.text = question.englishName
            binding.tvOptionOne.text = question.russianOption1
            binding.tvOptionTwo.text = question.russianOption2
            binding.tvOptionThree.text = question.russianOption3
        }

        // Enable answer buttons
        binding.tvOptionOne.isEnabled = true
        binding.tvOptionTwo.isEnabled = true
        binding.tvOptionThree.isEnabled = true
    }

    private fun defaultOptionsView() {

        val options = ArrayList<TextView>()
        options.add(0, binding.tvOptionOne)
        options.add(1, binding.tvOptionTwo)
        options.add(2, binding.tvOptionThree)

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this@FindTranslationTestActivity,
                R.drawable.default_option_border_bg
            )
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.tv_option_one -> {

                selectedOptionView(binding.tvOptionOne, 1)
            }

            R.id.tv_option_two -> {

                selectedOptionView(binding.tvOptionTwo, 2)
            }

            R.id.tv_option_three -> {

                selectedOptionView(binding.tvOptionThree, 3)
            }

            R.id.btn_submit -> {

                if (mSelectedOptionPosition == 0) {

                    mCurrentPosition++

                    when {

                        mCurrentPosition <= mQuestionsList.size -> {

                            setQuestion()
                        }

                        else -> {

                            val intent =
                                Intent(
                                    this@FindTranslationTestActivity,
                                    ActivityResult::class.java
                                )
                            intent.putExtra("CORRECT_ANSWERS", mCorrectAnswers)
                            intent.putExtra("TOTAL_QUESTIONS", mQuestionsList.size)
                            if (dbh != null) {
                                if (dbh?.saveDataToTest(
                                        DevLexDatabaseContract.Tables.FIND_TRANSLATION_TABLE,
                                        mCorrectAnswers,
                                        (mCorrectAnswers.toDouble() / mQuestionsList.size * 100).toInt(),
                                        questionsCount,
                                    ) == true
                                ) {
                                    Log.d(
                                        tag,
                                        "onClick: data saved $mCorrectAnswers + ${
                                            if (mQuestionsList.size != 0 && mCorrectAnswers != 0) {
                                                (mQuestionsList.size / mCorrectAnswers * 100)
                                            } else {
                                                "0/${mQuestionsList.size}"
                                            }
                                        }"
                                    )
                                } else {
                                    Log.d(tag, "onClick: data not saved")
                                }
                            } else {
                                Log.d(tag, "onClick: dbh is null")
                            }


                            startActivity(intent)
                            finish()
                            // END
                        }
                    }
                } else {
                    val question = mQuestionsList[mCurrentPosition - 1]

                    if (question!!.answer != mSelectedOptionPosition) {
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    } else {
                        mCorrectAnswers++
                    }

                    answerView(question.answer, R.drawable.correct_option_border_bg)

                    if (mCurrentPosition == mQuestionsList.size) {
                        binding.btnSubmit.text = getString(R.string.finish)
                    } else {
                        binding.btnSubmit.text = getString(R.string.go_to_next_question)
                    }

                    mSelectedOptionPosition = 0

                    // Disable answer buttons
                    binding.tvOptionOne.isEnabled = false
                    binding.tvOptionTwo.isEnabled = false
                    binding.tvOptionThree.isEnabled = false
                }
            }
        }
    }

    private fun answerView(answer: Int, drawableView: Int) {

        when (answer) {

            1 -> {
                binding.tvOptionOne.background = ContextCompat.getDrawable(
                    this@FindTranslationTestActivity,
                    drawableView
                )
            }

            2 -> {
                binding.tvOptionTwo.background = ContextCompat.getDrawable(
                    this@FindTranslationTestActivity,
                    drawableView
                )
            }

            3 -> {
                binding.tvOptionThree.background = ContextCompat.getDrawable(
                    this@FindTranslationTestActivity,
                    drawableView
                )
            }
        }
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {

        defaultOptionsView()

        mSelectedOptionPosition = selectedOptionNum

        tv.setTextColor(
            Color.parseColor("#363A43")
        )
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this@FindTranslationTestActivity,
            R.drawable.selected_option_border_bg
        )
    }

}
