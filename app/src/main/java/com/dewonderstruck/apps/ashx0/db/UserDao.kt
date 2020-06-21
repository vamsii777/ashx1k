package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dewonderstruck.apps.ashx0.viewobject.User
import com.dewonderstruck.apps.ashx0.viewobject.UserLogin

/**
 * Created by Vamsi Madduluri on 12/6/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(user: User?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(userList: List<User?>?)

    @get:Query("SELECT * FROM User")
    val all: LiveData<List<User?>?>?

    @Query("SELECT * FROM User WHERE userId = :userId")
    fun getUserData(userId: String?): LiveData<User?>?

    //region User Login Related
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userLogin: UserLogin?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(userLogin: UserLogin?)

    @Query("SELECT * FROM UserLogin WHERE userId = :userId")
    fun getUserLoginData(userId: String?): LiveData<UserLogin?>?

    @get:Query("SELECT * FROM UserLogin")
    val userLoginData: LiveData<List<UserLogin?>?>?

    @Query("DELETE FROM UserLogin")
    fun deleteUserLogin() //endregion
}