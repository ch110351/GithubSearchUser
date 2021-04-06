package com.example.githubsearchuser.data


import com.google.gson.annotations.SerializedName
/**
 * response
 */
data class SearchUserResponse(
    @SerializedName("total_count")
    var totalCount: Long,
    @SerializedName("incomplete_results")
    var incompleteResults: Boolean,
    @SerializedName("items")
    var items: List<UserItem>
)

data class UserItem(
    @SerializedName("id")
    var id: Long,
    @SerializedName("login")
    var login: String,
    @SerializedName("avatar_url")
    var avatarUrl: String,
    @SerializedName("name")
    var name: String
)

data class UserInfo(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("bio")
    val bio: String,
    @SerializedName("blog")
    val blog: String,
    @SerializedName("id")
    val id: Long,
    @SerializedName("location")
    val location: String,
    @SerializedName("login")
    val login: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("site_admin")
    val siteAdmin: Boolean
)

