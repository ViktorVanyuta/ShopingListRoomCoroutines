package com.sakhalinec.shopinglistroomcoroutines.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sakhalinec.shopinglistroomcoroutines.domain.ShopItem

@Dao
interface ShopListDao {

    // возвращает список всех записей
    @Query("SELECT * FROM shop_items")
    fun getShopList(): LiveData<List<ShopItemDbModel>>

    // добавляет новую запись
    // OnConflictStrategy.REPLACE если добавлен объект с уже существующим id то он будет перезаписан
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addShopItem(shopItemDbModel: ShopItemDbModel)

    // удаление записи по id объекта
    @Query("DELETE FROM shop_items WHERE id=:shopItemId")
    suspend fun deleteShopItem(shopItemId: Int)

    // возвращает один элемент из БД по id элемента, (LIMIT 1) всегда будет возвращать только 1 елемент
    @Query("SELECT * FROM shop_items WHERE id=:shopItemId LIMIT 1")
    suspend fun getShopItem(shopItemId: Int): ShopItemDbModel

}