package com.crafto.crafto_backend

import com.crafto.crafto_backend.entity.Category
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.index.TextIndexDefinition

// this responsible for creating text indexes automatically when the app starts, this means we not need to create index variable in website in our database

@Configuration
class MongoConfig(private val mongoTemplate: MongoTemplate) {

    @PostConstruct
    fun initIndexes() {
        val indexOps = mongoTemplate.indexOps(Category::class.java)
        val index = TextIndexDefinition.builder()
            .onField("categoryName")
            .build()

        indexOps.createIndex(index)
    }
}
