package modules.triggers;

import facebook4j.*;
import facebook4j.auth.AccessToken;
import modules.ITrigger;
import org.json.JSONObject;

public class HasNewLikedPost implements ITrigger {

    private Facebook mFacebook;
    private Like mLastLiked = null;

    public HasNewLikedPost(Facebook fb) {
        mFacebook = fb;
    }

    public String isActionned(String Users) {
        JSONObject user = new JSONObject(Users);
        JSONObject facebook = user.getJSONObject("Services")
                .getJSONObject("Facebook");
        String token = facebook.getString("access_token");
        mFacebook.setOAuthAccessToken(new AccessToken(token));
        try {
            ResponseList<Like> userLikes = mFacebook.getUserLikes();
            Like liked = userLikes.get(0);
            if (mLastLiked == null)
                mLastLiked = liked;
            else if (!liked.getId().equals(mLastLiked.getId())) {
                mLastLiked = liked;
                return "Page liked on Facebook : https://facebook.com/" + mLastLiked.getId();
            }
        } catch (FacebookException e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }

    public String getType() {
        return "Nouveau status sur le mur";
    }
}
