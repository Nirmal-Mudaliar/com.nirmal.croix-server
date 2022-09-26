package com.nirmal.data.request

import com.nirmal.data.utils.ParentType

data class LikeUpdateRequest(
    val parentId: String,
    val parentType: Int
)
