package com.gautam.digitalcourierdesk.data_classes

data class entity(
    val name: String="",
    val category: String=""
)
data class entities(
    val entities: List<entity>
)

