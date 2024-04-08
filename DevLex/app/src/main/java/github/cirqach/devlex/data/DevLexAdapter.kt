package github.cirqach.devlex.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import github.cirqach.devlex.R

class DevLexAdapter(private var wordList: ArrayList<DataList>) :
    RecyclerView.Adapter<DevLexAdapter.DevLexViewHolder>() {
    class DevLexViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tenglish_name: TextView = itemView.findViewById(R.id.english_name_textview)
        val trussian_name: TextView = itemView.findViewById(R.id.russian_name_text_view)
        val tdefinition: TextView = itemView.findViewById(R.id.word_defenition_textview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevLexViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return DevLexViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return wordList.size
    }

    override fun onBindViewHolder(holder: DevLexViewHolder, position: Int) {
        val currentItem = wordList[position]
        holder.tenglish_name.text = currentItem.english_name.toString()
        holder.trussian_name.text = currentItem.russian_name.toString()
        holder.tdefinition.text = currentItem.definition.toString()
    }

    fun setFilteredList(wordList: ArrayList<DataList>) {
        this.wordList = wordList
        notifyDataSetChanged()
    }

}