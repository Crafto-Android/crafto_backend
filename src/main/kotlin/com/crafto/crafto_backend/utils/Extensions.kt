package com.crafto.crafto_backend.utils

import org.bson.types.ObjectId


fun String.toObjectId(): ObjectId = ObjectId(this)