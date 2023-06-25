package com.practicum.practice_work_1_1

import android.app.Application
import androidx.paging.PagingSource
import androidx.room.*

class App : Application() {

    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "db"
        ).build()
    }
}

@Database(
    entities = [
        FeedPhoto::class,
        RemoteKeys::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getFeedPhotoDao(): FeedPhotoDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao
}

@Entity(tableName = "FeedPhoto")
data class FeedPhoto(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "urlsSmall")
    val urlsSmall: String,
    @ColumnInfo(name = "likes")
    val likes: Int,
    @ColumnInfo(name = "liked_by_user")
    val likedByUser: Boolean,
    @ColumnInfo(name = "nameAuthor")
    val nameAuthor: String,
    @ColumnInfo(name = "usernameAuthor")
    val usernameAuthor: String,
    @ColumnInfo(name = "userAvatar")
    val userAvatar: String
)

@Dao
interface FeedPhotoDao {
    @Query("SELECT*FROM FeedPhoto")
    fun getAll(): PagingSource<Int, FeedPhoto>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: List<FeedPhoto>)
    @Query("Delete From FeedPhoto")
    suspend fun delete()
    @Update
    suspend fun update(data: FeedPhoto)
}

@Entity(tableName = "remote_key")
data class RemoteKeys(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "photo_id")
    val photoId: String,
    val prevKey: Int?,
    val currentPage: Int,
    val nextKey: Int?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)
    @Query("Select * From remote_key Where photo_id = :id")
    suspend fun getRemoteKeyByPhotoID(id: String): RemoteKeys?
    @Query("Delete From remote_key")
    suspend fun clearRemoteKeys()
    @Query("Select created_at From remote_key Order By created_at DESC LIMIT 1")
    suspend fun getCreationTime(): Long?
}