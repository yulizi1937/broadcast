/**
 * 
 */
package com.application.utils;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain;
import com.facebook.crypto.Crypto;
import com.facebook.crypto.Entity;
import com.facebook.crypto.exception.CryptoInitializationException;
import com.facebook.crypto.exception.KeyChainException;
import com.facebook.crypto.util.SystemNativeCryptoLibrary;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class FBConcealCrypto {
	private static final String TAG = FBConcealCrypto.class.getSimpleName();

	private Crypto crypto;
	private Entity entity;
	private File mDecryptedFile;
	private File mPlainFile;

	public FBConcealCrypto(File mPlainFile) {
		entity = new Entity("mobcast");
		crypto = new Crypto(new SharedPrefsBackedKeyChain(ApplicationLoader
				.getApplication().getApplicationContext()),
				new SystemNativeCryptoLibrary());
		this.mPlainFile = mPlainFile;
	}

	public void startEncryption() {
		/*
		 * Creates a new Crypto object with default implementations of a key
		 * chain as well as native library. Check for whether the crypto
		 * functionality is available This might fail if android does not load
		 * libaries correctly.
		 */
		if (!crypto.isAvailable()) {
			return;
		}
		OutputStream fileStream;
		try {
			File mEncryptedFile = new File(mPlainFile.getPath().substring(0,
					mPlainFile.getPath().length() - 4)
					+ AppConstants.encrypted
					+ mPlainFile.getPath().substring(
							mPlainFile.getPath().length() - 4,
							mPlainFile.getPath().length()));

			fileStream = new BufferedOutputStream(new FileOutputStream(
					mEncryptedFile));

			/*
			 * Creates an output stream which encrypts the data as it is written
			 * to it and writes it out to the file.
			 */
			OutputStream outputStream;
			outputStream = crypto.getCipherOutputStream(fileStream, entity);
			// Read into a byte array.
			int read;
			byte[] buffer = new byte[1024];
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(mPlainFile));
			while ((read = bis.read(buffer)) != -1) {
				outputStream.write(buffer, 0, read);
			}
			outputStream.close();
			bis.close();
			File mRenameTo = new File(mPlainFile.getPath());
			mPlainFile.delete();
			mEncryptedFile.renameTo(mRenameTo);
			FileLog.e(TAG, "Encrypted to :" + mRenameTo.getName());
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e1.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		} catch (CryptoInitializationException e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		} catch (KeyChainException e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
	}

	public String startDecryption() {
		/* Get the file to which ciphertext has been written. */
		try {
			FileInputStream fileStream = new FileInputStream(mPlainFile);

			/*
			 * Creates an input stream which decrypts the data as it is read
			 * from it.
			 */
			InputStream inputStream;
			inputStream = crypto.getCipherInputStream(fileStream, entity);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			mDecryptedFile = new File(mPlainFile.getPath().substring(0,
					mPlainFile.getPath().length() - 4)
					+ AppConstants.decrypted
					+ (mPlainFile.getPath().substring(mPlainFile.getPath()
							.length() - 4, mPlainFile.getPath().length())));

			OutputStream outputStream = new FileOutputStream(mDecryptedFile);
			/* Trying with BufferedOutputStream */
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			int mRead;
			byte[] mBuffer = new byte[1024];
			while ((mRead = bis.read(mBuffer)) != -1) {
				outputStream.write(mBuffer, 0, mRead);
			}
			bis.close();
			out.writeTo(outputStream);
			inputStream.close();
			outputStream.close();
			out.close();
			FileLog.e(TAG, "Decrypted to :" + mDecryptedFile.getName());
			return mDecryptedFile.getPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		} catch (CryptoInitializationException e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		} catch (KeyChainException e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FileLog.e(TAG, e.toString());
		}
		return null;
	}

	public void deleteDecryptedFile() {
		try {
			mDecryptedFile.delete();
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		}
	}

}
