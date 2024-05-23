package github.cirqach.devlex.app_pages.fragments.StatisticPage.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import github.cirqach.devlex.R
import github.cirqach.devlex.database.TestDataList

class TestResultAdapter(private var resultList: MutableList<TestDataList>) :
    RecyclerView.Adapter<TestResultAdapter.TestResultsViewHolder>() {

    private var onItemClick: ((TestDataList) -> Unit)? = null

    class TestResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val score: TextView = itemView.findViewById(R.id.score)
        val procentScore: TextView = itemView.findViewById(R.id.procent_score)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestResultsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.test_recyclerview_item, parent, false)
        return TestResultsViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TestResultsViewHolder, position: Int) {
        val currentItem = resultList[position]
        holder.score.text = currentItem.score
        holder.procentScore.text = currentItem.resultPercent.toString() + "%"

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    fun setFilteredList(resultList: ArrayList<TestDataList>) {
        this.resultList = resultList
        notifyDataSetChanged() // Notify that the data has changed
    }

}
