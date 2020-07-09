package com.dewonderstruck.apps.ashx0.db;

import com.dewonderstruck.apps.ashx0.viewobject.AboutUs;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/**
 * Created by Vamsi Madduluri on 12/30/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */

@Dao
public interface AboutUsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AboutUs aboutUs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AboutUs> aboutUsList);

    @Query("SELECT * FROM AboutUs LIMIT '1'")
    LiveData<AboutUs> get();

    @Query("SELECT * FROM AboutUs")
    LiveData<List<AboutUs>> getAll();

    @Query("DELETE FROM AboutUs")
    void deleteTable();

}