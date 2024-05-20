package github.cirqach.devlex.app_pages.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
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
import androidx.fragment.app.DialogFragment
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


class ChangeDatabaseFragment : Fragment() {

    lateinit var dbh: DevLexDBHelper
    private lateinit var newArray: ArrayList<DataList>
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: DevLexAdapter
    private lateinit var russianEditText: EditText
    private lateinit var englishEditText: EditText
    private lateinit var definitionEditText: EditText
    private lateinit var deleteButton: Button
    private lateinit var saveButton: Button
    private lateinit var addButton: Button
    private lateinit var idTextView: TextView

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

        if (view.context != null) {
            dbh = DevLexDBHelper(view.context)
        }

        val refreshDatabaseButton: FloatingActionButton =
            view.findViewById(R.id.crud_refresh_database_button)
        refreshDatabaseButton.setOnClickListener {
            displayWord()
        }

        recyclerView = view.findViewById(R.id.crud_recycler_view)

        russianEditText = view.findViewById(R.id.crud_russian_name_edit_text_view)
        englishEditText = view.findViewById(R.id.crud_english_name_edit_text_view)
        definitionEditText = view.findViewById(R.id.crud_definition_edit_text_view)
        idTextView = view.findViewById(R.id.crud_id_text_view)


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

        deleteButton = view.findViewById(R.id.crud_delete_button)
        saveButton = view.findViewById(R.id.crud_save_button)
        addButton = view.findViewById(R.id.crud_add_button)
        deleteButton.setOnClickListener {
            deleteValues()
        }
        addButton.setOnClickListener {
            val intent = Intent(requireContext(), AddWordActivity::class.java)
            startActivity(intent)
        }
        saveButton.setOnClickListener {
            Toast.makeText(view.context, "Saving data", Toast.LENGTH_SHORT).show()
            Log.d(
                "save_button_click", "Data to save: " +
                        "english = ${englishEditText.text} , " +
                        "russian = ${russianEditText.text}, " +
                        "definition = ${definitionEditText.text} ," +
                        "ID = ${idTextView.text}"
            )
            dbh.saveDataToLexicon(
                englishEditText.text.toString(),
                russianEditText.text.toString(),
                definitionEditText.text.toString(),
                idTextView.text.toString()
            )

            displayWord()
        }
    }

    fun deleteValues() {
        dbh.deleteData(
            DevLexDatabaseContract.LexiconEntry.TABLE_NAME,
            idTextView.text.toString()
        )
        displayWord()
    }


    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<DataList>()
            for (i in newArray) {
                if (i.englishName.lowercase(Locale.ROOT).contains(query) ||
                    i.russianName.lowercase(Locale.ROOT).contains(query) ||
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
        val newCursor: Cursor? = dbh.readAll(DevLexDatabaseContract.LexiconEntry.TABLE_NAME)
        newArray = ArrayList()
        while (newCursor!!.moveToNext()) {
            val uEnglishName = newCursor.getString(1)
            val uRussianName = newCursor.getString(2)
            val uDefinition = newCursor.getString(3)
            val uid = newCursor.getString(0)
            newArray.add(DataList(uEnglishName, uRussianName, uDefinition, uid))
        }
        recyclerView.adapter = DevLexAdapter(newArray)
        adapter = DevLexAdapter(newArray)
        recyclerView.adapter = adapter


        adapter.onItemClick = { position ->

            // Get the DataList object at the clicked position

            // Set the values of the EditText fields
            russianEditText.setText(position.russianName)
            englishEditText.setText(position.englishName)
            definitionEditText.setText(position.definition)
            idTextView.text = position.id
        }
    }



}

