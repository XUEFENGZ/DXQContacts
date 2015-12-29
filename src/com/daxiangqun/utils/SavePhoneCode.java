package com.daxiangqun.utils;

import java.util.ArrayList;
import java.util.List;

import com.daxiangqun.contacts.db.PersonContacts;
import com.daxiangqun.listener.SavePhoneListener;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;

public class SavePhoneCode {
	public static void CopyAll2Phone(List<PersonContacts> list, Context ctx, SavePhoneListener listener) {
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		int rawContactInsertIndex;
		for (int i = 0; i < list.size(); i++) {
			rawContactInsertIndex = ops.size();// 这句好很重要，有了它才能给真正的实现批量添加。
			ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
					.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
					.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).withYieldAllowed(true).build());
			if(!TextUtils.isEmpty(list.get(i).getTitle())){
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
						.withValue(ContactsContract.Data.MIMETYPE,
								ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
						.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, list.get(i).getTitle())
						.withYieldAllowed(true).build());
			}
			if(!TextUtils.isEmpty(list.get(i).getTel())){
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
						.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, list.get(i).getTel())
						.withValue(ContactsContract.CommonDataKinds.Phone.TYPE, Phone.TYPE_TELEX).withYieldAllowed(true)
						.build());
			}
			if(!TextUtils.isEmpty(list.get(i).getPhone1())){
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
						.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, list.get(i).getPhone1())
						.withValue(ContactsContract.CommonDataKinds.Phone.TYPE, Phone.TYPE_MOBILE).withYieldAllowed(true)
						.build());
			}
			if(!TextUtils.isEmpty(list.get(i).getPhone2())){
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
						.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, list.get(i).getPhone2())
						.withValue(ContactsContract.CommonDataKinds.Phone.TYPE, Phone.TYPE_WORK_MOBILE).withYieldAllowed(true)
						.build());
			}
			if(!TextUtils.isEmpty(list.get(i).getEmail())){
				ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)  
			            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)  
			            .withValue(ContactsContract.Data.MIMETYPE,  
			                    ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)  
			            .withValue(ContactsContract.CommonDataKinds.Email.DATA, list.get(i).getEmail())  
			            .withValue(ContactsContract.CommonDataKinds.Email.TYPE, "1")  
			            .build());  
			}
		}
		try {
			// 这里才调用的批量添加
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
