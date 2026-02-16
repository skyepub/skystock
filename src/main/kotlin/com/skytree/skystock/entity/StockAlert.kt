package com.skytree.skystock.entity

import jakarta.persistence.*
import java.time.LocalDateTime

enum class AlertLevel { NORMAL, WARNING, CRITICAL }

@Entity
@Table(name = "stock_alerts")
class StockAlert(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_alert_id")
    val id: Int = 0,

    @Column(name = "skymall_product_id", nullable = false, unique = true)
    var skymallProductId: Int,

    @Column(name = "skymall_product_name", nullable = false, length = 100)
    var skymallProductName: String,

    @Column(name = "safety_stock", nullable = false)
    var safetyStock: Int = 10,

    @Column(name = "reorder_point", nullable = false)
    var reorderPoint: Int = 20,

    @Column(name = "reorder_quantity", nullable = false)
    var reorderQuantity: Int = 50,

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_level", nullable = false)
    var alertLevel: AlertLevel = AlertLevel.NORMAL,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
