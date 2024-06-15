package com.jdccmobile.costofliving.ui.home.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.databinding.ViewCostItemBinding
import com.jdccmobile.costofliving.model.ItemCostInfo

class CostInfoAdapter(
    private val name: String,
    private val costInfo: List<ItemCostInfo>,
) : RecyclerView.Adapter<CostInfoAdapter.CostInfoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CostInfoViewHolder {
        val view = LayoutInflater.from(
            parent.context,
        ).inflate(R.layout.view_cost_item, parent, false)
        return CostInfoViewHolder(view)
    }

    override fun getItemCount() = costInfo.size

    override fun onBindViewHolder(holder: CostInfoViewHolder, position: Int) {
        val item = costInfo[position]
        holder.bind(item)
    }

    inner class CostInfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ViewCostItemBinding.bind(view)

        fun bind(item: ItemCostInfo) {
            binding.tvCostDescription.text = item.description
            val cityText = "$name: ${item.cost} €"
            binding.tvCity.text = cityText
            binding.ivCostImage.setImageResource(item.imageId)
        }
    }
}
