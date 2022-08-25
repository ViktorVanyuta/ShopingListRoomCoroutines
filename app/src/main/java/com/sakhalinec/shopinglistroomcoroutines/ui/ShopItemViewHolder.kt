package com.sakhalinec.shopinglistroomcoroutines.ui

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

// Холдер с использованием dataBinding для одного типа вью
/*class ShopItemViewHolder(
    val binding: ItemShopDisabledBinding) : RecyclerView.ViewHolder(binding.root)*/

// Холдер с использованием dataBinding для нескольких типов вью
// получается просто класс который хранит ссылку на переменную binding
class ShopItemViewHolder(
    // используем не ItemShopDisabledBinding или ItemShopEnabledBinding,
    // а его родительский класс ViewDataBinding потому что при использовании нескольких типов вью
    // мы не можем знать какой из типов сюда прилетит
    val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root)
// наследуемся от RecyclerView и во ViewHolder нужно передать корневой вью элемент
