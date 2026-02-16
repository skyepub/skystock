package com.skytree.skystock.repository

import com.skytree.skystock.entity.PurchaseItem
import org.springframework.data.jpa.repository.JpaRepository

interface PurchaseItemRepository : JpaRepository<PurchaseItem, Long> {

    fun findByPurchaseOrderId(purchaseOrderId: Int): List<PurchaseItem>
}
