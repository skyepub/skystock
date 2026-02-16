package com.skytree.skystock.controller

import com.skytree.skystock.dto.DashboardResponse
import com.skytree.skystock.dto.SupplierPerformanceResponse
import com.skytree.skystock.service.StatsService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/stats")
class StatsController(
    private val statsService: StatsService
) {
    @GetMapping("/dashboard")
    fun getDashboard(): DashboardResponse =
        statsService.getDashboard()

    @GetMapping("/supplier-performance")
    fun getAllSupplierPerformance(): List<SupplierPerformanceResponse> =
        statsService.getAllSupplierPerformance()

    @GetMapping("/supplier-performance/{supplierId}")
    fun getSupplierPerformance(@PathVariable supplierId: Int): SupplierPerformanceResponse =
        statsService.getSupplierPerformance(supplierId)
}
