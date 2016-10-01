package industries.muskaqueers.thunderechosaber.UI;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.net.URL;

import de.greenrobot.event.EventBus;
import industries.muskaqueers.thunderechosaber.DB.MLADatabaseHelper;
import industries.muskaqueers.thunderechosaber.ThunderEchoSabreEvent;

/**
 * Created by vincekearney on 01/10/2016.
 */

public class ProcessImage {

    private static final String TAG = "ProcessImage";
    private MLADatabaseHelper databaseHelper;

    public ProcessImage() {
        this.databaseHelper = new MLADatabaseHelper();
    }

    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public void getDataFromImage(String url, final String mlaID) {
        BitmapFromImage bitFromImage = new BitmapFromImage() {
            @Override
            protected void onPostExecute(byte[] byteArray) {
                super.onPostExecute(byteArray);
                Log.d(TAG, "onPostExecute: Byte Array = " + byteArray.toString());
                databaseHelper.updateImageData(databaseHelper.fetchMLA(mlaID), byteArray);
                EventBus.getDefault().post(new ThunderEchoSabreEvent(ThunderEchoSabreEvent.eventBusEventType.UPDATE_MLAS));
            }
        };
        bitFromImage.execute(url);
    }

    class BitmapFromImage extends AsyncTask<String, Void, byte[]> {
        @Override
        protected byte[] doInBackground(String... params) {
            Log.d(TAG, "URL: " + params[0]);
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(new URL(params[0]).openStream());
                Log.d(TAG, "doInBackground: Bitmap = " + bitmap);
                return getBitmapAsByteArray(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
