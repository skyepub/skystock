package com.skytree.skystock.repository

import com.skytree.skystock.entity.AlertLevel
import com.skytree.skystock.entity.StockAlert
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface StockAlertRepository : JpaRepository<StockAlert, Int> {

    fun findBySkymallProductId(skymallProductId: Int): StockAlert?

    fun findByAlertLevel(alertLevel: AlertLevel, pageable: Pageable): Page<StockAlert>

    fun countByAlertLevel(alertLevel: AlertLevel): Long
}
