package modules;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import modules.actions.PostANewMessage;
import modules.triggers.HasNewLikedPost;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class FacebookModule implements IModules {
    public static Facebook mFacebook;
    private List<IAction> mActions = new ArrayList<IAction>();
    private List<ITrigger> mTriggers = new ArrayList<ITrigger>();
    private static final String mAppId = "1251794458175503";
    private static final String mClientSecret = "eaa2e8f372b4e4b4fd91772c83d8dc8b";
    private static String mAuthTokenLink = "https://graph.facebook.com/v2.8/oauth/access_token?";

    private OkHttpClient _client = new OkHttpClient();

    public FacebookModule() {
        mFacebook = new FacebookFactory().getInstance();
        mFacebook.setOAuthAppId(mAppId, mClientSecret);
        mFacebook.setOAuthPermissions("public_profile,user_friends,publish_actions,user_posts,user_likes");
        mActions.add(new PostANewMessage(mFacebook));
        mTriggers.add(new HasNewLikedPost(mFacebook));
    }

    public String getModuleName() {
        return "Facebook";
    }

    public List<IAction> getActions() {
        return mActions;
    }

    public List<ITrigger> getTrigger() {
        return mTriggers;
    }

    @Override
    public String getAuthLink(String RedirectLink) {
        URI uri = URI.create(mFacebook.getOAuthAuthorizationURL(RedirectLink));
        return uri.toASCIIString();
    }

    public String Auth(String AuthCode, String ID) {
        String link = mAuthTokenLink
                + "client_id=" + mAppId
                + "&redirect_uri=" + URLEncoder.encode("http://127.0.0.1:80/Module/Facebook/Auth?ID=" + ID)
                + "&client_secret=" + mClientSecret
                + "&code=" + AuthCode;
        Request request = new Request.Builder()
                .url(link).get()
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
