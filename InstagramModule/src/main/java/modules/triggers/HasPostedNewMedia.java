package modules.triggers;

import modules.ITrigger;
import org.json.JSONObject;

public class HasPostedNewMedia implements ITrigger {

    private JSONObject mOldNbMedias = new JSONObject();

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
        String username = data.get("username").toString();
        String medias = data.getJSONObject("counts").get("media").toString();
        if (mOldNbMedias.has(token)) {
            if (!medias.equals(mOldNbMedias.getString(token))) {
                mOldNbMedias.put(token, medias);
                return "Check out my profile : https://instagram.com/" + username;
            }
        }
        else
            mOldNbMedias.put(token, medias);
        return "";
    }

    @Override
    public String getType() {
        return "New posted Media";
    }
}
