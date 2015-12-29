package com.daxiangqun.utils;

import java.util.ArrayList;

import com.daxiangqun.listener.SavePhoneListener;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;

public class SaveOnePhoneCode {
	public static void CopyOnePhone(Context ctx, SavePhoneListener listener, String name, String tel, String phone1,
			String phone2, String email) {
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		int rawContactInsertIndex;
		rawContactInsertIndex = ops.size();
		ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
				.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
				.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).withYieldAllowed(true).build());
		if (!TextUtils.isEmpty(name)) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
					.withValue(ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
					.withYieldAllowed(true).build());
		}
		if (!TextUtils.isEmpty(tel)) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, tel)
					.withValue(ContactsContract.CommonDataKinds.Phone.TYPE, Phone.TYPE_TELEX).withYieldAllowed(true)
					.build());
		}
		if (!TextUtils.isEmpty(phone1)) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone1)
					.withValue(ContactsContract.CommonDataKinds.Phone.TYPE, Phone.TYPE_MOBILE).withYieldAllowed(true)
					.build());
		}
		if (!TextUtils.isEmpty(phone2)) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone2)
					.withValue(ContactsContract.CommonDataKinds.Phone.TYPE, Phone.TYPE_WORK_MOBILE)
					.withYieldAllowed(true).build());
		}
		if (!TextUtils.isEmpty(email)) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
					.withValue(ContactsContract.CommonDataKinds.Email.TYPE, "1").build());
		}
		// 这里才调用的批量添加
		try {
			ctx.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
			listener.onSavePhoneListener(0);
		} catch (RemoteException e) {
			e.printStackTrace();
			listener.onSavePhoneListener(1);
		} catch (OperationApplicationException e) {
			e.printStackTrace();
			listener.onSavePhoneListener(1);
		}
	}
}
