package com.skytree.skystock.service

import com.skytree.skystock.dto.*
import com.skytree.skystock.entity.AlertLevel
import com.skytree.skystock.entity.StockAlert
import com.skytree.skystock.exception.DuplicateException
import com.skytree.skystock.exception.EntityNotFoundException
import com.skytree.skystock.repository.StockAlertRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class StockAlertService(
    private val alertRepo: StockAlertRepository
) {
    fun getAllAlerts(pageable: Pageable): Page<StockAlertResponse> =
        alertRepo.findAll(pageable).map { StockAlertResponse.from(it) }

    fun getAlert(id: Int): StockAlertResponse =
        StockAlertResponse.from(findAlertOrThrow(id))

    fun getAlertByProduct(skymallProductId: Int): StockAlertResponse {
        val alert = alertRepo.findBySkymallProductId(skymallProductId)
            ?: throw EntityNotFoundException("재고 알림(상품)", skymallProductId)
        return StockAlertResponse.from(alert)
    }

    fun getAlertsByLevel(level: AlertLevel, pageable: Pageable): Page<StockAlertResponse> =
        alertRepo.findByAlertLevel(level, pageable).map { StockAlertResponse.from(it) }

    @Transactional
    fun createAlert(req: CreateStockAlertRequest): StockAlertResponse {
        alertRepo.findBySkymallProductId(req.skymallProductId)?.let {
            throw DuplicateException("재고 알림(상품ID)", req.skymallProductId)
        }

        val alert = alertRepo.save(
            StockAlert(
                skymallProductId = req.skymallProductId,
                skymallProductName = req.skymallProductName,
                safetyStock = req.safetyStock,
                reorderPoint = req.reorderPoint,
                reorderQuantity = req.reorderQuantity,
                alertLevel = req.alertLevel
            )
        )
        return StockAlertResponse.from(alert)
    }

    @Transactional
    fun updateAlert(id: Int, req: UpdateStockAlertRequest): StockAlertResponse {
        val alert = findAlertOrThrow(id)
        req.skymallProductName?.let { alert.skymallProductName = it }
        req.safetyStock?.let { alert.safetyStock = it }
        req.reorderPoint?.let { alert.reorderPoint = it }
        req.reorderQuantity?.let { alert.reorderQuantity = it }
        req.alertLevel?.let { alert.alertLevel = it }
        alert.updatedAt = LocalDateTime.now()
        return StockAlertResponse.from(alert)
    }

    @Transactional
    fun updateAlertLevel(id: Int, req: UpdateAlertLevelRequest): StockAlertResponse {
        val alert = findAlertOrThrow(id)
        alert.alertLevel = req.alertLevel
        alert.updatedAt = LocalDateTime.now()
        return StockAlertResponse.from(alert)
    }

    @Transactional
    fun deleteAlert(id: Int) {
        findAlertOrThrow(id)
        alertRepo.deleteById(id)
    }

    private fun findAlertOrThrow(id: Int): StockAlert =
        alertRepo.findById(id)
            .orElseThrow { EntityNotFoundException("재고 알림", id) }
}
