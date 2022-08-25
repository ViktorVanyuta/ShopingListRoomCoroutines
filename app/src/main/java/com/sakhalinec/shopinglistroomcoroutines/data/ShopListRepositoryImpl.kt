package com.sakhalinec.shopinglistroomcoroutines.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.sakhalinec.shopinglistroomcoroutines.domain.ShopItem
import com.sakhalinec.shopinglistroomcoroutines.domain.ShopListRepository

class ShopListRepositoryImpl(application: Application): ShopListRepository {

    // создание экземпляра DAO
    private val shopListDao = AppDatabase.getInstance(application).shopListDao()

    // создание mapper-a
    private val mapper = ShopListMapper()


    override suspend fun addShopItem(shopItem: ShopItem) {
        // shopListDao.addShopItem(shopItem)
        // так написать не получится потому что shopListDao.addShopItem() принимает в качестве параметра
        // ShopItemDbModel то есть модель БД, а addShopItem() из репозитория принимает просто
        // объект ShopItem из domain слоя! Нужно создать функцию которая будет принимать в качестве
        // параметра сущность из domain слоя и преобразовывать в модель БД. Класс который отвечает
        // за преобразование одних объетов в другие называется Mapper! Лежит в data слое,
        // так как domain слой ничего не должен знать про другие слои

        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun deleteShopItem(shopItem: ShopItem) {
        shopListDao.deleteShopItem(shopItem.id)
    }

    override suspend fun editShopItem(shopItem: ShopItem) {
        // тут вызывается функция addShopItem потому что OnConflictStrategy.REPLACE - при добавлении
        // объекта с уже существующим id старый объект просто перезапишится
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun getShopItem(shopItemId: Int): ShopItem {
        // получение объекта dbModel и вернем его значение в виде объекта domain слоя
        val dbModel = shopListDao.getShopItem(shopItemId)
        return mapper.mapDbModelToEntity(dbModel)
    }

    // если нужно преобразовывать объект из исходной liveData-ы в другой тип, то лучше использовать
    // функцию map из класса Transformations, эта функция делает то же самое что и MediatorLiveData
    override fun getShopList(): LiveData<List<ShopItem>> = Transformations.map(
        shopListDao.getShopList()) {
        mapper.mapListDbModelToListEntity(it)
    }

    /*
    MediatorLiveData это класс посредник который может перехватывать объекты из другой liveData-ы
    и как то их обрабатывать, например преобразовывать в другой тип или устанавливать значение только
    при выполнении какого то условия...

    override fun getShopList(): LiveData<List<ShopItem>> = MediatorLiveData<List<ShopItem>>().apply {

//        в функции addSource() первым параметром устанавливается источник данных, объекты какой liveData
//        будут перехвачены, то есть будут перехвачены объекты базы данных.
//        вторым параметром принимает лямбда выражение которое в качестве параметра принимает
//        list<ShopItemDbModel> эта функция будет вызвана при каждом изменении в оригинальной liveData
        addSource(shopListDao.getShopList()){

//            тут внутри лямбды мы сами можем решать какой тип данных нужен и при необходимость при
//            каких условиях
            value = mapper.mapListDbModelToListEntity(it) // преобразовываем данную коллекцию
        }
    }
    */

}
