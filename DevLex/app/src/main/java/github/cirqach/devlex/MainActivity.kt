package github.cirqach.devlex

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import github.cirqach.devlex.app_pages.RealMainActivity
import github.cirqach.devlex.database.DevLexDBHelper


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonStart: Button? = findViewById(R.id.start_button)
        buttonStart!!.setOnClickListener {

            val dbh = DevLexDBHelper(this)
            dbh.copyDatabase(this)

            val toRealActivityIntent = Intent(this, RealMainActivity::class.java)
            startActivity(toRealActivityIntent)
        }
        val buttonGithub: Button? = findViewById(R.id.github_button)
        buttonGithub!!.setOnClickListener {
            val url = "https://github.com/Cirqach/DevLex.git"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }


    }

}