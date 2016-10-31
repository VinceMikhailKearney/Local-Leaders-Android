package industries.muskaqueers.thunderechosaber.UI;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.net.URL;

import de.greenrobot.event.EventBus;
import industries.muskaqueers.thunderechosaber.DB.DatabaseManager;
import industries.muskaqueers.thunderechosaber.Events.DatabaseEvent;

/**
 * Created by vincekearney on 01/10/2016.
 */

public class ProcessImage {

    /**
     * Just to sort of give a view of what is going on with this all:
     * This class will download an image via a URL as a bitmap. We then alter this to a byte
     * array that is saved against the MLA in the DB.
     * When we are interacting with MLAs in the LeadersFragment the DatabaseManager will convert
     * this byte array to a Bitmap to make it easier for us to interact with :D
     */

    private static final String TAG = "ProcessImage";
    public enum type {Party, MLA}
    private int threadNumber;

    public ProcessImage(int number) {
        this.threadNumber = number;
    }

    /**
     * Returns a bitmap as byte array
     * @param bitmap - The bitmap that we need to return as a byte array
     * @return - Byte array of given bitmap
     */
    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    /**
     * Method for getting data from an image that we download from the URL of a MLA
     * @param url - URL of the MLA/Party
     * @param objectId - ID of the MLA/Party so we can update the image data of the given MLA once downloaded
     */
    public void getDataFromImage(String url, final String objectId, final type state) {
        BitmapFromImage bitFromImage = new BitmapFromImage() {
            @Override
            protected void onPostExecute(byte[] byteArray) {
                super.onPostExecute(byteArray);
                if(byteArray != null)
                    Log.d(TAG, "onPostExecute: Byte Array = " + byteArray.toString());
                if(state == ProcessImage.type.MLA) { // This is no longer used, but keeping incase we need it.
                    DatabaseManager.mlaHelper().updateImageData(DatabaseManager.mlaHelper().fetchMlaWithID(objectId), byteArray);
                    EventBus.getDefault().post(new DatabaseEvent(DatabaseEvent.type.DownloadedImage));
                } else if (state == ProcessImage.type.Party) {
                    DatabaseManager.partyHelper().updateImageData(DatabaseManager.partyHelper().fetchParty(objectId), byteArray);
                    EventBus.getDefault().post(new DatabaseEvent(DatabaseEvent.type.UpdateParties));
                    // Right now the above event is not caught anywhere, need to redesign some stuff first.
                }
            }
        };
        bitFromImage.execute(url);
    }

    /**
     * Async class for retrieving an image and returning as a byte array. The onPostExecute above then saves this to the DB.
     */
    class BitmapFromImage extends AsyncTask<String, Void, byte[]> {
        @Override
        protected byte[] doInBackground(String... params) {
            Log.d(TAG, "URL: " + params[0]);
            try {
                return getBitmapAsByteArray(BitmapFactory.decodeStream(new URL(params[0]).openStream()));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
