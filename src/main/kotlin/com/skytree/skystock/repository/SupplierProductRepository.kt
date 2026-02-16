package com.skytree.skystock.repository

import com.skytree.skystock.entity.SupplierProduct
import org.springframework.data.jpa.repository.JpaRepository

interface SupplierProductRepository : JpaRepository<SupplierProduct, Int> {

    fun findBySupplierId(supplierId: Int): List<SupplierProduct>

    fun findBySkymallProductId(skymallProductId: Int): List<SupplierProduct>

    fun findBySupplierIdAndIsActiveTrue(supplierId: Int): List<SupplierProduct>
}
