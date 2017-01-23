package modules.actions;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.auth.AccessToken;
import modules.IAction;
import org.json.JSONObject;

public class PostANewMessage implements IAction {

    private Facebook mFacebook;

    public PostANewMessage(Facebook fb) {
        mFacebook = fb;
    }

    public void Activate(String obj) {
        JSONObject data = new JSONObject(obj);
        JSONObject facebook = data.getJSONObject("User")
                .getJSONObject("Services")
                .getJSONObject("Facebook");
        String msg = data.getString("data");
        String token = facebook.getString("access_token");
        mFacebook.setOAuthAccessToken(new AccessToken(token));
        try {
            mFacebook.postStatusMessage(msg);
        } catch (FacebookException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getType() {
        return "Post un message sur son mur";
    }
}
