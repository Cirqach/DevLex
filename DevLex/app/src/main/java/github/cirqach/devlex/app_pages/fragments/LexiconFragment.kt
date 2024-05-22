package github.cirqach.devlex.app_pages.fragments

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import github.cirqach.devlex.R
import github.cirqach.devlex.app_pages.AddWordActivity
import github.cirqach.devlex.database.DataList
import github.cirqach.devlex.database.DevLexAdapter
import github.cirqach.devlex.database.DevLexDBHelper
import github.cirqach.devlex.database.DevLexDatabaseContract
import java.util.Locale

class LexiconFragment : Fragment() {


    private lateinit var dbh: DevLexDBHelper
    private lateinit var newArry: ArrayList<DataList>
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: DevLexAdapter

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_WORD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Word added successfully, update the list
            fillArray()
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lexicon, container, false)
    }

    companion object {
        const val ADD_WORD_REQUEST_CODE = 1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val floatingActionButton: FloatingActionButton =
            view.findViewById(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener {
            val intent = Intent(
                requireContext(),
                AddWordActivity::class.java
            )
            startActivityForResult(intent, ADD_WORD_REQUEST_CODE)
        }


        recyclerView = view.findViewById(R.id.lexiconRecyclerView)
        dbh = DevLexDBHelper(view.context)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.setHasFixedSize(true)

        fillArray()
        adapter = DevLexAdapter(newArry)
        recyclerView.adapter = adapter
        sortDisplayWordAscendingEnglish()
        displayList()

        searchView = view.findViewById(R.id.searchView)!!
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })
        val sortButton = view.findViewById<Button>(R.id.sortButtonEnglish)
        var isReverseSort = false
        sortButton.setOnClickListener {
            if (isReverseSort) {
                sortDisplayWordAscendingEnglish()
                displayList()
            } else {
                sortDisplayWordDescendingEnglish()
                displayList()
            }
            isReverseSort = !isReverseSort
        }
        var isReverseSortRussian = false
        val sortButtonRussian = view.findViewById<Button>(R.id.sortButtonRussian)
        sortButtonRussian.setOnClickListener {
            if (isReverseSortRussian) {
                sortDisplayWordAscendingRussian()
                displayList()
            } else {
                sortDisplayWordDescendingRussian()
                displayList()
            }
            isReverseSortRussian = !isReverseSortRussian
        }


    }

    private fun fillArray() {
        val newCursor: Cursor? = dbh.readAll(DevLexDatabaseContract.LexiconEntry.TABLE_NAME)
        newArry = ArrayList()
        while (newCursor!!.moveToNext()) {
            val uEnglishName = newCursor.getString(1)
            val uRussianName = newCursor.getString(2)
            val uDefinition = newCursor.getString(3)
            val uid = newCursor.getString(0)
            newArry.add(
                DataList(
                    uEnglishName.lowercase(Locale.ROOT),
                    uRussianName.lowercase(Locale.ROOT),
                    uDefinition,
                    uid
                )
            )
        }
        newCursor.close()

    }

    private fun filterList(query: String?) {
        if (query.isNullOrEmpty()) {
            adapter.setFilteredList(newArry)
            return
        }

        val filteredList = ArrayList<DataList>()
        for (i in newArry) {
            if (i.englishName.lowercase(Locale.ROOT).contains(query) ||
                i.russianName.lowercase(Locale.ROOT).contains(query) ||
                i.definition.lowercase(Locale.ROOT).contains(query)
            ) {
                filteredList.add(i)
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(requireView().context, "No data found", Toast.LENGTH_SHORT).show()
        }

        adapter.setFilteredList(filteredList)
    }

    private fun displayList() {
        adapter.setFilteredList(newArry)
    }

    private fun sortDisplayWordAscendingEnglish() {
        newArry.sortWith(compareBy { it.englishName.lowercase() })
    }

    private fun sortDisplayWordDescendingEnglish() {
        newArry.sortWith(compareByDescending { it.englishName.lowercase() })
    }

    private fun sortDisplayWordAscendingRussian() {
        newArry.sortWith(compareBy { it.russianName.lowercase() })
    }

    private fun sortDisplayWordDescendingRussian() {
        newArry.sortWith(compareByDescending { it.russianName.lowercase() })
    }
}


