package modules;

import modules.actions.PostMessageAction;
import modules.triggers.HasNewMessageTrigger;
import modules.triggers.HasSendANewMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import okhttp3.*;

public class YammerModules implements IModules {
    private List<IAction> _Actions = new ArrayList<IAction>();
    private List<ITrigger> _Triggers = new ArrayList<ITrigger>();
    static private final String _ClientID = "OlqGsVBBuJfcVlzR1H1w";
    static private final String _ClientSecret = "f55Yu0cxZUXwMkvn2Ao4J3Y7hOTJWoRpmLGR99ob58";
    static private final String _AuthTokenLink = "https://www.yammer.com/oauth2/access_token";
    private OkHttpClient _client = new OkHttpClient();

    public YammerModules () {
        _Actions.add(new PostMessageAction());
        _Triggers.add(new HasNewMessageTrigger());
        _Triggers.add(new HasSendANewMessage());
    }

    public List<IAction> getActions(){
        return _Actions;
    }

    public List<ITrigger> getTrigger() {
        return _Triggers;
    }

    @Override
    public String getAuthLink(String RedirectLink) {
        URI uri = URI.create("https://www.yammer.com/epitech.eu/oauth2/authorize?client_id=" + _ClientID + "&response_type=code&redirect_uri=" + RedirectLink);
        return uri.toASCIIString();
    }

    public String getModuleName() {
        return "Yammer";
    }

    public String Auth(String AuthCode, String ID) {
        RequestBody formBody = new FormBody.Builder()
                .add("client_id", _ClientID)
                .add("client_secret", _ClientSecret)
                .add("code", AuthCode)
                .add("grant_type", "authorization_code")
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
        return null;
    }
}
