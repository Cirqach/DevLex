import android.content.Intent
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import github.cirqach.devlex.R
import github.cirqach.devlex.add_word_activity
import github.cirqach.devlex.data.DataList
import github.cirqach.devlex.data.DevLexAdapter
import github.cirqach.devlex.data.DevLexDBHelper
import java.util.Locale

class LexiconFragment : Fragment() {

    lateinit var dbh: DevLexDBHelper
    private lateinit var newArry: ArrayList<DataList>
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: DevLexAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lexicon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val floatingActionButton: FloatingActionButton =
            view.findViewById(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener {
            // Обработка нажатия на кнопку добавления слова
            val intent = Intent(requireContext(), add_word_activity::class.java)
            startActivity(intent)
        }


        recyclerView = view.findViewById(R.id.lexiconRecyclerView)
        dbh = DevLexDBHelper(view.context)
        dbh.copyDatabase(view.context)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.setHasFixedSize(true)
        displayWord()

        adapter = DevLexAdapter(newArry)
        recyclerView.adapter = adapter

        searchView = view.findViewById(R.id.searchView)
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
            val filteredList = ArrayList<DataList>()
            for (i in newArry) {
                if (i.english_name.lowercase(Locale.ROOT).contains(query) ||
                    i.russian_name.lowercase(Locale.ROOT).contains(query) ||
                    i.definition.lowercase(Locale.ROOT).contains(query)
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
        var newcursor: Cursor? = dbh.readAllDataFromLexicon()
        newArry = ArrayList<DataList>()
        while (newcursor!!.moveToNext()) {
            val uenglish_name = newcursor.getString(1)
            val urussian_name = newcursor.getString(2)
            val udefinition = newcursor.getString(3)
            newArry.add(DataList(uenglish_name, urussian_name, udefinition))
        }
        recyclerView.adapter = DevLexAdapter(newArry)
    }
}