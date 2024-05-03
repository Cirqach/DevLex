package github.cirqach.devlex.app_pages.fragments.tests

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import github.cirqach.devlex.R
import github.cirqach.devlex.app_pages.tests.FindTranslationTest.FindTranlationTestActivity
import github.cirqach.devlex.database.DevLexDBHelper


class FindTranslationTestFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_translation_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val dbh: DevLexDBHelper = DevLexDBHelper(view.context)
        val questionsCountEditText: EditText =
            requireView().findViewById(R.id.fragment_find_translation_editTextNumber)
        val start_button: Button = requireView().findViewById(R.id.fragment_find_translation_button)


        val TAG = "FindTranslationTestFragment"
        Log.d(TAG, "onViewCreated: on view created")
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: try to hand button click")
        start_button.setOnClickListener {
            Log.d(TAG, "onViewCreated: handle button click")
            val questionsCount = questionsCountEditText.text.toString().toIntOrNull()
            Log.d(TAG, "onViewCreated: question count = $questionsCount")
            val intent = Intent(
                view.context,
                FindTranlationTestActivity::class.java
            )
            Log.d(TAG, "onViewCreated: put extra $questionsCount")
            intent.putExtra("count_of_test", questionsCount)
            startActivity(intent)

        }
    }

}