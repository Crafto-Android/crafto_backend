package com.crafto.crafto_backend.database.repository

import com.crafto.crafto_backend.database.entity.Category
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface CategoryRepository:MongoRepository<Category, ObjectId>