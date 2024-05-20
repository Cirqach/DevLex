package github.cirqach.devlex.database

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import github.cirqach.devlex.R

class DevLexAdapter(private var wordList: ArrayList<DataList>) :
    RecyclerView.Adapter<DevLexAdapter.DevLexViewHolder>() {

    var onItemClick: ((DataList) -> Unit)? = null

    class DevLexViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tEnglishName: TextView = itemView.findViewById(R.id.english_name_textview)
        val tRussianName: TextView = itemView.findViewById(R.id.russian_name_text_view)
        val tDefinition: TextView = itemView.findViewById(R.id.word_definition_textview)
        val tid: TextView = itemView.findViewById(R.id.id_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevLexViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.lexicon_recyclerview_item, parent, false)
        return DevLexViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return wordList.size
    }

    override fun onBindViewHolder(holder: DevLexViewHolder, position: Int) {
        if (position < wordList.size) {
            val currentItem = wordList[position]
            holder.tEnglishName.text = currentItem.englishName
            holder.tRussianName.text = currentItem.russianName
            holder.tDefinition.text = currentItem.definition
            holder.tid.text = currentItem.id
        }

        holder.itemView.setOnClickListener {
            if (position < wordList.size) {
                onItemClick?.invoke(wordList[position])
            }
        }
    }

    fun setFilteredList(wordList: ArrayList<DataList>) {
        this.wordList = wordList
        notifyDataSetChanged() // Notify that the data has changed
    }


}