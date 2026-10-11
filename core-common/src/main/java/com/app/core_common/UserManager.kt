package com.app.core_common

import android.util.Log
import com.app.core_common.datastore.DatastoreManager
import com.app.core_common.datastore.DatastoreConstants
import com.app.core_common.utils.enums.UserType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.Required
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(private val datastoreManager: DatastoreManager) {

    suspend fun saveUserData(userData : String?){
        userData?.let {
            datastoreManager.write(DatastoreConstants.USER_DATA, it)
        }
    }

    suspend fun saveUserToken(token : String?){
        token?.let {
            datastoreManager.write(DatastoreConstants.TOKEN, token)
        }
    }


    suspend fun saveUserType(userType : Int?){
        userType?.let {
            datastoreManager.write(DatastoreConstants.USER_TYPE, it)
        }
    }

     suspend fun getUserType() = datastoreManager.read(DatastoreConstants.USER_TYPE, UserType.USER.value).first()
     suspend fun getUserDataString() = datastoreManager.read(DatastoreConstants.USER_DATA,"").first()
/*
    suspend fun getUserData(): ProfileDetailsResponse.UserData? {

        Log.d("dlcmcldvmdl", "getUserDat 2 a")
        val userDataStr = getUserDataString()
        if(userDataStr.isNullOrEmpty()) return null
        return try {
             userDataStr.fromJson<ProfileDetailsResponse.UserData>()
        } catch(e: Exception) {
            null
        }
    }*/

     suspend fun getUserToken() = datastoreManager.read(DatastoreConstants.TOKEN,"").first()

//     suspend fun getUserType() = datastoreManager.read(DatastoreConstants.USER_TYPE, UserType.invalid()).first()


    suspend fun isLoggedIn() : Boolean{
        return try {
            val userData = getUserDataString()
            val userToken = getUserToken()

            Log.d("fvmknvkfnv", userToken.toString())
            Log.d("fvmknvkfnv", userData.toString())

            userData.isNotEmpty() && userToken.isNotEmpty()
        }catch (e: Exception){
            Log.d("dkvkvnfkvnf", e.message.toString())
            false
        }
    }

    suspend fun clearLocalData(){
        datastoreManager.clearDataOfKey(DatastoreConstants.USER_DATA)
        datastoreManager.clearDataOfKey(DatastoreConstants.TOKEN)
        datastoreManager.clearDataOfKey(DatastoreConstants.USER_TYPE)
        datastoreManager.clearDataOfKey(DatastoreConstants.IS_ONLINE)

    }

//    suspend fun getUserDetails(): ProfileResponse.Data.UserDetails? {
//        return getUserDataString().fromJson<ProfileResponse.Data.UserDetails>()
//
//    }

    suspend fun saveLastUserLocation(latLngJson : String?){
        latLngJson?.let {
            datastoreManager.write(DatastoreConstants.LAST_LOCATION, latLngJson)
        }
    }

    suspend fun  removeLastLocation(){
        datastoreManager.clearDataOfKey(DatastoreConstants.LAST_LOCATION)
    }

    suspend fun getLastUserLocation(): String {

        return datastoreManager.read(DatastoreConstants.LAST_LOCATION,"").first()
    }
    suspend fun isGuestUser() = !isLoggedIn()

    suspend fun setOnBoardingCompleteStatus(){
        datastoreManager.write(DatastoreConstants.IS_ONBOARDING_COMPLETED, true)
    }
    suspend fun isOnboardingCompleted() = datastoreManager.read(DatastoreConstants.IS_ONBOARDING_COMPLETED, false).first()

}