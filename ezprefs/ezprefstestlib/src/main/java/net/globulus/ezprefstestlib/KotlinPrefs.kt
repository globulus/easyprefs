package net.globulus.ezprefstestlib

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import net.globulus.easyprefs.IEasyPrefs
import net.globulus.easyprefs.annotation.*
import java.util.*

/**
 * Created by gordanglavas on 21/11/2017.
 */

// Add fields from this class to EasyPrefs, but only those annotated with @Pref.
// Add fields directly to EasyPrefs, and not inside an enclosing static class.
@PrefClass(autoInclude = false, staticClass = false, origin = true)
class KotlinPrefs {

    internal var test1: Int = 0 // This field won't be included

    @Pref
    internal var test2: String? = null // This field will be included.

    @Pref(key = "notTest3")
    internal var test3: Long = 0 // This field will be included with a custom key.

    @Pref(key = "database_plain_text", nullable = true, clear = false)
    internal var databasePlainText: String? = null
    @Pref(key = "enrolled_sites")
    internal var enrolledSites: Set<String>? = null
    @Pref(key = "material_tool_tips")
    internal var dismissedTooltips: Set<String>? = null
    @Pref(key = "inactive_devices")
    internal var inactiveDevices: Set<String>? = null
    @Pref
    internal var enrollmentMedications: Set<String>? = null

    @Pref(key = "bluetooth_address", nullable = true, clear = false)
    internal var msbAddress: String? = null

    @Pref(key = "ble_meters")
    internal var bleMeterAddresses: Set<String>? = null
    @Pref(key = "ble_meter_should_get_units")
    internal var bleMeterShouldGetUnits: Boolean = false

    @Pref(key = "comm_error", comment = "Simulated communication error preference")
    internal var commErrorPreference: Boolean = false
    @Pref(key = "meter_available", comment = "Override meter available preference")
    internal var meterAvailablePreference: Boolean = false

    @Pref(key = "push_enabled")
    internal val pushEnabled = true
    @Pref(key = "viewed_dayview_tutorial")
    internal val dayViewShowtutorial = true
    @Pref(key = "dont_show_this_message_again_video_tutorial")
    internal var dontShowThisMessageAgainVideoTutorial: Boolean = false

    @Pref(key = "app_version", nullable = true)
    internal var appVersion: String? = null
    @Pref(key = COOKIE_KEY, nullable = true)
    internal var cookie: String? = null
    @Pref(key = PROVIDER_COOKIE_KEY, nullable = true, clear = false)
    internal var providerCookie: String? = null
    @Pref(key = "oauth_token_secret", nullable = true)
    internal var oAuthTokenSecret: String? = null
    @Pref(key = "oauth_token", nullable = true)
    internal var oAuthToken: String? = null

    @Pref(key = "hypo_show_tooltip")
    internal val hypoShowTooltip = true
    @Pref(key = "foodsearch_show_tooltip")
    internal val foodSearchToolTip = true
    @Pref(key = "quickadd_show_tooltip")
    internal val quickAddToolTip = true
    @Pref(key = "mealtimes_show_tooltip")
    internal val mealTimesToolTip = true

    @Pref(key = "server_offset")
    internal var serverOffset: Long = 0
    @Pref(key = "logged_in")
    internal var finishedLoggingIn: Boolean = false
    @Pref(key = "writing_audio_samples_to_disk")
    internal var writingAudioSamplesToDisk: Boolean = false

    @Pref(key = "did_migrate_prefs", autoset = "true")
    internal var didMigrate: Boolean = false
    @Pref(key = "is_deprecated")
    internal var isDeprecated: Boolean = false

    @Pref(key = "medication_references_upgraded", comment = "Medication References upgraded")
    internal var medicationReferencesUpgraded: Boolean = false
    @Pref(key = "cvs_banner_closed", autoset = "true", comment = "CVS Banner closed")
    internal var cvsBannerClosed: Boolean = false
    @Pref(key = "terms_of_service_accepted", clear = false)
    internal var termsOfUseAccepted: Boolean = false
    @Pref(key = "history_dialog")
    internal var showDialogHistory: Boolean = false
    @Pref(key = "last_logged_in_account", nullable = true)
    internal var lastLoggedInAccount: String? = null
    @Pref
    internal var fragmentTypeString: String? = null
    @Pref
    internal var signUpToC4cWebsite: Boolean = false
    @Pref
    internal var testMeterInRelease: Boolean = false
    @Pref
    internal var exerciseBarGraphOpen: Boolean = false
    @Pref
    internal var insulinBarGraphOpen: Boolean = false
    @Pref
    internal var carbBarGraphOpen: Boolean = false
    @Pref
    internal var getStartedScreenShown: Boolean = false
    @Pref
    internal var enrollmentStartedTimestamp: String? = null
    @Pref
    internal var cppStartedTimestamp: String? = null

    @Pref(function = NullableDateLong::class, rawDefaultValue = "0L", clear = false)
    internal var appOpenTimestamp: Date? = null
    @Pref
    internal var enrollmentStarted: Boolean = false


    @PrefClass(autoInclude = false)
    class CareLink {

        @Pref(key = "carelink_username_key", comment = "Carelink Username for Kiosk Minimed Connect feature")
        internal var username: String? = null
        @Pref(key = "in_carelink_flow")
        internal var inFlow: Boolean = false
        @Pref(key = "carelink_serial_number")
        internal var serialNumber: String? = null

        @PrefClass
        class CareLinkPumpIndices {
            @Pref(key = "carelink_last_history_page_number")
            internal var lastHistoryPageNumber: Int = 0
            @Pref(key = "carelink_last_glucose_history_page_number")
            internal var lastGlucoseHistoryPageNumber: Int = 0
            @Pref(key = "carelink_request_partial_sync_months")
            internal var requestedPartialSyncMonths: Int = 0
        }

        companion object {

            private const val CARELINK_TEST_METER = "carelink_test_meter"

            @PrefMethod
            fun putCareLinkTest(context: Context, test: Boolean) {
                IEasyPrefs.get().putPreferencesField(context, CARELINK_TEST_METER, test)
            }

            @PrefMethod
            fun getCareLinkTest(context: Context): Boolean {
                return if (BuildConfig.DEBUG) {
                    IEasyPrefs.get().getPreferencesField(context, CARELINK_TEST_METER, false)
                } else false
            }

            fun clear(editor: SharedPreferences.Editor) {
                editor.remove(CARELINK_TEST_METER)
            }
        }
    }

    @PrefClass(autoInclude = false)
    class Kiosk {

        @Pref(key = "kiosk_is_kiosk", clear = false)
        internal var isKiosk: Boolean = false
        @Pref(key = "kiosk_accept_terms_of_use", clear = false)
        internal var acceptTermsOfUse: Boolean = false
        @Pref(key = "kiosk_mrn_required", clear = false)
        internal var mrnRequired: Boolean = false
        @Pref(key = "kiosk_mrn_show", clear = false)
        internal var mrnShow: Boolean = false
        @Pref(key = "kiosk_phone_number_required", clear = false)
        internal var phoneNumberRequired: Boolean = false
        @Pref(key = "kiosk_phone_number_show", clear = false)
        internal var phoneNumberShow: Boolean = false
        @Pref(key = "kiosk_type_of_diabetes_required", clear = false)
        internal var typeOfDiabetesRequired: Boolean = false
        @Pref(key = "kiosk_type_of_diabetes_show", clear = false)
        internal var typeOfDiabetesShow: Boolean = false
        @Pref(key = "kiosk_print_format", clear = false)
        internal val printFormat = 30
        @Pref(key = "kiosk_require_email", clear = false)
        internal val requireEmail = true
        @Pref(key = "kiosk_is_new_user", clear = false)
        internal var isNewUser: Boolean = false
        @Pref(key = "in_kiosk_device_sync", clear = false)
        internal var inDeviceSync: Boolean = false

        companion object {
            private val GK_PROVIDER_KEY = "kiosk_gk_provider"
            private val KIOSK_TIMEOUT_RESUME_TIMESTAMP = "kiosk_timeout_resume_timestamp"
            private val KIOSK_PRINT_OPTIONS = "kiosk_print_options"

            @PrefMethod
            fun putKioskTimeoutResumeTimestamp(context: Context, timestamp: Long) {
                IEasyPrefs.get().putPreferencesField(context, KIOSK_TIMEOUT_RESUME_TIMESTAMP, timestamp)
            }

            @PrefMethod
            fun getKioskTimeoutResumeTimestamp(context: Context): Long {
                return IEasyPrefs.get().getPreferencesField(context, KIOSK_TIMEOUT_RESUME_TIMESTAMP, System.currentTimeMillis())
            }
        }

    }

    @PrefClass
    class Mids {
        internal val shouldShowFbgDefinition = true
        internal var waitingForReminders: Boolean = false
        internal val firstTimeStarted = true
        @Pref(function = NonNullDateLong::class, rawDefaultValue = "0L")
        internal var titrationPeriodStart: Date? = null
        @Pref(function = NullableDateLong::class, rawDefaultValue = "0L")
        internal var nextCheckupDate: Date? = null
        @Pref(function = NullableDateLong::class, rawDefaultValue = "0L")
        internal var lastCheckupDate: Date? = null
        @Pref(nullable = true)
        internal var lastPlanGuid: String? = null
        internal var lastStartingDose: Int = 0
        @Pref(nullable = true)
        internal var lastInsulinType: String? = null
        internal var shownRevisedDialog: Boolean = false
        internal var shownSuspendedDialog: Boolean = false
        internal var checkupStarted: Boolean = false
        internal var hypoCheckupStarted: Boolean = false
        internal var shouldCheckMidsHypoOnStart: Boolean = false
        internal var insulinReminderDisabledByProgram: Boolean = false
        internal var triggeredAlarms: Set<String>? = null
    }

    @PrefClass
    class MidsTest {
        @Pref(function = NonNullDateLong::class, rawDefaultValue = "0L")
        internal var currentTime: Date? = null
        internal var hideReminderWhenRunningTest: Boolean = false
    }


    @PrefClass(autoInclude = false)
    class React {

        @Pref(oldKey = "react_day_count")
        internal val dayCount = 30
        @Pref(oldKey = "react_stats_swiper_index")
        internal var statsIndex: Int = 0
        @Pref(oldKey = REACT_GRAPHS_END_DATE_STRING, addClearMethod = true, nullable = true)
        internal var graphsEndDateString: String? = null

        companion object {

            private val REACT_ONBOARDING_INDEX_1 = "react_onboarding_index_1"
            private val REACT_ONBOARDING_INDEX_2 = "react_onboarding_index_2"
            private val REACT_ONBOARDING_INDEX_3 = "react_onboarding_index_3"
            private val REACT_ONBOARDING_INDEX_4 = "react_onboarding_index_4"
            private val REACT_COMPARISONS_SHOW_0 = "react_comparisons_show_0"
            private val REACT_COMPARISONS_SHOW_1 = "react_comparisons_show_1"
            private val REACT_COMPARISONS_SHOW_2 = "react_comparisons_show_2"
            private val REACT_COMPARISONS_SHOW_3 = "react_comparisons_show_3"
            private val REACT_GLUCOSE_DATA_SOURCE = "react_glucose_data_source"
            val REACT_DATA_SOURCE_BG = "bg"
            val REACT_DATA_SOURCE_EGV = "cgm"
            val REACT_DATA_SOURCE_NOT_SPECIFIED = "not_specified"
            const val REACT_GRAPHS_END_DATE_STRING = "react_graphs_end_date_string"

            @PrefMethod
            fun resetReactOnboardingValues(context: Context) {
                setReactOnboardingValueAtIndex(context, 1, 0)
                setReactOnboardingValueAtIndex(context, 2, 0)
                setReactOnboardingValueAtIndex(context, 3, 0)
                setReactOnboardingValueAtIndex(context, 4, 0)
            }

            @PrefMethod
            fun getReactOnboardingValueAtIndex(context: Context, index: Int): Int {
                when (index) {
                    1 -> return IEasyPrefs.get().getPreferencesField(context, REACT_ONBOARDING_INDEX_1, 0)
                    2 -> return IEasyPrefs.get().getPreferencesField(context, REACT_ONBOARDING_INDEX_2, 0)
                    3 -> return IEasyPrefs.get().getPreferencesField(context, REACT_ONBOARDING_INDEX_3, 0)
                    4 -> return IEasyPrefs.get().getPreferencesField(context, REACT_ONBOARDING_INDEX_4, 0)
                    else -> return -1
                }
            }

            @PrefMethod
            fun setReactOnboardingValueAtIndex(context: Context, index: Int, value: Int) {
                when (index) {
                    1 -> IEasyPrefs.get().putPreferencesField(context, REACT_ONBOARDING_INDEX_1, value)
                    2 -> IEasyPrefs.get().putPreferencesField(context, REACT_ONBOARDING_INDEX_2, value)
                    3 -> IEasyPrefs.get().putPreferencesField(context, REACT_ONBOARDING_INDEX_3, value)
                    4 -> IEasyPrefs.get().putPreferencesField(context, REACT_ONBOARDING_INDEX_4, value)
                    else -> {
                    }
                }
            }

            @PrefMethod
            fun getReactComparisonsShowValue(context: Context, index: Int): Boolean {
                when (index) {
                    0 -> return IEasyPrefs.get().getPreferencesField(context, REACT_COMPARISONS_SHOW_0, false)
                    1 -> return IEasyPrefs.get().getPreferencesField(context, REACT_COMPARISONS_SHOW_1, false)
                    2 -> return IEasyPrefs.get().getPreferencesField(context, REACT_COMPARISONS_SHOW_2, false)
                    3 -> return IEasyPrefs.get().getPreferencesField(context, REACT_COMPARISONS_SHOW_3, false)
                    else -> return false
                }
            }

            @PrefMethod
            fun setReactComparisonsShowValue(context: Context, index: Int, value: Boolean) {
                when (index) {
                    0 -> IEasyPrefs.get().putPreferencesField(context, REACT_COMPARISONS_SHOW_0, value)
                    1 -> IEasyPrefs.get().putPreferencesField(context, REACT_COMPARISONS_SHOW_1, value)
                    2 -> IEasyPrefs.get().putPreferencesField(context, REACT_COMPARISONS_SHOW_2, value)
                    3 -> IEasyPrefs.get().putPreferencesField(context, REACT_COMPARISONS_SHOW_3, value)
                    else -> {
                    }
                }
            }

            @PrefMethod
            fun clearReactComparisonsShowValue(editor: SharedPreferences.Editor) {
                editor.remove(REACT_COMPARISONS_SHOW_0)
                editor.remove(REACT_COMPARISONS_SHOW_1)
                editor.remove(REACT_COMPARISONS_SHOW_2)
                editor.remove(REACT_COMPARISONS_SHOW_3)
            }

            @PrefMethod
            fun clearIsiInfo(context: Context, editor: SharedPreferences.Editor) {
                val keyList = IEasyPrefs.get().getPreferencesField(context, ISI_DONT_SHOW_AGAIN_KEYLIST, null as String?)
                if (keyList != null && !keyList.isEmpty()) {
                    val arr = keyList.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                    for (str in arr) {
                        editor.remove(str)
                    }
                }
            }


            internal fun clearOnboardingIndices(editor: SharedPreferences.Editor) {
                editor.remove(REACT_ONBOARDING_INDEX_1)
                editor.remove(REACT_ONBOARDING_INDEX_2)
                editor.remove(REACT_ONBOARDING_INDEX_3)
                editor.remove(REACT_ONBOARDING_INDEX_4)
            }

            internal fun clear(editor: SharedPreferences.Editor) {
                editor.remove(REACT_GLUCOSE_DATA_SOURCE)
            }
        }
    }

    class NullableDateLong : PrefFunction<Date, Long> {

        override fun put(date: Date?): Long? {
            return date?.time ?: 0L
        }

        override fun get(time: Long?): Date? {
            return if (time?.equals(0L) == true) {
                null
            } else Date(time!!)
        }
    }

    class NonNullDateLong : PrefFunction<Date, Long> {

        override fun put(date: Date): Long? {
            return date.time
        }

        override fun get(time: Long?): Date {
            return Date(time ?: 0L)
        }
    }

    companion object {

        private var sSecureInstance: SharedPreferences? = null

        //
        // PrefMaster annotation is placed on a public static method returning an instance of
        // SharedPreferences, and this method will be used in generated code to retrieve the
        // SharedPreferences instance we want to work with.
        //
        @PrefMaster
        fun getSecureInstance(context: Context): SharedPreferences? {
            if (sSecureInstance == null) {
                sSecureInstance = PreferenceManager.getDefaultSharedPreferences(context)
            }
            return sSecureInstance
        }


        private val METER_KEY = "meter"
        private val DIAGNOSTICS_MODE_KEY = "diagnostics_mode"
        private const val COOKIE_KEY = "cookie_key"
        private val COOKIES_KEY = "cookies_key"
        private const val PROVIDER_COOKIE_KEY = "provider_cookie_key"
        private val LAST_SYNC_KEY = "last_sync_date"

        private val EFAX_1 = "efax_1"
        private val EFAX_2 = "efax_2"
        private val EFAX_3 = "efax_3"

        private val DEVICE_GUID_KEY = "device_guid"
        private val GK_USER_KEY = "gkuser"
        private val PRO_CONNECT_CODE = "pro_connect_code"
        private val SIGNUP_PROCONNECT_PROVIDER_NAME = "signup_proconnect_provider_name"
        private val PERSISTENT_BANNER = "persistent_banner"
        private val IS_DEPRECATED = "is_deprecated"
        private val PROVIDER_GROUP_SITES = "provider_group_sites"
        private val ISI_DONT_SHOW_AGAIN_KEYLIST = "isi_dont_show_again_keylist"

        private val MIGRATED_GET_PARAMS = "migratedGetParams"
        private val GET_PARAMS = "getParams"

        private val MSB_VS_MSC_DIALOG = "msb_vs_msc_dialog"
        private val MSB_VS_MSC_USER_SELECTED = "msb_vs_msc_user_touched"
        private val ISI_DONT_SHOW_AGAIN = "isi_dont_show_again"
        private val SYNC_EXCEPTION = "sync_exception"

        private val RECENT_INSULIN = "recent_insulin"
        private val CUSTOM_INSULIN = "custom_insulin"
        private val RECENT_MEDICATION = "recent_medication"
        private val CUSTOM_MEDICATION = "custom_medication"


        val INDEX_LOGBOOK = 4

        @PrefMethod
        fun removePartValueFromPreferencesField(context: Context, key: String, value: String?) {
            val set = HashSet<String>()// IEasyPrefs.get().getPreferencesField(context, key, new HashSet<>());
            if (value != null) {
                for (type in set) {
                    if (type.contains(value)) {
                        set.remove(type)
                        break
                    }
                }
            }
            IEasyPrefs.get().putPreferencesField(context, key, set)
        }


        @PrefMethod
        fun putSyncException(context: Context, exception: Exception?) {
            IEasyPrefs.get().putPreferencesField(context, SYNC_EXCEPTION, exception?.javaClass?.getName())
        }

        @PrefMethod
        fun getSyncException(context: Context): String {
            return IEasyPrefs.get().getPreferencesField(context, SYNC_EXCEPTION, null as String?)
        }

        //
        // EFax limits - not wiped on logout
        //


        @PrefMethod
        fun getDeviceGuid(context: Context): String {
            var guid: String? = IEasyPrefs.get().getPreferencesField(context, DEVICE_GUID_KEY, null as String?)

            if (guid == null) {
                guid = UUID.randomUUID().toString()
                IEasyPrefs.get().putPreferencesField(context, DEVICE_GUID_KEY, guid)
            }

            return guid
        }


        @PrefMethod
        fun putShowMsbVsMscDialog(context: Context, preference: Boolean) {
            IEasyPrefs.get().putPreferencesField(context, MSB_VS_MSC_USER_SELECTED, true)
            IEasyPrefs.get().putPreferencesField(context, MSB_VS_MSC_DIALOG, preference)
        }

        @PrefMethod
        fun hasMigratedGetParams(context: Context, name: String): Boolean {
            return IEasyPrefs.get().getPreferencesField(context, MIGRATED_GET_PARAMS + name, false)
        }

        @PrefMethod
        fun putMigratedGetParams(context: Context, name: String, migrated: Boolean) {
            IEasyPrefs.get().putPreferencesField(context, MIGRATED_GET_PARAMS + name, migrated)
        }

        @PrefMethod
        fun getGetParamsJson(context: Context, name: String): String {
            return IEasyPrefs.get().getPreferencesField(context, GET_PARAMS + name, null as String?)
        }

        @PrefMethod
        fun putGetParamsJson(context: Context, name: String, json: String) {
            IEasyPrefs.get().putPreferencesField(context, GET_PARAMS + name, json)
        }

        //
        // Clear everything on logout
        //
        @PrefMethod
        fun clearTutorialPreferences(context: Context) {
            val settings = getSecureInstance(context)
            val editor = settings!!.edit()
            editor.commit()
        }


        @PrefMethod
        fun clearCookie(context: Context) {
            IEasyPrefs.get().removePreferencesField(context, COOKIE_KEY)
            IEasyPrefs.get().removePreferencesField(context, COOKIES_KEY)
        }

        @PrefMethod
        fun clearProviderCookie(context: Context) {
            IEasyPrefs.get().removePreferencesField(context, PROVIDER_COOKIE_KEY)
        }

        /**
         * Upon logout, clear all data associated with the logged out account.
         * @param context
         */
        @PrefMethod
        fun clearDataForLogout(context: Context) {
            IEasyPrefs.get().clearAll(context)
            val settings = getSecureInstance(context)
            val editor = settings!!.edit()
            editor.remove(METER_KEY)
            editor.remove(COOKIE_KEY)
            editor.remove(COOKIES_KEY)
            editor.remove(DEVICE_GUID_KEY)
            editor.remove(GK_USER_KEY)
            editor.remove(PRO_CONNECT_CODE)
            editor.remove(PERSISTENT_BANNER)
            editor.remove(IS_DEPRECATED)
            editor.remove(SYNC_EXCEPTION)
            editor.remove(PROVIDER_GROUP_SITES)
            editor.remove(MSB_VS_MSC_DIALOG)
            editor.remove(MSB_VS_MSC_USER_SELECTED)
            React.clearOnboardingIndices(editor)
            React.clearReactComparisonsShowValue(editor)
            React.clearIsiInfo(context, editor)
            React.clear(editor)
            CareLink.clear(editor)
            editor.commit()

        }

        fun clearLastSyncKey(context: Context) {
            IEasyPrefs.get().removePreferencesField(context, LAST_SYNC_KEY)
        }

        @PrefMethod
        fun getIsiDontShowAgain(context: Context, medName: String): Boolean {
            return IEasyPrefs.get().getPreferencesField(context, ISI_DONT_SHOW_AGAIN + "_" + medName.toLowerCase(), false)
        }


        @PrefMethod
        fun putSignupProconnectProviderName(context: Context, name: String) {
            IEasyPrefs.get().putPreferencesField(context, SIGNUP_PROCONNECT_PROVIDER_NAME, name)
        }

        @PrefMethod
        fun getSignupProconnectProviderName(context: Context): String {
            return IEasyPrefs.get().getPreferencesField(context, SIGNUP_PROCONNECT_PROVIDER_NAME, "")
        }
    }


}
