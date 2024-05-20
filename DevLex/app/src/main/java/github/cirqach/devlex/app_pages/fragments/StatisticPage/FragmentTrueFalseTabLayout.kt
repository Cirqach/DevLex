package github.cirqach.devlex.app_pages.fragments.StatisticPage

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import github.cirqach.devlex.R
import github.cirqach.devlex.database.DevLexDBHelper
import github.cirqach.devlex.database.DevLexDatabaseContract
import github.cirqach.devlex.database.TestDataList
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var dbh: DevLexDBHelper
private lateinit var newArry: ArrayList<TestDataList>
private lateinit var recyclerView: RecyclerView
private lateinit var adapter: TestResultAdapter

class FragmentTrueFalseTabLayout : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_true_false_tab_layout, container, false)
    }

    companion object {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.tfRecyclerView)
        dbh = DevLexDBHelper(view.context)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.setHasFixedSize(true)
        displayWord()

        adapter = TestResultAdapter(newArry)
        recyclerView.adapter = adapter

        val searchView: SearchView = view.findViewById(R.id.tfSearchView)
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
        if (query != null) {
            val filteredList = ArrayList<TestDataList>()
            for (i in newArry) {
                if (i.score.toString().lowercase(Locale.ROOT).contains(query) ||
                    i.resultPercent.toString().lowercase(Locale.ROOT).contains(query)
                ) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(requireView().context, "No data found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilteredList(filteredList)

            }
        }
    }

    private fun displayWord() {
        val newCursor: Cursor? = dbh.readAll(DevLexDatabaseContract.Tables.TRUE_FALSE_TABLE)
        newArry = ArrayList()
        while (newCursor!!.moveToNext()) {
            val result = newCursor.getInt(1)
            val resultProcent = newCursor.getInt(2)
            val questionCount = newCursor.getInt(3)
            newArry.add(TestDataList("$result/$questionCount", resultProcent))
        }
        recyclerView.adapter = TestResultAdapter(newArry)
    }
}