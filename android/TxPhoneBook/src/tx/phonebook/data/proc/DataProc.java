package tx.phonebook.data.proc;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tx.phonebook.util.file.FileUtils;
import tx.phonebook.util.http.PostJSONTextbyStream;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;



public class DataProc extends Activity{

	//读取SD卡文件数据
    public String readDataSdcard(String strpath, String strfileName){
    	String strOutput = "";
    	FileUtils fileHelper = new FileUtils();
    	strOutput = fileHelper.readFromSDCard(strpath, strfileName);
    	return strOutput;
    }
    //保存数据到SD卡
    public boolean backupDataSdcard(String strpath, String strfileName, String strData){
    	FileUtils fileHelper = new FileUtils();
        File file = null;
     	file = fileHelper.writeToSDCard(strpath, strfileName, strData);
    	return true;
    }
	public void phonebookInit(){
		//读取电话簿内容至SD卡中
		readAllContacts();
	}
	//读取电话簿内容 wj
    public void readAllContacts()
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
		        ContentPhonebook[nCurIndex].setUserId("60815935");
		        ContentPhonebook[nCurIndex].setImsi("310260000000000");
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

    public void upLoadData(){
    	String strSDPhoneOutput = "";
    	int nIndex = 0;
    	int nPhoneBookNum = 0;
    	String strUploadData = "";
    	FileUtils myfu = new FileUtils();

    	//从SD卡中读取本机电话簿内容并转化为对象
    	strSDPhoneOutput = readDataSdcard("TxPhonebook","phone.dat");
    	ContentResolver cr = getContentResolver();
    	Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
    	nPhoneBookNum = cur.getCount();
    	PhoneBook ContactPhoneBook[] = new PhoneBook[nPhoneBookNum];

    	Type SDPhonelistType = new TypeToken<LinkedList<PhoneBook>>(){}.getType();
		Gson SDPhonegson = new Gson();
		LinkedList<PhoneBook> SDPhoneusers = SDPhonegson.fromJson(strSDPhoneOutput, SDPhonelistType);
		for(Iterator iterator = SDPhoneusers.iterator();iterator.hasNext();){
			PhoneBook user = (PhoneBook)iterator.next();
			ContactPhoneBook[nIndex] = user;
			nIndex++;
		}

		//如果文件不存在，则为第一次上传
        if(false == myfu.isFileExist("TxPhonebook/backup.dat"))
        {
        	//组织发送数据内容
    		String json = "";
    		List<PhoneBook> list = new ArrayList<PhoneBook>();
    		for(int i=0;i<nPhoneBookNum;i++){
    			ContactPhoneBook[i].setAction("creat");
    			list.add(ContactPhoneBook[i]);
    		}
    		json= JsonUtils.book2Json(list);

        	//发送数据
        	strUploadData = json;
    		PostJSONTextbyStream http = new PostJSONTextbyStream();
    		//http.POST("http://192.168.56.186:8080/tx-mtk-server/androidclient/pbbackup.do",strUploadData);
    		//发送完保存数据
    		List<PhoneBook> postEndList = new ArrayList<PhoneBook>();
    		for(int i=0;i<nPhoneBookNum;i++){
    			ContactPhoneBook[i].setAction(null);
    			postEndList.add(ContactPhoneBook[i]);
    		}
    		json= JsonUtils.book2Json(postEndList);
        	//备份至sd卡中
        	this.backupDataSdcard("TxPhonebook","backup.dat",json);
       }
        //第二次上传
        else
        {
            //从SD卡中读取备份电话簿并转化为对象
        	int nTotal = 0;//sd卡中人数
        	String strSDOutput = "";//读取SD卡中内容
        	//搜索人数有几个联系人
    		String regex="strId";
    		strSDOutput = readDataSdcard("TxPhonebook","backup.dat");
    		Matcher mat=Pattern.compile(regex).matcher(strSDOutput);
    		while(mat.find()){
    			nTotal++;
    		}
    		PhoneBook sdBackupPhonebook[] = new PhoneBook[nTotal];

        	//拷贝至对象,通过json格式解析
    		Type listType = new TypeToken<LinkedList<PhoneBook>>(){}.getType();
    		Gson gson = new Gson();

    		LinkedList<PhoneBook> users = gson.fromJson(strSDOutput, listType);
    		nIndex =0;
    		for(Iterator iterator = users.iterator();iterator.hasNext();){
    			PhoneBook user = (PhoneBook)iterator.next();
    			sdBackupPhonebook[nIndex] = user;
    			nIndex++;
    		}
        	nIndex = 0;

        	//当前索引
        	int nModifyNum = 0;
        	boolean bExist = false;
         	//进行数据比较，数据修改，上传SD中修改过的内容
        	for(int i = 0;i < nPhoneBookNum;i++)//nPhoneBookNum为电话簿读取出的人数
        	{
        		for(int j = 0; j < nTotal; j++)//nTotal为SD备份文件人数
        		{
        			//有该联系人
        			if(ContactPhoneBook[i].getPsInfo().getStrId().equals(sdBackupPhonebook[j].getPsInfo().getStrId()))
        			{
        				//数据不同则替换
        				if(false == ContactPhoneBook[i].getPsInfo().comparePersonInfo(sdBackupPhonebook[j].getPsInfo()))
        				{
        					sdBackupPhonebook[j].setPsInfo(ContactPhoneBook[i].getPsInfo());
        					sdBackupPhonebook[j].setAction("update");
        					//sdBackupPerson[j].copyPersonInfo(sdBackupPerson[i]);
        					//sdBackupPerson[j].setStrState("modify");
        					nModifyNum++;
        				}
        			}
        			else
        			{
           	    		//没有该联系人，拷贝该数据至SD卡数据中
            			sdBackupPhonebook[nIndex] = new PhoneBook();
            			sdBackupPhonebook[nIndex].setPsInfo(ContactPhoneBook[nIndex].getPsInfo());
            			sdBackupPhonebook[nIndex].setAction("creat");
            			nIndex++;
            			nModifyNum++;
        			}
        		}
        	}
        	//组织发送数据，如果state为new或者modify则为待发送数据
    		String json = "";
    		List<PhoneBook> list = new ArrayList<PhoneBook>();
    		for(int i = 0; i < nIndex; i++){
    			if(null != sdBackupPhonebook[i].getAction()){
    				list.add(sdBackupPhonebook[i]);
    			};
    		}
    		json= JsonUtils.book2Json(list);
    		//上传数据
    		if(json.length() > "[]".length()){
    			//如果有数据则上传
    			strUploadData = json;
        		PostJSONTextbyStream http = new PostJSONTextbyStream();
        		http.POST("http://192.168.56.186:8080/tx-mtk-server/androidclient/pbbackup.do",strUploadData);
            	//上传成功后删除备份文件，并将SD对象中内容备份至SD卡中
        		//发送完保存数据
        		List<PhoneBook> postEndList = new ArrayList<PhoneBook>();
        		for(int i=0;i<nPhoneBookNum;i++){
        			sdBackupPhonebook[i].setAction(null);
        			postEndList.add(sdBackupPhonebook[i]);
        		}
        		json= JsonUtils.book2Json(postEndList);
            	this.backupDataSdcard("TxPhonebook","backup.dat",json);
    		}
    		else
    		{
    			//提示数据已最新
    		}
        }
    }
    public void downLoadDataTest(){
    }
    public void downLoadData(){
    	//下载数据
    	String json = "";
    	String strUploadData = "";
    	String strDownloadData = "";
    	int nDownloadItemTotal = 0;
		int nIndex =0;
    	int nModifyNum = 0;

    	PersonUpdate myPersonUpdate = new PersonUpdate();
    	myPersonUpdate.setStrUserId("123456");
    	String myString[] = new String[2];
    	myString[0] = "string1";
    	myString[1] = "string2";
    	myPersonUpdate.setStrPsi(myString);

		List<PersonUpdate> list = new ArrayList<PersonUpdate>();
		list.add(myPersonUpdate);

		json= JsonUtils.psUpdate2Json(list);
		strUploadData = json;
		PostJSONTextbyStream http = new PostJSONTextbyStream();
		strDownloadData = http.POST("http://192.168.56.186:8080/tx-mtk-server/androidclient/pbbackup.do",strUploadData);

		//下载下来的数据转换为对象
		String regex="strId";
		Matcher mat=Pattern.compile(regex).matcher(strDownloadData);
		while(mat.find()){
			nDownloadItemTotal++;
		}
		Type downloadType = new TypeToken<LinkedList<PhoneBook>>(){}.getType();
		Gson gson = new Gson();
		PhoneBook downloadPhonebook[] = new PhoneBook[nDownloadItemTotal];

		LinkedList<PhoneBook> users = gson.fromJson(strDownloadData, downloadType);
		for(Iterator iterator = users.iterator();iterator.hasNext();){
			PhoneBook user = (PhoneBook)iterator.next();
			downloadPhonebook[nIndex] = user;
			nIndex++;
		}

    	//读取SD卡中数据
        //从SD卡中读取备份电话簿并转化为对象
    	int nTotal = 0;//sd卡中人数
    	String strSDOutput = "";//读取SD卡中内容
		strSDOutput = readDataSdcard("TxPhonebook","backup.dat");
		Matcher sdmat=Pattern.compile(regex).matcher(strSDOutput);
		while(sdmat.find()){
			nTotal++;
		}
		PhoneBook sdBackupPhonebook[] = new PhoneBook[nTotal];

    	//拷贝至对象,通过json格式解析
		Type listType = new TypeToken<LinkedList<PhoneBook>>(){}.getType();
		Gson sdgson = new Gson();

		LinkedList<PhoneBook> sdusers = sdgson.fromJson(strSDOutput, listType);
		nIndex =0;
		for(Iterator iterator = sdusers.iterator();iterator.hasNext();){
			PhoneBook user = (PhoneBook)iterator.next();
			sdBackupPhonebook[nIndex] = user;
			nIndex++;
		}
    	//读取出来的与下载数据比较，如有修改或新增，则修改或新增本地电话簿内容
    	for(int i = 0;i < nDownloadItemTotal;i++)//nDownloadItemTotal为下载的人数
    	{
    		for(int j = 0; j < nTotal; j++)//nTotal为SD备份文件人数
    		{
    			//有该联系人
    			if(downloadPhonebook[i].getPsInfo().getStrId().equals(sdBackupPhonebook[j].getPsInfo().getStrId()))
    			{
    				//数据不同则替换
    				if(false == downloadPhonebook[i].getPsInfo().comparePersonInfo(sdBackupPhonebook[j].getPsInfo()))
    				{
    					sdBackupPhonebook[j].setPsInfo(downloadPhonebook[i].getPsInfo());
    					sdBackupPhonebook[j].setAction("update");
    					//sdBackupPerson[j].copyPersonInfo(sdBackupPerson[i]);
    					//sdBackupPerson[j].setStrState("modify");
    					nModifyNum++;
    				}
    			}
    			else
    			{
       	    		//没有该联系人，拷贝该数据至SD卡数据中
        			sdBackupPhonebook[nIndex] = new PhoneBook();
        			sdBackupPhonebook[nIndex].setPsInfo(downloadPhonebook[nIndex].getPsInfo());
        			sdBackupPhonebook[nIndex].setAction("creat");
        			nIndex++;
        			nModifyNum++;
    			}
    		}
    	}
    	//比较SD卡与本地数据，如果有修改，则替换本地数据，如果新增，则添加记录
    	for(int i =0; i < nModifyNum; i++){
    		if(sdBackupPhonebook[nIndex].getAction() == "creat"){
    			//新增记录
    			sdBackupPhonebook[nIndex].setAction(null);
    			insertPhonebookData(sdBackupPhonebook[nIndex].getPsInfo());
    		}else{
    			if(sdBackupPhonebook[nIndex].getAction() == "update"){
    				//更新记录
    				sdBackupPhonebook[nIndex].setAction(null);
    				updatePhoneBookData(sdBackupPhonebook[nIndex].getPsInfo());
    			}
    		}
    	}
    	//修改本地备份文件
		String jbackupson = "";
		List<PhoneBook> backuplist = new ArrayList<PhoneBook>();
		for(int i = 0; i < nIndex; i++){
				backuplist.add(sdBackupPhonebook[i]);
		}
		jbackupson= JsonUtils.book2Json(backuplist);
    	this.backupDataSdcard("TxPhonebook","backup.dat",jbackupson);


    	//以下数据为模拟下载下来的测试数据
/*
    	String[] strNameArray= new String[]{"insert4","insert5","insert6"};
    	String[] strNumberArray=new String[]{"111111","222222","333333"};
    	String[] strEmailArray=new String[]{"","",""};
    	String[] strQQArray=new String[]{"","",""};

    	//3是读取的信息数据大小
        	PersonInfo myPersonInfo[] = new PersonInfo[3];
        	PersonInfo.nPersonNum = 3;
    	//保存下载下来的数据
        	for(int k = 0; k < PersonInfo.nPersonNum; k++)
        	{
        		myPersonInfo[k] = new PersonInfo(strNameArray[k],strNumberArray[k],strEmailArray[k],strQQArray[k]);
        	}
        	//插入到手机电话簿
    	for(int i = 0; i < PersonInfo.nPersonNum;i++)
    	{
    		insertData(myPersonInfo[i].strName, myPersonInfo[i].strNumber1, myPersonInfo[i].strEmail1, myPersonInfo[i].strEmail1);
    	}*/
        //保存数据到T卡
        //backupDataSdcard("TxPhonebook","backup.dat",str);
    	//readDataSdcard("TxPhonebook","backup.dat");
    	//String strOutPut= "";
    	//strOutPut = readDataSdcard("TxPhonebook","phone.dat");
    }

}
