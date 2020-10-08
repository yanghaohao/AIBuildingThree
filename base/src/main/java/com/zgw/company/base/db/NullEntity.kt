package com.zgw.company.base.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zgw.company.base.core.config.Config

@Entity(tableName = Config.NULL_TABLE_NAME)
class NullEntity {

    @PrimaryKey()
    var id: String = ""
}