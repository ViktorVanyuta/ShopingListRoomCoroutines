package com.sakhalinec.shopinglistroomcoroutines.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sakhalinec.shopinglistroomcoroutines.R
import com.sakhalinec.shopinglistroomcoroutines.VIEW_TYPE_DISABLED
import com.sakhalinec.shopinglistroomcoroutines.VIEW_TYPE_ENABLED
import com.sakhalinec.shopinglistroomcoroutines.databinding.ItemShopDisabledBinding
import com.sakhalinec.shopinglistroomcoroutines.databinding.ItemShopEnabledBinding
import com.sakhalinec.shopinglistroomcoroutines.domain.ShopItem

class ShopListAdapter : ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {

    var onShopItemLongClickListener: ((ShopItem) -> Unit?)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {

        // выбор какой тип вью использовать
        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }

        // создание вью холдера с использованием dataBinding для нескольких типов вью,
        // создать нужно при помощи класса DataBindingUtil и угловых скобках указать,
        // что тут будет использоваться родительский тип ViewDataBinding
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )
        // возвращаем созданный ViewHolder
        return ShopItemViewHolder(binding)

    }

    // viewHolder: ShopItemViewHolder держит ссылку на все вью из макета
    // и так же на все вью которые находятся внутри маккета, чтобы им можно было устанавливать значения
    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        // получение объекта по его позиции
        val shopItem = getItem(position)
        // получаем объект binding из объекта viewHolder: ShopItemViewHolder
        // viewHolder так же хранит ссылку на все нобходимые вью, вместо viewHolder можно использовать
        // объект типа binding, но Recycler пока не может работать напрямую с binding и поэтому
        // необходимо binding передавать внутрь viewHolder-a
        val binding = viewHolder.binding
        // для установки значений внутрь элементов вью, ноебходимо сделать каст binding-a и привести
        // его к своему дочернему классу, иначе не сможем получить доступ к элементам вью
        when (binding) {
            is ItemShopDisabledBinding -> {
                // код не дублируется!!! несмотря на одинаковые строчки кода, объекты binding разные
//                binding.tvName.text = shopItem.name
//                binding.tvCount.text = shopItem.count.toString()
                binding.shopItem = shopItem
            }
            is ItemShopEnabledBinding -> {
                // код не дублируется!!! несмотря на одинаковые строчки кода, объекты binding разные
//                binding.tvName.text = shopItem.name
//                binding.tvCount.text = shopItem.count.toString()
                binding.shopItem = shopItem
            }
        }
        // установка слушателя клика на корневой элемент binding, root элемент есть
        // у любого binding как и у родительского ViewDataBinding поэтому не нужны никакие касты
        binding.root.setOnLongClickListener{
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        // установка слушателя клика на корневой элемент binding
        binding.root.setOnClickListener{
            onShopItemClickListener?.invoke(shopItem)
        }
    }

    /* отвечает за получение объекта по его позиции а так же, за определение типа view,
    для разных элементов используются разные макеты */
    override fun getItemViewType(position: Int): Int {

        val item = getItem(position)
        return if (item.enabled) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }

}

// РЕАЛИЗАЦИЯ АДАПТЕРА В КОТОРОМ ИСПОЛЬЗУЕТСЯ DATA BINDING И ОДИН ТИП ВЬЮ
/*
class ShopItemDiffListAdapter: ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {

    var onShopItemLongClickListener: ((ShopItem) -> Unit?)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {

        // выбор какой тип вью использовать
        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }

        // создание вью холдера с использованием dataBinding для одного типа вью
        val binding = ItemShopDisabledBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShopItemViewHolder(binding)

    }

    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        // получаем объект binding из объекта viewHolder: ShopItemViewHolder
        val binding = viewHolder.binding
        binding.tvName.text = shopItem.name
        binding.tvCount.text = shopItem.count.toString()
        // установка слушателя клика на корневой элемент binding
        binding.root.setOnLongClickListener{
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        // установка слушателя клика на корневой элемент binding
        binding.root.setOnClickListener{
            onShopItemClickListener?.invoke(shopItem)
        }
    }

    /* отвечает за получение объекта по его позиции а так же, за определение типа view,
    для разных элементов используются разные макеты */
    override fun getItemViewType(position: Int): Int {

        val item = getItem(position)
        return if (item.enabled) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }
}
*/


