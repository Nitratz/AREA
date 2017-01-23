package modules.triggers;

import modules.ITrigger;
import org.json.JSONObject;

public class HasChangedProfilePicture implements ITrigger {

    private JSONObject mOldProfilePic = new JSONObject();

    @Override
    public String isActionned(String Users) {
        JSONObject uInsta = RequesterTrigger.request(Users);
        String token = new JSONObject(Users)
                .getJSONObject("Services")
                .getJSONObject("Instagram")
                .getString("access_token");

        if (uInsta == null)
            return "";
        JSONObject data = uInsta.getJSONObject("data");
        String pic = data.get("profile_picture").toString();
        String username = data.get("username").toString();
        if (mOldProfilePic.has(token)) {
            if (!pic.equals(mOldProfilePic.getString(token))) {
                mOldProfilePic.put(token, pic);
                return "I've changed my profile picture : " + pic
                        + "\n\nCheck out my profile : https://instagram.com/" + username;
            }
        }
        else
            mOldProfilePic.put(token, pic);
        return "";
    }

    @Override
    public String getType() {
        return "New Profile Picture";
    }
}
