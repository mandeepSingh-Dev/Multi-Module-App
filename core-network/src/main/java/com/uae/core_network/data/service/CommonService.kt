package com.uae.core_network.data.service

import com.uae.core_network.data.model.CMSResponse
import com.uae.core_network.data.model.CitiesResponse
import com.uae.core_network.data.model.FaqsResponse
import com.uae.core_network.data.model.FavouriteHostsListResponse
import com.uae.core_network.data.model.FavouriteUnFavResponse
import com.uae.core_network.data.model.ImageUploadResponse
import com.uae.core_network.data.model.InterestResponse
import com.uae.core_network.data.model.LanguageListResponse
import com.uae.core_network.data.model.UnreadNotificationResponse
import com.uae.core_network.data.model.requestBody.ReviewRequestBody
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.models.ApiResponse3
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.EndPoints
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface CommonService {

    @Multipart
    @POST(EndPoints.Common.IMAGE_UPLOAD)
    suspend fun uploadImage(
        @Part files: MultipartBody.Part,
        @Part(ApiConstants.folderName) folderName: RequestBody
    ): Response<ImageUploadResponse>


    @GET(EndPoints.Common.INTERESTS)
    suspend fun getInterests(): Response<InterestResponse>

    @GET(EndPoints.Common.CITIES)
    suspend fun getCities(
        @Query(ApiConstants.search) search : String?
    ): Response<CitiesResponse>

    @GET(EndPoints.Common.LANGUAGES)
    suspend fun getLanguages(): Response<LanguageListResponse>


    @POST(EndPoints.Common.REVIEW)
    suspend fun review( @Body reviewRequestBody: ReviewRequestBody?): Response<ApiResponse2>



    @GET(EndPoints.Common.NOTIFICATION_UNREAD)
    suspend fun getUnreadNotification(): Response<UnreadNotificationResponse>


    @GET(EndPoints.Common.CMS)
    suspend fun getCms(
        @Query(ApiConstants.type) type : Int?,
        @Query(ApiConstants.role) role : Int?
    ): Response<CMSResponse>


    @GET(EndPoints.Common.FAQS)
    suspend fun getFaqs(): Response<FaqsResponse>

    @PUT(EndPoints.Common.FCM_TOKEN)
    suspend fun uploadFCMToken(
        @Body body : Map<String,String?>
    ): Response<ApiResponse2>

    @POST(EndPoints.HomeUser.FAVOURITE)
    suspend fun favouriteHost(
        @Body map: Map<String, String?>?,
    ): Response<FavouriteUnFavResponse>


    @GET(EndPoints.HomeUser.FAVOURITE)
    suspend fun getFavouriteHost(
        @Query(ApiConstants.page) page: Int,
        @Query(ApiConstants.limit) limit: Int,
    ): Response<FavouriteHostsListResponse>



}