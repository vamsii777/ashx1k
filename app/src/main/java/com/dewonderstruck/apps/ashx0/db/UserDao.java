package com.dewonderstruck.apps.ashx0.db;

import com.dewonderstruck.apps.ashx0.viewobject.User;
import com.dewonderstruck.apps.ashx0.viewobject.UserLogin;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Created by Vamsi Madduluri on 12/6/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<User> userList);

    @Query("SELECT * FROM User")
    LiveData<List<User>> getAll();

    @Query("SELECT * FROM User WHERE userId = :userId")
    LiveData<User> getUserData(String userId);

    //region User Login Related

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserLogin userLogin);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(UserLogin userLogin);

    @Query("SELECT * FROM UserLogin WHERE userId = :userId")
    LiveData<UserLogin> getUserLoginData(String userId);

    @Query("SELECT * FROM UserLogin")
    LiveData<List<UserLogin>> getUserLoginData();

    @Query("DELETE FROM UserLogin")
    void deleteUserLogin();

    //endregion
}
