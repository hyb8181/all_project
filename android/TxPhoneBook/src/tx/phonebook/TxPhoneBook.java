
package tx.phonebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import tx.connect.R;
import tx.phonebook.util.file.FileUtils;
import tx.phonebook.util.http.HttpDownloader;
import tx.phonebook.util.http.PostJSONTextbyStream;
import tx.phonebook.data.proc.JsonUtils;
import tx.phonebook.data.proc.PersonInfo;
import tx.phonebook.data.proc.PersonUpdate;
import tx.phonebook.data.proc.PhoneBook;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.Contacts.People;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

public class TxPhoneBook extends Activity  {
	private Button uploadBut;
	private Button downloadBut;
	protected CursorAdapter mCursorAdapter;
	protected Cursor mCursor = null;
	protected Cursor cursor = null;
	private String phoneImsi = "";
	private String phoneUserid = "";

	//读取SD卡文件数据
    public String readDataSdcard(String strpath, String strfileName){
    	String strOutput = "";
    	FileUtils fileHelper = new FileUtils();
    	return fileHelper.readFromSDCard(strpath, strfileName);
    }
    //保存数据到SD卡
    public boolean backupDataSdcard(String strpath, String strfileName, String strData){
    	FileUtils fileHelper = new FileUtils();
        File file = fileHelper.writeToSDCard(strpath, strfileName, strData);
    	return true;
    }
	public void phonebookInit(){
		//初始化imsi,useid
		phoneUserid = "60815935";
		//phoneImsi = "310260000000000";
		TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		phoneImsi =tm.getSimSerialNumber();

		//读取电话簿内容至SD卡中
		readContactToSdcard();
	}
	//读取电话簿内容 wj
    public void readContactToSdcard()
    {
    	int nIndex = 0;
    	int nTotal = 4;
    	int nCurIndex = 0;
    	ContentResolver cr = getContentResolver();
    	Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
    	//电话簿内容

    	PhoneBook ContentPhonebook[] = new PhoneBook[cur.getCount()];
    	//PersonInfo ContentPersonInfo[] = new PersonInfo[cur.getCount()];
    	if (cur.getCount() > 0)
        {
        	while (cur.moveToNext())
	        {
        		PhoneBook myContentPhonebook = new PhoneBook();
        		PersonInfo myContentPersonInfo = new PersonInfo();
        		ContentPhonebook[nCurIndex] = myContentPhonebook;
        		//ContentPersonInfo[nCurIndex] = myContentPersonInfo;
        		ContentPhonebook[nCurIndex].setPsInfo(myContentPersonInfo);
	        	//读取名字
	        	String rawContactsId = "";
	            String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
		        String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		       // str += "USERID:" + id + "\n";
		        ContentPhonebook[nCurIndex].setAction(null);
		        ContentPhonebook[nCurIndex].setUserId(phoneImsi);
		        ContentPhonebook[nCurIndex].setImsi(phoneUserid);
		        ContentPhonebook[nCurIndex].setPsId(id);
		        ContentPhonebook[nCurIndex].getPsInfo().setStrId(id);

		        // 读取rawContactsId
		        Cursor rawContactsIdCur = cr.query(RawContacts.CONTENT_URI,
    					null,
    					RawContacts.CONTACT_ID +" = ?",
    					new String[]{id}, null);

		        if (rawContactsIdCur.moveToFirst())
		        {
		        	rawContactsId = rawContactsIdCur.getString(rawContactsIdCur.getColumnIndex(RawContacts._ID));

		        }
		        rawContactsIdCur.close();
		        ContentPhonebook[nCurIndex].getPsInfo().setStrName(name);

		        // 读取号码
		        if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
		        {
		        	//Uri phoneUri=Uri.parse("content://com.android.contacts/data/phones");
		        	// 下面的ContactsContract.CommonDataKinds.Phone.CONTENT_URI可以用phoneUri代替
		        	Cursor PhoneCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
		        					null,
		        					ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID +" = ?",
		        					new String[]{rawContactsId}, null);
		        	while (PhoneCur.moveToNext() && nIndex < nTotal)
		        	{
		        		String number = PhoneCur.getString(PhoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		        		String numberType = PhoneCur.getString(PhoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

		        		if(nIndex == 0)
		        		{
		        			ContentPhonebook[nCurIndex].getPsInfo().setStrNumber1_type(numberType);
		        			ContentPhonebook[nCurIndex].getPsInfo().setStrNumber1(number);
		        		}
		        		else if(nIndex == 1)
		        		{
		        			ContentPhonebook[nCurIndex].getPsInfo().setStrNumber2_type(numberType);
		        			ContentPhonebook[nCurIndex].getPsInfo().setStrNumber2(number);
		        		}
		        		else if(nIndex == 2)
		        		{
		        			ContentPhonebook[nCurIndex].getPsInfo().setStrNumber3_type(numberType);
		        			ContentPhonebook[nCurIndex].getPsInfo().setStrNumber3(number);
		        		}
		        		else if(nIndex == 3)
		        		{
		        			ContentPhonebook[nCurIndex].getPsInfo().setStrNumber4_type(numberType);
		        			ContentPhonebook[nCurIndex].getPsInfo().setStrNumber4(number);
		        		}

		        		nIndex++;
		        	}
		        	nIndex = 0;
		        	PhoneCur.close();
		        }

		        // 读取Email
		        //Uri emailUri=Uri.parse("content://com.android.contacts/data/emails");
		        // 下面的ContactsContract.CommonDataKinds.Email.CONTENT_URI可以用emailUri代替
		        Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
					        	    null,
					        	    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
					        	    new String[]{id}, null);
	        	while (emailCur.moveToNext() && nIndex < nTotal)
	        	{
	        	    String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
	        	    String emailType = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

	        	    if(nIndex == 0)
	        	    {
	        	    	ContentPhonebook[nCurIndex].getPsInfo().setStrEmail1(email);
	        	    	ContentPhonebook[nCurIndex].getPsInfo().setStrEmail1_type(emailType);
	        	    }else if(nIndex == 1)
	        	    {
	        	    	ContentPhonebook[nCurIndex].getPsInfo().setStrEmail2(email);
	        	    	ContentPhonebook[nCurIndex].getPsInfo().setStrEmail2_type(emailType);
	        	    }else if(nIndex == 2)
	        	    {
	        	    	ContentPhonebook[nCurIndex].getPsInfo().setStrEmail3(email);
	        	    	ContentPhonebook[nCurIndex].getPsInfo().setStrEmail3_type(emailType);
	        	    }else if(nIndex == 3)
	        	    {
	        	    	ContentPhonebook[nCurIndex].getPsInfo().setStrEmail4(email);
	        	    	ContentPhonebook[nCurIndex].getPsInfo().setStrEmail4_type(emailType);
	        	    }
	        		nIndex++;
	        	}
	        	/*Email类型：
	        	1：TYPE_HOME
				2：TYPE_WORK
				3：TYPE_OTHER
				4：TYPE_MOBILE
	        	*/
        		nIndex = 0;
		        emailCur.close();

		        // 读取邮政地址
		        String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
		        String[] addrWhereParams = new String[]{id,ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
		        Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI, null, addrWhere, addrWhereParams, null);
		        while(addrCur.moveToNext() && nIndex < nTotal)
		        {
	                String street = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
		            String poBox = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
	                String square = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD));
	                String city = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
	                String state = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
	                String postalCode = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
	                String country = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
	                String type = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));



                	if(nIndex == 0){
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd1_Street(street);
						ContentPhonebook[nCurIndex].getPsInfo().setStrAdd1_POBox(poBox);
						ContentPhonebook[nCurIndex].getPsInfo().setStrAdd1_Suqare(square);
						ContentPhonebook[nCurIndex].getPsInfo().setStrAdd1_City(city);
						ContentPhonebook[nCurIndex].getPsInfo().setStrAdd1_State(state);
						ContentPhonebook[nCurIndex].getPsInfo().setStrAdd1_PostalCode(postalCode);
						ContentPhonebook[nCurIndex].getPsInfo().setStrAdd1_Country(country);
						ContentPhonebook[nCurIndex].getPsInfo().setStrAdd2_type(type);
                	}else if(nIndex == 1){
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd2_Street(street);
	                	ContentPhonebook[nCurIndex].getPsInfo().setStrAdd2_POBox(poBox);
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd1_Suqare(square);
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd2_City(city);
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd2_State(state);
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd2_PostalCode(postalCode);
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd2_Country(country);
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd2_type(type);
                	}else if(nIndex == 2){
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd3_Street(street);
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd3_POBox(poBox);
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd1_Suqare(square);
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd3_City(city);
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd3_State(state);
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd3_PostalCode(postalCode);
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd3_Country(country);
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd3_type(type);
                	}else if(nIndex == 3){
	                	ContentPhonebook[nCurIndex].getPsInfo().setStrAdd4_Street(street);
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd4_POBox(poBox);
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd1_Suqare(square);
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd4_City(city);
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd4_State(state);
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd4_PostalCode(postalCode);
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd4_Country(country);
                		ContentPhonebook[nCurIndex].getPsInfo().setStrAdd4_type(type);
                	}
                	nIndex++;
		        }
		        nIndex = 0;
		        addrCur.close();

	        	// 读取公司及职位
	        	String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
	            String[] orgWhereParams = new String[]{id,ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
	            Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI,null, orgWhere, orgWhereParams, null);
	            while (orgCur.moveToNext() && nIndex < 1)
	            {
	                String orgName = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
	                String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
	                ContentPhonebook[nCurIndex].getPsInfo().setStrOrgCmp(orgName);
	                ContentPhonebook[nCurIndex].getPsInfo().setStrOrgPoz(title);
	            }
	            orgCur.close();
	            nCurIndex++;
	        }
        }
    	//将对象改为json并存入文件
		String json = "";
		List<PhoneBook> list = new ArrayList<PhoneBook>();
		for(int i=0;i<nCurIndex;i++){
			list.add(ContentPhonebook[i]);
		}
		json= JsonUtils.book2Json(list);

		this.backupDataSdcard("TxPhonebook","phone.dat",json);
		Log.e("phoneinitdata", json);
    }
    public void  insertPhonebookDataTest()    {
    	PersonInfo psif = new PersonInfo();
    	psif.setStrName("54321");
    	psif.setStrNumber1("1111111");
    	psif.setStrNumber1_type("1");
    	psif.setStrNumber2("2222");
    	psif.setStrNumber2_type("2");
    	psif.setStrNumber3("3333");
    	psif.setStrNumber3_type("3");
    	psif.setStrNumber4("4444");
    	psif.setStrNumber4_type("4");
    	psif.setStrEmail1("11");
    	psif.setStrEmail1_type("1");
    	psif.setStrEmail2("22");
    	psif.setStrEmail2_type("2");
    	psif.setStrEmail3("33");
    	psif.setStrEmail3_type("3");
    	psif.setStrEmail4("44");
    	psif.setStrEmail4_type("4");
    	psif.setStrAdd1_Street("Street");
    	psif.setStrAdd1_POBox("POBox");
    	psif.setStrAdd1_Suqare("Suqare");
    	psif.setStrAdd1_City("City");
    	psif.setStrAdd1_State("State");
    	psif.setStrAdd1_PostalCode("222");
    	psif.setStrAdd1_Country("11");
    	psif.setStrAdd1_type("1");
    	psif.setStrAdd2_Street("2");
    	psif.setStrAdd2_POBox("2");
    	psif.setStrAdd2_Suqare("2");
    	psif.setStrAdd2_City("2");
    	psif.setStrAdd2_State("2");
    	psif.setStrAdd2_PostalCode("2");
    	psif.setStrAdd2_Country("2");
    	psif.setStrAdd2_type("2");
    	psif.setStrAdd3_Street("3");
    	psif.setStrAdd3_POBox("3");
    	psif.setStrAdd3_Suqare("3");
    	psif.setStrAdd3_City("3");
    	psif.setStrAdd3_State("3");
    	psif.setStrAdd3_PostalCode("3");
    	psif.setStrAdd3_Country("3");
    	psif.setStrAdd3_type("3");
    	psif.setStrAdd4_Street("4");
    	psif.setStrAdd4_POBox("4");
    	psif.setStrAdd4_Suqare("4");
    	psif.setStrAdd4_City("4");
    	psif.setStrAdd4_State("4");
    	psif.setStrAdd4_PostalCode("4");
    	psif.setStrAdd4_Country("4");
    	psif.setStrAdd4_type("4");
    	psif.setStrOrgCmp("1");
    	psif.setStrOrgPoz("2");

    	insertPhonebookData(psif);

    }
    //新建联系人 wj
    public boolean insertPhonebookData(PersonInfo personinfo)
    {
    	try
	    {
	    	ContentValues values = new ContentValues();

	    	// 下面的操作会根据RawContacts表中已有的rawContactId使用情况自动生成新联系人的rawContactId
	    	Uri rawContactUri = getContentResolver().insert(RawContacts.CONTENT_URI, values);
	    	long rawContactId = ContentUris.parseId(rawContactUri);

	    	// 向data表插入姓名数据
	    	if (personinfo.getStrName() != "")
	    	{
		    	values.clear();
		    	values.put(Data.RAW_CONTACT_ID, rawContactId);
		    	values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		    	values.put(StructuredName.GIVEN_NAME, personinfo.getStrName());
		    	getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
	    	}

	    	// 向data表插入电话数据
	    	if (personinfo.getStrNumber1() != null)
	    	{
		    	values.clear();
		    	values.put(Data.RAW_CONTACT_ID, rawContactId);
		    	values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		    	values.put(Phone.NUMBER, personinfo.getStrNumber1());
		    	values.put(Phone.TYPE, personinfo.getStrNumber1_type());
		    	getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
	    	}
	    	if (personinfo.getStrNumber2() != null)
	    	{
		    	values.clear();
		    	values.put(Data.RAW_CONTACT_ID, rawContactId);
		    	values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		    	values.put(Phone.NUMBER, personinfo.getStrNumber2());
		    	values.put(Phone.TYPE, personinfo.getStrNumber2_type());
		    	getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
	    	}
	    	if (personinfo.getStrNumber3() != null)
	    	{
		    	values.clear();
		    	values.put(Data.RAW_CONTACT_ID, rawContactId);
		    	values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		    	values.put(Phone.NUMBER, personinfo.getStrNumber3());
		    	values.put(Phone.TYPE, personinfo.getStrNumber3_type());
		    	getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
	    	}
	    	if (personinfo.getStrNumber4() != null)
	    	{
		    	values.clear();
		    	values.put(Data.RAW_CONTACT_ID, rawContactId);
		    	values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		    	values.put(Phone.NUMBER, personinfo.getStrNumber4());
		    	values.put(Phone.TYPE, personinfo.getStrNumber4_type());
		    	getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
	    	}
	    	// 向data表插入Email数据
	    	if (personinfo.getStrEmail1() != null)
	    	{
		    	values.clear();
		    	values.put(Data.RAW_CONTACT_ID, rawContactId);
		    	values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
		    	values.put(Email.DATA, personinfo.getStrEmail1());
		    	values.put(Email.TYPE, personinfo.getStrEmail1_type());
		    	getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
	    	}
	    	if (personinfo.getStrEmail2() != null)
	    	{
		    	values.clear();
		    	values.put(Data.RAW_CONTACT_ID, rawContactId);
		    	values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
		    	values.put(Email.DATA, personinfo.getStrEmail2());
		    	values.put(Email.TYPE, personinfo.getStrEmail2_type());
		    	getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
	    	}
	    	if (personinfo.getStrEmail3() != null)
	    	{
		    	values.clear();
		    	values.put(Data.RAW_CONTACT_ID, rawContactId);
		    	values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
		    	values.put(Email.DATA, personinfo.getStrEmail3());
		    	values.put(Email.TYPE, personinfo.getStrEmail3_type());
		    	getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
	    	}
	    	if (personinfo.getStrEmail4() != null)
	    	{
		    	values.clear();
		    	values.put(Data.RAW_CONTACT_ID, rawContactId);
		    	values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
		    	values.put(Email.DATA, personinfo.getStrEmail4());
		    	values.put(Email.TYPE, personinfo.getStrEmail4_type());
		    	getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);


	    	}
	    	//向data表插入地址数据
	    	if(personinfo.getStrAdd1_type() != null)
	    	{
	    		values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE,android.provider.ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
                if(personinfo.getStrAdd1_Street() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.STREET,personinfo.getStrAdd1_Street());//街道
                }
                if(personinfo.getStrAdd1_POBox() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.POBOX,personinfo.getStrAdd1_POBox());//信箱
                }
               if(personinfo.getStrAdd1_Suqare() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD,personinfo.getStrAdd1_Suqare());//街区
               }
                if(personinfo.getStrAdd1_City() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.CITY,personinfo.getStrAdd1_City());//城市
                }
                if(personinfo.getStrAdd1_State() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.REGION,personinfo.getStrAdd1_State());//省/直辖市/自治区
                }
                if(personinfo.getStrAdd1_PostalCode() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE,personinfo.getStrAdd1_PostalCode());//邮编
                }
                if(personinfo.getStrAdd1_Country() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY,personinfo.getStrAdd1_Country());//国家
                }
                values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.TYPE, personinfo.getStrAdd1_type());
                getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI,values);
	    	}
	    	if(personinfo.getStrAdd2_type() != null)
	    	{
	    		values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE,android.provider.ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
                if(personinfo.getStrAdd2_Street() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.STREET,personinfo.getStrAdd2_Street());//街道
                }
                if(personinfo.getStrAdd2_POBox() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.POBOX,personinfo.getStrAdd2_POBox());//信箱
                }

                if(personinfo.getStrAdd2_Suqare() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD,personinfo.getStrAdd2_Suqare());//街区
                }
                if(personinfo.getStrAdd2_City() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.CITY,personinfo.getStrAdd2_City());//城市
                }
                if(personinfo.getStrAdd2_State() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.REGION,personinfo.getStrAdd2_State());//省/直辖市/自治区
                }
                if(personinfo.getStrAdd2_PostalCode() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE,personinfo.getStrAdd2_PostalCode());//邮编
                }
                if(personinfo.getStrAdd2_Country() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY,personinfo.getStrAdd2_Country());//国家
                }
                values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.TYPE, personinfo.getStrAdd2_type());
                getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI,values);
	    	}
	    	if(personinfo.getStrAdd3_type() != null)
	    	{
	    		values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE,android.provider.ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
                if(personinfo.getStrAdd3_Street() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.STREET,personinfo.getStrAdd3_Street());//街道
                }
                if(personinfo.getStrAdd3_POBox() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.POBOX,personinfo.getStrAdd3_POBox());//信箱
                }

                if(personinfo.getStrAdd3_Suqare() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD,personinfo.getStrAdd3_Suqare());//街区
                }
                if(personinfo.getStrAdd3_City() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.CITY,personinfo.getStrAdd3_City());//城市
                }
                if(personinfo.getStrAdd3_State() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.REGION,personinfo.getStrAdd3_State());//省/直辖市/自治区
                }
                if(personinfo.getStrAdd3_PostalCode() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE,personinfo.getStrAdd3_PostalCode());//邮编
                }
                if(personinfo.getStrAdd3_Country() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY,personinfo.getStrAdd3_Country());//国家
                }
                values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.TYPE, personinfo.getStrAdd3_type());
                getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI,values);
	    	}
	    	if(personinfo.getStrAdd4_type() != null)
	    	{
	    		values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE,android.provider.ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
                if(personinfo.getStrAdd4_Street() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.STREET,personinfo.getStrAdd4_Street());//街道
                }
                if(personinfo.getStrAdd4_POBox() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.POBOX,personinfo.getStrAdd4_POBox());//信箱
                }
                if(personinfo.getStrAdd4_Suqare() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD,personinfo.getStrAdd4_Suqare());//街区
                }
                if(personinfo.getStrAdd4_City() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.CITY,personinfo.getStrAdd4_City());//城市
                }
                if(personinfo.getStrAdd4_State() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.REGION,personinfo.getStrAdd4_State());//省/直辖市/自治区
                }
                if(personinfo.getStrAdd4_PostalCode() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE,personinfo.getStrAdd4_PostalCode());//邮编
                }
                if(personinfo.getStrAdd4_Country() != null){
                    values.put(android.provider.ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY,personinfo.getStrAdd4_Country());//国家
                }
                getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI,values);
	    	}
	    	//向data表插入组织数据
    		values.clear();
            values.put(Data.RAW_CONTACT_ID, rawContactId);
            values.put(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.Organization.TYPE,"1");
         	if(personinfo.getStrOrgCmp() != null)
	    	{
         		values.put(ContactsContract.CommonDataKinds.Organization.DATA,personinfo.getStrOrgCmp());//公司名
	    	}
         	if(personinfo.getStrOrgPoz() != null)
         	{
         		values.put(ContactsContract.CommonDataKinds.Organization.TITLE,personinfo.getStrOrgPoz());//职位
         	}
         	getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
	    }
        catch (Exception e)
        {
            return false;
        }
        return true;
	}

    //更新联系人
    public boolean updatePhoneBookData(PersonInfo personinfo)
    {
    	try
    	{
    		String Where;
    		String[] WhereParams;
            ContentValues values = new ContentValues();
            values.clear();
            //更新名字
            values.put(ContactsContract.Data.RAW_CONTACT_ID, personinfo.getStrId());
            values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, personinfo.getStrName());
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);

            int NameRow = -1;
            String where = ContactsContract.Data.RAW_CONTACT_ID + " = " + personinfo.getStrId() + " AND " +
                            ContactsContract.Data.MIMETYPE + "=='" +
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE + "'";
            Cursor cursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, where, null, null);
            int idIdx = cursor.getColumnIndexOrThrow(ContactsContract.Data._ID);
            if (cursor.moveToFirst()) {
                NameRow = cursor.getInt(idIdx);
            }
            cursor.close();

            getContentResolver().update(ContactsContract.Data.CONTENT_URI, values,
                    ContactsContract.Data._ID + " = " + NameRow, null);


            //更新电话号码
            values.clear();
        	values.put(Phone.NUMBER, personinfo.getStrNumber1());
        	values.put(Phone.TYPE, personinfo.getStrNumber1_type());

        	Where = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
            WhereParams = new String[]{personinfo.getStrId(), Phone.CONTENT_ITEM_TYPE, };

        	getContentResolver().update(ContactsContract.Data.CONTENT_URI, values, Where, WhereParams);

            //更新电话号码
            values.clear();
        	values.put(Phone.NUMBER, personinfo.getStrNumber2());
        	values.put(Phone.TYPE, personinfo.getStrNumber2_type());

        	Where = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
            WhereParams = new String[]{personinfo.getStrId(), Phone.CONTENT_ITEM_TYPE, };

        	getContentResolver().update(ContactsContract.Data.CONTENT_URI, values, Where, WhereParams);
    	}
    	catch(Exception e)
    	{
    		return false;
    	}
    	return true;
    }
    public boolean insertData(String given_name, String mobile_number, String work_email, String im_qq)
    {
    	try
	    {
	    	ContentValues values = new ContentValues();

	    	// 下面的操作会根据RawContacts表中已有的rawContactId使用情况自动生成新联系人的rawContactId
	    	Uri rawContactUri = getContentResolver().insert(RawContacts.CONTENT_URI, values);
	    	long rawContactId = ContentUris.parseId(rawContactUri);

	    	// 向data表插入姓名数据
	    	if (given_name != "")
	    	{
		    	values.clear();
		    	values.put(Data.RAW_CONTACT_ID, rawContactId);
		    	values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		    	values.put(StructuredName.GIVEN_NAME, given_name);
		    	getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
	    	}

	    	// 向data表插入电话数据
	    	if (mobile_number != "")
	    	{
		    	values.clear();
		    	values.put(Data.RAW_CONTACT_ID, rawContactId);
		    	values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		    	values.put(Phone.NUMBER, mobile_number);
		    	values.put(Phone.TYPE, Phone.TYPE_MOBILE);
		    	getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
	    	}

	    	// 向data表插入Email数据
	    	if (work_email != "")
	    	{
		    	values.clear();
		    	values.put(Data.RAW_CONTACT_ID, rawContactId);
		    	values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
		    	values.put(Email.DATA, work_email);
		    	values.put(Email.TYPE, Email.TYPE_WORK);
		    	getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
	    	}

	    	// 向data表插入QQ数据
	    	if (im_qq != "")
	    	{
		    	values.clear();
		    	values.put(Data.RAW_CONTACT_ID, rawContactId);
		    	values.put(Data.MIMETYPE, Im.CONTENT_ITEM_TYPE);
		    	values.put(Im.DATA, im_qq);
		    	values.put(Im.PROTOCOL, Im.PROTOCOL_QQ);
		    	getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
	    	}
	    }

        catch (Exception e)
        {
            return false;
        }

        return true;
	}
    public void upLoadDataAll(){
    	String strUploadData = "";
		String strResponse = "";
    	String strSDPhoneOutput = "";
    	String strSDBackup = "";
    	Log.e("uploaddataall", "入口");
    	//从SD卡中读取本机电话簿内容通过json格式解析,拷贝至对象
    	strSDPhoneOutput = readDataSdcard("TxPhonebook","phone.dat");

		List<PhoneBook> ContactPhoneBookList = new ArrayList<PhoneBook>();
		Type ContactPblistType = new TypeToken<List<PhoneBook>>(){}.getType();
		Gson contactPblistgson = new Gson();
		ContactPhoneBookList = contactPblistgson.fromJson(strSDPhoneOutput, ContactPblistType);

    	//组织发送数据内容
		for(int i=0;i<ContactPhoneBookList.size();i++){
			ContactPhoneBookList.get(i).setAction("creat");
		}
		strUploadData= JsonUtils.book2Json(ContactPhoneBookList);

    	//发送数据
		PostJSONTextbyStream http = new PostJSONTextbyStream();
		strResponse = http.POST("http://192.168.56.190:8080/tx-mtk-server/androidclient/pbbackup.do",strUploadData);

		Log.e("Txphonbook", "上传数据:"+strUploadData);
		//备份至sd卡中
		for(int i=0;i<ContactPhoneBookList.size();i++){
			ContactPhoneBookList.get(i).setAction(null);
		}
		strSDBackup= JsonUtils.book2Json(ContactPhoneBookList);
    	this.backupDataSdcard("TxPhonebook","backup.dat",strSDBackup);
    	Log.e("uploaddataall", "备份数据"+strSDBackup);
}
  public void upLoadLatestData(){
    	String strUploadData = "";
		String strResponse = "";
    	String strSDPhoneOutput = "";
    	String strSDBackup = "";
    	String strSDBackupOutput = "";

    	//从SD卡中读取本机电话簿内容通过json格式解析,拷贝至对象
    	Log.e("upLoadLatestData", "入口");
    	strSDPhoneOutput = readDataSdcard("TxPhonebook","phone.dat");

		List<PhoneBook> ContactPhoneBookList = new ArrayList<PhoneBook>();
		Type ContactPblistType = new TypeToken<List<PhoneBook>>(){}.getType();
		Gson contactPblistgson = new Gson();
		ContactPhoneBookList = contactPblistgson.fromJson(strSDPhoneOutput, ContactPblistType);

		//如果文件不存在，则为第一次上传
    	FileUtils myfu = new FileUtils();
        if(false == myfu.isFileExist("TxPhonebook/backup.dat"))
        {
        	//组织发送数据内容
    		for(int i=0;i<ContactPhoneBookList.size();i++){
    			ContactPhoneBookList.get(i).setAction("creat");
    		}
    		strUploadData= JsonUtils.book2Json(ContactPhoneBookList);

        	//发送数据
    		PostJSONTextbyStream http = new PostJSONTextbyStream();
    		strResponse = http.POST("http://192.168.56.190:8080/tx-mtk-server/androidclient/pbbackup.do",strUploadData);

    		Log.e("Txphonbook", "第1次上传数据"+strUploadData);

    		//备份至sd卡中
    		for(int i=0;i<ContactPhoneBookList.size();i++){
    			ContactPhoneBookList.get(i).setAction(null);
    		}
    		strSDBackup= JsonUtils.book2Json(ContactPhoneBookList);
        	this.backupDataSdcard("TxPhonebook","backup.dat",strSDBackup);
    		Log.e("Txphonbook", "第1次备份数据"+strSDBackup);
       }
        //第二次上传
        else
        {
            //从SD卡中读取备份电话簿拷贝至对象,通过json格式解析
        	strSDBackupOutput = readDataSdcard("TxPhonebook","backup.dat");

    		List<PhoneBook> sdBackupPhoneBookList = new ArrayList<PhoneBook>();
    		Type sdBackupPblistType = new TypeToken<List<PhoneBook>>(){}.getType();
    		Gson sdBackupListgson = new Gson();
    		sdBackupPhoneBookList = sdBackupListgson.fromJson(strSDBackupOutput, sdBackupPblistType);

        	//当前索引
        	boolean bExist = false;
         	//进行数据比较，数据修改，上传SD中修改过的内容
        	for(int i = 0;i < ContactPhoneBookList.size();i++)//电话簿读取出的人数
        	{
        		for(int j = 0; j < sdBackupPhoneBookList.size(); j++)//SD备份文件人数
        		{
        			//有该联系人
        			if(ContactPhoneBookList.get(i).getPsInfo().getStrId().equals(sdBackupPhoneBookList.get(j).getPsInfo().getStrId()))
        			{
        	        	Log.e("有该联系人",ContactPhoneBookList.get(i).getPsInfo().getStrId());
        				//数据不同则替换
        				if(false == ContactPhoneBookList.get(i).getPsInfo().comparePersonInfo(sdBackupPhoneBookList.get(j).getPsInfo()))
        				{
            	        	Log.e("联系人相同，数据不同update",ContactPhoneBookList.get(i).getPsInfo().getStrId());
        					sdBackupPhoneBookList.get(j).setPsInfo(ContactPhoneBookList.get(i).getPsInfo());
        					sdBackupPhoneBookList.get(j).setPsId(ContactPhoneBookList.get(i).getPsInfo().getStrId());
        					sdBackupPhoneBookList.get(j).setAction("update");
        				}
        				bExist = true;
        			}
        		}
        		if(false == bExist)
        		{
       	    		//没有该联系人，拷贝该数据至SD卡数据中
        			PhoneBook pb = new PhoneBook();
        			pb.setPsInfo(ContactPhoneBookList.get(i).getPsInfo());
        			pb.setAction("creat");
        			pb.setUserId(phoneImsi);
        			pb.setImsi(phoneUserid);
        			pb.setPsId(ContactPhoneBookList.get(i).getPsInfo().getStrId());
        			sdBackupPhoneBookList.add(pb);
        		}
        		bExist = false;
        	}
        	//组织发送数据，如果state为new或者modify则为待发送数据
    		List<PhoneBook> sendList = new ArrayList<PhoneBook>();
    		for(int i = 0; i < sdBackupPhoneBookList.size(); i++){
    			if(null != sdBackupPhoneBookList.get(i).getAction()){
    				sendList.add(sdBackupPhoneBookList.get(i));
    			};
    		}
    		strUploadData= JsonUtils.book2Json(sendList);
    		//上传数据
    		if(strUploadData.length() > "[]".length()){
    			//如果有数据则上传
        		//PostJSONTextbyStream http = new PostJSONTextbyStream();
        		//http.POST("http://192.168.56.190:8080/tx-mtk-server/androidclient/pbbackup.do",strUploadData);
        		Log.e("Txphonbook", "第2次上传数据"+strUploadData);

            	//上传成功后删除备份文件，并将SD对象中内容备份至SD卡中
        		//发送完保存数据
        		for(int i=0;i<sdBackupPhoneBookList.size();i++){
        			if(null !=sdBackupPhoneBookList.get(i).getAction()){
            			sdBackupPhoneBookList.get(i).setAction(null);
        			}
        		}
        		strSDBackup= JsonUtils.book2Json(sdBackupPhoneBookList);
            	this.backupDataSdcard("TxPhonebook","backup.dat",strSDBackup);
            	Log.e("TxPhoneBook","第2次备份数据"+strSDBackup);
    		}
    		else
    		{
    			//提示数据已最新
    			Log.e("TxPhoneBook","提示数据已最新");
    		}
        }
    }

    public void downLoadDataTest(){

    	String strDownload = "";
		PostJSONTextbyStream http = new PostJSONTextbyStream();
		PersonUpdate book = new PersonUpdate();

		book.setStrUserId("608159360");
		//List<PersonUpdate> list = new ArrayList<PersonUpdate>();
		//list.add(book);

		String json = JsonUtils.psUpdatesigle2Json(book);
		System.out.println("======>json:"+json);

		strDownload = http.POST("http://192.168.56.190:8080/tx-mtk-server/androidclient/pbrecover.do",json);
    }
    public void downLoadDataAll(){
    	String strUploadData = "";
    	String strDownloadData = "";
    	String strSDOutput = "";
		boolean bExist = false;
    	//下载数据
    	PersonUpdate myPersonUpdate = new PersonUpdate();
    	myPersonUpdate.setStrUserId("608159360");

    	strUploadData= JsonUtils.psUpdatesigle2Json(myPersonUpdate);
	/*	PostJSONTextbyStream http = new PostJSONTextbyStream();
		strDownloadData = http.POST("http://192.168.56.190:8080/tx-mtk-server/androidclient/pbrecover.do",strUploadData);*/
    	//模拟下载数据
    	strDownloadData = readDataSdcard("TxPhonebook","download.dat");
		//下载下来的数据转换为对象
		List<PhoneBook> downloadPhonebookList = new ArrayList<PhoneBook>();
		Type sdBackupPblistType = new TypeToken<List<PhoneBook>>(){}.getType();
		Gson downloadPbListgson = new Gson();
		downloadPhonebookList = downloadPbListgson.fromJson(strDownloadData, sdBackupPblistType);

    	//读取backup.dat(服务器备份)文件
		strSDOutput = readDataSdcard("TxPhonebook","backup.dat");
		//拷贝至对象,通过json格式解析
		List<PhoneBook> sdPhonebookList = new ArrayList<PhoneBook>();
		Type listType = new TypeToken<List<PhoneBook>>(){}.getType();
		Gson sdgson = new Gson();
		sdPhonebookList = sdgson.fromJson(strSDOutput, listType);

    	//读取出来的与下载数据比较，如有修改或新增，则修改或新增本地电话簿内容
    	for(int i = 0;i < downloadPhonebookList.size();i++)//下载下来记录个数
    	{
    		for(int j = 0; j < sdPhonebookList.size(); j++)//本地电话簿记录个数
    		{
    			//有该联系人
    			if(downloadPhonebookList.get(i).getPsInfo().getStrId().equals(sdPhonebookList.get(j).getPsInfo().getStrId()))
    			{
    				//数据不同则替换
    				if(false == downloadPhonebookList.get(i).getPsInfo().comparePersonInfo(sdPhonebookList.get(j).getPsInfo()))
    				{
    					sdPhonebookList.get(j).setPsInfo(downloadPhonebookList.get(i).getPsInfo());
    					sdPhonebookList.get(j).setAction("update");
    				}
    				bExist = true;
    			}
    		}
    		if(false == bExist)
    		{
   	    		//没有该联系人，拷贝下载数据数据至要备份数据中
    			PhoneBook pb = new PhoneBook();
    			pb.setPsInfo(downloadPhonebookList.get(i).getPsInfo());
    			pb.setAction("creat");
    			pb.setUserId(phoneImsi);
    			pb.setImsi(phoneUserid);
    			sdPhonebookList.add(pb);
    		}
    		bExist = false;
    	}
    	//比较SD卡与本地数据，如果有修改，则替换本地数据，如果新增，则添加记录
    	for(int i =0; i < sdPhonebookList.size(); i++){
    		if(sdPhonebookList.get(i).getAction() == "creat"){
    			//新增记录
    			sdPhonebookList.get(i).setAction(null);
    			insertPhonebookData(sdPhonebookList.get(i).getPsInfo());
    		}else if(sdPhonebookList.get(i).getAction() == "update"){
    			//更新记录
    			sdPhonebookList.get(i).setAction(null);
    			updatePhoneBookData(sdPhonebookList.get(i).getPsInfo());
    		}
    	}
    	//修改本地备份文件
		String backupjson = "";
		backupjson= JsonUtils.book2Json(sdPhonebookList);
    	this.backupDataSdcard("TxPhonebook","backup.dat",backupjson);
    	readContactToSdcard();
    }
    /** Called when the activity is first created. */
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        uploadBut = (Button)findViewById(R.id.UploadBut);
        downloadBut = (Button)findViewById(R.id.DownloadBut);

/*
        // Get a cursor with all people
        Cursor c = getContentResolver().query(People.CONTENT_URI, null, null, null, null);
        startManagingCursor(c);

        ListAdapter adapter = new SimpleCursorAdapter(this,
                // Use a template that displays a text view
                android.R.layout.simple_list_item_1,
                // Give the cursor to the list adatper
                c,
                // Map the NAME column in the people database to...
                new String[] {People.NAME} ,
                // The "text1" view defined in the XML template
                new int[] {android.R.id.text1});
        setListAdapter(adapter);
*/
        uploadBut.setOnClickListener(new Button.OnClickListener()
        {

        @Override
        public void onClick(View v)
        {
        	//getContactList(true);
        	//getContactsInfoListFromPhone();
        	HttpDownloader httpdownloader = new HttpDownloader();
			String lrc =  httpdownloader.download("http://192.168.56.120:8080/a1.lrc");
			System.out.println("lrc--->" + lrc);
        }});

        downloadBut.setOnClickListener(new Button.OnClickListener()
        {
        	@Override
	        public void onClick(View v){
	        	//初始化
	        	phonebookInit();
	        	//上传全部
	        	//upLoadDataAll();
	        	//上传本地更新部分
	        	//upLoadLatestData();
	        	//下载全部
	        	//downLoadDataAll();
	        	//下载服务器更新部分
	        	//downLoadDataAll();
	        	//测试新增联系人
	        	//insertPhonebookDataTest();

	        }
        });
    }
    //wm
    public String getContactList(boolean isFirstTime) {

        StringBuilder contactListBuilder = new StringBuilder();

        if(isFirstTime == false) {
            contactListBuilder.append("ContactList is returned before.");
            return contactListBuilder.toString();
        }

        try {
            ContentResolver cr = getContentResolver();
            Uri uri = android.provider.ContactsContract.Contacts.CONTENT_URI;
            Cursor cur = cr.query(uri, null, null, null, null);

            if (cur.moveToFirst()) {
                String name;
                String contactId;
                String phoneNumber;

                int nameColumn = cur.getColumnIndex(PhoneLookup.DISPLAY_NAME);
                int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);

                do{
                    // 获取联系人姓名
                    name = cur.getString(nameColumn);
                    // 获取联系人电话号码
                    contactId = cur.getString(idColumn);
                    Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                    if(phone.moveToNext()){
                        int phoneColumn = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        phoneNumber = phone.getString(phoneColumn);
                    } else {
                        phoneNumber = "";
                    }
                    // 构造通讯录
                    contactListBuilder.append("(");
                    contactListBuilder.append(name + ":");
                    contactListBuilder.append(phoneNumber + "");
                    contactListBuilder.append(") ");
                }while(cur.moveToNext());
            } else {
                contactListBuilder.append("no result!");
            }
        } catch(SQLiteException ex) {
            Log.e("SQLiteException in getContactList", ex.getMessage());
        }

        return contactListBuilder.toString();
    }
    /* 读取手机中的contacts内容 wm */
    private void getContactsInfoListFromPhone() {
        /* 取得ContentResolver */
        ContentResolver content = this.getContentResolver();
        /* 取得通讯录的Phones表的cursor */
        Cursor contactcursor = content.query(Contacts.Phones.CONTENT_URI, null,
               null, null, Contacts.People.DEFAULT_SORT_ORDER);
        /* 在LogCat里打印所有关于的列名 */
        for (int i = 0; i < contactcursor.getColumnCount(); i++) {
            String columnName = contactcursor.getColumnName(i);
            Log.e("readTXT", "column name:" + columnName);
        }
        /* 逐条读取记录信息 */
        int Num = contactcursor.getCount();
        Log.e("readTXT", "recNum=" + Num);
        String name, number;
        for (int i = 0; i < Num; i++) {
            contactcursor.moveToPosition(i);
            String type = contactcursor.getString(contactcursor
                    .getColumnIndexOrThrow(Contacts.Phones.TYPE));
            Log.e("readTXT", "type=" + type);
            String person_id = contactcursor.getString(contactcursor
                    .getColumnIndexOrThrow(Contacts.Phones.PERSON_ID));
            Log.e("readTXT", "person_id=" + person_id);
            name = contactcursor.getString(contactcursor
                    .getColumnIndexOrThrow(Contacts.Phones.NAME));
            number = contactcursor.getString(contactcursor
                   .getColumnIndexOrThrow(Contacts.Phones.NUMBER));
//          number = number == null ? "无输入电话" : number;// 当找不到电话时显示"无输入电话"
            //nameContactsInPhone.add(name);
            Log.e("readTXT", "name=" + name);
            //numberContactsInPhone.add(number);
            Log.e("readTXT", "*****number=" + number);
        }
    }
    //wm
    public void WriteFile(String filePath, String tempcon)throws FileNotFoundException {
    	// 创建PrintWriter对象，用于写入数据到文件中
	   try {
	    PrintWriter pw = new PrintWriter(new FileOutputStream(filePath));
	    // 用文本格式打印整数Writestr
	    pw.println(tempcon);
	    // 清除PrintWriter对象
	    pw.close();
	   } catch (IOException e) {
	    // 错误处理
	    System.out.println("写入文件错误" + e.getMessage());
	   }
  }
}