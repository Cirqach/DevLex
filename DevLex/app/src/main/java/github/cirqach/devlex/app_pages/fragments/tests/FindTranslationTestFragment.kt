package github.cirqach.devlex.app_pages.fragments.tests

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import github.cirqach.devlex.R
import github.cirqach.devlex.app_pages.tests.FindTranlationTestActivity


class FindTranslationTestFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_translation_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val start_button: Button = view.findViewById(R.id.start_button)

        val questionsCountEditText: EditText = view.findViewById(R.id.fragment_find_translation_editTextNumber)
        val questionsCount = questionsCountEditText.text.toString().toIntOrNull()
        if (questionsCount != null && questionsCount > 0) {
            start_button.setOnClickListener {
                val intent = Intent(requireContext(), github.cirqach.devlex.app_pages.add_word_activity::class.java)



            intent.putExtra("questionsCount", questionsCount)
                startActivity(intent)
        }
        }
    }

}