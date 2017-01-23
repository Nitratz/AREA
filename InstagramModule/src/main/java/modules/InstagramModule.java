package modules;

import modules.actions.FollowUser;
import modules.triggers.HasChangedProfilePicture;
import modules.triggers.HasPostedNewMedia;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class InstagramModule implements IModules {

    private List<IAction> mActions = new ArrayList<IAction>();
    private List<ITrigger> mTriggers = new ArrayList<ITrigger>();
    private static final String mAppId = "84ca334854064fefb2545c320a77da2c";
    private static final String mClientSecret = "4fcf04f6649c4566b15762cf5eb9004a";
    private static final String mAuthLink = "https://api.instagram.com/oauth/authorize/?";
    private static String mAuthTokenLink = "https://api.instagram.com/oauth/access_token";
    private JSONObject redirect_URI = new JSONObject();

    public InstagramModule() {
        mActions.add(new FollowUser());
        mTriggers.add(new HasChangedProfilePicture());
        mTriggers.add(new HasPostedNewMedia());
    }

    @Override
    public List<IAction> getActions() {
        return mActions;
    }

    @Override
    public List<ITrigger> getTrigger() {
        return mTriggers;
    }

    @Override
    public String getAuthLink(String RedirectLink) {
        String ID = RedirectLink.substring(RedirectLink.length() - 40);
        try {
            redirect_URI.put(ID, URLDecoder.decode(RedirectLink, "UTF-8"));
            return mAuthLink +
                    "client_id=" + mAppId
                    + "&redirect_uri=" + RedirectLink
                    + "&response_type=code";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            redirect_URI.remove(ID);
            return null;
        }
    }

    @Override
    public String getModuleName() {
        return "Instagram";
    }

    @Override
    public String Auth(String AuthCode, String ID) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("client_id", mAppId)
                .addFormDataPart("client_secret", mClientSecret)
                .addFormDataPart("grant_type", "authorization_code")
                .addFormDataPart("redirect_uri", redirect_URI.get(ID).toString())
                .addFormDataPart("code", AuthCode)
                .build();

        Request request = new Request.Builder()
                .url(mAuthTokenLink)
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody)
                .build();

        try {
            Response resp = client.newCall(request).execute();
            return new JSONObject(resp.body().string()).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
