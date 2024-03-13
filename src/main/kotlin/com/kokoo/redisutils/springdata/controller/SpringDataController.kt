package com.kokoo.redisutils.springdata.controller

import com.kokoo.redisutils.springdata.domain.Dummy
import com.kokoo.redisutils.springdata.repository.DummyRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(value = ["/spring-data"])
class SpringDataController(
    private val dummyRepository: DummyRepository
) {

    @PostMapping("")
    fun setSpringData() {
        val uuid = UUID.randomUUID().toString()
        dummyRepository.save(Dummy(id = uuid, field = "${uuid}-field"))
    }

    @GetMapping("")
    fun getSpringData(): MutableIterable<Dummy> {
        return dummyRepository.findAll()
    }
}