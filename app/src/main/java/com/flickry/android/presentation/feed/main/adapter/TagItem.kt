package com.flickry.android.presentation.feed.main.adapter

import android.view.View
import com.flickry.android.R
import com.flickry.android.databinding.ListItemPopularTagBinding
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem

internal class TagItem(internal val tagText: String) :
    BindableItem<ListItemPopularTagBinding>() {

    override fun bind(viewBinding: ListItemPopularTagBinding, position: Int) {
        viewBinding.tagText.text = tagText
    }

    override fun getLayout(): Int = R.layout.list_item_popular_tag

    override fun initializeViewBinding(view: View): ListItemPopularTagBinding = ListItemPopularTagBinding.bind(view)

    override fun hasSameContentAs(other: Item<*>): Boolean {
        return this.tagText == (other as TagItem).tagText
    }

}