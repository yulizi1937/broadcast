/*
 * This is the source code of Telegram for Android v. 1.3.2.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013.
 */

package com.application.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.RSAPublicKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Cipher;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.application.beans.Theme;
import com.application.sqlite.DBConstant;
import com.mobcast.R;
import com.squareup.okhttp.OkHttpClient;

public class Utilities {
	private static final String TAG = Utilities.class.getSimpleName();
    public static Pattern pattern = Pattern.compile("[0-9]+");
    public static SecureRandom random = new SecureRandom();
    public static Point displaySize = new Point();

    public static ArrayList<String> goodPrimes = new ArrayList<String>();

    public static class TPFactorizedValue {
        public long p, q;
    }

    public static volatile DispatchQueue stageQueue = new DispatchQueue("stageQueue");
    public static volatile DispatchQueue globalQueue = new DispatchQueue("globalQueue");
    public static volatile DispatchQueue searchQueue = new DispatchQueue("searchQueue");
    public static volatile DispatchQueue photoBookQueue = new DispatchQueue("photoBookQueue");
    public static volatile DispatchQueue downloadQueue = new DispatchQueue("downloadQueue");
    public static volatile DispatchQueue reportQueue = new DispatchQueue("reportQueue");

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static Integer parseInt(String value) {
        if (value == null) {
            return 0;
        }
        Integer val = 0;
        try {
            Matcher matcher = pattern.matcher(value);
            if (matcher.find()) {
                String num = matcher.group(0);
                val = Integer.parseInt(num);
            }
        } catch (Exception e) {
            FileLog.e("tmessages", e);
        }
        return val;
    }

    public static String parseIntToString(String value) {
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }


    public static boolean isGoodGaAndGb(BigInteger g_a, BigInteger p) {
        return !(g_a.compareTo(BigInteger.valueOf(1)) != 1 || g_a.compareTo(p.subtract(BigInteger.valueOf(1))) != -1);
    }

    public static boolean arraysEquals(byte[] arr1, int offset1, byte[] arr2, int offset2) {
        if (arr1 == null || arr2 == null || arr1.length - offset1 != arr2.length - offset2 || arr1.length - offset1 < 0) {
            return false;
        }
        for (int a = offset1; a < arr1.length; a++) {
            if (arr1[a + offset1] != arr2[a + offset2]) {
                return false;
            }
        }
        return true;
    }

    public static byte[] computeSHA1(byte[] convertme, int offset, int len) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(convertme, offset, len);
            return md.digest();
        } catch (Exception e) {
            FileLog.e("tmessages", e);
        }
        return null;
    }

    public static byte[] computeSHA1(ByteBuffer convertme, int offset, int len) {
        int oldp = convertme.position();
        int oldl = convertme.limit();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            convertme.position(offset);
            convertme.limit(len);
            md.update(convertme);
            return md.digest();
        } catch (Exception e) {
            FileLog.e("tmessages", e);
        } finally {
            convertme.limit(oldl);
            convertme.position(oldp);
        }
        return null;
    }

    public static byte[] computeSHA1(ByteBuffer convertme) {
        return computeSHA1(convertme, 0, convertme.limit());
    }

    public static byte[] computeSHA1(byte[] convertme) {
        return computeSHA1(convertme, 0, convertme.length);
    }

    public static byte[] encryptWithRSA(BigInteger[] key, byte[] data) {
        try {
            KeyFactory fact = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(key[0], key[1]);
            PublicKey publicKey = fact.generatePublic(keySpec);
            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            FileLog.e("tmessages", e);
        }
        return null;
    }

    public static long bytesToLong(byte[] bytes) {
        return ((long) bytes[7] << 56) + (((long) bytes[6] & 0xFF) << 48) + (((long) bytes[5] & 0xFF) << 40) + (((long) bytes[4] & 0xFF) << 32)
                + (((long) bytes[3] & 0xFF) << 24) + (((long) bytes[2] & 0xFF) << 16) + (((long) bytes[1] & 0xFF) << 8) + ((long) bytes[0] & 0xFF);
    }

    public static byte[] compress(byte[] data) {
        if (data == null) {
            return null;
        }

        byte[] packedData = null;
        ByteArrayOutputStream bytesStream = new ByteArrayOutputStream();
        try {
            GZIPOutputStream zip = new GZIPOutputStream(bytesStream);
            zip.write(data);
            zip.close();
            packedData = bytesStream.toByteArray();
        } catch (IOException e) {
            FileLog.e("tmessages", e);
        }
        return packedData;
    }

    public static boolean copyFile(InputStream sourceFile, File destFile) throws IOException {
        OutputStream out = new FileOutputStream(destFile);
        byte[] buf = new byte[4096];
        int len;
        while ((len = sourceFile.read(buf)) > 0) {
            Thread.yield();
            out.write(buf, 0, len);
        }
        out.close();
        return true;
    }

    public static boolean copyFile(File sourceFile, File destFile) throws IOException {
        if(!destFile.exists()) {
            destFile.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } catch (Exception e) {
            FileLog.e("tmessages", e);
            return false;
        } finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }
        return true;
    }

    public static String MD5(String md5) {
        if (md5 == null) {
            return null;
        }
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            FileLog.e("tmessages", e);
        }
        return null;
    }

    public static void addMediaToGallery(String fromPath) {
        if (fromPath == null) {
            return;
        }
        File f = new File(fromPath);
        Uri contentUri = Uri.fromFile(f);
        addMediaToGallery(contentUri);
    }

    public static void addMediaToGallery(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        ApplicationLoader.applicationContext.sendBroadcast(mediaScanIntent);
    }

    private static File getAlbumDir() {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Telegram");
            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()){
                        FileLog.d("tmessages", "failed to create directory");
                        return null;
                    }
                }
            }
        } else {
            FileLog.d("tmessages", "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    @SuppressLint("NewApi") 
    public static String getPath(final Uri uri) {
        try {
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
            if (isKitKat && DocumentsContract.isDocumentUri(ApplicationLoader.applicationContext, uri)) {
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                } else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(ApplicationLoader.applicationContext, contentUri, null, null);
                } else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] {
                            split[1]
                    };

                    return getDataColumn(ApplicationLoader.applicationContext, contentUri, selection, selectionArgs);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(ApplicationLoader.applicationContext, uri, null, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } catch (Exception e) {
            FileLog.e("tmessages", e);
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            FileLog.e("tmessages", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static File generatePicturePath() {
        try {
            File storageDir = getAlbumDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            return new File(storageDir, "IMG_" + timeStamp + ".jpg");
        } catch (Exception e) {
            FileLog.e("tmessages", e);
        }
        return null;
    }

    public static CharSequence generateSearchName(String name, String name2, String q) {
        if (name == null && name2 == null) {
            return "";
        }
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String wholeString = name;
        if (wholeString == null || wholeString.length() == 0) {
            wholeString = name2;
        } else if (name2 != null && name2.length() != 0) {
            wholeString += " " + name2;
        }
        wholeString = wholeString.trim();
        String lower = " " + wholeString.toLowerCase();

        int index = -1;
        int lastIndex = 0;
        while ((index = lower.indexOf(" " + q, lastIndex)) != -1) {
            int idx = index - (index == 0 ? 0 : 1);
            int end = q.length() + (index == 0 ? 0 : 1) + idx;

            if (lastIndex != 0 && lastIndex != idx + 1) {
                builder.append(wholeString.substring(lastIndex, idx));
            } else if (lastIndex == 0 && idx != 0) {
                builder.append(wholeString.substring(0, idx));
            }

            String query = wholeString.substring(idx, end);
            if (query.startsWith(" ")) {
                builder.append(" ");
            }
            query.trim();
            builder.append(Html.fromHtml("<font color=\"#4d83b3\">" + query + "</font>"));

            lastIndex = end;
        }

        if (lastIndex != -1 && lastIndex != wholeString.length()) {
            builder.append(wholeString.substring(lastIndex, wholeString.length()));
        }

        return builder;
    }

    public static File generateVideoPath() {
        try {
            File storageDir = getAlbumDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            return new File(storageDir, "VID_" + timeStamp + ".mp4");
        } catch (Exception e) {
            FileLog.e("tmessages", e);
        }
        return null;
    }

    public static String formatFileSize(long size) {
        if (size < 1024) {
            return String.format("%d B", size);
        } else if (size < 1024 * 1024) {
            return String.format("%.1f KB", size / 1024.0f);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", size / 1024.0f / 1024.0f);
        } else {
            return String.format("%.1f GB", size / 1024.0f / 1024.0f / 1024.0f);
        }
    }

    public static byte[] decodeQuotedPrintable(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int i = 0; i < bytes.length; i++) {
            final int b = bytes[i];
            if (b == '=') {
                try {
                    final int u = Character.digit((char) bytes[++i], 16);
                    final int l = Character.digit((char) bytes[++i], 16);
                    buffer.write((char) ((u << 4) + l));
                } catch (Exception e) {
                    FileLog.e("tmessages", e);
                    return null;
                }
            } else {
                buffer.write(b);
            }
        }
        return buffer.toByteArray();
    }
    
    public static void showCrouton(Activity mActivity, String mMessage, Style mStyle){
    	Crouton.cancelAllCroutons();
    	Crouton.makeText(mActivity, mMessage, mStyle).show();
    }
    
    public static void showCrouton(Activity mActivity,ViewGroup mViewGroup, String mMessage, Style mStyle){
    	Crouton.cancelAllCroutons();
    	Crouton.makeText(mActivity, mMessage, mStyle,mViewGroup).show();
    }
    
    public static void showCroutonAsSteady(Activity mActivity, ViewGroup mViewGroup, String mMessage, int mMinutes, String mHexColorCode){
    	Crouton.cancelAllCroutons();
    	// Define configuration options
    	Configuration croutonConfiguration = new Configuration.Builder()
    	    .setDuration(1000 * 60 * mMinutes).build();
    	// Define custom styles for crouton
    	Style style = new Style.Builder()
    	    .setBackgroundColorValue(Color.parseColor(mHexColorCode))
    	    .setGravity(Gravity.CENTER)
    	    .setConfiguration(croutonConfiguration)
    	    .setHeight(60)
    	    .setTextColorValue(Color.parseColor("#ffffff")).build();
    	Crouton.makeText(mActivity, mMessage, style, mViewGroup).show();
    }
    
    public static int getAppColor(){
    	return Color.parseColor(ApplicationLoader.getApplication().getResources().getString(R.color.toolbar_background));
    }
    
    public static int getDividerColor(){
    	return Color.parseColor(ApplicationLoader.getApplication().getResources().getString(R.color.divider_color));
    }
    
    public static int getAppHighlightedColor(){
    	return Color.parseColor(ApplicationLoader.getApplication().getResources().getString(R.color.text_highlight));
    }
    
	public static int dpToPx(float dp, Resources resources) {
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				resources.getDisplayMetrics());
		return (int) px;
	}
	
	public static float dipOrDpToFloat(String value) {
		if (value.indexOf("dp") != -1) {
			value = value.replace("dp", "");
		} else {
			value = value.replace("dip", "");
		}
		return Float.parseFloat(value);
	}
	
	public static int getRelativeTop(View myView) {
		Rect bounds = new Rect();
		myView.getGlobalVisibleRect(bounds);
		return bounds.top;
	}

	public static int getRelativeLeft(View myView) {
		// if (myView.getParent() == myView.getRootView())
		if (myView.getId() == android.R.id.content)
			return myView.getLeft();
		else
			return myView.getLeft()
					+ getRelativeLeft((View) myView.getParent());
	}
	
	
	public static void showBadgeNotification(Context mContext){
    	try{
    		int mCount  = 0;
    		mCount = Utilities.getUnreadCount(mContext);		
    		if(mCount>0){
    			BadgeUtils.setBadge(mContext, mCount);
    		}else{
    			BadgeUtils.clearBadge(mContext);
    		}
    	}catch(Exception e){
    		FileLog.e(TAG, e.toString());
    	}
    }
	public static int getUnreadCount(Context mContext){
		int mCount = 0;
		try{
			mCount = getUnreadOfMobcast(mContext)
					+ getUnreadOfTraining(mContext)
					+ getUnreadOfAward(mContext) 
					+ getUnreadOfEvent(mContext)
					+ getUnreadOfBirthday(mContext); 
			return mCount;
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
			return mCount;
		}
	}
	
	public static int getUnreadOfMobcast(Context mContext){
		Cursor mCursor = mContext.getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, DBConstant.Mobcast_Columns.COLUMN_MOBCAST_IS_READ + "=?", new String[]{"false"}, null);
		if(mCursor!=null && mCursor.getCount() > 0){
			return mCursor.getCount();
		}
		if(mCursor!=null){
			mCursor.close();
		}
		return 0;
		
	}
	
	public static int getUnreadOfTraining(Context mContext){
		Cursor mCursor = mContext.getContentResolver().query(DBConstant.Training_Columns.CONTENT_URI, null, DBConstant.Training_Columns.COLUMN_TRAINING_IS_READ + "=?", new String[]{"false"}, null);
		if(mCursor!=null && mCursor.getCount() > 0){
			return mCursor.getCount();
		}
		if(mCursor!=null){
			mCursor.close();
		}
		return 0;
	}
	
	public static int getUnreadOfEvent(Context mContext){
		Cursor mCursor = mContext.getContentResolver().query(DBConstant.Event_Columns.CONTENT_URI, null, DBConstant.Event_Columns.COLUMN_EVENT_IS_READ + "=?", new String[]{"false"}, null);
		if(mCursor!=null && mCursor.getCount() > 0){
			return mCursor.getCount();
		}
		if(mCursor!=null){
			mCursor.close();
		}
		return 0;
	}
	
	public static int getUnreadOfAward(Context mContext){
		Cursor mCursor = mContext.getContentResolver().query(DBConstant.Award_Columns.CONTENT_URI, null, DBConstant.Award_Columns.COLUMN_AWARD_IS_READ + "=?", new String[]{"false"}, null);
		if(mCursor!=null && mCursor.getCount() > 0){
			return mCursor.getCount();
		}
		if(mCursor!=null){
			mCursor.close();
		}
		return 0;
	}
	
	public static int getUnreadOfBirthday(Context mContext){
		Cursor mCursor = mContext.getContentResolver().query(DBConstant.Birthday_Columns.CONTENT_URI, null, DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_READ + "=?" + " OR " + DBConstant.Birthday_Columns.COLUMN_BIRTHDAY_IS_READ + "=?" , new String[]{"false","0"}, null);
		if(mCursor!=null && mCursor.getCount() > 0){
			return mCursor.getCount();
		}
		if(mCursor!=null){
			mCursor.close();
		}
		return 0;
	}
	
	@SuppressLint("DefaultLocale") public static String getTimeFromMilliSeconds(long milliSeconds){
		return String.format("%02d:%02d",
	            TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
	            TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));
		
	}
	
	public static String getTodayDate(){
		try{
			long mCurrentTimeMillis = System.currentTimeMillis();
			SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date mDate = new Date(mCurrentTimeMillis);
			return mDateFormat.format(mDate);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
		return "2020-01-01";
	}
	
	public static String getCurrentYear(){
		try{
			long mCurrentTimeMillis = System.currentTimeMillis();
			SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy");
			Date mDate = new Date(mCurrentTimeMillis);
			return mDateFormat.format(mDate);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
		return "2020";
	}
	
	@SuppressLint("SimpleDateFormat") public static String getCurrentDay(int mMonth,int mDay){
		try{
			
			SimpleDateFormat mDateFormat = new SimpleDateFormat("dd MMMM EEEE");
			/*Date mDate = new Date(mCurrentTimeMillis);
			long mCurrentTimeMillis = System.currentTimeMillis();
			mDate.setYear(Integer.parseInt(getCurrentYear()));
			mDate.setMonth(mMonth-1);
			mDate.setDate(mDay);
			return mDateFormat.format(mDate);*/
			
			Calendar c = Calendar.getInstance();
			c.setFirstDayOfWeek(Calendar.MONDAY);
			c.set(Calendar.YEAR, Integer.parseInt(getCurrentYear()));
			c.set(Calendar.MONTH, mMonth-1);
			c.set(Calendar.DATE, mDay);
			
			return mDateFormat.format(c.getTime());
			
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
		return "January 01";
	}
	
	public static String getTodayTime(){
		try {
			long mCurrentTimeMillis = System.currentTimeMillis();
			SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
			Date mDate = new Date(mCurrentTimeMillis);
			return timeFormatter.format(mDate);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
		return "00:00:00";
	}
	
	public static String getSyncTime(boolean isFailed){
		try{
			long mCurrentTimeMillis = System.currentTimeMillis();
			SimpleDateFormat mDateFormat = new SimpleDateFormat("dd MMM hh:mm");
			Date mDate = new Date(mCurrentTimeMillis);
			String mCurrentDateAndTime = mDateFormat.format(mDate);
			if(isFailed){
				return ApplicationLoader.getApplication().getResources().getString(R.string.sync_failed)+" "+ mCurrentDateAndTime;
			}else{
				return ApplicationLoader.getApplication().getResources().getString(R.string.sync_passed)+"  "+ mCurrentDateAndTime;
			}
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
			return "Failed due to unknown error";
		}
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("SimpleDateFormat") 
	public static boolean isContentDateSameAsToday(String mDate) {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			Date todayDate = dateFormatter.parse(dateFormatter.format(new Date()));
			Date contentDate = dateFormatter.parse(mDate);

			if (todayDate.getYear() == contentDate.getYear()) {
				if (todayDate.getMonth() == contentDate.getMonth()) {
					if (todayDate.getDate() == contentDate.getDate()) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
			return false;
		}
	}
	
	
	public static String convertTimeFromSecsTo(long milliSeconds){
		if(milliSeconds >= 60){
			return (milliSeconds/60) + " mins";
		}else{
			return milliSeconds + " sec";
		}
	}
	
    public static int resolveDimension(Context context, @AttrRes int attr, @DimenRes int fallbackRes) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            return a.getDimensionPixelSize(0,
                    (int) context.getResources().getDimension(fallbackRes));
        } finally {
            a.recycle();
        }
    }

    @ColorInt
    public static int resolveColor(Context context, @AttrRes int attr, int fallback) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            return a.getColor(0, fallback);
        } finally {
            a.recycle();
        }
    }

    public static int resolveInt(Context context, @AttrRes int attr, int fallback) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            return a.getInt(0, fallback);
        } finally {
            a.recycle();
        }
    }

    public static String resolveString(Context context, @AttrRes int attr) {
        TypedValue v = new TypedValue();
        context.getTheme().resolveAttribute(attr, v, true);
        return (String) v.string;
    }

    public static int resolveResId(Context context, @AttrRes int attr, int fallback) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            return a.getResourceId(0, fallback);
        } finally {
            a.recycle();
        }
    }
    
    public static String getDeviceMfg(){
    	try {
			String manufacturer = Build.MANUFACTURER;
			return capitalize(manufacturer);
		} catch (Exception e) {
			return "Device Mfg Unidentified";
		}
    }
    
	public static String getDeviceName() {
		try {
			String manufacturer = Build.MANUFACTURER;
			String model = Build.MODEL;
			if (model.startsWith(manufacturer)) {
				return capitalize(model);
			} else {
				return capitalize(manufacturer) + " " + model;
			}
		} catch (Exception e) {
			return "Device Unidentified";
		}
	}

	public static String capitalize(String s) {
		try {
			if (s == null || s.length() == 0) {
				return "";
			}
			char first = s.charAt(0);
			if (Character.isUpperCase(first)) {
				return s;
			} else {
				return Character.toUpperCase(first) + s.substring(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@SuppressLint("NewApi")
	public static String checkDisplaySize() {
		try {
			WindowManager manager = (WindowManager) ApplicationLoader.applicationContext
					.getSystemService(Context.WINDOW_SERVICE);
			if (manager != null) {
				Display display = manager.getDefaultDisplay();
				if (display != null) {
					if (android.os.Build.VERSION.SDK_INT < 13) {
						displaySize
								.set(display.getWidth(), display.getHeight());
					} else {
						display.getSize(displaySize);
					}
					FileLog.e("tmessages", "display size = " + displaySize.x
							+ " " + displaySize.y);
				}
			}
			
//			return Math.sqrt((displaySize.x * displaySize.x)
//					+ (displaySize.y * displaySize.y)) + AndroidUtilities.getScreenSizeInInches();
			return   displaySize.x+ " x " + displaySize.y  + " "+ AndroidUtilities.getScreenSizeInInches();
		} catch (Exception e) {
			FileLog.e("tmessages", e);
			return "0";
		}
	}
	
	public static String getDeviceId() {
		try {
			TelephonyManager telephonyManager = (TelephonyManager) ApplicationLoader
					.getApplication().getApplicationContext()
					.getSystemService(Context.TELEPHONY_SERVICE);
			if(!TextUtils.isEmpty(telephonyManager.getDeviceId())){
				return telephonyManager.getDeviceId();	
			}else{
				return Secure.getString(ApplicationLoader.getApplication().getContentResolver(), Secure.ANDROID_ID);
			}
		} catch (Exception e) {
			return "No Device Id Found!";
		}
	}

	public static String getSDKVersion() {
		return String.valueOf(Build.VERSION.SDK_INT);
	}

	public static String getApplicationVersion() {
		PackageInfo pInfo = null;
		try {
			pInfo = ApplicationLoader
					.getApplication()
					.getApplicationContext()
					.getPackageManager()
					.getPackageInfo(
							ApplicationLoader.getApplication()
									.getApplicationContext().getPackageName(),
							0);
			return pInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "VERSION NAME NOT FOUND";
		}
	}
	
	public static String getApplicationVersionCode() {
		PackageInfo pInfo = null;
		try {
			pInfo = ApplicationLoader
					.getApplication()
					.getApplicationContext()
					.getPackageManager()
					.getPackageInfo(
							ApplicationLoader.getApplication()
									.getApplicationContext().getPackageName(),
							0);
			return String.valueOf(pInfo.versionCode);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "VERSION NAME NOT FOUND";
		}
	}
	
	/** Open another app.
	 * @param context current Context, like Activity, App, or Service
	 * @param packageName the full package name of the app to open
	 * @return true if likely successful, false if unsuccessful
	 */
	public static boolean openApp(Context context, String packageName) {
	    PackageManager manager = context.getPackageManager();
	    try {
	        Intent i = manager.getLaunchIntentForPackage(packageName);
	        if (i == null) {
	            return false;
	            //throw new PackageManager.NameNotFoundException();
	        }
	        i.addCategory(Intent.CATEGORY_LAUNCHER);
	        context.startActivity(i);
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	public static boolean isInternetConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) ApplicationLoader.applicationContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null
				&& activeNetworkInfo.isConnectedOrConnecting();
	}
	
	public static boolean isSuccessFromApi(String mResponseFromApi){
		try {
			return new JSONObject(mResponseFromApi).getBoolean(AppConstants.API_KEY_PARAMETER.success);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		} 
		return false;
	}
	
	public static String getErrorMessageFromApi(String mResponseFromApi){
		try {
			return new JSONObject(mResponseFromApi)
					.getString(AppConstants.API_KEY_PARAMETER.errorMessage);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return "Please try again after sometime!";
	}
	
	
	public static String getSuccessMessageFromApi(String mResponseFromApi){
		try {
			return new JSONObject(mResponseFromApi)
					.getString(AppConstants.API_KEY_PARAMETER.successMessage);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return  "Successfully!";
	}
	
	public static String readFile(String s) {
		BufferedReader r;
		StringBuilder str = new StringBuilder();
		try {
			r = new BufferedReader(new InputStreamReader(ApplicationLoader
					.getApplication().getApplicationContext().getAssets()
					.open(s)));
			String line;
			while ((line = r.readLine()) != null) {
				str.append(line);
			}
		} catch (IOException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

		return str.toString();
	}
	
	public static String getFileName(String mFilePath){
		return mFilePath.substring(mFilePath.lastIndexOf("/") + 1,
				mFilePath.length());
	}
	
	public static String getFilePath(int mType, boolean mIsThumbnail, String mFileName){
		createDirectories();
		if(!mIsThumbnail){
			switch(mType){
			case AppConstants.TYPE.IMAGE:
				return AppConstants.FOLDER.IMAGE_FOLDER + mFileName;
			case AppConstants.TYPE.AUDIO:
				return AppConstants.FOLDER.AUDIO_FOLDER + mFileName;
			case AppConstants.TYPE.VIDEO:
				return AppConstants.FOLDER.VIDEO_FOLDER + mFileName;
			case AppConstants.TYPE.PROFILE:
				return AppConstants.FOLDER.PROFILE_FOLDER + mFileName;
			default:
				return AppConstants.FOLDER.DOCUMENT_FOLDER+ mFileName;
			}
		}else{
			return AppConstants.FOLDER.THUMBNAIL_FOLDER + mFileName;
		}
	}
	
	public static void createDirectories(){
		File mFileBuildDirectory = new File(AppConstants.FOLDER.BUILD_FOLDER);
		File mFileImageDirectory = new File(AppConstants.FOLDER.IMAGE_FOLDER);
		File mFileVideoDirectory = new File(AppConstants.FOLDER.VIDEO_FOLDER);
		File mFileAudioDirectory = new File(AppConstants.FOLDER.AUDIO_FOLDER);
		File mFileDocumentDirectory = new File(AppConstants.FOLDER.DOCUMENT_FOLDER);
		File mFileThumbnailDirectory = new File(AppConstants.FOLDER.THUMBNAIL_FOLDER);
		File mFileProfileDirectory = new File(AppConstants.FOLDER.PROFILE_FOLDER);
		File mFileLogDirectory = new File(AppConstants.FOLDER.LOG_FOLDER);
		
		mFileBuildDirectory.mkdirs();
		mFileImageDirectory.mkdirs();
		mFileVideoDirectory.mkdirs();
		mFileAudioDirectory.mkdirs();
		mFileDocumentDirectory.mkdirs();
		mFileThumbnailDirectory.mkdirs();
		mFileProfileDirectory.mkdirs();
		mFileLogDirectory.mkdirs();
	}
	
	@SuppressLint("DefaultLocale") 
	public static int getMediaType(String mType){
		if(mType.toLowerCase().equalsIgnoreCase("text")){
			return AppConstants.TYPE.TEXT;
		}else if(mType.toLowerCase().equalsIgnoreCase("image")){
			return AppConstants.TYPE.IMAGE;
		}else if(mType.toLowerCase().equalsIgnoreCase("audio")){
			return AppConstants.TYPE.AUDIO;
		}else if(mType.toLowerCase().equalsIgnoreCase("video")){
			return AppConstants.TYPE.VIDEO;
		}else if(mType.toLowerCase().equalsIgnoreCase("pdf")){
			return AppConstants.TYPE.PDF;
		}else if(mType.toLowerCase().equalsIgnoreCase("doc")){
			return AppConstants.TYPE.DOC;
		}else if(mType.toLowerCase().equalsIgnoreCase("ppt")){
			return AppConstants.TYPE.PPT;
		}else if(mType.toLowerCase().equalsIgnoreCase("xls")){
			return AppConstants.TYPE.XLS;
		}else if(mType.toLowerCase().equalsIgnoreCase("livestreamyoutube")){
			return AppConstants.TYPE.STREAM;
		}else if(mType.toLowerCase().equalsIgnoreCase("feedback")){
			return AppConstants.TYPE.FEEDBACK;
		}else if(mType.toLowerCase().equalsIgnoreCase("quiz")){
			return AppConstants.TYPE.QUIZ;
		}else if(mType.toLowerCase().equalsIgnoreCase("award")){
			return AppConstants.TYPE.AWARD;
		}else if(mType.toLowerCase().equalsIgnoreCase("event")){
			return AppConstants.TYPE.EVENT;
		}else{
			return AppConstants.TYPE.OTHER;	
		}
	}
	
	public static String getEncodedImageToByteArray(Bitmap mBitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		//this will convert image to byte[] 
		byte[] byteArrayImage = baos.toByteArray(); 
		// this will convert byte[] to string
		return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
	}
	
	public static String getEncodedFileToByteArray(String filePath) {
		String strFile = null;
		File file = new File(filePath);
		try {
			byte[] data = FileUtils.readFileToByteArray(file);
			strFile = Base64.encodeToString(data, Base64.NO_WRAP);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strFile;
	}
	
	@SuppressLint("SimpleDateFormat") 
	public static long getMilliSecond(String mDate, String mTime) {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date contentTime;
			contentTime = dateFormatter.parse(mDate + " " + mTime);
			Calendar mCal = Calendar.getInstance();
			mCal.setTime(contentTime);
			return mCal.getTimeInMillis();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Calendar.getInstance().getTimeInMillis();
		}

	}
	
	public static String formatCount(String mCount, int interation) {
		double n  = Double.parseDouble(mCount);
		char[] c = new char[]{'K', 'M', 'B', 'T'};
	    double d = ((long) n / 100) / 10.0;
	    boolean isRound = (d * 10) %10 == 0;//true if the decimal part is equal to 0 (then it's trimmed anyway)
	    return (d < 1000? //this determines the class, i.e. 'k', 'm' etc
	        ((d > 99.9 || isRound || (!isRound && d > 9.99)? //this decides whether to trim the decimals
	         (int) d * 10 / 10 : d + "" // (int) d * 10 / 10 drops the decimal
	         ) + "" + c[0]) 
	        : formatCount(String.valueOf(d), 1));
	}
	
	public static boolean isToEncryptFile(int mIntType) {
		switch (mIntType) {
		case AppConstants.TYPE.PDF:
			return false;
		case AppConstants.TYPE.XLS:
			return false;
		case AppConstants.TYPE.DOC:
			return false;
		case AppConstants.TYPE.PPT:
			return false;
		case AppConstants.TYPE.OTHER:
			return false;
		case AppConstants.TYPE.AUDIO:
			return false;
		case AppConstants.TYPE.IMAGE:
			return false;
		case AppConstants.TYPE.EVENT:
			return false;
		case AppConstants.TYPE.AWARD:
			return false;
		default:
			return true;
		}
	}
	
	public static boolean isContainsDecrypted(String mPath){
		if(mPath!=null && mPath.contains(AppConstants.decrypted)){
			return true;
		}
		return false;
	}
	
	public static void fbConcealEncryptFile(String TAG,File mPlainFile){
		try{
			FBConcealCrypto mCryptography = new FBConcealCrypto(mPlainFile);
			mCryptography.startEncryption();
			mCryptography = null;
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	public static String fbConcealDecryptFile(String TAG, File mEncryptedFile) {
		try{
			String mDecryptedPath = null;
			FBConcealCrypto mCryptography = new FBConcealCrypto(mEncryptedFile);
			mDecryptedPath = mCryptography.startDecryption();
			mCryptography = null;
			return mDecryptedPath;
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
			return null;
		}
	}
	
	public static String formatDaysLeft(String mDate, String mTime){
		try{
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append(getLeftDateTime(mDate, mTime));
			return strBuilder.toString();
		}catch(Exception e){
			return " Days Left";
		}
	}
	
	@SuppressLint("SimpleDateFormat") public static String getLeftDateTime(String mDate, String mTime){
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dateFormatterWithYear = new SimpleDateFormat("MMM dd yyyy");
			Date todayDate = dateFormatter.parse(dateFormatter.format(new Date()));
			Date contentDate = dateFormatter.parse(mDate);

			SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
			Date todayTime = timeFormatter.parse(timeFormatter.format(new Date()));
			Date contentTime = timeFormatter.parse(mTime);

			Calendar currentCalendar = Calendar.getInstance();
			currentCalendar.setTime(todayDate);
			Calendar targetCalendar = Calendar.getInstance();
			targetCalendar.setTime(contentDate);

			int currentWeek = currentCalendar.get(Calendar.DAY_OF_YEAR);
			int targetWeek = targetCalendar.get(Calendar.DAY_OF_YEAR);
			int numberOfDays = targetWeek - currentWeek;
			
			if (todayDate.getYear() == contentDate.getYear()) {
				if (todayDate.getMonth() == contentDate.getMonth()) {
					if (todayDate.getDate() == contentDate.getDate()) {
						int todayHours = todayTime.getHours();
						int contentHours = contentTime.getHours();
						int todayMinutes = todayTime.getMinutes();
						int contentMinutes = contentTime.getMinutes();
						if (todayHours == contentHours || (Math.abs(contentHours - todayHours) == 1 && contentMinutes < todayMinutes)) {
							int todaySeconds = todayTime.getSeconds();
							int contentSeconds = contentTime.getSeconds();
							if (todayMinutes == contentMinutes
									|| (Math.abs((contentMinutes - todayMinutes)) == 1 && todaySeconds < contentSeconds)) {
								if (contentSeconds >= todaySeconds)
									return String.format("%02d", contentSeconds - todaySeconds)
											+ " secs left";
								else
									return String.format("%02d", todaySeconds + 60 - contentSeconds)
											+ " secs ago";
							} else {
								if (contentMinutes <= todayMinutes)
									return String.format("%02d", todayMinutes - contentMinutes)
											+ " mins left";
								else
									return String.format("%02d", todayMinutes + 60 - contentMinutes)
											+ " mins ago";
							}
						} else {
							if (Math.abs(contentHours - todayHours) != 1)
								return String.format("%02d", Math.abs(todayHours - contentHours))
										+ " hours left";
							else
								return "01 hour left";
						}
					} else if (numberOfDays == 1){
						return String.format("%02d", Math.abs(numberOfDays)) + " day left";
					} else if(String.valueOf(numberOfDays).contains("-")){
						return String.format("%02d", Math.abs(numberOfDays)) + " days ago";
					}else{
						return String.format("%02d", Math.abs(numberOfDays)) + " days left";	
					}
				} else {
					if (numberOfDays == 1) {
						return String.format("%02d", Math.abs(numberOfDays)) + "day left";
					} else if(String.valueOf(numberOfDays).contains("-")){
						return String.format("%02d", Math.abs(numberOfDays)) + " days ago";
					}else{
						return String.format("%02d", Math.abs(numberOfDays)) + " days left";	
					}
				}
			} else {
				return dateFormatterWithYear.format(contentDate);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {
			FileLog.e(TAG, ex.toString());
		}
		return null;
	}
	
	
	public static String formatBy(String mBy, String mDate, String mTime){
		try{
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append(ApplicationLoader.getApplication().getResources().getString(R.string.content_by));
			strBuilder.append(" " + mBy + " - ");
			strBuilder.append(getByDateTime(mDate, mTime));
			return strBuilder.toString();
		}catch(Exception e){
			return "By : " +mBy;
		}
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("SimpleDateFormat") 
	public static String getByDateTime(String mDate, String mTime) {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dateFormatterWithYear = new SimpleDateFormat("MMM dd yyyy");
			SimpleDateFormat dateFormatterWithOutYear = new SimpleDateFormat("MMM dd");
			Date todayDate = dateFormatter.parse(dateFormatter.format(new Date()));
			Date contentDate = dateFormatter.parse(mDate);

			SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
			Date todayTime = timeFormatter.parse(timeFormatter.format(new Date()));
			Date contentTime = timeFormatter.parse(mTime);

			if (todayDate.getYear() == contentDate.getYear()) {
				Calendar currentCalendar = Calendar.getInstance();
				currentCalendar.setTime(todayDate);
				Calendar targetCalendar = Calendar.getInstance();
				targetCalendar.setTime(contentDate);

				int currentWeek = currentCalendar.get(Calendar.DAY_OF_YEAR);
				int targetWeek = targetCalendar.get(Calendar.DAY_OF_YEAR);
				int numberOfDays = Math.abs(currentWeek - targetWeek);

				if (todayDate.getMonth() == contentDate.getMonth()) {
					if (todayDate.getDate() == contentDate.getDate()) {
						int todayHours = todayTime.getHours();
						int contentHours = contentTime.getHours();
						int todayMinutes = todayTime.getMinutes();
						int contentMinutes = contentTime.getMinutes();
						if (todayHours == contentHours
								|| (Math.abs(todayHours - contentHours) == 1 && todayMinutes < contentMinutes)) {
							int todaySeconds = todayTime.getSeconds();
							int contentSeconds = contentTime.getSeconds();

							if (todayMinutes == contentMinutes
									|| (Math.abs((todayMinutes - contentMinutes)) == 1 && todaySeconds < contentSeconds)) {
								if (todaySeconds >= contentSeconds)
									return todaySeconds - contentSeconds
											+ " secs ago";
								else
									return todaySeconds + 60 - contentSeconds
											+ " secs ago";
							} else {
								if (todayMinutes >= contentMinutes)
									return todayMinutes - contentMinutes
											+ " mins ago";
								else
									return todayMinutes + 60 - contentMinutes
											+ " mins ago";
							}
						} else {
							if (Math.abs(todayHours - contentHours) != 1)
								return Math.abs(todayHours - contentHours)
										+ " hours ago";
							else
								return "1 hour ago";
						}
					} else if (numberOfDays < 7) // check if same week
					{
						if (numberOfDays == 1)
							return "Yesterday";

						String dayOfWeek = new SimpleDateFormat("EEEE",
								Locale.ENGLISH).format(contentDate);
						return dayOfWeek;
					} else {
						return dateFormatterWithOutYear.format(contentDate);
					}
				} else {
					if (numberOfDays < 7) // check if same week if different
											// month
					{
						if (numberOfDays == 1)
							return "Yesterday";

						String dayOfWeek = new SimpleDateFormat("EEEE",
								Locale.ENGLISH).format(contentDate);
						return dayOfWeek;
					}
					return dateFormatterWithOutYear.format(contentDate);
				}
			} else {
				return dateFormatterWithYear.format(contentDate);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {
			FileLog.e(TAG, ex.toString());
		}
		return null;
	}
	
	@SuppressLint("SimpleDateFormat") @SuppressWarnings("deprecation")
	public static String getDate(String mDate){
		try{
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			Date contentDate = dateFormatter.parse(mDate);
			return String.valueOf(contentDate.getDate());
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
			return " ";
		}
	}
	
	@SuppressLint({ "SimpleDateFormat", "DefaultLocale" }) @SuppressWarnings("deprecation")
	public static String getMonth(String mDate){
		try{
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			Date contentDate = dateFormatter.parse(mDate);
			return String.valueOf(String.valueOf(new SimpleDateFormat("MMM").format(contentDate)).toUpperCase());
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
			return " ";
		}
	}
	
	public static boolean downloadFile(int mType, boolean mIsThumbnail,boolean mIsEncrypt,String mUrl, String mFileName){
		File mFile = new File(Utilities.getFilePath(mType, mIsThumbnail, mFileName));
		
		return RetroFitClient.downloadFileWith(new OkHttpClient(), mUrl, mFile.getAbsolutePath(), mIsThumbnail, mIsEncrypt,TAG);
	}
	
	public static boolean checkWhetherInsertedOrNot(String TAG, Uri mUri) {
		try{
			if(String.valueOf(ContentUris.parseId(mUri)).matches("\\d+")){
				FileLog.e(TAG, "Inserted Successfully!");
				return true;
			}
			return false;
		}catch(NumberFormatException ex){
			FileLog.e(TAG, ex.toString());
			return false;
		}catch(UnsupportedOperationException en){
			FileLog.e(TAG, en.toString());
			return false;
		}
	}
	
	public static boolean checkIfFileExists(String mFilePath){
    	try{
    		if(new File(mFilePath).exists()){
    			return true;	
    		}else{
    			return false;	
    		}
    	}catch(Exception e){
    		return false;	
    	}
    }
	
	public static void writeBitmapToSDCard(Bitmap mBitmap, String mPath){
		try{
			createDirectories();
			File file = new File (mPath);
			if (file.exists()){
				file.delete(); 
			}
			try {
			       FileOutputStream out = new FileOutputStream(file);
			       mBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			       out.flush();
			       out.close();
			} catch (Exception e) {
			       e.printStackTrace();
			}
		}catch(Exception e){
			Log.i(TAG, e.toString());
		}
	}
	
	public static Drawable getRoundedBitmapForContextMenu(int mType) {
		int imageId = 0;
		switch (mType) {
		case AppConstants.TYPE.TEXT:
			imageId = R.drawable.ic_mobcast_text_focused;
			break;
		case AppConstants.TYPE.AUDIO:
			imageId = R.drawable.ic_mobcast_audio_focused;
			break;
		case AppConstants.TYPE.VIDEO:
			imageId = R.drawable.ic_mobcast_video_focused;
			break;
		case AppConstants.TYPE.IMAGE:
			imageId = R.drawable.ic_mobcast_image_focus;
			break;
		case AppConstants.TYPE.PDF:
			imageId = R.drawable.ic_mobcast_pdf_focused;
			break;
		case AppConstants.TYPE.XLS:
			imageId = R.drawable.ic_mobcast_xls_focused;
			break;
		case AppConstants.TYPE.PPT:
			imageId = R.drawable.ic_mobcast_ppt_focused;
			break;
		case AppConstants.TYPE.DOC:
			imageId = R.drawable.ic_mobcast_doc_focus;
			break;
		case AppConstants.TYPE.FEEDBACK:
			imageId = R.drawable.ic_mobcast_feedback_focused;
			break;
		case AppConstants.TYPE.QUIZ:
			imageId = R.drawable.ic_mobcast_quiz_focused;
			break;
		case AppConstants.TYPE.STREAM:
			imageId = R.drawable.ic_mobcast_video_focused;
			break;
		case AppConstants.TYPE.EVENT:
			imageId = R.drawable.ic_event_focused;
			break;
		case AppConstants.TYPE.RECRUITMENT:
			imageId = R.drawable.ic_drawer_recruitment;
			break;
		default:
			imageId = R.drawable.ic_launcher;
			break;
		}
		Bitmap src = BitmapFactory.decodeResource(ApplicationLoader.getApplication().getResources(), imageId);
		Bitmap dst;
		if (src.getWidth() >= src.getHeight()) {
			dst = Bitmap.createBitmap(src, src.getWidth() / 2 - src.getHeight()
					/ 2, 0, src.getHeight(), src.getHeight());
		} else {
			dst = Bitmap.createBitmap(src, 0,
					src.getHeight() / 2 - src.getWidth() / 2, src.getWidth(),
					src.getWidth());
		}
		RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory
				.create(ApplicationLoader.getApplication().getResources(), dst);
		roundedBitmapDrawable.setCornerRadius(dst.getWidth() / 2);
		roundedBitmapDrawable.setAntiAlias(true);
		return roundedBitmapDrawable;
	}
	
	public static Bitmap getRoundedBitmap(String mPath) {
		try{
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 4;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap src =  BitmapFactory.decodeFile(mPath,options);
			Bitmap dst;
			if (src.getWidth() >= src.getHeight()) {
				dst = Bitmap.createBitmap(src, src.getWidth() / 2 - src.getHeight()
						/ 2, 0, src.getHeight(), src.getHeight());
			} else {
				dst = Bitmap.createBitmap(src, 0,
						src.getHeight() / 2 - src.getWidth() / 2, src.getWidth(),
						src.getWidth());
			}
			RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(ApplicationLoader.getApplication().getResources(), dst);
			roundedBitmapDrawable.setCornerRadius(dst.getWidth() / 2);
			roundedBitmapDrawable.setAntiAlias(true);
			return dst;
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
		return null;
	}
	
	@SuppressLint("NewApi") 
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
	public static Bitmap blurBitmap(Bitmap bitmap) {

		Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);

		RenderScript rs = RenderScript.create(ApplicationLoader.getApplication().getApplicationContext());

		ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs,Element.U8_4(rs));

		Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
		Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

		blurScript.setRadius(25.f);

		blurScript.setInput(allIn);
		blurScript.forEach(allOut);

		allOut.copyTo(outBitmap);

		bitmap.recycle();

		rs.destroy();

		return outBitmap;

	}
	
	public static ArrayList<Theme> getThemeList(){
		ArrayList<Theme> mList = new ArrayList<>();
		int i = ApplicationLoader.getPreferences().getAppTheme();
		Theme Obj1 = new Theme("#ff254E7A", i==0?true:false);
		Theme Obj2 = new Theme("#ff3F51B5", i==1?true:false);
		Theme Obj3 = new Theme("#ff4CB050", i==2?true:false);
		Theme Obj4 = new Theme("#ffE91E63", i==3?true:false);
		Theme Obj5 = new Theme("#ff009688", i==4?true:false);
		Theme Obj6 = new Theme("#ff795548", i==5?true:false);
		
		mList.add(Obj1);
		mList.add(Obj2);
		mList.add(Obj3);
		mList.add(Obj4);
		mList.add(Obj5);
		mList.add(Obj6);
		return mList;
	}
	
	public static void deleteAppFolder(File fileOrDirectory) {
	    try{
//	    	if (fileOrDirectory.isDirectory()){
			if (!fileOrDirectory.isFile()) {
				for (File child : fileOrDirectory.listFiles()) {
					deleteAppFolder(child);
				}
			} else {
				fileOrDirectory.delete();
			}	
	    }catch(Exception e){
	    	FileLog.e(TAG, e.toString());
	    }
	}
	
	public static void dropDatabase() {
		try{
			String currentDBPath = "/data/data/"
					+ ApplicationLoader.getApplication().getPackageName()
					+ "/databases/ApplicationDB";
			new File(currentDBPath).delete();
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	public static void deleteTables(){
		try{
			Context mContext= ApplicationLoader.getApplication().getApplicationContext();
			mContext.getContentResolver().delete(DBConstant.Mobcast_Columns.CONTENT_URI, null, null);
			mContext.getContentResolver().delete(DBConstant.Training_Columns.CONTENT_URI, null, null);
			mContext.getContentResolver().delete(DBConstant.Award_Columns.CONTENT_URI, null, null);
			mContext.getContentResolver().delete(DBConstant.Event_Columns.CONTENT_URI, null, null);
			mContext.getContentResolver().delete(DBConstant.Birthday_Columns.CONTENT_URI, null, null);
			mContext.getContentResolver().delete(DBConstant.Mobcast_File_Columns.CONTENT_URI, null, null);
			mContext.getContentResolver().delete(DBConstant.Training_File_Columns.CONTENT_URI, null, null);
			mContext.getContentResolver().delete(DBConstant.Mobcast_Feedback_Columns.CONTENT_URI, null, null);
			mContext.getContentResolver().delete(DBConstant.Training_Quiz_Columns.CONTENT_URI, null, null);
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}
	
	public static void checkLogOut(){
		try{
			Context mContext= ApplicationLoader.getApplication().getApplicationContext();
			Log.i(TAG, ApplicationLoader.getPreferences().getAccessToken()!=null?ApplicationLoader.getPreferences().getAccessToken():"null");
			Log.i(TAG, String.valueOf(mContext.getContentResolver().query(DBConstant.Mobcast_Columns.CONTENT_URI, null, null, null, null).getCount()));
		}catch(Exception e){
			FileLog.e(TAG, e.toString());
		}
	}


	@SuppressWarnings("resource")
	public static void devSendDBInMail(Context c) {
		if(BuildVars.DEBUG_VERSION){
			try {
				File sd = Environment.getExternalStorageDirectory();

				if (sd.canWrite()) {
					String currentDBPath = "/data/data/"
							+ ApplicationLoader.getApplication().getPackageName()
							+ "/databases/ApplicationDB";
					String backupDBPath = "ApplicationDB.db_Dev.db";
					File currentDB = new File(currentDBPath);
					File backupDB = new File(sd, backupDBPath);

					if (currentDB.exists()) {
						FileChannel src = new FileInputStream(currentDB)
								.getChannel();
						FileChannel dst = new FileOutputStream(backupDB)
								.getChannel();
						dst.transferFrom(src, 0, src.size());
						src.close();
						dst.close();
						new MailTask(c, backupDB.getAbsolutePath()).execute();
					}
				}
			} catch (Exception e) {
			}
		}
	}

	public static class MailTask extends AsyncTask<String, Void, String> {
		public Context mContext;
		public ProgressDialog pDialog;
		private String compressedPath;

		public MailTask(Context c, String compressedPath) {
			this.compressedPath = compressedPath;
			mContext = c;
		}

		@Override
		protected String doInBackground(String... params) {
			/** MAIL SENDING */
			Mail m = new Mail(BuildVars.EMAIL_USERNAME,
					BuildVars.EMAIL_PASSWORD);
			String[] toArr = { BuildVars.EMAIL_TO };
			m.setTo(toArr);
			m.setFrom(BuildVars.EMAIL_USERNAME);
			m.setSubject(BuildVars.EMAIL_SUBJECT);
			// m.setBody("<html><body><b><p>Dear Sir,"
			// + "  Following are the details added on Portfolio Application."
			// + "  Name:"+ _name +"  Contact No:"+_contact
			// +"  Address:"+_address+"</p><p> These is autogenerated mail. </p></b></body></html>");

			m.setBody(BuildVars.EMAIL_BODY);
			try {
				if (compressedPath != null && compressedPath.length() > 0)
					m.addAttachment(compressedPath);
				if (m.send()) {
					Log.e("MailApp", "Mail sent successfully!");
				} else {
					Log.e("MailApp", "Could not send email");
				}
			} catch (Exception e) {
				Log.e("MailApp", "Could not send email", e);
			}
			return "MailSent";
		}

		@Override
		protected void onPostExecute(String result) {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}
	
}
