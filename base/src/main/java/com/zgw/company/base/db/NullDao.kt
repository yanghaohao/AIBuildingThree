package com.zgw.company.base.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zgw.company.base.core.config.Config

@Dao
interface NullDao {
    @Query("SELECT * FROM" + Config.NULL_TABLE_NAME)
    fun getAllNewsSource() : LiveData<List<NullEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSources(favoriteEntity: List<NullEntity>)

    @Delete
    fun deleteSources(favoriteEntity: List<NullEntity>)
}