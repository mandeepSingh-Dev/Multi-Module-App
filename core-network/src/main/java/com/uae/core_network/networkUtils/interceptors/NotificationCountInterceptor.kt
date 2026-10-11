package com.audix.data.remote.networkUtils.interceptors

/*
class NotificationCountInterceptor @Inject constructor(private val appDatastore: AppDatastore) : Interceptor{


    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {

        CoroutineScope(Dispatchers.Main).launch {

            try{

                appDatastore.read(DatastoreConstants.USER_DATA,"").first().let {
                    val userData = it.fromJson<UserData>()

                    //If user token is not empty then we will paas token to header otherwise return from try block
                    try {
                        val authToken = userData.auth_token
                        if (authToken?.isEmpty() == true || authToken?.isBlank() == true || authToken == null) {
                            return@let
                        }else{
                            hitUnreadNotificationCountsApi(authToken)
                        }
                    }catch (e:Exception){}
                }
            }catch (e:Exception){

            }



                }

        return chain.proceed(chain.request())
    }


    private suspend fun hitUnreadNotificationCountsApi(authToken : String?){
        try{
        val commonServices = ManualRetrofit.getService(CommonServices::class.java)

        val response = commonServices?.getUnreadNotificationCounts(userToken = authToken)

        if(response?.isSuccessful == true){
            if(response.body() != null){
                Log.d("fbmkf333333mvf",response.body().toString())

                val notificationCount = response.body()?.count ?: 0

                val previousCount = appDatastore.read(DatastoreConstants.NOTIFICATION_COUNT,0).first() //Previous stored notification count.

                //Only store count, If count-from-api do not matched with alread stored count in Datastore.
                if(previousCount != notificationCount){
                    appDatastore.write(DatastoreConstants.NOTIFICATION_COUNT, notificationCount)
                }

            }
        }else{
            Log.d("fbmkf333333mvf",response?.code().toString())

        }
    }
    catch (e:Exception){
        Log.d("fbmkf333333mvf",e.message.toString())
    }
    }

}*/
