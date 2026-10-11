package com.example.storydemo

import com.google.gson.annotations.SerializedName
import java.util.UUID

/**
 * Matches the API's overlay shape:
 * { type: "emoji", x: 0.4796, y: 0.8497, scale: 1, rotation: 0, value: "🤩" }
 *
 * x, y        : normalized 0..1 — fraction of the media's width/height
 *               where the overlay's CENTER sits.
 * scale       : multiplier, 1.0 = original size.
 * rotation    : degrees.
 * color / fontSize / backgroundColor : optional, only meaningful for "text".
 *
 * `id` is client-side only (used for drag/select bookkeeping in the
 * editor) and is excluded from serialization via @Transient, so it is
 * never sent to the server.
 */
data class Overlay(
    @Transient val id: String = UUID.randomUUID().toString(),
    @SerializedName("type") val type: String = "emoji",       // "emoji" | "text"
    @SerializedName("x") val x: Double = 0.5,
    @SerializedName("y") val y: Double = 0.5,
    @SerializedName("scale") val scale: Double = 1.0,
    @SerializedName("rotation") val rotation: Double = 0.0,
    @SerializedName("value") val value: String = "",
    @SerializedName("color") val color: String? = null,
    @SerializedName("fontSize") val fontSize: Double? = null,
    @SerializedName("backgroundColor") val backgroundColor: String? = null
)
