package com.app.feature_home.domain.model

import com.uae.core_network.data.model.Interest
import kotlin.math.min

data class HostFilterState(
    val gender: Int? = null,
    val isOnline: Int? = null,
    val ageRange: ClosedFloatingPointRange<Float>? = null,
    val location: String? = null,
    val interests: List<Interest>? = null,
    val minimumRating: String? = null
) {


    fun getResetState() = HostFilterState(interests = interests?.map { it.copy(isSelected = false) })

    fun checkStateIsPopulated(): Boolean {

        return if (gender != null ||
            isOnline != null ||
            (ageRange != null) ||
            !location.isNullOrEmpty() ||
            (interests?.filter { it.isSelected }?.map { it._id }?.isNotEmpty() == true) ||
            minimumRating != null
        ){
            true
        }else{
            false
        }


    }
}