package com.sakhalinec.shopinglistroomcoroutines.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakhalinec.shopinglistroomcoroutines.data.ShopListRepositoryImpl
import com.sakhalinec.shopinglistroomcoroutines.domain.DeleteShopItemUseCase
import com.sakhalinec.shopinglistroomcoroutines.domain.EditShopItemUseCase
import com.sakhalinec.shopinglistroomcoroutines.domain.GetShopListUseCase
import com.sakhalinec.shopinglistroomcoroutines.domain.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository = ShopListRepositoryImpl(application)

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()

    // корутины должны запускаться внутри какого то скоупа с определенным жизненным циклом,
    // чтобы использовать корутины во viewModel-i нужно использовать скоуп жизненный цикл которого
    // совпадает с жизненным циклом viewModel-i который очищается в методе onCleared()
    // создание своего скоупа для корутин, в параметре скоупа в конструктор нужно передать
    // корутин контекст, он описывает на каком потоке будет выполняться корутина,
    // как реагировать на ошибки, для передачи потока на котором будет выполняться корутина
    // используется класс Dispatchers и указывается сам поток например Main или IO, Default
    // Main - выполнение на главном потоке
    // IO - выполнение на фоновом потоке, хорошо подходит для чтения и записи например в БД или сети,
    // использует (кеш тред пулл - если свободных потоков нет и прилетела новая задача то, будет создан
    // новый поток и будет создавать до тех пор пока их количество не достигнет 64 потоков
    // Default - выполнение в фоновом потоке, использует пулл потоков фиксированной длины равной
    // количеству ядер на центральном процессоре, но не менее двух. подходит дли выполнения
    // сложных операций и все вычисления будут проводиться паралельно с максимальной производительностью
//    private val scope = CoroutineScope(Dispatchers.Main)


    fun deleteShopItem(shopItem: ShopItem) {
        viewModelScope.launch {
            deleteShopItemUseCase.deleteShopItem(shopItem)
        }
    }

    fun changeEnableState(shopItem: ShopItem) {
        viewModelScope.launch {
            // создается полная копия ShopItem
            // но состояние будет противоположным к первоначальному обьекту
            val newItem = shopItem.copy(enabled = !shopItem.enabled)
            editShopItemUseCase.editShopItem(newItem)
        }

    }

    // тут нужно отменять скоуп для корутин
//    override fun onCleared() {
//        super.onCleared()
//        scope.cancel()
//    }

}