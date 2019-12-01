package com.example.testsearchfragment.adapter


import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.arlib.floatingsearchview.util.Util
import com.example.testsearchfragment.R
import com.example.testsearchfragment.data.ColorWrapper

import java.util.ArrayList

class SearchResultsListAdapter : RecyclerView.Adapter<SearchResultsListAdapter.ViewHolder>() {

    private var mDataSet: List<ColorWrapper> = ArrayList()

    private var mLastAnimatedItemPosition = -1

    private var mItemsOnClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClick(colorWrapper: ColorWrapper)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mColorName: TextView
        val mColorValue: TextView
        val mTextContainer: View

        init {
            mColorName = view.findViewById(R.id.color_name) as TextView
            mColorValue = view.findViewById(R.id.color_value) as TextView
            mTextContainer = view.findViewById(R.id.text_container)
        }
    }

    fun swapData(mNewDataSet: List<ColorWrapper>) {
        mDataSet = mNewDataSet
        notifyDataSetChanged()
    }

    fun setItemsOnClickListener(onClickListener: OnItemClickListener) {
        this.mItemsOnClickListener = onClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_results_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val colorSuggestion = mDataSet[position]
        holder.mColorName.text = colorSuggestion.name
        holder.mColorValue.text = colorSuggestion.hex

        val color = Color.parseColor(colorSuggestion.hex)
        holder.mColorName.setTextColor(color)
        holder.mColorValue.setTextColor(color)

        if (mLastAnimatedItemPosition < position) {
            animateItem(holder.itemView)
            mLastAnimatedItemPosition = position
        }

        if (mItemsOnClickListener != null) {
            holder.itemView.setOnClickListener { mItemsOnClickListener!!.onClick(mDataSet[position]) }
        }
    }

    override fun getItemCount()= mDataSet.size


    private fun animateItem(view: View) {
        view.translationY = Util.getScreenHeight(view.context as Activity).toFloat()
        view.animate()
            .translationY(0f)
            .setInterpolator(DecelerateInterpolator(3f))
            .setDuration(700)
            .start()
    }
}
