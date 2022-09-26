package com.nirmal.data.request

import com.nirmal.data.utils.ParentType
import java.sql.Timestamp

data class CreateCommentRequest(
    val comment: String,
    val postId: String,
    val parentType: Int
)
