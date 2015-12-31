package edu.uml.swin.autotest;

import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

public class FileUploader extends AsyncTask<Void, Void, String> {
    public static String TAG = "InteractionService/FileUploader";



    private Context mContext;
	private ConnectionDetector mConnectionDetector;
    private DBhelper logDbHelper;
	private String fileName;


	public FileUploader(Context context) {
		mContext = context;
    	mConnectionDetector = new ConnectionDetector(mContext);
        logDbHelper = new DBhelper(context);
		fileName = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
	}
	
	@Override
    protected void onPreExecute() {
    	super.onPreExecute();
    	
    	if (! mConnectionDetector.isConnectingToInternet()) {
    		Log.d(TAG, "WiFi is not connected to Internet, cannot upload file");
    		cancel(true);
    	}
    }
	
	@Override
	protected String doInBackground(Void... params) {
//		Log.e(TAG, "==============");
        String message = "Oops, there must be something wrong.";

        SQLiteDatabase db = logDbHelper.getWritableDatabase();
		String DB_PATH = db.getPath();

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Constants.POST_FILE_URL);
			// Check if the database has already been created
			File dbFile = new File(DB_PATH);
			if (!dbFile.exists()) {
                message = "Database has not been created, cancel uploading.\n";
				Log.e(TAG, "Database has not been created, cancel uploading.");
				httpClient.getConnectionManager().shutdown();
				return message;
			}

			FileBody fileBody = new FileBody(dbFile);
			
			// Build the post 
			MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
			reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			reqEntity.addPart("file", fileBody);
			fileName += "_" + String.valueOf(System.currentTimeMillis());
			reqEntity.addTextBody("fileName", fileName + ".db");
			httpPost.setEntity(reqEntity.build());
			
			// execute HTTP post request
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity resEntity = response.getEntity();

            Log.e(TAG, "========== Status code = " + response.getStatusLine().getStatusCode());

			if (resEntity != null) {
				String responseStr = EntityUtils.toString(resEntity).trim();
				//Log.e(TAG, "File uploader received response: " + responseStr);
//				Log.e(TAG, "************************");
//				if (responseStr.equals("success")) {
//					updateUploadTime();
//				}
                message = "File uploader received response: " + responseStr + ".";
                resEntity.consumeContent();
			} else {
				Log.e(TAG, "File uploader got no response from remote server");

                message = "Oops, file uploader got no response from remote server.";
			}

			httpClient.getConnectionManager().shutdown();
		} catch (NullPointerException e) {
        	e.printStackTrace();
        } catch (Exception e) {
        	e.printStackTrace();
        }
		message = "Upload succeeded!";
		return message;
	}
	

	protected void onPostExecute(String message){

    }
	
}
