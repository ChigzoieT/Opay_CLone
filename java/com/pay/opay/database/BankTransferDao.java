package com.pay.opay.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BankTransferDao {

    @Insert
    void insert(BankTransfer transfer);

    @Update
    void update(BankTransfer transfer);

    @Delete
    void delete(BankTransfer transfer);

    @Query("SELECT * FROM BankTransfer ORDER BY timestamp DESC")
    LiveData<List<BankTransfer>> getAllTransfers();

    @Query("SELECT COUNT(*) FROM BankTransfer WHERE senderName = :sender AND accountNumber = :account")
    int countByDetails(String sender, String account);
}
