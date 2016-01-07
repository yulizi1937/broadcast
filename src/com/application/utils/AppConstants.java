/* HISTORY
 * CATEGORY 		:- ACTIVITY
 * DEVELOPER		:- VIKALP PATEL
 * AIM			    :- ADD IPD ACTIVITY
 * DESCRIPTION 		:- SAVE IPD
 * 
 * S - START E- END  C- COMMENTED  U -EDITED A -ADDED
 * --------------------------------------------------------------------------------------------------------------------
 * INDEX       DEVELOPER		DATE			FUNCTION		DESCRIPTION
 * --------------------------------------------------------------------------------------------------------------------
 * 1000B      VIKALP PATEL    07/02/2014        RELEASE         ADD VIDEO EXTENSION
 * 1000E      VIKALP PATEL    15/02/2014        RELEASE         ADDED PASS HASH IN JSON
 * --------------------------------------------------------------------------------------------------------------------
 */
package com.application.utils;

import android.Manifest;
import android.os.Environment;

import com.squareup.okhttp.MediaType;

public class AppConstants {

	public interface API {
		//Content Type
		public static final MediaType API_CONTENTTYPE = MediaType.parse("application/json; charset=utf-8");
		// API V2
//		public static final String API_HOST        = "http://54.165.41.77";
//		public static final String API_CLIENT      = "";
		public static final String API_HOST        = "http://52.0.29.242";
		public static final String API_CLIENT      = "/demo";
		public static final String API_VERSION     = "/v2";
		public static final String API_FOLDER      ="/api/";
		
		public static final String API_URL                  = API_HOST + API_CLIENT +  API_VERSION + API_FOLDER ; //http://52.0.29.242/demo/v2/api/sample.php
		
		public static final String API_LOGIN                = API_URL + "checkUser";
		public static final String API_VERIFY               = API_URL + "verifyUser";
		public static final String API_RESEND_OTP           = API_URL + "resendOTP";
		public static final String API_UPDATE_USER          = API_URL + "updateUser";
		public static final String API_LOGOUT_USER          = API_URL + "appLogOut";
		public static final String API_APP_VERSION          = API_URL + "checkAppVersion";
		public static final String API_APP_FEEDBACK         = API_URL + "submitAppFeedback";
		public static final String API_APP_REPORT           = API_URL + "submitAppReport";
		public static final String API_APP_SETTINGS         = API_URL + "submitAppSetting";
		public static final String API_FETCH_PUSH_DATA      = API_URL + "fetchPushData";
		public static final String API_FETCH_FEED_MOBCAST   = API_URL + "fetchFeedMobcast";
		public static final String API_FETCH_FEED_TRAINING  = API_URL + "fetchFeedTraining";
		public static final String API_FETCH_FEED_EVENT     = API_URL + "fetchFeedEvent";
		public static final String API_FETCH_FEED_AWARD     = API_URL + "fetchFeedAward";
		public static final String API_FETCH_FEED_RECRUITMENT= API_URL + "fetchFeedRecruitment";
		public static final String API_FETCH_FEED_PARICHAY  = API_URL + "fetchFeedParichay";
		public static final String API_FETCH_FEED_BIRTHDAY  = API_URL + "fetchFeedBirthday";
		public static final String API_FETCH_FEED_PARICHAYREF= API_URL + "fetchFeedParichayReferral";
		public static final String API_FETCH_FEED_ACTION    = API_URL + "fetchFeedAction";
		public static final String API_FETCH_FEED_ACTION_PAR= API_URL + "fetchFeedActionParichay";
		public static final String API_SYNC                 = API_URL + "syncMobcastData";
		public static final String API_UPDATE_REPORT        = API_URL + "updateUserReport";
		public static final String API_UPDATE_REMOTE_REPORT = API_URL + "updateUserRemoteReport";
		public static final String API_SUBMIT_FEEDBACK      = API_URL + "submitUserFeedback";
		public static final String API_SUBMIT_QUIZ          = API_URL + "submitUserQuiz";
		public static final String API_SUBMIT_CAPTURE       = API_URL + "submitCapture";
		public static final String API_SUBMIT_AWARD_MESSAGE = API_URL + "submitAwardMessage";
		public static final String API_SUBMIT_BDAY_MESSAGE  = API_URL + "submitBirthdayMessage";
		public static final String API_SUBMIT_REF           = API_URL + "submitRecruitmentReference";
		public static final String API_SEND_CHAT            = API_URL + "sendChatMessage";
		public static final String API_SEARCH               = API_URL + "search";
		public static final String API_SUBMIT_ISSUE         = API_URL + "submitAppIssue";
		public static final String API_SUBMIT_APP_DATA      = API_URL + "getAppData";
		public static final String API_SUBMIT_PARICHAY      = API_URL + "submitParichayReference";
	}
	
	public interface API_KEY_PARAMETER{
		public static final String userName                = "mobileNumber";
		public static final String otp                     = "otp";
		public static final String deviceType              = "deviceType";
		public static final String device                  = "device";
		public static final String deviceMfg               = "deviceMfg";
		public static final String deviceName              = "deviceName";
		public static final String regId                   = "regId";
		public static final String deviceSize              = "deviceSize";
		public static final String deviceOs                = "deviceOs";
		public static final String deviceId                = "deviceId";
		public static final String appVersion              = "appVersion";
		public static final String deviceIsTablet          = "deviceIsTablet";
		public static final String success                 = "success";
		public static final String updateAvailable         = "updateAvailable";
		public static final String apiKey                  = "apiKey";
		public static final String errorMessage            = "errorMessage";
		public static final String successMessage          = "successMessage";
		public static final String accessToken             = "accessToken";
		public static final String from             	   = "from";
		public static final String to             		   = "to";
		public static final String message                 = "message";
		public static final String sortByAsc               = "sortByAsc";
		public static final String limit                   = "limit";
		public static final String searchTerm              = "searchTerm";
		public static final String by                      = "by";
		public static final String filter                  = "filter";
		public static final String name                    = "name";
		public static final String user                    = "user";
		public static final String emailAddress            = "emailAddress";
		public static final String profileImage            = "profileImage";
		public static final String employeeId              = "employeeId";
		public static final String favouriteQuestion       = "favouriteQuestion";
		public static final String favouriteAnswer         = "favouriteAnswer";
		public static final String birthdate               = "birthDate";
		public static final String removedProfile          = "removedProfile";
		public static final String countryCode             = "countryCode";
		public static final String category                = "category";
		public static final String issue                   = "issue";
		public static final String uniqueId                = "uniqueId";
		public static final String syncFrequencyMins           = "syncFrequencyMins";
		public static final String description             = "description";
		public static final String company                 = "company";
		public static final String subcategory             = "subcategory";
		public static final String subCategory             = "subCategory";
		public static final String action                  = "action";
		public static final String duration                = "duration";
		public static final String _id                     = "id";
		public static final String lastMobcastId           = "lastMobcastId";
		public static final String lastTrainingId          = "lastTrainingId";
		public static final String lastEventId             = "lastEventId";
		public static final String lastAwardId             = "lastAwardId";
		public static final String lastBirthdayId          = "lastBirthdayId";
		public static final String lastRecruitmentId       = "lastRecruitmentId";
		public static final String lastParichayId          = "lastParichayId";
		public static final String lastReferralId		   = "lastReferralId";
		public static final String mobcast                 = "mobcast";
		public static final String training                = "training";
		public static final String chat                    = "chat";
		public static final String award                   = "award";
		public static final String recruitment             = "recruitment";
		public static final String event                   = "event";
		public static final String birthday                = "birthday";
		public static final String feedback                = "feedback";
		public static final String quiz                    = "quiz";
		public static final String mobcastId               = "mobcastId";
		public static final String mobcastTitle            = "mobcastTitle";
		public static final String mobcastBy               = "mobcastBy";
		public static final String mobcastDescription      = "mobcastDescription";
		public static final String mobcastViewCount        = "mobcastViewCount";
		public static final String mobcastLikeCount        = "mobcastLikeCount";
		public static final String mobcastDate             = "mobcastDate";
		public static final String mobcastTime        	   = "mobcastTime";
		public static final String mobcastType        	   = "mobcastType";
		public static final String mobcastIsRead           = "mobcastIsRead";
		public static final String mobcastIsLiked          = "mobcastIsLiked";
		public static final String mobcastIsSharing        = "mobcastIsSharing";
		public static final String mobcastIsDownloadable   = "mobcastIsDownloadable";
		public static final String mobcastLink             = "mobcastLink";
		public static final String mobcastExpiryDate       = "mobcastExpiryDate";
		public static final String mobcastExpiryTime     	   = "mobcastExpiryTime";
		public static final String mobcastFileInfo        	   = "mobcastFileInfo";
		public static final String mobcastFileId        	   = "mobcastFileId";
		public static final String mobcastFileLink        	   = "mobcastFileLink";
		public static final String mobcastFileLang        	   = "mobcastFileLang";
		public static final String mobcastFileSize        	   = "mobcastFileSize";
		public static final String mobcastFileDuration         = "mobcastFileDuration";
		public static final String mobcastFilePages        	   = "mobcastFilePages";
		public static final String mobcastisDefault        	   = "mobcastIsDefault";
		public static final String mobcastLiveStream           = "mobcastLiveStream";
		public static final String mobcastLiveStreamYouTube    = "mobcastLiveStreamYouTube";
		public static final String mobcastReadDuration         = "mobcastReadDuration";
		public static final String mobcastFileName        	   = "mobcastFileName";
		public static final String mobcastThumbnail        	   = "mobcastThumbnail";
		public static final String mobcastFeedbackInfo         = "mobcastFeedbackInfo";
		public static final String mobcastFeedbackId           = "mobcastFeedbackId";
		public static final String mobcastFeedbackQueId        = "mobcastFeedbackQueId";
		public static final String mobcastFeedbackQueType      = "mobcastFeedbackQueType";
		public static final String mobcastFeedbackAnswer       = "mobcastFeedbackAnswer";
		public static final String mobcastFeedbackQueTitle     = "mobcastFeedbackQueTitle";
		public static final String mobcastFeedbackOption1      = "mobcastFeedbackOption1";
		public static final String mobcastFeedbackOption2      = "mobcastFeedbackOption2";
		public static final String mobcastFeedbackOption3      = "mobcastFeedbackOption3";
		public static final String mobcastFeedbackOption4      = "mobcastFeedbackOption4";
		public static final String mobcastFeedbackOption5      = "mobcastFeedbackOption5";
		public static final String mobcastFeedbackOption6      = "mobcastFeedbackOption6";
		public static final String mobcastFeedbackOption7      = "mobcastFeedbackOption7";
		public static final String trainingId               = "trainingId";
		public static final String trainingTitle            = "trainingTitle";
		public static final String trainingBy               = "trainingBy";
		public static final String trainingDescription      = "trainingDescription";
		public static final String trainingViewCount        = "trainingViewCount";
		public static final String trainingLikeCount        = "trainingLikeCount";
		public static final String trainingDate             = "trainingDate";
		public static final String trainingTime        	    = "trainingTime";
		public static final String trainingType        	    = "trainingType";
		public static final String trainingIsRead           = "trainingIsRead";
		public static final String trainingIsLiked          = "trainingIsLiked";
		public static final String trainingIsSharing        = "trainingIsSharing";
		public static final String trainingIsDownloadable   = "trainingIsDownloadable";
		public static final String trainingLink             = "trainingLink";
		public static final String trainingExpiryDate       = "trainingExpiryDate";
		public static final String trainingExpiryTime     	   = "trainingExpiryTime";
		public static final String trainingFileInfo        	   = "trainingFileInfo";
		public static final String trainingFileId        	   = "trainingFileId";
		public static final String trainingFileLink        	   = "trainingFileLink";
		public static final String trainingFileLang        	   = "trainingFileLang";
		public static final String trainingFileSize        	   = "trainingFileSize";
		public static final String trainingFileDuration        = "trainingFileDuration";
		public static final String trainingFilePages           = "trainingFilePages";
		public static final String trainingisDefault        	= "trainingIsDefault";
		public static final String trainingLiveStream           = "trainingLiveStream";
		public static final String trainingLiveStreamYouTube    = "trainingLiveStreamYouTube";
		public static final String trainingReadDuration         = "trainingReadDuration";
		public static final String trainingFileName        	    = "trainingFileName";
		public static final String trainingThumbnail        	= "trainingThumbnail";
		public static final String trainingQuizInfo         = "trainingQuizInfo";
		public static final String trainingQuizId           = "trainingQuizId";
		public static final String trainingQuizQueId        = "trainingQuizQueId";
		public static final String trainingQuizQueType      = "trainingQuizQueType";
		public static final String trainingQuizCorrectAnswer= "trainingQuizCorrectAnswer";
		public static final String trainingQuizAnswer       = "trainingQuizAnswer";
		public static final String trainingQuizScore        = "trainingQuizScore";
		public static final String trainingQuizDuration     = "trainingQuizDuration";
		public static final String isSingleAttempt          = "isSingleAttempt";
		public static final String trainingQuizQueTitle     = "trainingQuizQueTitle";
		public static final String trainingQuizOption1      = "trainingQuizOption1";
		public static final String trainingQuizOption2      = "trainingQuizOption2";
		public static final String trainingQuizOption3      = "trainingQuizOption3";
		public static final String trainingQuizOption4      = "trainingQuizOption4";
		public static final String trainingQuizOption5      = "trainingQuizOption5";
		public static final String trainingQuizOption6      = "trainingQuizOption6";
		public static final String trainingQuizOption7      = "trainingQuizOption7";
		public static final String trainingQuizTimeTaken    = "trainingQuizTimeTaken";
		public static final String eventId                  = "eventId";
		public static final String eventSentDate            = "eventSentDate";
		public static final String eventSentTime            = "eventSentTime";
		public static final String eventTitle               = "eventTitle";
		public static final String eventBy                  = "eventBy";
		public static final String eventDescription         = "eventDescription";
		public static final String eventGoingCount          = "eventGoingCount";
		public static final String eventInvitedCount        = "eventInvitedCount";
		public static final String eventLikeCount           = "eventLikeCount";
		public static final String eventReadCount           = "eventReadCount";
		public static final String eventViewCount           = "eventViewCount";
		public static final String eventStartDate           = "eventStartDate";
		public static final String eventEndDate             = "eventEndDate";
		public static final String eventLandMark            = "eventLandmark";
		public static final String eventFromTime            = "eventFromTime";
		public static final String eventToTime              = "eventToTime";
		public static final String eventIsRead              = "eventIsRead";
		public static final String eventIsLiked             = "eventIsLiked";
		public static final String eventIsGoing             = "eventIsGoing";
		public static final String eventIsSharing           = "eventIsSharing";
		public static final String eventCoverLink           = "eventCoverLink";
		public static final String eventFileSize            = "eventFileSize";
		public static final String eventIsExpiryDateSet     = "eventIsExpiryDateSet";
		public static final String eventExpiryDate          = "eventExpiryDate";
		public static final String eventExpiryTime          = "eventExpiryTime";
		public static final String eventVenue               = "eventVenue";
		public static final String eventMap                 = "eventMap";
		public static final String awardId                  = "awardId";
		public static final String awardTitle               = "awardTitle";
		public static final String awardReceiverName        = "awardReceiverName";
		public static final String awardReceiverEmail       = "awardReceiverEmail";
		public static final String awardReceiverMobile      = "awardReceiverMobile";
		public static final String awardDate                = "awardDate";
		public static final String awardDescription         = "awardDescription";
		public static final String awardCity                = "awardCity";
		public static final String awardDepartment          = "awardDepartment";
		public static final String awardReadCount           = "awardReadCount";
		public static final String awardViewCount           = "awardViewCount";
		public static final String awardLikeCount           = "awardLikeCount";
		public static final String awardShareCount          = "awardShareCount";
		public static final String awardCongratulatedCount  = "awardCongratulatedCount";
		public static final String awardSentDate            = "awardSentDate";
		public static final String awardSentTime            = "awardSentTime";
		public static final String awardIsRead              = "awardIsRead";
		public static final String awardIsLike              = "awardIsLiked";
		public static final String awardIsCongratulated     = "awardIsCongratulated";
		public static final String awardIsSharing           = "awardIsSharing";
		public static final String awardCoverLink           = "awardCoverLink";
		public static final String awardCoverThumbnail      = "awardCoverThumbnail";
		public static final String awardFileSize            = "awardFileSize";
		public static final String awardIsExpiryDateSet     = "awardIsExpiryDateSet";
		public static final String awardExpiryDate          = "awardExpiryDate";
		public static final String awardExpiryTime          = "awardExpiryTime";
		public static final String captureFileCount         = "fileCount";
		public static final String captureNote              = "note";
		public static final String captureFile          	= "file";
		public static final String jobTitle          	    = "jobTitle";
		public static final String jobDesignation          	= "jobDesignation";
		public static final String jobLocation          	= "jobLocation";
		public static final String jobExperience          	= "jobExperience";
		public static final String jobExperienceFrom        = "jobExperienceFrom";
		public static final String jobExperienceTo          = "jobExperienceTo";
		public static final String jobAgeLimit          	= "jobAgeLimit";
		public static final String jobSkills              	= "jobSkills";
		public static final String jobDescription          	= "jobDescription";
		public static final String jobQualification         = "jobQualification";
		public static final String jobDesiredProfile        = "jobDesiredProfile";
		public static final String jobHQ        			= "jobHQ";
		public static final String jobRegion       		  	= "jobRegion";
		public static final String jobDivision         		= "jobDivision";
		public static final String installment         		= "installment";
		public static final String jobCTC          	        = "jobCTC";
		public static final String contactName          	= "contactName";
		public static final String contactMobile          	= "contactMobile";
		public static final String contactDesignation       = "contactDesignation";
		public static final String contactEmail          	= "contactEmail";
		public static final String isContactSharing         = "isContactSharing";
		public static final String isSharing          	    = "isSharing";
		public static final String likeCount          	    = "likeCount";
		public static final String isLiked          	    = "isLiked";
		public static final String isRead          	        = "isRead";
		public static final String viewCount          	    = "viewCount";
		public static final String shareCount             	= "shareCount";
		public static final String isExpiryDateSet          = "isExpiryDateSet";
		public static final String expiryDate          	    = "expiryDate";
		public static final String expiryTime          	    = "expiryTime";
		public static final String sentDate          	    = "sentDate";
		public static final String sentTime          	    = "sentTime";
		public static final String referenceName            = "referenceName";
		public static final String referencePhoneNumber     = "referencePhoneNumber";
		public static final String referenceEmailAddress    = "referenceEmailAddress";
		public static final String birthdayId               = "birthdayId";
		public static final String birthdayUserId           = "birthdayUserId";
		public static final String birthdayDateOfBirth      = "birthdayDateOfBirth";
		public static final String birthdayCurrentYear      = "birthdayCurrentYear";
		public static final String birthdayCurrentMonth     = "birthdayCurrentMonth";
		public static final String birthdayCreateDate       = "birthdayCreateDate";
		public static final String birthdayUserName         = "birthdayUserName";
		public static final String birthdayUserEmailId      = "birthdayUserEmailId";
		public static final String birthdayUserMobileNumber = "birthdayUserMobileNumber";
		public static final String birthdayUserEmployeeId   = "birthdayUserEmployeeId";
		public static final String birthdayUserProfileImage = "birthdayUserProfileImage";
		public static final String birthdayUserDep          = "birthdayUserDep";
		public static final String birthdayUserCity         = "birthdayUserCity";
		public static final String birthdayUserSunSign      = "birthdayUserSunSign";
		public static final String birthdayUserIsMale       = "isMale";
		public static final String parichay                 = "parichay";
		public static final String parichayReferral         = "parichayReferral";
		public static final String jobId                	= "jobId";
		public static final String jobUnit                  = "jobUnit";
		public static final String jobPosition              = "jobPosition";
		public static final String qualification        	= "qualification";
		public static final String organization       		= "organization";
		public static final String designation              = "designation";
		public static final String experience        		= "experience";
		public static final String relationship        		= "relationship";
		public static final String city       				= "city";
		public static final String age       				= "age";
		public static final String region       			= "region";
		public static final String divison       			= "division";
		public static final String headquarter       		= "headquarter";
		public static final String jobReferredFor       	= "jobReferredFor";
		public static final String referredId		       	= "referredId";
		public static final String referredDate		       	= "referredDate";
		public static final String isTelephonic		       	= "isTelephonic";
		public static final String isOnlineWritten		    = "isOnlineWritten";
		public static final String isPR1		       		= "isPR1";
		public static final String isPR2		       		= "isPR2";
		public static final String isHR		       			= "isHR";
		public static final String isDuplicate		       	= "isDuplicate";
		public static final String reason		       		= "reason";
		public static final String installment1		       	= "installment1";
		public static final String installment2		       	= "installment2";
		public static final String installment3		       	= "installment3";
	}
	
	public interface API_HEADER{
		public static final String USER                 ="";
		public static final String PASSWORD             ="";
	}
	
	public interface PUSH{
		public static final String PROJECT_ID ="798674642543";
		public static final String API_KEY    ="AIzaSyBQ5MYjN4U7U5E2nBLufyBKdCt-01IayJY";
	}
	
	public interface YOUTUBE{
		public static final String API_KEY = "AIzaSyCnzuex7v-cNLcLN_ca39kQbPEfJcDCQaI";
	}
	
	public interface REPORT{
		public static final String READ = "read";
//		public static final String VIEW = "view";
		public static final String PUSH = "push";
		public static final String SYNC = "sync";
		public static final String PLAY = "play";
		public static final String SHARE= "share";
		public static final String ANYDO= "anydo";
		public static final String JOIN = "join";
		public static final String CALE = "cale";
		public static final String DECL = "decline";
		public static final String LINK = "link";
		public static final String LIKE = "like";
		public static final String UNLIKE = "unlike";
		public static final String OPEN = "open";
		public static final String CONG = "congratulate";
		public static final String WISH = "wish";
	}

	public interface FOLDER{
		public static final String ROOT_FOLDER = "/.con/.mobcast";
		public static final String BUILD_FOLDER = Environment.getExternalStorageDirectory().getPath() + ROOT_FOLDER + "/.mobcastvanilla/";
		public static final String BUILD_FOLDER_FILE_MANAGER = ROOT_FOLDER + "/.mobcastvanilla/";
		public static final String LOG_FOLDER = BUILD_FOLDER + "logs/" ;
		public static final String IMAGE_FOLDER = BUILD_FOLDER + "image/" ;
		public static final String AUDIO_FOLDER = BUILD_FOLDER + "audio/" ;
		public static final String VIDEO_FOLDER = BUILD_FOLDER + "video/" ;
		public static final String DOCUMENT_FOLDER = BUILD_FOLDER + "document/" ;
		public static final String THUMBNAIL_FOLDER = BUILD_FOLDER + "thumbnail/" ;
		public static final String PROFILE_FOLDER = BUILD_FOLDER + "profile/" ;
	}
	
	public interface MOBCAST{
		public static final String HEADER   = "header";//0
		public static final String TEXT     = "text";//1
		public static final String NEWS     = "news";//2
		public static final String IMAGE    = "image";//3
		public static final String VIDEO    = "video";//4
		public static final String AUDIO    = "audio";//5
		public static final String DOC      = "doc";//6
		public static final String PDF      = "pdf";//7
		public static final String PPT      = "ppt";//8
		public static final String XLS      = "xls";//9
		public static final String FEEDBACK = "feedback";//10
		public static final String STREAM   = "livestreamyoutube";//11
		public static final String FOOTER   = "footer";//12
		public static final String INTERACTIVE= "interactive";//13
	}
	
	public interface PARICHAY{
		public static final String HEADER   = "header";//0
		public static final String BANNER   = "banner";//1
		public static final String IRF      = "irf";//2
		public static final String FMCG     = "fmcg";//3
		public static final String FOOTER   = "footer";//4
	}
	
	public interface BIRTHDAY{
		public static final String SECTION   = "section";//0
		public static final String DETAIL    = "detail";//1
	}
	
	public interface TYPE{
		public static final int IMAGE     = 0;
		public static final int VIDEO     = 1;
		public static final int AUDIO     = 2;
		public static final int DOC       = 3;
		public static final int PDF       = 4;
		public static final int PPT       = 5;
		public static final int XLS       = 6;
		public static final int OTHER     = 7;
		public static final int TEXT      = 8;
		public static final int STREAM    = 9;
		public static final int FEEDBACK  = 10;
		public static final int QUIZ      = 11;
		public static final int PROFILE   = 12;
		public static final int EVENT     = 13;
		public static final int AWARD     = 14;
		public static final int RELOGIN   = 15;
		public static final int CHAT      = 16;
		public static final int RECRUITMENT= 17;
		public static final int UPDATEAPP = 18;
		public static final int BIRTHDAY  = 19;
		public static final int PARICHAY  = 20;
		public static final int REFERRAL  = 21;
		public static final int APPREMIND = 22;
		public static final int NOTIFREMIND = 23;
		public static final int INTERACTIVE = 24;
	}
	
	public interface TRAINING{
		public static final String HEADER          = "header";//0
		public static final String TEXT            = "text";//1
		public static final String INTERACTIVE     = "interactive";//2
		public static final String IMAGE           = "image";//3
		public static final String VIDEO           = "video";//4
		public static final String AUDIO           = "audio";//5
		public static final String DOC             = "doc";//6
		public static final String PDF             = "pdf";//7
		public static final String PPT             = "ppt";//8
		public static final String XLS             = "xls";//9
		public static final String QUIZ            = "quiz";//10
		public static final String STREAM          = "livestreamyoutube";//11
	}
	
	public interface INTENT{
		public static final int INTENT_LANGUAGE     = 1001;
		public static final int INTENT_CATEGORY     = 1002;
	}
	
	public interface INTENTCONSTANTS{
		public static final String LANGUAGE         = "language";
		public static final String LANGUAGECODE     = "languageCode";
		public static final String ACTIVITYTITLE    = "activityTitle";
		public static final String CATEGORYARRAY    = "categoryArray";
		public static final String CATEGORY         = "category";
		public static final String INSTALLMENT		= "installment";
		public static final String HQ		        = "hq";
		public static final String REGION		    = "region";
		public static final String DIVISION		    = "division";
		public static final String ACTION           = "action";
		public static final String ISFROMNOTIFICATION= "isFromNotification";
		public static final String ISFROMAPP        = "isFromApp";
		public static final String HELP             = "help";
		public static final String USERNAME         = "userName";
		public static final String COUNTRYCODE      = "countryCode";
		public static final String OTP              = "otp";
		public static final String MOBCAST          = "mobcast";
		public static final String SYNC             = "sync";
		public static final String CHAT             = "chat";
		public static final String TRAINING         = "training";
		public static final String AWARD            = "award";
		public static final String EVENT            = "event";
		public static final String BIRTHDAY         = "birthday";
		public static final String ISSUE            = "issue";
		public static final String REMOTECONTENTDELETE= "remoteContentDelete";
		public static final String REMOTEWIPE       = "remoteWipe";
		public static final String REMOTELOGOUT     = "remoteLogout";
		public static final String UPDATEAPP        = "updateApp";
		public static final String GCMSUCCESS       = "gcmSuccess";
		public static final String REMOTESUSPEND    = "remoteSuspend";
		public static final String APPPULLDATA      = "appPullData";
		public static final String GCMUNREADREMIND  = "gcmUnreadRemind";
		public static final String SYNCFREQUENCY    = "syncFrequency";
		public static final String RECRUITMENT      = "recruitment";
		public static final String PARICHAY         = "parichay";
		public static final String PARICHAYREF      = "parichayReferred";
		public static final String REFERRAL         = "referral";
		public static final String RELOGIN          = "relogin";
		public static final String APPREMIND        = "appRemind";
		public static final String NOTIFREMIND      = "notifRemind";
		public static final String LOGIN            = "login";
		public static final String OBJECT           = "object";
		public static final String POSITION         = "position";
		public static final String POINTS           = "points";
		public static final String TOTALPOINTS      = "totalPoints";
		public static final String TOTAL            = "total";
		public static final String TIMETAKEN        = "timeTaken";
		public static final String QUIZINCORRECT    = "quizInCorrect";
		public static final String LOGOUT           = "logOut";
		public static final String ID               = "id";
		public static final String TYPE             = "type";
		public static final String LINK             = "link";
		public static final String OPEN             = "open";
		public static final String DISPLAYNAME      = "name";
		public static final String FILEPATH         = "path";
		public static final String TIME             = "time";
		public static final String FROM             = "from";
		public static final String TO               = "to";
		public static final String DP               = "dp";
		public static final String CAPTURE          = "capture";
		public static final String RESENDOTP        = "resendOTP";
	}
	
	public interface NOTIFICATION{
		public static final String TITLE      = "title";
		public static final String MESSAGE    = "message";
		public static final String SIZE       = "size";
		public static final String DURATION   = "duration";
		public static final String PAGES      = "pages";
		public static final String FILEPATH   = "filePath";
		public static final String THUMBPATH  = "ThumbnailPath";
	}
	
	public interface COLOR{
		public static final String MOBCAST_RED     = "#FD3F1F";
		public static final String MOBCAST_YELLOW  = "#FFCD00";
		public static final String MOBCAST_PURPLE  = "#8F46AD";
		public static final String MOBCAST_GREEN   = "#02B86B";
		public static final String MOBCAST_BLUE    = "#008CED";
	}
	
	public interface SHOWCASEVIEW{
		public static final String SHOWCASE_ID = "786";
		public static final String SEQUENCE1 = "1234";
		public static final String SEQUENCE2 = "2345";
		public static final String SEQUENCE3 = "3456";
		public static final String SEQUENCE4 = "4567";
		public static final String SEQUENCE5 = "5678";	
	}
	
	public interface PERMISSION{
		public final String CALENDAR= Manifest.permission.WRITE_CALENDAR;
		public final String CONTACTS= Manifest.permission.READ_CONTACTS;
		public final String AUDIO   = Manifest.permission.RECORD_AUDIO;
		public final String SMS     = Manifest.permission.READ_SMS;
		public final String STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
	}
	
	public static final String deviceType = "android";
	
	public static final String mChatUser1 = "9920277790";//"9920277790";
	public static final String mChatUser2 = "9819135685";//"9769688838";
	
	public static final String mChatName1  = "Anupriya";//"Anup Burade";
	public static final String mChatName2  = "Varun";//"Ashwin Roy Choudhary";
	
	public static final String mChatUserDp2 = "https://scontent-hkg3-1.xx.fbcdn.net/hphotos-xfp1/v/t1.0-9/11060272_10152903098622331_8045057861757358066_n.jpg?oh=560fe1ff0355546a203020293534b9ba&oe=567B3824";
	public static final String mChatUserDp1 = "https://media.licdn.com/mpr/mpr/shrinknp_400_400/p/5/005/013/2cd/311ec51.jpg";
	
	public static final String mStoreLink = "https://play.google.com/store/apps/details?id=com.mobcast";
	public static final String mSupportEmail = "support@mobcast.in";
	
	public static final String decrypted = "_decrypted";
	
	public static final String encrypted = "_encrypted";

	public static final String GREEN = "#006400";
	public static final String BLUE = "#01AFD2";
	
	public static final int DRAWER_RECRUITMENT_POS = 3;//3-CK
	
	public static final int NOTIFICATION_ID = 434;
	public static final int BULK = 20;
	
	public static final int BUBBLESTAY = 5000;
	
	public static final String SEARCH_FILTER = "-100000000";
	public static final String SEARCH_WEB    = "-200000000";

}
