package github.cirqach.devlex.app_pages

import android.content.Intent
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import github.cirqach.devlex.R
import github.cirqach.devlex.database.DataList
import github.cirqach.devlex.database.DevLexAdapter
import github.cirqach.devlex.database.DevLexDBHelper
import github.cirqach.devlex.database.DevLexDatabaseContract
import java.util.Locale


class ChangeDatabaseFragment : Fragment() {

    private lateinit var dbh: DevLexDBHelper
    private lateinit var newArry: ArrayList<DataList>
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: DevLexAdapter
    private lateinit var russian_edit_text: EditText
    private lateinit var english_edit_text: EditText
    private lateinit var definition_edit_text: EditText
    private lateinit var delete_button: Button
    private lateinit var save_button: Button
    private lateinit var add_button: Button
    private lateinit var id_text_view: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_database, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val refresh_database_button: FloatingActionButton =
            view.findViewById(R.id.crud_refresh_database_button)
        refresh_database_button.setOnClickListener {
            displayWord()
        }

        recyclerView = view.findViewById(R.id.crud_recycler_view)

        russian_edit_text = view.findViewById(R.id.crud_russian_name_edit_text_view)
        english_edit_text = view.findViewById(R.id.crud_english_name_edit_text_view)
        definition_edit_text = view.findViewById(R.id.crud_defenition_edit_text_view)
        id_text_view = view.findViewById(R.id.crud_id_text_view)

        if (view.context != null) {
            dbh = DevLexDBHelper(view.context)
        }
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.setHasFixedSize(true)
        displayWord()



        searchView = view.findViewById(R.id.crud_search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        delete_button = view.findViewById(R.id.crud_delete_button)
        save_button = view.findViewById(R.id.crud_save_button)
        add_button = view.findViewById(R.id.crud_add_button)
        delete_button.setOnClickListener {
            dbh.deleteData(
                DevLexDatabaseContract.LexiconEntry.TABLE_NAME,
                id_text_view.text.toString()
            )
            displayWord()
        }
        add_button.setOnClickListener {
            val intent = Intent(requireContext(), add_word_activity::class.java)
            startActivity(intent)
        }
        save_button.setOnClickListener {
            Toast.makeText(view.context, "Saving data", Toast.LENGTH_SHORT).show()
            Log.d(
                "save_button_click", "Data to save: " +
                        "english = ${english_edit_text.text.toString()} , " +
                        "russian = ${russian_edit_text.text.toString()}, " +
                        "defeniton = ${definition_edit_text.text.toString()} ," +
                        "ID = ${id_text_view.text.toString()}"
            )
            dbh.saveDataToLexicon(
                english_edit_text.text.toString(),
                russian_edit_text.text.toString(),
                definition_edit_text.text.toString(),
                id_text_view.text.toString()
            )

            displayWord()
        }
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
        var newcursor: Cursor? = dbh.readAll(DevLexDatabaseContract.LexiconEntry.TABLE_NAME)
        newArry = ArrayList<DataList>()
        while (newcursor!!.moveToNext()) {
            val uenglish_name = newcursor.getString(1)
            val urussian_name = newcursor.getString(2)
            val udefinition = newcursor.getString(3)
            val uid = newcursor.getString(0)
            newArry.add(DataList(uenglish_name, urussian_name, udefinition, uid))
        }
        recyclerView.adapter = DevLexAdapter(newArry)
        adapter = DevLexAdapter(newArry)
        recyclerView.adapter = adapter


        adapter.onItemClick = { position ->

            // Get the DataList object at the clicked position

            // Set the values of the EditText fields
            russian_edit_text.setText(position.russian_name.toString())
            english_edit_text.setText(position.english_name.toString())
            definition_edit_text.setText(position.definition.toString())
            id_text_view.text = position.id.toString()
        }
    }

}