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
import github.cirqach.devlex.database.DevLexDBHelper
import github.cirqach.devlex.database.DevLexDatabaseContract
import github.cirqach.devlex.database.TestDataList
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var dbh: DevLexDBHelper
private lateinit var newArry: ArrayList<TestDataList>
private lateinit var recyclerView: RecyclerView
private lateinit var adapter: TestResultAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentFindWordTabLayout.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentFindWordTabLayout : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val TAG = "find word tab layout"
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
        return inflater.inflate(R.layout.fragment_find_word_tab_layout, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentFindWordTabLayout.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentFindWordTabLayout().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.fwRecyclerView)
        dbh = DevLexDBHelper(view.context)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.setHasFixedSize(true)
        displayWord()

        adapter = TestResultAdapter(newArry)
        recyclerView.adapter = adapter
        val searchView: SearchView = view.findViewById(R.id.fwSearchView)
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
                if (i.score.lowercase(Locale.ROOT).contains(query) ||
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
        val newcursor: Cursor? = dbh.readAll(DevLexDatabaseContract.Tables.FIND_WORD_TABLE)
        newArry = ArrayList<TestDataList>()
        while (newcursor!!.moveToNext()) {
            val result = newcursor.getString(1)
            val result_procent = newcursor.getInt(2)
            newArry.add(TestDataList(result, result_procent))
        }
        Log.d(TAG, "displayWord: reading data from ${DevLexDatabaseContract.Tables.FIND_WORD_TABLE} is $newArry")
        recyclerView.adapter = TestResultAdapter(newArry)
    }
}