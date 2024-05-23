package github.cirqach.devlex.app_pages.fragments.StatisticPage.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import github.cirqach.devlex.R
import github.cirqach.devlex.database.HangmanGameDataList

class HangmanGameAdapter(private var resultList: MutableList<HangmanGameDataList>) :
    RecyclerView.Adapter<HangmanGameAdapter.HangmanGameViewHolder>() {

    private var onItemClick: ((HangmanGameDataList) -> Unit)? = null

    class HangmanGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val score: TextView = itemView.findViewById(R.id.score)
        val procentScore: TextView = itemView.findViewById(R.id.procent_score)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HangmanGameViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.test_recyclerview_item, parent, false)
        return HangmanGameViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: HangmanGameViewHolder, position: Int) {
        val currentItem = resultList[position]
        holder.score.text = currentItem.result
        holder.procentScore.text = currentItem.word

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    fun setFilteredList(resultList: ArrayList<HangmanGameDataList>) {
        this.resultList = resultList
        notifyDataSetChanged() // Notify that the data has changed
    }

}