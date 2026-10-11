package com.app.core_common.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DatastoreConstants {

    const val UAE_ASSIST_PREFERENCE = "UAE_Assist_Preferences"



    val USER_DATA = stringPreferencesKey("user_data")
    val USER_TYPE = intPreferencesKey("user_type")
    val TOKEN = stringPreferencesKey("token")

    val AMAZON_S3_CREDENTIALS = stringPreferencesKey("amazon_s3_credentials")
    val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
    val LAST_LOCATION = stringPreferencesKey("last_location")
    val SERVER_PUBLIC_KEY = stringPreferencesKey("server_public_key")
    val IS_ONLINE = booleanPreferencesKey("is_online")

}