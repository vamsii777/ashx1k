package com.dewonderstruck.apps.ashx0.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dewonderstruck.apps.ashx0.viewobject.CommentDetail

@Dao
abstract class CommentDetailDao {
    //region Comment Detail
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(commentDetail: CommentDetail?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun update(commentDetail: CommentDetail?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllCommentDetailList(commentDetailList: List<CommentDetail?>?)

    @Query("DELETE FROM CommentDetail WHERE headerId = :headerId")
    abstract fun deleteCommentDetailListByHeaderId(headerId: String?)

    @Query("SELECT * FROM CommentDetail WHERE headerId = :headerId ORDER BY addedDate desc")
    abstract fun getAllCommentDetailList(headerId: String?): LiveData<List<CommentDetail?>?>?

    @Query("DELETE FROM CommentDetail")
    abstract fun deleteAll() //endregion
}