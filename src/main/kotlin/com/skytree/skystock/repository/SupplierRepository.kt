package com.skytree.skystock.repository

import com.skytree.skystock.entity.Supplier
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface SupplierRepository : JpaRepository<Supplier, Int> {

    fun findByIsActiveTrue(): List<Supplier>

    @Query("""
        SELECT s FROM Supplier s
        WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(s.contactEmail) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    fun searchByKeyword(@Param("keyword") keyword: String, pageable: Pageable): Page<Supplier>
}
