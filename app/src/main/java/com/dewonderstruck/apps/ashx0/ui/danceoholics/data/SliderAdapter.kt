package com.dewonderstruck.apps.ashx0.ui.danceoholics.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.ui.danceoholics.data.SliderAdapter.SliderViewHolder
import com.rishabhharit.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import java.util.ArrayList

internal class SliderAdapter(private val sliderItems: ArrayList<Any>, private val viewPager2: ViewPager2) : RecyclerView.Adapter<SliderViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.structure_pagexml, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.setImage(sliderItems[position] as SliderItem)
    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }

    internal inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageSlide)
        fun setImage(sliderItem: SliderItem) {
            Picasso.get().load(sliderItem.image).into(imageView)
            //imageView.setImageResource(sliderItem.getImage());
        }

    }

}