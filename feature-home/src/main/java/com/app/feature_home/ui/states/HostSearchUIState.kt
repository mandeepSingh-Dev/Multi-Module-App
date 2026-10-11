package com.app.feature_home.ui.states

import com.app.core_common.base.UIState
import com.app.feature_home.domain.model.HostFilterState
import com.uae.core_network.data.model.CitiesResponse

data class HostSearchUIState(
    val isLoading : Boolean = false,
    val isRefreshing : Boolean = false,
    val isCitiesLoading : Boolean = false,
    val filterState : HostFilterState? = HostFilterState(),
    val citiesList : List<CitiesResponse.CityData?>? = null,
) : UIState