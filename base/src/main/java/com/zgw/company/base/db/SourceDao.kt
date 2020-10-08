package com.zgw.company.base.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zgw.company.base.core.config.Config

@Dao
interface SourceDao {

    @Query("SELECT * FROM" + Config.FAVORITE_TABLE_NAME)
    fun getAllNewsSource() : LiveData<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSources(favoriteEntity: List<FavoriteEntity>)

    @Delete
    fun deleteSources(favoriteEntity: List<FavoriteEntity>)
}