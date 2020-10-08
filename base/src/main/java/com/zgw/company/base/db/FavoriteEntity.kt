package com.zgw.company.base.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zgw.company.base.core.config.Config

@Entity(tableName = Config.FAVORITE_TABLE_NAME)
class FavoriteEntity {
    @PrimaryKey()
    @ColumnInfo(name = "userId")
    var id: String = ""
    var name: String? = ""
    var description: String? = ""
    var url: String? = ""
    var avatarUrl: String? = ""
    var img1Url: String? = ""
    var img2Url: String? = ""
    var img3Url: String? = ""
    var readNo: String? = ""
    var likeNo: String? = ""
}