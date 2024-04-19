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
        val tenglish_name: TextView = itemView.findViewById(R.id.english_name_textview)
        val trussian_name: TextView = itemView.findViewById(R.id.russian_name_text_view)
        val tdefinition: TextView = itemView.findViewById(R.id.word_defenition_textview)
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
        val currentItem = wordList[position]
        holder.tenglish_name.text = currentItem.english_name.toString()
        holder.trussian_name.text = currentItem.russian_name.toString()
        holder.tdefinition.text = currentItem.definition.toString()
        holder.tid.text = currentItem.id.toString()

        holder.itemView.setOnClickListener() {
            onItemClick?.invoke(currentItem)
        }
    }

    fun setFilteredList(wordList: ArrayList<DataList>) {
        this.wordList = wordList
        notifyDataSetChanged()
    }

}