package com.sakhalinec.shopinglistroomcoroutines.data

import com.sakhalinec.shopinglistroomcoroutines.domain.ShopItem

class ShopListMapper {

    // преобразует сущность из репозитория domain слоя в сущность модели БД data слоя
    fun mapEntityToDbModel(shopItem: ShopItem): ShopItemDbModel {
        // можно сделать конструкцию короче как в функциях ниже, но для примера пусть будет так
        return ShopItemDbModel(
            id = shopItem.id,
            name = shopItem.name,
            count = shopItem.count,
            enabled = shopItem.enabled
        )
    }

    // обратное преобразование из data слоя в domain
    fun mapDbModelToEntity(shopItemDbModel: ShopItemDbModel) = ShopItem(
        id = shopItemDbModel.id,
        name = shopItemDbModel.name,
        count = shopItemDbModel.count,
        enabled = shopItemDbModel.enabled
    )

    // преобразовывает List объектов из ShopItemDbModel в List ShopItem
    fun mapListDbModelToListEntity(list: List<ShopItemDbModel>) = list.map{
        mapDbModelToEntity(it)
    }

}