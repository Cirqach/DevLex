package github.cirqach.devlex.app_pages.fragments.tests

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import github.cirqach.devlex.R
import github.cirqach.devlex.app_pages.tests.FindTranslationTestActivity


class FindTranslationTestFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_translation_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val questionsCountEditText: EditText =
            requireView().findViewById(R.id.fragment_find_translation_editTextNumber)
        val startButton: Button = requireView().findViewById(R.id.fragment_find_translation_button)


        val tag = "FindTranslationTestFragment"
        Log.d(tag, "onViewCreated: on view created")
        super.onViewCreated(view, savedInstanceState)
        Log.d(tag, "onViewCreated: try to hand button click")

        startButton.setOnClickListener {
            Log.d(tag, "onViewCreated: handle button click")
            if (questionsCountEditText.text.toString().toInt() <= 0) {
                Toast.makeText(view.context, getString(R.string.cant_use_zero), Toast.LENGTH_SHORT)
                    .show()
            } else {
                val questionsCount = questionsCountEditText.text.toString().toIntOrNull()
                Log.d(tag, "onViewCreated: question count = $questionsCount")
                val intent = Intent(
                    view.context,
                    FindTranslationTestActivity::class.java
                )
                Log.d(tag, "onViewCreated: put extra $questionsCount")
                intent.putExtra("count_of_test", questionsCount)
                startActivity(intent)
            }
        }
    }

}