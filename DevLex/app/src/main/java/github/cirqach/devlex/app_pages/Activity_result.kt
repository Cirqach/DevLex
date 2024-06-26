package github.cirqach.devlex.app_pages

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import github.cirqach.devlex.R
import github.cirqach.devlex.databinding.ActivityResultBinding

class ActivityResult : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_result)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 0)
        val correctAnswers = intent.getIntExtra("CORRECT_ANSWERS", 0)

        val binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvScore.text =
            getString(
                R.string.your_score_is_out_of,
                correctAnswers.toString(),
                totalQuestions.toString()
            )

        binding.btnFinish.setOnClickListener {
            startActivity(Intent(this@ActivityResult, RealMainActivity::class.java))
        }


    }
}