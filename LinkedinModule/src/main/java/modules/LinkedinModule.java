package modules;

import modules.actions.PostMessageAction;
import modules.triggers.HasNewProfilePicture;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URLDecoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxime on 05/01/2017.
 */
public class LinkedinModule implements IModules {
    private List<IAction> _Actions = new ArrayList<IAction>();
    private List<ITrigger> _Triggers = new ArrayList<ITrigger>();
    static private final String _ClientID = "7710yjdhjtbwj0";
    static private final String _ClientSecret = "yov3LOl8GF2T3puX";
    static private final String _AuthTokenLink = "https://www.linkedin.com/oauth/v2/accessToken";
    private OkHttpClient _client = new OkHttpClient();
    private JSONObject redirect_URI = new JSONObject();


    public LinkedinModule() {
        _Actions.add(new PostMessageAction());
        _Triggers.add(new HasNewProfilePicture());
    }

    public List<IAction> getActions() {
        return _Actions;
    }

    public List<ITrigger> getTrigger() {
        return _Triggers;
    }

    public String getModuleName() {
        return "Linkedin";
    }

    @Override
    public String getAuthLink(String RedirectLink) {
        SecureRandom random = new SecureRandom();
        String randomString = new BigInteger(130, random).toString(32);
        String ID = RedirectLink.substring(RedirectLink.length() - 40);
        URI uri = URI.create("https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id=" + _ClientID + "&redirect_uri=" + RedirectLink + "&state=" + randomString);
        try {
            redirect_URI.put(ID, URLDecoder.decode(RedirectLink, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return (uri.toASCIIString());
    }

    public String Auth(String AuthCode, String ID) {
        RequestBody formBody = new FormBody.Builder()
                .add("client_id", _ClientID)
                .add("client_secret", _ClientSecret)
                .add("code", AuthCode)
                .add("grant_type", "authorization_code")
                .add("redirect_uri", redirect_URI.getString(ID))
                .build();

        Request request = new Request.Builder()
                .url(_AuthTokenLink)
                .post(formBody)
                .build();

        Response response = null;
        try {
            response = _client.newCall(request).execute();
            String ret = response.body().string();
            JSONObject obj = new JSONObject(ret);
            return obj.toString();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return (null);
    }
}
