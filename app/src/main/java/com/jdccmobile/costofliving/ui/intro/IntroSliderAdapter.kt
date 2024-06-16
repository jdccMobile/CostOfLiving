package com.jdccmobile.costofliving.ui.intro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdccmobile.costofliving.R
import com.jdccmobile.costofliving.databinding.ViewIntroSlideBinding
import com.jdccmobile.costofliving.domain.models.IntroSlide

class IntroSliderAdapter(private val introSlide: List<IntroSlide>) :
    RecyclerView.Adapter<IntroSliderAdapter.IntroSliderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroSliderViewHolder {
        return IntroSliderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_intro_slide, parent, false),
        )
    }

    override fun getItemCount() = introSlide.size

    override fun onBindViewHolder(holder: IntroSliderViewHolder, position: Int) {
        holder.bind(introSlide[position])
    }

    inner class IntroSliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ViewIntroSlideBinding.bind(view)

        fun bind(introSlide: IntroSlide) {
            binding.tvTitle.text = introSlide.title
            binding.tvDescription.text = introSlide.description
            binding.ivSlideImage.setImageResource(introSlide.image)
        }
    }
}
