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
import github.cirqach.devlex.app_pages.tests.TrueFalseTestActivity


class TrueFalseTestFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_true_false_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val button = view.findViewById<Button>(R.id.tft_fragment_start_button)
        button.setOnClickListener {

            val tag = "True false test fragment"
            Log.d(tag, "onViewCreated: handle button click")
            val questionsCountEditText =
                view.findViewById<EditText>(R.id.true_false_test_fragment_edit_text)
            if (questionsCountEditText.text.toString().toInt() <= 0) {
                Toast.makeText(view.context, getString(R.string.cant_use_zero), Toast.LENGTH_SHORT)
                    .show()
            } else {
                val questionsCount = questionsCountEditText.text.toString().toIntOrNull()
                Log.d(tag, "onViewCreated: question count = $questionsCount")
                val intent = Intent(
                    view.context,
                    TrueFalseTestActivity::class.java
                )
                Log.d(tag, "onViewCreated: put extra $questionsCount")
                intent.putExtra("count_of_test", questionsCount)
                startActivity(intent)
            }
        }
    }

}