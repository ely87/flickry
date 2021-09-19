package com.flickry.android.presentation.feed.main.adapter

import android.view.View
import com.flickry.android.R
import com.flickry.android.databinding.ListItemPlaceholderBinding
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem

internal class FeedAdapterPlaceholderItem : BindableItem<ListItemPlaceholderBinding>() {

    override fun bind(viewBinding: ListItemPlaceholderBinding, position: Int) = Unit

    override fun getLayout(): Int = R.layout.list_item_placeholder

    override fun initializeViewBinding(view: View): ListItemPlaceholderBinding =
        ListItemPlaceholderBinding.bind(view)

    override fun isSameAs(other: Item<*>): Boolean {
        return other is FeedAdapterPlaceholderItem
    }

}