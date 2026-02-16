package com.skytree.skystock.entity

import jakarta.persistence.*

@Entity
@Table(name = "purchase_items")
class PurchaseItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_item_id")
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    var purchaseOrder: PurchaseOrder? = null,

    @Column(name = "skymall_product_id", nullable = false)
    var skymallProductId: Int,

    @Column(name = "skymall_product_name", nullable = false, length = 100)
    var skymallProductName: String,

    @Column(nullable = false)
    var quantity: Int,

    @Column(name = "unit_cost", nullable = false)
    var unitCost: Double,

    @Column(nullable = false)
    var subtotal: Double = 0.0
)
