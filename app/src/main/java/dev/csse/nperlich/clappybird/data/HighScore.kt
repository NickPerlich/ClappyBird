package dev.csse.nperlich.clappybird.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "high_scores")
data class HighScore(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val score: Int,
    val username: String,
    val timestamp: Long = System.currentTimeMillis()
)