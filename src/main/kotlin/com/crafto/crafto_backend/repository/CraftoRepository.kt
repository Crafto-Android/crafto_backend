package com.crafto.crafto_backend.repository

import com.crafto.crafto_backend.entity.Category
import org.springframework.data.mongodb.repository.MongoRepository

interface CraftoRepository:MongoRepository<Category, String>