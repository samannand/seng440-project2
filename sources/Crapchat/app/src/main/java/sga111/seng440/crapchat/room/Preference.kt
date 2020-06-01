package sga111.seng440.crapchat.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preferences_table")
data class Preference(@PrimaryKey @ColumnInfo(name = "key") val key: String,  @ColumnInfo(name = "value") val value: Boolean)