/**
 * 
 */
package com.application.ui.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain;
import com.facebook.crypto.Crypto;
import com.facebook.crypto.Entity;
import com.facebook.crypto.exception.CryptoInitializationException;
import com.facebook.crypto.exception.KeyChainException;
import com.facebook.crypto.util.SystemNativeCryptoLibrary;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class FacebookConcealActivity extends AppCompatActivity {
	private static final String TAG = FacebookConcealActivity.class
			.getSimpleName();

	private AppCompatTextView mEncryptionTv;
	private AppCompatTextView mDecryptionTv;

	private AppCompatButton mEncryptBtn;
	private AppCompatButton mDecryptBtn;

	private Crypto crypto;
	private Entity entity;

	private File mEncryptedFile;
	private File mDecryptedFile;
	private File mPlainFile;

	private String mEncryptStart;
	private String mEncryptEnd;

	private String mDecryptStart;
	private String mDecryptEnd;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook_conceal);
		initUi();
		setUiListener();
	}

	private void initUi() {
		mEncryptionTv = (AppCompatTextView) findViewById(R.id.activityFacebookConcealEncryptTv);
		mDecryptionTv = (AppCompatTextView) findViewById(R.id.activityFacebookConcealDecryptTv);

		mEncryptBtn = (AppCompatButton) findViewById(R.id.activityFacebookConcealEncrptBtn);
		mDecryptBtn = (AppCompatButton) findViewById(R.id.activityFacebookConcealDecryptBtn);
	}

	private void setUiListener() {
		setClickListener();
	}

	private void setClickListener() {
		mEncryptBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startEncryption();
			}
		});

		mDecryptBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startDecryption();
			}
		});
	}

	private void startEncryption() {
		// Creates a new Crypto object with default implementations of
		// a key chain as well as native library.
		AppCompatDialog mAppCompatDialog = new AppCompatDialog(
				FacebookConcealActivity.this);
		mAppCompatDialog.setTitle("Encrypting...");
		mAppCompatDialog.show();
		mEncryptStart = String.valueOf(System.currentTimeMillis());
		crypto = new Crypto(new SharedPrefsBackedKeyChain(
				FacebookConcealActivity.this), new SystemNativeCryptoLibrary());

		// Check for whether the crypto functionality is available
		// This might fail if android does not load libaries correctly.
		if (!crypto.isAvailable()) {
			return;
		}

		mEncryptedFile = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/.con/Encrypted.mp4");
		OutputStream fileStream;
		try {
			fileStream = new BufferedOutputStream(new FileOutputStream(
					mEncryptedFile));
			entity = new Entity("mobcast");
			// Creates an output stream which encrypts the data as
			// it is written to it and writes it out to the file.
			OutputStream outputStream;
			outputStream = crypto.getCipherOutputStream(fileStream, entity);
			mPlainFile = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/download/IntroProtection3.mp4");
			outputStream.write(FileUtils.readFileToByteArray(mPlainFile));
			outputStream.close();
			mEncryptEnd = String.valueOf(System.currentTimeMillis());
			mAppCompatDialog.dismiss();
			mEncryptionTv
					.setText(String.valueOf((Long.valueOf(mEncryptEnd) - Long
							.valueOf(mEncryptStart)) / 1000));

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CryptoInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyChainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void startDecryption() {
		// Get the file to which ciphertext has been written.
		try {
			AppCompatDialog mAppCompatDialog = new AppCompatDialog(
					FacebookConcealActivity.this);
			mAppCompatDialog.setTitle("Decrypting...");
			mAppCompatDialog.show();
			mDecryptStart = String.valueOf(System.currentTimeMillis());
			FileInputStream fileStream = new FileInputStream(mEncryptedFile);

			// Creates an input stream which decrypts the data as
			// it is read from it.
			InputStream inputStream;
			inputStream = crypto.getCipherInputStream(fileStream, entity);

			ByteArrayOutputStream out = new ByteArrayOutputStream();

			// Read into a byte array.
			int read;
			byte[] buffer = new byte[1024];

			// You must read the entire stream to completion.
			// The verification is done at the end of the stream.
			// Thus not reading till the end of the stream will cause
			// a security bug.
			while ((read = inputStream.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}

			mDecryptedFile = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/.con/Decrypted.mp4");
			
			OutputStream outputStream = new FileOutputStream (mDecryptedFile); 
			out.writeTo(outputStream);
			
			inputStream.close();
			mDecryptEnd = String.valueOf(System.currentTimeMillis());
			mAppCompatDialog.dismiss();
			mDecryptionTv
					.setText(String.valueOf((Long.valueOf(mDecryptEnd) - Long
							.valueOf(mDecryptStart)) / 1000));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CryptoInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyChainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
