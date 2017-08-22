package industries.muskaqueers.thunderechosaber.Managers;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import industries.muskaqueers.thunderechosaber.Database.GreenDatabaseManager;
import industries.muskaqueers.thunderechosaber.Database.MLADb;
import industries.muskaqueers.thunderechosaber.ParserUtils;
import industries.muskaqueers.thunderechosaber.Utils.MLAThread;

/**
 * Created by andrewcunningham on 11/17/16.
 */

public class ServerManager {

    private final static String TAG = "ServerManager";
    private final static String MLA_POINT = "https://vincetestaccount.herokuapp.com/leaders/mlas/";
//    private final static String PARTIES_POINT = "https://vincetestaccount.herokuapp.com/leaders/parties/";
    private final static String RESPONSE = "response";

    public ServerManager(Context context) {
        if (GreenDatabaseManager.getMlaTable().loadAll().isEmpty()) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, MLA_POINT, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray(RESPONSE);
                        Log.i(TAG, "The JSON that we got back -> " + jsonArray);
                        List<MLADb> mlaDbs = ParserUtils.getMLAsFromJSONArray(jsonArray);
                        addMLAsToDatabase(mlaDbs);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "We got an error: " + error.toString());
                }
            });

            RetryPolicy policy = new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsObjRequest.setRetryPolicy(policy);
            requestQueue.add(jsObjRequest);
        }

    }

    public void addMLAsToDatabase(List<MLADb> mlaDbs) {
        MLAThread thread = new MLAThread(mlaDbs);
        thread.execute();
    }
}
