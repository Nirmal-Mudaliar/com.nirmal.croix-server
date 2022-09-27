package com.nirmal.data.models

import com.nirmal.data.response.ProfileResponse
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(

    val email: String,
    val username: String,
    val password: String,
    val profileImageUrl: String,
    val bio: String,
    val githubUrl: String?,
    val instagramUrl: String?,
    val linkedinUrl: String?,
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val postCount: Int = 0,
    val skills: List<String> = listOf(),
    @BsonId
    val id: String = ObjectId().toString(),

)

