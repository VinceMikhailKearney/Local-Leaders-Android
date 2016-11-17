package industries.muskaqueers.thunderechosaber.Managers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import industries.muskaqueers.thunderechosaber.NewDB.MLADb;
import industries.muskaqueers.thunderechosaber.ParserUtils;
import industries.muskaqueers.thunderechosaber.Utils.MLAThread;

/**
 * Created by andrewcunningham on 11/17/16.
 */

public class ServerManager {

    private final static String TAG = "ServerManager";

    private final static String MLA_POINT = "https://localleaders.herokuapp.com/leaders/mlas/";
    private final static String PARTIES_POINT = "https://localleaders.herokuapp.com/leaders/parties/";

    public ServerManager(Context context) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, MLA_POINT, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "AAC --> We got a response: " + response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("response");
                            List<MLADb> mlaDbs = ParserUtils.getMLAsFromJSONArray(jsonArray);
                            addMLAsToDatabase(mlaDbs);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "AAC --> We got an error: " + error.toString());
                    }
                });

        requestQueue.add(jsObjRequest);

    }


    public void addMLAsToDatabase(List<MLADb> mlaDbs) {
        MLAThread thread = new MLAThread(mlaDbs);
        thread.execute();
    }

}
