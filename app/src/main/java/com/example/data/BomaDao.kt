package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BomaDao {
    // Property operations
    @Query("SELECT * FROM properties")
    fun getAllProperties(): Flow<List<Property>>

    @Query("SELECT * FROM properties WHERE category = :category")
    fun getPropertiesByCategory(category: String): Flow<List<Property>>

    @Query("SELECT * FROM properties WHERE isSaved = 1")
    fun getSavedProperties(): Flow<List<Property>>

    @Query("SELECT * FROM properties WHERE id = :id")
    suspend fun getPropertyById(id: String): Property?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperties(properties: List<Property>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperty(property: Property)

    @Query("UPDATE properties SET isSaved = :isSaved WHERE id = :id")
    suspend fun setSavedStatus(id: String, isSaved: Boolean)

    @Query("UPDATE properties SET fraudReportCount = fraudReportCount + 1 WHERE id = :id")
    suspend fun incrementFraudReport(id: String)

    // Viewing Requests
    @Query("SELECT * FROM viewing_requests ORDER BY timestamp DESC")
    fun getAllViewingRequests(): Flow<List<ViewingRequest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertViewingRequest(request: ViewingRequest)

    // Document Vault
    @Query("SELECT * FROM document_vault ORDER BY id DESC")
    fun getAllDocuments(): Flow<List<DocumentVaultItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: DocumentVaultItem)

    // Mortgage Leads
    @Query("SELECT * FROM mortgage_leads ORDER BY timestamp DESC")
    fun getAllMortgageLeads(): Flow<List<MortgageLead>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMortgageLead(lead: MortgageLead)
}
