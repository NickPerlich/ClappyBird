package dev.csse.nperlich.clappybird.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HighScoreDao {
    @Query("SELECT * FROM high_scores ORDER BY score DESC, timestamp ASC LIMIT 10")
    fun getTop10Scores(): Flow<List<HighScore>>

    @Query("SELECT * FROM high_scores ORDER BY score DESC LIMIT 1")
    fun getHighScore(): Flow<HighScore?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(highScore: HighScore)

    @Query("DELETE FROM high_scores WHERE id NOT IN (SELECT id FROM high_scores ORDER BY score DESC LIMIT 10)")
    suspend fun deleteOldScores()
}