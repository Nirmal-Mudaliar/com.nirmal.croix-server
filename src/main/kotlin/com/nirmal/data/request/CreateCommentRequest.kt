package com.nirmal.data.request

import java.sql.Timestamp

data class CreateCommentRequest(
    val comment: String,
    val postId: String,
)
