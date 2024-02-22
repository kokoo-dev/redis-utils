package com.kokoo.redisutils.lock.controller

import com.kokoo.redisutils.lock.LockService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/lock"])
class LockController(
    private val lockService: LockService
) {

    @GetMapping("")
    fun lock(@RequestParam(value = "lockKey") lockKey: String,
             @RequestParam(value = "counter") counter: Int) {
        lockService.lock(lockKey, counter)
    }
}