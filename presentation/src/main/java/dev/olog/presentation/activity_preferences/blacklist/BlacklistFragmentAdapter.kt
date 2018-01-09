package dev.olog.presentation.activity_preferences.blacklist

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR
import dev.olog.presentation._base.list.DataBoundViewHolder
import dev.olog.presentation.model.DisplayableItem
import dev.olog.shared.clearThenAdd
import javax.inject.Inject

class BlacklistFragmentAdapter @Inject constructor()
    : RecyclerView.Adapter<DataBoundViewHolder<*>>() {

    private val data: MutableList<DisplayableItem> = mutableListOf()

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int = data[position].type

    override fun onBindViewHolder(holder: DataBoundViewHolder<*>, position: Int) {
        holder.binding.setVariable(BR.item, data[position])
        holder.binding.executePendingBindings()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<*> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, viewType, parent, false)
        val viewHolder = DataBoundViewHolder(binding)
        initViewHolderListeners(viewHolder)
        return viewHolder
    }

    private fun initViewHolderListeners(viewHolder: DataBoundViewHolder<*>) {

    }

    fun updateDataSet(list: List<DisplayableItem>){
        data.clearThenAdd(list)
        notifyDataSetChanged()
    }


}