package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dewonderstruck.apps.ashx0.viewobject.Comment

@Dao
abstract class CommentDao {
    //region Discounts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(comment: Comment?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllCommentList(commentList: List<Comment?>?)

    @Query("DELETE FROM Comment WHERE productId = :product_id")
    abstract fun deleteAllCommentList(product_id: String?)

    @Query("SELECT * FROM Comment WHERE productId = :product_id order by addedDate desc")
    abstract fun getAllCommentList(product_id: String?): LiveData<List<Comment?>?>?

    @Query("DELETE FROM Comment")
    abstract fun deleteAll()
}