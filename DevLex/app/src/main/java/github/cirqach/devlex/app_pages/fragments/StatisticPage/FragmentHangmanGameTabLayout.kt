package github.cirqach.devlex.app_pages.fragments.StatisticPage

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import github.cirqach.devlex.R
import github.cirqach.devlex.app_pages.fragments.StatisticPage.adapters.HangmanGameAdapter
import github.cirqach.devlex.database.DevLexDBHelper
import github.cirqach.devlex.database.DevLexDatabaseContract
import github.cirqach.devlex.database.HangmanGameDataList
import java.util.Locale

class FragmentHangmanGameTabLayout : Fragment() {

    private lateinit var dbh: DevLexDBHelper
    private lateinit var newArry: ArrayList<HangmanGameDataList>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HangmanGameAdapter
    private val TAG = "FragmentHangmanGameTabLayout"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hangman_game_tab_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: view created")
        recyclerView = view.findViewById(R.id.ftRecyclerView9999)
        dbh = DevLexDBHelper(view.context)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.setHasFixedSize(true)
        displayWord()

        adapter = HangmanGameAdapter(newArry)
        recyclerView.adapter = adapter
        val searchView: SearchView = view.findViewById(R.id.ftSearchView9999)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })


    }

    private fun filterList(query: String?) {
        Log.d(tag, "filterList: filtering list")
        if (query != null) {
            val filteredList = ArrayList<HangmanGameDataList>()
            for (i in newArry) {
                if (i.result.lowercase(Locale.ROOT).contains(query) ||
                    i.word.lowercase(Locale.ROOT).contains(query)
                ) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(requireView().context, getString(R.string.no_data_found), Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilteredList(filteredList)

            }
        }
    }

    private fun displayWord() {
        Log.d(tag, "displayWord: displaying items")
        val newCursor: Cursor? = dbh.readAll(DevLexDatabaseContract.HangmanGame.TABLE_NAME)
        newArry = ArrayList()
        while (newCursor!!.moveToNext()) {
            val result = newCursor.getString(0)
            val word = newCursor.getString(2)
            newArry.add(HangmanGameDataList(result, "word you guessed $word"))
        }
        Log.d(
            tag,
            "displayWord: reading data from ${DevLexDatabaseContract.HangmanGame.TABLE_NAME} is $newArry"
        )
        recyclerView.adapter = HangmanGameAdapter(newArry)
    }
}