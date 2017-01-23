package modules.triggers;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;

public class RequesterTrigger {

    public static JSONObject request(String uData) {
        JSONObject user = new JSONObject(uData);
        JSONObject facebook = user.getJSONObject("Services")
                .getJSONObject("Instagram");
        String token = facebook.getString("access_token");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.instagram.com/v1/users/self/?access_token=" + token).get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
