package com.app.core_common.utils


object Constants {


    const val SPLASH_DELAY = 2000L
    const val YOE_SIZE = 60

    object FileFolders{
        const val POST_IMAGES = "Post images"
        const val IMAGE_FOLDER = "images"
    }

    const val HANGUP_PENDING_INTENT_CODE = 101
    object NotificationKeys{
        const val SIMPLE_NOTIFICATION_ID = 100
        const val SIMPLE_NOTIFICATION_CHANNEL_ID = "simple_notification_channel_id"
        const val SIMPLE_NOTIFICATION_CHANNEL_NAME = "simple_notification_channel"

        const val CALL_INITIATED_NOTIFICATION_ID = 101
        const val CALL_INITIATED_NOTIFICATION_CHANNEL_ID = "call_initiated_notification_channel_id"
        const val CALL_INITIATED_NOTIFICATION_CHANNEL_NAME = "call_initiated_notification_channel"


        const val ONGOING_VIDEO_CALL_NOTIFICATION_ID = 102
        const val ONGOING_VIDEO_CALL_NOTIFICATION_CHANNEL_ID = "ongoing_video_call_notification_channel_id"
        const val ONGOING_VIDEO_CALL_NOTIFICATION_CHANNEL_NAME = "ongoing_video_call_notification_channel"
    }

    object IntentKeys{
        const val `401_UNAUTHORIZE_ACTION` = "401_unauthorize_action"

        const val CHAT_ID = "chat_id"
        const val PARTNER_ID = "partener_id"
        const val CHANNEL_NAME = "channel_name"
        const val BOOKING_ID = "booking_id"


        const val ACTION_LEAVE_CALL = "action_leave_call"

        const val PERSON_NAME = "person_name"
        const val CALL_TYPE = "call_type"
        const val PERSON_IMAGE = "person_image"
        const val IS_FROM_INCOMING_CALL = "is_from_incoming_call"
        const val CALL_ID = "call_id"

    }

}

object DateFormats{
    const val DATE_FORMAT_1 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX"
    const val DATE_FORMAT_2 = "yyyy-MM-dd"
    const val DATE_FORMAT_3 = "E"
    const val DATE_FORMAT_4 = "dd MMM, yyy"
    const val DATE_FORMAT_5 = "yyyy-MM-dd'T'HH:mm:ss.SSSX"


    const val DATE_FORMAT_6 = "MMM dd, yyyy"
    const val DATE_FORMAT_7 = "MM/dd/yyyy"
    const val DATE_FORMAT_8 = "dd/MM/yyyy"
    const val DATE_FORMAT_9 = "dd MMM, yyyy"
    const val DATE_FORMAT_10 = "dd MMM"
    const val DATE_FORMAT_11 = "dd, MMM yyyy"
    const val DATE_FORMAT_12 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    const val DATE_FORMAT_13 = "EEE MMM dd yyyy"
    const val DATE_FORMAT_14 = "dd-MMM-yyyy"
    const val DATE_FORMAT_15 = "EEE MMM dd yyyy HH:mm:ss 'GMT'Z"
    const val DATE_FORMAT_16 = "EEE MMM dd yyyy HH:mm:ss"

    const val DATE_FORMAT_17 = "dd MMM yyyy"
    const val DATE_FORMAT_18 = "EEE MMM dd yyyy h:mm:ss a"
    const val DATE_FORMAT_19 = "MMMM yyyy"
    const val DATE_FORMAT_20 = "dd-MMMM-yyyy"
    const val DATE_FORMAT_21 = "EEEE MMMM dd yyyy"
    const val DATE_FORMAT_22 = "MMMM dd yyyy"
    const val DATE_FORMAT_23 = "EEEE, d MMMM, a h:mm yyyy"

    const val DATE_FORMAT_24 = "EEE, MMM d, h:mm a yyyy"
    const val DATE_FORMAT_25 = "dd MMMM yyyy"
    const val DATE_FORMAT_26 = "d MMMM yyyy"


    const val DATE_FORMAT_YEAR = "YYYY"
    const val DATE_FORMAT_MONTH = "MM"
    const val DATE_FORMAT_MONTH_NAME = "MMM"
    const val DATE_FORMAT_DAY = "dd"
    const val DATE_FORMAT_WEEK = "EEE"


    const val TIME_FORMAT_1 = "hh:mm a"
    const val TIME_FORMAT_2 = "hh:mm:ss a"
    const val TIME_FORMAT_3 = "hh:mm"
    const val TIME_FORMAT_4 = "HH:mm"
    const val DATE_TIME_FORMAT = "yyyy-MMM-dd hh:mm aa"
    const val DATE_TIME_FORMAT_2 = "yyyy-MMM-dd hh:mm"
    const val DATE_TIME_FORMAT_3 = "dd MMM yyyy hh:mm aa"
    const val PM = "pm"
    const val AM = "am"



}
