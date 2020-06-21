package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dewonderstruck.apps.ashx0.viewobject.Country

@Dao
abstract class CountryDao {
    //region history
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(country: Country?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(countryList: List<Country?>?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun update(country: Country?)

    @Query("DELETE FROM Country")
    abstract fun deleteCountry()

    @get:Query("SELECT * FROM Country ORDER BY addedDate DESC")
    abstract val allCountry: LiveData<List<Country?>?>?

    @Query("SELECT * FROM Country WHERE id=:countryId")
    abstract fun getCountryList(countryId: String?): LiveData<List<Country?>?>?

    @Query("DELETE FROM Country WHERE id =:id")
    abstract fun deleteCountryById(id: String?) //endregion
}