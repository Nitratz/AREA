package modules.triggers;

import modules.ITrigger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;

public class HasNewProfilePicture implements ITrigger {

    static private final String _SentLink = "https://api.linkedin.com/v1/people/~:(picture-url)?format=json";
    private OkHttpClient _client = new OkHttpClient();
    static private JSONObject _PreviousPictureUrl = new JSONObject();

    public HasNewProfilePicture(){
    }

    private String getProfilePicture(String token) {

        Request request = new Request.Builder()
                .url(_SentLink)
                .addHeader("Authorization", "Bearer " + token)
                .get()
                .build();
        Response response = null;
        try {
            response = _client.newCall(request).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject obj = null;
        try {
            obj = new JSONObject(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (_PreviousPictureUrl.has(token)) {
            if (!obj.getString("pictureUrl").toString().equals(_PreviousPictureUrl.getString(token))) {
                _PreviousPictureUrl.put(token, obj.getString("pictureUrl"));
                return (obj.getString("pictureUrl"));
            }
        }
        _PreviousPictureUrl.put(token, obj.getString("pictureUrl"));
        return (null);
    }

    public String isActionned(String Users) {

        JSONObject user = new JSONObject(Users);
        return (getProfilePicture(user
                .getJSONObject("Services")
                .getJSONObject("Linkedin")
                .getString("access_token")));
    }

    public String getType() {
        return "Nouvelle photo de profil LinkedIn";
    }
}
