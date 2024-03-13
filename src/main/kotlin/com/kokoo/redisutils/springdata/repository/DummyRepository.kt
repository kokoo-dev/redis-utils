package com.kokoo.redisutils.springdata.repository

import com.kokoo.redisutils.springdata.domain.Dummy
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DummyRepository: CrudRepository<Dummy, String> {
}