package com.enpassio.apis.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.enpassio.apis.ListOfMailIds
import com.enpassio.apis.MessageWithIdAndThreadId

@Dao
interface ListOfMailIdsDao {

    @Query("SELECT * FROM listofmailidstable ORDER BY id")
    fun loadAllListOfMailIds(): LiveData<ListOfMailIds>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMailId(messageWithIdAndThreadId: MessageWithIdAndThreadId)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateMessageWithIdAndThread(messageWithIdAndThreadId: MessageWithIdAndThreadId)

    @Delete
    fun deleteMessageWithIdAndThread(messageWithIdAndThreadId: MessageWithIdAndThreadId)
}
