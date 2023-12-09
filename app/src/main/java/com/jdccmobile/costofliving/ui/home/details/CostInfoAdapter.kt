package com.jdccmobile.costofliving.ui.home.details

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.data.remote.model.cost.Price
import com.jdccmobile.costofliving.databinding.ViewCostItemBinding

class CostInfoAdapter(
    private val costInfo: List<Price>
): RecyclerView.Adapter<CostInfoAdapter.CostInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CostInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_cost_item, parent, false)
        return CostInfoViewHolder(view)
    }

    override fun getItemCount() : Int {
        Log.i("JDJD", "size: ${costInfo.size}")
        return costInfo.size
    }


    override fun onBindViewHolder(holder: CostInfoViewHolder, position: Int) {
        val item = costInfo[position]
        holder.bind(item)
    }

    inner class CostInfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ViewCostItemBinding.bind(view)

        fun bind(price: Price) {
            Log.i("JDJD", "price: ${price}")
            binding.tvCostDescription.text = price.itemName
            binding.tvCity.text = "prueba"
            binding.tvCountry.text = "price.countryName"
        }
    }
}

