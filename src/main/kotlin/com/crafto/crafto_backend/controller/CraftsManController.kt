package com.crafto.crafto_backend.controller

import com.crafto.crafto_backend.service.CraftsManService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/crafts_man")
class CraftsManController(private val service: CraftsManService) {
}
