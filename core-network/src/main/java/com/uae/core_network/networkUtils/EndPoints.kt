package com.uae.core_network.networkUtils


object EndPoints {


    object Auth {
        const val LOGIN = "auth/login"
        const val OTP_VERIFY = "auth/otp/verify"
        const val PROFILE = "auth/profile"
        const val SIGN_UP = "auth/signup"
        const val LOGOUT = "auth/logout"
        const val PASSWORD = "auth/password"
    }

    object Leaderboard {
        const val LEADERBOARD = "user/leaderboard"
    }
    object Common {
        const val SERVER_PUBLIC_KEY = "server-public-key"
        const val IMAGE_UPLOAD = "upload"
        const val INTERESTS = "user/interest"
        const val CITIES = "city"
        const val LANGUAGES = "languages"
        const val REVIEW = "user/review"
        const val NOTIFICATION_UNREAD = "user/notification/unread"
        const val FAQS = "user/faq"
        const val FCM_TOKEN = "user/fcmToken"
        const val CMS = "cms"
        const val NOTIFICATION = "user/notification"
    }

    object REVIEW{
        const val REVIEW = "user/review"
    }

    object HomeUser {
        const val USER_HOME = "user/home"
        const val CHAT_LIST = "user/chatList"
        const val BLOCK_UNBLOCK = "user/block"
        const val GIFT = "user/gift"
        const val GIFT_SEND = "user/gift/send"
        const val FAVOURITE = "user/favourite"
        const val HOST = "user/host"
    }

    object STREAK {
        const val DAILY_STREAK = "user/daily-streak"
    }
    object Visitors {
        const val TRACK_VISITOR = "user/visitors/track"
        const val VISITORS = "user/visitors"
        const val VISITORS_DETAILS = "user/visitors/{${ApiConstants.visitorID}}"
    }

    object Earning {
        const val EARNING_DETAILS = "user/host-dashboard-summary"
        const val HOST_EARNING_TREND = "user/host-earnings-trend"
    }

    object Wallet {
        const val TRANSACTIONS = "user/transactions"
        const val WALLET = "user/wallet"
        const val COIN_PACKAGES = "user/coin-package"
        const val WALLET_RECHARGE = "user/wallet/recharge"
    }

    object HomeHost {
        const val HOST_HOME = "user/home"
         const val CHAT_LIST = "user/chatList"
        const val BLOCK_UNBLOCK = "user/block"
        const val ONLINE_OFFLINE = "user/status"
        const val DASHBOARD_STATS = "user/dashboard-stats"
    }

    object Bean {
        const val BEAN_PACKAGES = "user/bean-package"
    }

    object Withdrawal {
        const val WITHDRAWAL = "user/withdrawal"
        const val WITHDRAWAL_VERIFICATION_STATUS = "user/withdrawal/verification/status"
        const val WITHDRAWAL_SUMMARY = "user/withdrawal-summary"
        const val MOBILE_VERIFICATION_START = "user/withdrawal/verification/mobile/start"
        const val MOBILE_VERIFY = "user/withdrawal/verification/mobile/verify"
        const val FACE_VERIFICATION = "user/withdrawal/verification/face"
        const val FACE_VERIFICATION_COMPLETE = "user/withdrawal/verification/face/complete"
        const val VOICE_VERIFICATION = "user/withdrawal/verification/voice"
        const val VOICE_VERIFICATION_COMPLETE = "user/withdrawal/verification/voice/complete"
    }



    object CHAT {
        const val MESSAGES = "user/chat/message"
    }

    object Referral {
        const val REFERRAL = "user/referral"
        const val REFERRAL_APPLY = "user/referral/apply"
        const val REFERRAL_VIEWED = "user/referral/viewed"
    }

    object CALL {
        const val GENERATE_TOKEN = "user/generateToken"
        const val CALL_HISTORY = "user/call-history"
    }
    object STORY {
        const val STORY = "user/story"
    }

    val apisWithoutEncryption = listOf(
        Common.IMAGE_UPLOAD,
        Common.INTERESTS,
        Common.LANGUAGES,
        "logout",
    )
}


//enum class ApiWithoutTokens(val value : String){
//    FAQS(value = EndPoints.CMS.FAQS)
//}