package com.app.feature_block.remote.model


data class BlockUnblockResponse(
    val count: Int?,
    val `data`: Data?,
    val message: String?,
    val status: Int?
) {
    data class Data(
        val id: String?,
        val isBlock: Boolean?
    )
}