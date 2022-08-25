package com.sakhalinec.shopinglistroomcoroutines.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShopItemDbModel::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    // абстрактная функция которая возвращает реализацию интерфейса Dao
    abstract fun shopListDao(): ShopListDao

    // реализация синглтоном, для того чтобы на все приложение был только один экземпляр БД
    companion object {

        // переменная экземпляра БД
        private var INSTANCE: AppDatabase? = null
        // объект для синхронизации БД
        private val LOCK = Any()
        // константа для имени БД
        private const val DB_NAME = "shop_item.db"

        // функция создания БД, для этого нужно передать контекст то есть application в данном случае
        // и возвращает экземпляр БД AppDatabase
        fun getInstance(application: Application): AppDatabase {
            // проверяем, если инстансу уже присвоенно значение то, просто вернем его
            INSTANCE?.let {
                return it
            }

            // синглтоны должны быть синхронизированы, чтобы несколько потоков не могли иметь одновременно доступ
            synchronized(LOCK) {
                // повторная проверка нужна чтобы не переприсвоилось новое значение другим потоком,
                // такой способ проверки внутри блока синхронизации называется double check
                INSTANCE?.let {
                    return it
                }

                // создание БД, если инстанс равен null
                val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = db
                return db
            }
        }

    }

}