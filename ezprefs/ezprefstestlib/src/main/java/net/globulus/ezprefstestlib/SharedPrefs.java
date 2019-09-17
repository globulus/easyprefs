package net.globulus.ezprefstestlib;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import net.globulus.easyprefs.IEasyPrefs;
import net.globulus.easyprefs.annotation.Pref;
import net.globulus.easyprefs.annotation.PrefFunction;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by gordanglavas on 21/11/2017.
 */

// Add fields from this class to EasyPrefs, but only those annotated with @Pref.
// Add fields directly to EasyPrefs, and not inside an enclosing static class.
////@PrefClass(autoInclude = false, staticClass = false, origin = true)
public class SharedPrefs {

    private static SharedPreferences sSecureInstance;

    //
    // PrefMaster annotation is placed on a public static method returning an instance of
    // SharedPreferences, and this method will be used in generated code to retrieve the
    // SharedPreferences instance we want to work with.
    //
//    @PrefMaster
    public static SharedPreferences getSecureInstance(@NonNull Context context) {
        if (sSecureInstance == null) {
           sSecureInstance = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sSecureInstance;
    }

    int test1; // This field won't be included

    @Pref
    String test2; // This field will be included.

    @Pref(key = "notTest3")
    long test3; // This field will be included with a custom key.


        private static final String METER_KEY = "meter";
        private static final String DIAGNOSTICS_MODE_KEY = "diagnostics_mode";
        private static final String COOKIE_KEY = "cookie_key";
        private static final String COOKIES_KEY = "cookies_key";
        private static final String PROVIDER_COOKIE_KEY = "provider_cookie_key";
        private static final String LAST_SYNC_KEY = "last_sync_date";

        private static final String EFAX_1 = "efax_1";
        private static final String EFAX_2 = "efax_2";
        private static final String EFAX_3 = "efax_3";

        private static final String DEVICE_GUID_KEY = "device_guid";
        private static final String GK_USER_KEY = "gkuser";
        private static final String PRO_CONNECT_CODE = "pro_connect_code";
        private static final String SIGNUP_PROCONNECT_PROVIDER_NAME = "signup_proconnect_provider_name";
        private static final String PERSISTENT_BANNER = "persistent_banner";
        private static final String IS_DEPRECATED = "is_deprecated";
        private static final String PROVIDER_GROUP_SITES = "provider_group_sites";
        private static final String ISI_DONT_SHOW_AGAIN_KEYLIST = "isi_dont_show_again_keylist";

        private static final String MIGRATED_GET_PARAMS = "migratedGetParams";
        private static final String GET_PARAMS = "getParams";

        private static final String MSB_VS_MSC_DIALOG = "msb_vs_msc_dialog";
        private static final String MSB_VS_MSC_USER_SELECTED = "msb_vs_msc_user_touched";
        private static final String ISI_DONT_SHOW_AGAIN = "isi_dont_show_again";
        private static final String SYNC_EXCEPTION = "sync_exception";

        private static final String RECENT_INSULIN = "recent_insulin";
        private static final String CUSTOM_INSULIN = "custom_insulin";
        private static final String RECENT_MEDICATION = "recent_medication";
        private static final String CUSTOM_MEDICATION = "custom_medication";


        public static final int INDEX_LOGBOOK = 4;

        @Pref(key = "database_plain_text", nullable = true, clear = false) String databasePlainText;
        @Pref(key = "enrolled_sites")
        Set<String> enrolledSites;
        @Pref(key = "material_tool_tips") Set<String> dismissedTooltips;
        @Pref(key = "inactive_devices") Set<String> inactiveDevices;
        @Pref Set<String> enrollmentMedications;

        @Pref(key = "bluetooth_address", nullable = true, clear = false) String msbAddress;

        @Pref(key = "ble_meters") Set<String> bleMeterAddresses;
        @Pref(key = "ble_meter_should_get_units") boolean bleMeterShouldGetUnits;

        @Pref(key = "comm_error", comment = "Simulated communication error preference") boolean commErrorPreference;
        @Pref(key = "meter_available", comment = "Override meter available preference") boolean meterAvailablePreference;

        @Pref(key = "push_enabled") final boolean pushEnabled = true;
        @Pref(key = "viewed_dayview_tutorial") final boolean dayViewShowtutorial = true;
        @Pref(key = "dont_show_this_message_again_video_tutorial") boolean dontShowThisMessageAgainVideoTutorial;

        @Pref(key = "app_version", nullable = true) String appVersion;
        @Pref(key = COOKIE_KEY, nullable = true) String cookie;
        @Pref(key = PROVIDER_COOKIE_KEY, nullable = true, clear = false) String providerCookie;
        @Pref(key = "oauth_token_secret", nullable = true) String oAuthTokenSecret;
        @Pref(key = "oauth_token", nullable = true) String oAuthToken;

        @Pref(key = "hypo_show_tooltip") final boolean hypoShowTooltip = true;
        @Pref(key = "foodsearch_show_tooltip") final boolean foodSearchToolTip = true;
        @Pref(key = "quickadd_show_tooltip") final boolean quickAddToolTip = true;
        @Pref(key = "mealtimes_show_tooltip") final boolean mealTimesToolTip = true;

        @Pref(key = "server_offset") long serverOffset;
        @Pref(key = "logged_in") boolean finishedLoggingIn;
        @Pref(key = "writing_audio_samples_to_disk") boolean writingAudioSamplesToDisk;

        @Pref(key = "did_migrate_prefs", autoset = "true") boolean didMigrate;
        @Pref(key = "is_deprecated") boolean isDeprecated;

        @Pref(key = "medication_references_upgraded", comment = "Medication References upgraded")
        boolean medicationReferencesUpgraded;
        @Pref(key = "cvs_banner_closed", autoset = "true", comment = "CVS Banner closed")
        boolean cvsBannerClosed;
        @Pref(key = "terms_of_service_accepted", clear = false)
        boolean termsOfUseAccepted;
        @Pref(key = "history_dialog") boolean showDialogHistory;
        @Pref(key = "last_logged_in_account", nullable = true) String lastLoggedInAccount;
        @Pref String fragmentTypeString;
        @Pref boolean signUpToC4cWebsite;
        @Pref boolean testMeterInRelease;
        @Pref boolean exerciseBarGraphOpen;
        @Pref boolean insulinBarGraphOpen;
        @Pref boolean carbBarGraphOpen;
        @Pref boolean getStartedScreenShown;
        @Pref String enrollmentStartedTimestamp;
        @Pref String cppStartedTimestamp;

        @Pref(function = NullableDateLong.class, rawDefaultValue = "0L", clear = false)
        Date appOpenTimestamp;
        @Pref boolean enrollmentStarted;

        //@PrefMethod
        public static void removePartValueFromPreferencesField(Context context, String key, String value) {
            final Set<String> set = new HashSet<>();// IEasyPrefs.get().getPreferencesField(context, key, new HashSet<>());
            if (value != null) {
                for (String type : set) {
                    if (type.contains(value)) {
                        set.remove(type);
                        break;
                    }
                }
            }
            IEasyPrefs.get().putPreferencesField(context, key, set);
        }


        //@PrefMethod
        public static void putSyncException(Context context, Exception exception) {
            IEasyPrefs.get().putPreferencesField(context, SYNC_EXCEPTION, (exception != null) ? exception.getClass().getName() : null);
        }

        //@PrefMethod
        public static String getSyncException(Context context) {
            return IEasyPrefs.get().getPreferencesField(context, SYNC_EXCEPTION, (String) null);
        }

        //
        // EFax limits - not wiped on logout
        //


        //@PrefMethod
        public static String getDeviceGuid(Context context) {
            String guid = IEasyPrefs.get().getPreferencesField(context, DEVICE_GUID_KEY, (String) null);

            if (guid == null) {
                guid = UUID.randomUUID().toString();
                IEasyPrefs.get().putPreferencesField(context, DEVICE_GUID_KEY, guid);
            }

            return guid;
        }


        //@PrefMethod
        public static void putShowMsbVsMscDialog(Context context, boolean preference) {
            IEasyPrefs.get().putPreferencesField(context, MSB_VS_MSC_USER_SELECTED, true);
            IEasyPrefs.get().putPreferencesField(context, MSB_VS_MSC_DIALOG, preference);
        }

        //@PrefMethod
        public static boolean hasMigratedGetParams(Context context, String name) {
            return IEasyPrefs.get().getPreferencesField(context, MIGRATED_GET_PARAMS + name, false);
        }

        //@PrefMethod
        public static void putMigratedGetParams(Context context, String name, boolean migrated) {
            IEasyPrefs.get().putPreferencesField(context, MIGRATED_GET_PARAMS + name, migrated);
        }

        //@PrefMethod
        public static String getGetParamsJson(Context context, String name) {
            return IEasyPrefs.get().getPreferencesField(context, GET_PARAMS + name, (String) null);
        }

        //@PrefMethod
        public static void putGetParamsJson(Context context, String name, String json) {
            IEasyPrefs.get().putPreferencesField(context, GET_PARAMS + name, json);
        }

        //
        // Clear everything on logout
        //
        //@PrefMethod
        public static void clearTutorialPreferences(Context context) {
            final SharedPreferences settings = getSecureInstance(context);
            final SharedPreferences.Editor editor = settings.edit();
            editor.commit();
        }


        //@PrefMethod
        public static void clearCookie(Context context) {
            IEasyPrefs.get().removePreferencesField(context, COOKIE_KEY);
            IEasyPrefs.get().removePreferencesField(context, COOKIES_KEY);
        }

        //@PrefMethod
        public static void clearProviderCookie(Context context) {
            IEasyPrefs.get().removePreferencesField(context, PROVIDER_COOKIE_KEY);
        }

        /**
         * Upon logout, clear all data associated with the logged out account.
         * @param context
         */
        //@PrefMethod
        public static void clearDataForLogout(Context context) {
            IEasyPrefs.get().clearAll(context);
            final SharedPreferences settings = getSecureInstance(context);
            final SharedPreferences.Editor editor = settings.edit();
            editor.remove(METER_KEY);
            editor.remove(COOKIE_KEY);
            editor.remove(COOKIES_KEY);
            editor.remove(DEVICE_GUID_KEY);
            editor.remove(GK_USER_KEY);
            editor.remove(PRO_CONNECT_CODE);
            editor.remove(PERSISTENT_BANNER);
            editor.remove(IS_DEPRECATED);
            editor.remove(SYNC_EXCEPTION);
            editor.remove(PROVIDER_GROUP_SITES);
            editor.remove(MSB_VS_MSC_DIALOG);
            editor.remove(MSB_VS_MSC_USER_SELECTED);
            React.clearOnboardingIndices(editor);
            React.clearReactComparisonsShowValue(editor);
            React.clearIsiInfo(context, editor);
            React.clear(editor);
            CareLink.clear(editor);
            editor.commit();

        }

        public static void clearLastSyncKey(Context context) {
            IEasyPrefs.get().removePreferencesField(context, LAST_SYNC_KEY);
        }


        //@PrefClass(autoInclude = false)
        public static class CareLink {

            private static final String CARELINK_TEST_METER = "carelink_test_meter";

            @Pref(key = "carelink_username_key", comment = "Carelink Username for Kiosk Minimed Connect feature")
            String username;
            @Pref(key = "in_carelink_flow") boolean inFlow;
            @Pref(key = "carelink_serial_number") String serialNumber;

            //@PrefMethod
            public static void putCareLinkTest(Context context, boolean test) {
                IEasyPrefs.get().putPreferencesField(context, CARELINK_TEST_METER, test);
            }

            //@PrefMethod
            public static boolean getCareLinkTest(Context context) {
                if (BuildConfig.DEBUG) {
                    return IEasyPrefs.get().getPreferencesField(context, CARELINK_TEST_METER, false);
                }
                return false;
            }

            //@PrefClass
            public static class CareLinkPumpIndices {
                @Pref(key = "carelink_last_history_page_number") int lastHistoryPageNumber;
                @Pref(key = "carelink_last_glucose_history_page_number") int lastGlucoseHistoryPageNumber;
                @Pref(key = "carelink_request_partial_sync_months") int requestedPartialSyncMonths;
            }

            public static void clear(SharedPreferences.Editor editor) {
                editor.remove(CARELINK_TEST_METER);
            }
        }

        //@PrefClass(autoInclude = false)
        public static class Kiosk {
            private static final String GK_PROVIDER_KEY = "kiosk_gk_provider";
            private static final String KIOSK_TIMEOUT_RESUME_TIMESTAMP = "kiosk_timeout_resume_timestamp";
            private static final String KIOSK_PRINT_OPTIONS = "kiosk_print_options";

            @Pref(key = "kiosk_is_kiosk", clear = false) boolean isKiosk;
            @Pref(key = "kiosk_accept_terms_of_use", clear = false) boolean acceptTermsOfUse;
            @Pref(key = "kiosk_mrn_required", clear = false) boolean mrnRequired;
            @Pref(key = "kiosk_mrn_show", clear = false) boolean mrnShow;
            @Pref(key = "kiosk_phone_number_required", clear = false) boolean phoneNumberRequired;
            @Pref(key = "kiosk_phone_number_show", clear = false) boolean phoneNumberShow;
            @Pref(key = "kiosk_type_of_diabetes_required", clear = false) boolean typeOfDiabetesRequired;
            @Pref(key = "kiosk_type_of_diabetes_show", clear = false) boolean typeOfDiabetesShow;
            @Pref(key = "kiosk_print_format", clear = false) final int printFormat = 30;
            @Pref(key = "kiosk_require_email", clear = false) final boolean requireEmail = true;
            @Pref(key = "kiosk_is_new_user", clear = false) boolean isNewUser;
            @Pref(key = "in_kiosk_device_sync", clear = false) boolean inDeviceSync;

            //@PrefMethod
            public static void putKioskTimeoutResumeTimestamp(Context context, long timestamp) {
                IEasyPrefs.get().putPreferencesField(context, KIOSK_TIMEOUT_RESUME_TIMESTAMP, timestamp);
            }

            //@PrefMethod
            public static long getKioskTimeoutResumeTimestamp(Context context) {
                return IEasyPrefs.get().getPreferencesField(context, KIOSK_TIMEOUT_RESUME_TIMESTAMP, System.currentTimeMillis());
            }

        }

        //@PrefClass
        public static class Mids {
            final boolean shouldShowFbgDefinition = true;
            boolean waitingForReminders;
            final boolean firstTimeStarted = true;
            @Pref(function = NonNullDateLong.class, rawDefaultValue = "0L") Date titrationPeriodStart;
            @Pref(function = NullableDateLong.class, rawDefaultValue = "0L") Date nextCheckupDate;
            @Pref(function = NullableDateLong.class, rawDefaultValue = "0L") Date lastCheckupDate;
            @Pref(nullable = true) String lastPlanGuid;
            int lastStartingDose;
            @Pref(nullable = true) String lastInsulinType;
            boolean shownRevisedDialog;
            boolean shownSuspendedDialog;
            boolean checkupStarted;
            boolean hypoCheckupStarted;
            boolean shouldCheckMidsHypoOnStart;
            boolean insulinReminderDisabledByProgram;
            Set<String> triggeredAlarms;
        }

        //@PrefClass
        public static class MidsTest {
            @Pref(function = NonNullDateLong.class, rawDefaultValue = "0L") Date currentTime;
            boolean hideReminderWhenRunningTest;
        }


        //@PrefClass(autoInclude = false)
        public static class React {

            private static final String REACT_ONBOARDING_INDEX_1 = "react_onboarding_index_1";
            private static final String REACT_ONBOARDING_INDEX_2 = "react_onboarding_index_2";
            private static final String REACT_ONBOARDING_INDEX_3 = "react_onboarding_index_3";
            private static final String REACT_ONBOARDING_INDEX_4 = "react_onboarding_index_4";
            private static final String REACT_COMPARISONS_SHOW_0 = "react_comparisons_show_0";
            private static final String REACT_COMPARISONS_SHOW_1 = "react_comparisons_show_1";
            private static final String REACT_COMPARISONS_SHOW_2 = "react_comparisons_show_2";
            private static final String REACT_COMPARISONS_SHOW_3 = "react_comparisons_show_3";
            private static final String REACT_GLUCOSE_DATA_SOURCE = "react_glucose_data_source";
            public static final String REACT_DATA_SOURCE_BG = "bg";
            public static final String REACT_DATA_SOURCE_EGV = "cgm";
            public static final String REACT_DATA_SOURCE_NOT_SPECIFIED = "not_specified";
            public static final String REACT_GRAPHS_END_DATE_STRING = "react_graphs_end_date_string";

            @Pref(oldKey = "react_day_count") final int dayCount = 30;
            @Pref(oldKey = "react_stats_swiper_index") int statsIndex;
            @Pref(oldKey = REACT_GRAPHS_END_DATE_STRING, addClearMethod = true, nullable = true) String graphsEndDateString;

            //@PrefMethod
            public static void resetReactOnboardingValues(Context context) {
                setReactOnboardingValueAtIndex(context, 1, 0);
                setReactOnboardingValueAtIndex(context, 2, 0);
                setReactOnboardingValueAtIndex(context, 3, 0);
                setReactOnboardingValueAtIndex(context, 4, 0);
            }

            //@PrefMethod
            public static int getReactOnboardingValueAtIndex(Context context, int index) {
                switch (index) {
                    case (1) :
                        return IEasyPrefs.get().getPreferencesField(context, REACT_ONBOARDING_INDEX_1, 0);
                    case (2) :
                        return IEasyPrefs.get().getPreferencesField(context, REACT_ONBOARDING_INDEX_2, 0);
                    case (3) :
                        return IEasyPrefs.get().getPreferencesField(context, REACT_ONBOARDING_INDEX_3, 0);
                    case (4) :
                        return IEasyPrefs.get().getPreferencesField(context, REACT_ONBOARDING_INDEX_4, 0);
                    default:
                        return -1;
                }
            }

            //@PrefMethod
            public static void setReactOnboardingValueAtIndex(Context context, int index, int value) {
                switch (index) {
                    case (1) :
                        IEasyPrefs.get().putPreferencesField(context, REACT_ONBOARDING_INDEX_1, value);
                        break;
                    case (2) :
                        IEasyPrefs.get().putPreferencesField(context, REACT_ONBOARDING_INDEX_2, value);
                        break;
                    case (3) :
                        IEasyPrefs.get().putPreferencesField(context, REACT_ONBOARDING_INDEX_3, value);
                        break;
                    case (4) :
                        IEasyPrefs.get().putPreferencesField(context, REACT_ONBOARDING_INDEX_4, value);
                        break;
                    default:
                        break;
                }
            }

            //@PrefMethod
            public static boolean getReactComparisonsShowValue(Context context, int index) {
                switch (index) {
                    case (0) :
                        return IEasyPrefs.get().getPreferencesField(context, REACT_COMPARISONS_SHOW_0, false);
                    case (1) :
                        return IEasyPrefs.get().getPreferencesField(context, REACT_COMPARISONS_SHOW_1, false);
                    case (2) :
                        return IEasyPrefs.get().getPreferencesField(context, REACT_COMPARISONS_SHOW_2, false);
                    case (3) :
                        return IEasyPrefs.get().getPreferencesField(context, REACT_COMPARISONS_SHOW_3, false);
                    default:
                        return false;
                }
            }

            //@PrefMethod
            public static void setReactComparisonsShowValue(Context context, int index, boolean value) {
                switch (index) {
                    case (0) :
                        IEasyPrefs.get().putPreferencesField(context, REACT_COMPARISONS_SHOW_0, value);
                        break;
                    case (1) :
                        IEasyPrefs.get().putPreferencesField(context, REACT_COMPARISONS_SHOW_1, value);
                        break;
                    case (2) :
                        IEasyPrefs.get().putPreferencesField(context, REACT_COMPARISONS_SHOW_2, value);
                        break;
                    case (3) :
                        IEasyPrefs.get().putPreferencesField(context, REACT_COMPARISONS_SHOW_3, value);
                        break;
                    default:
                        break;
                }
            }

            //@PrefMethod
            public static void clearReactComparisonsShowValue(SharedPreferences.Editor editor) {
                editor.remove(REACT_COMPARISONS_SHOW_0);
                editor.remove(REACT_COMPARISONS_SHOW_1);
                editor.remove(REACT_COMPARISONS_SHOW_2);
                editor.remove(REACT_COMPARISONS_SHOW_3);
            }

            //@PrefMethod
            public static void clearIsiInfo(Context context, SharedPreferences.Editor editor) {
                String keyList = IEasyPrefs.get().getPreferencesField(context, ISI_DONT_SHOW_AGAIN_KEYLIST, (String) null);
                if (keyList != null && !keyList.isEmpty()) {
                    String[] arr = keyList.split(",");
                    for (String str : arr) {
                        editor.remove(str);
                    }
                }
            }


            private static void clearOnboardingIndices(SharedPreferences.Editor editor) {
                editor.remove(REACT_ONBOARDING_INDEX_1);
                editor.remove(REACT_ONBOARDING_INDEX_2);
                editor.remove(REACT_ONBOARDING_INDEX_3);
                editor.remove(REACT_ONBOARDING_INDEX_4);
            }

            private static void clear(SharedPreferences.Editor editor) {
                editor.remove(REACT_GLUCOSE_DATA_SOURCE);
            }
        }

        //@PrefMethod
        public static boolean getIsiDontShowAgain(Context context, String medName) {
            return IEasyPrefs.get().getPreferencesField(context, ISI_DONT_SHOW_AGAIN + "_" + medName.toLowerCase(), false);
        }


        //@PrefMethod
        public static void putSignupProconnectProviderName(Context context, String name) {
            IEasyPrefs.get().putPreferencesField(context, SIGNUP_PROCONNECT_PROVIDER_NAME, name);
        }

        //@PrefMethod
        public static String getSignupProconnectProviderName(Context context) {
            return IEasyPrefs.get().getPreferencesField(context, SIGNUP_PROCONNECT_PROVIDER_NAME, "");
        }

        public static class NullableDateLong implements PrefFunction<Date, Long> {

            @Override
            public Long put(Date date) {
                return (date != null) ? date.getTime() : 0L;
            }

            @Override
            public Date get(Long time) {
                if (time == 0) {
                    return null;
                }
                return new Date(time);
            }
        }

        public static class NonNullDateLong implements PrefFunction<Date, Long> {

            @Override
            public Long put(Date date) {
                return date.getTime();
            }

            @Override
            public Date get(Long time) {
                return new Date(time != null ? time : 0L);
            }
        }


}
