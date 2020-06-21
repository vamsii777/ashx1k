package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dewonderstruck.apps.ashx0.viewobject.City

@Dao
abstract class CityDao {
    //region history
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(city: City?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(cityList: List<City?>?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun update(city: City?)

    @Query("DELETE FROM City")
    abstract fun deleteCity()

    @get:Query("SELECT * FROM City ORDER BY addedDate DESC")
    abstract val allCity: LiveData<List<City?>?>?

    @Query("SELECT * FROM City WHERE id=:cityId")
    abstract fun getCityList(cityId: String?): LiveData<List<City?>?>?

    @Query("DELETE FROM City WHERE id =:id")
    abstract fun deleteCityById(id: String?) //endregion
}