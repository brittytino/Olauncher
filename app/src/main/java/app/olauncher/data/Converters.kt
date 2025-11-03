package app.olauncher.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromIntList(list: List<Int>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toIntList(string: String): List<Int> {
        return string.split(",").map { it.toInt() }
    }
}
