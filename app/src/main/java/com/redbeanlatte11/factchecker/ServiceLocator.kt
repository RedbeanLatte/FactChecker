package com.redbeanlatte11.factchecker

import android.content.Context
import androidx.annotation.VisibleForTesting

object ServiceLocator {

    private val lock = Any()
//    private var database: GarimDatabase? = null
    @Volatile
    var productsRepository: VideosRepository? = null
        @VisibleForTesting set

    fun provideProductsRepository(context: Context): VideosRepository {
//        synchronized(this) {
//            if (productsRepository == null) {
//                productsRepository = createProductsRepository(context)
//            }
//            return productsRepository as ProductsRepository
//        }
        return VideosRepository()
    }

//    private fun createProductsRepository(context: Context): ProductsRepository {
//        return DefaultProductsRepository(
//            ProductsRemoteDataSource(GarimFirestore(context)),
////            FakeProductsRemoteDataSource(ProductsJsonParser(context, "products.json")),
//            createProductLocalDataSource(context)
//        )
//    }
//
//    private fun createProductLocalDataSource(context: Context): ProductsDataSource {
//        val database = database ?: createDataBase(context)
//        return ProductsLocalDataSource(database.productDao())
//    }
//
//    private fun createDataBase(context: Context): GarimDatabase {
//        val result = Room.databaseBuilder(
//            context.applicationContext,
//            GarimDatabase::class.java, "Products.db"
//        ).build()
//        database = result
//        return result
//    }
}
