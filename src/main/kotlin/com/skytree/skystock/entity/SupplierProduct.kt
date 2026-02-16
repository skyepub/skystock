package com.skytree.skystock.entity

import jakarta.persistence.*

@Entity
@Table(name = "supplier_products")
class SupplierProduct(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_product_id")
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    var supplier: Supplier,

    @Column(name = "skymall_product_id", nullable = false)
    var skymallProductId: Int,

    @Column(name = "skymall_product_name", nullable = false, length = 100)
    var skymallProductName: String,

    @Column(name = "unit_cost", nullable = false)
    var unitCost: Double,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true
)
