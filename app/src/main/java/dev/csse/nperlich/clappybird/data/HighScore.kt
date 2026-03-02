package dev.csse.nperlich.clappybird.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "high_scores")
data class HighScore(
    @PrimaryKey val id: Int = 1,  // Always ID 1 since only one high score is stored
    val score: Int,
    val username: String
)