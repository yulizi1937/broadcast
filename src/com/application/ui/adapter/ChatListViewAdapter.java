/**
 * 
 */
package com.application.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.application.beans.MessageObject;
import com.mobcast.R;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 * 
 */
public class ChatListViewAdapter extends BaseFragmentAdapter {
	private static final String TAG = ChatListViewAdapter.class.getSimpleName();

	private Context mContext;
	private ArrayList<MessageObject> mListMessageObject;

	public ChatListViewAdapter(Context mContext,
			ArrayList<MessageObject> mListMessageObject) {
		this.mContext = mContext;
		this.mListMessageObject = mListMessageObject;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int i) {
		return true;
	}

	@Override
	public int getCount() {
		int count = mListMessageObject.size();
		return count;
	}

	public void addMessageObject(MessageObject messageObject) {
		this.mListMessageObject.add(messageObject);
		notifyDataSetChanged();
	}

	public void addMessageObjectList(
			ArrayList<MessageObject> mListMessageObjectCollections) {
		this.mListMessageObject.addAll(mListMessageObjectCollections);
		notifyDataSetChanged();
	}

	public void syncMessageObjectList(
			ArrayList<MessageObject> mListMessageObjectCollections) {
		this.mListMessageObject = mListMessageObjectCollections;
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int i) {
		return null;
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int i, View convertView, ViewGroup viewGroup) {
		int offset = 1;
		View v = null;
		final MessageObject messageObj = mListMessageObject.get(i);
		MessageObject messageObjLast = null;
		int type = messageObj.getMessageType();
		if (type == 0) {
			RelativeLayout mSingleMessageContainerLeft;
			TextView mSingleMessageTextLeft;

			RelativeLayout mSingleMessageContainerRight;
			TextView mSingleMessageTextRight;

			LayoutInflater li = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = li.inflate(R.layout.item_chat_text, viewGroup, false);
			mSingleMessageContainerLeft = (RelativeLayout) v.findViewById(R.id.chatTextContainerLeft);
			mSingleMessageTextLeft = (TextView) v.findViewById(R.id.chatTextTextLeft);

			mSingleMessageContainerRight = (RelativeLayout) v.findViewById(R.id.chatTextContainerRight);
			mSingleMessageTextRight = (TextView) v.findViewById(R.id.chatTextTextRight);


			try {
				if (i != 0) {
					messageObjLast = mListMessageObject.get(i - 1);
				} else {
					messageObjLast = new MessageObject();
					messageObjLast.setMessageDate("0000000");
				}
			} catch (Exception e) {
				Log.i(TAG, e.toString());
			}

			setMessageObject(messageObj, messageObjLast,
					mSingleMessageContainerLeft, mSingleMessageContainerRight,
					mSingleMessageTextLeft, mSingleMessageTextRight);
		}

		return v;
	}

	@Override
	public int getItemViewType(int i) {
		MessageObject message = mListMessageObject.get(i);
		return message.getMessageType();
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public boolean isEmpty() {
		int count = mListMessageObject.size();
		return count == 0;
	}

	public void setMessageObject(final MessageObject messageObj,
			MessageObject messageObjLast,
			RelativeLayout mSingleMessageContainerLeft,
			RelativeLayout mSingleMessageContainerRight,
			TextView mSingleMessageTextLeft, TextView mSingleMessageTextRight) {

		if (messageObj.isThisUserSentRight()) {
			mSingleMessageContainerRight.setVisibility(View.VISIBLE);
			mSingleMessageContainerLeft.setVisibility(View.GONE);
			mSingleMessageTextRight.setText(messageObj.getMessageText());
		} else {
			mSingleMessageContainerRight.setVisibility(View.GONE);
			mSingleMessageContainerLeft.setVisibility(View.VISIBLE);
			mSingleMessageTextLeft.setText(messageObj.getMessageText());
		}
	}

}
