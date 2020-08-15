package com.dewonderstruck.apps.ashx0.ui.language

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.dewonderstruck.apps.ashx0.R
import com.dewonderstruck.apps.ashx0.utils.LanguageData

class LanguageSelectionListAdapter internal constructor(private val dataSet: List<LanguageData>?, context: Context?, private val selectedPosition: Int) : ArrayAdapter<LanguageData?>(context!!, R.layout.item_language_selection_list_adapter, dataSet!!) {

    private class ViewHolder {
        var languageNameTextView: TextView? = null
        var languageSelectedImageView: ImageView? = null
    }

    override fun getCount(): Int {
        return dataSet?.size ?: 0
    }

    override fun getItem(item: Int): LanguageData? {
        val gi: LanguageData? = dataSet?.get(item)
        return gi
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get the data item for this position
        var convertView = convertView
        val dataModel = getItem(position)
        val viewHolder: ViewHolder // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            convertView = inflater.inflate(R.layout.item_language_selection_list_adapter, parent, false)
            viewHolder.languageNameTextView = convertView.findViewById(R.id.languageNameTextView)
            viewHolder.languageSelectedImageView = convertView.findViewById(R.id.languageSelectedImageView)
            if (dataModel != null) {
                viewHolder!!.languageNameTextView!!.setText(dataModel.languageName)
            }
            if (selectedPosition == position) {
                viewHolder!!.languageSelectedImageView!!.setVisibility(View.VISIBLE)
            } else {
                viewHolder!!.languageSelectedImageView!!.setVisibility(View.GONE)
            }
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
            viewHolder.languageNameTextView = convertView.findViewById(R.id.languageNameTextView)
            viewHolder.languageSelectedImageView = convertView.findViewById(R.id.languageSelectedImageView)
            if (dataModel != null) {
                viewHolder!!.languageNameTextView!!.setText(dataModel.languageName)
            }
            if (selectedPosition == position) {
                viewHolder!!.languageSelectedImageView!!.setVisibility(View.VISIBLE)
            } else {
                viewHolder!!.languageSelectedImageView!!.setVisibility(View.GONE)
            }
        }

        // Return the completed view to render on screen
        return convertView!!
    }

}