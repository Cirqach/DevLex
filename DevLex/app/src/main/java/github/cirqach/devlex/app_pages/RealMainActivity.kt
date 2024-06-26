package github.cirqach.devlex.app_pages

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import github.cirqach.devlex.R
import github.cirqach.devlex.app_pages.fragments.ChangeDatabaseFragment
import github.cirqach.devlex.app_pages.fragments.StatisticPage.StatisticFragment
import github.cirqach.devlex.app_pages.fragments.tests.HangmanGameFragment

class RealMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_real_main)
        drawerLayout = findViewById(R.id.drawer_layout)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val action = supportActionBar
        action!!.title = ""

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    github.cirqach.devlex.app_pages.fragments.LexiconFragment()
                ).commit()
            navigationView.setCheckedItem(R.id.nav_words)
        }


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_words -> supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    github.cirqach.devlex.app_pages.fragments.LexiconFragment()
                ).commit()

            R.id.nav_statistic -> supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    StatisticFragment()
                ).commit()

            R.id.nav_true_false_test -> supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    github.cirqach.devlex.app_pages.fragments.tests.TrueFalseTestFragment()
                ).commit()

            R.id.nav_find_word_test -> supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    github.cirqach.devlex.app_pages.fragments.tests.FindWordTestFragment()
                ).commit()

            R.id.nav_find_translation_test -> supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    github.cirqach.devlex.app_pages.fragments.tests.FindTranslationTestFragment()
                ).commit()

            R.id.nav_edit_database -> supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    ChangeDatabaseFragment()
                ).commit()

            R.id.hangman_test -> supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    HangmanGameFragment()
                ).commit()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

}

