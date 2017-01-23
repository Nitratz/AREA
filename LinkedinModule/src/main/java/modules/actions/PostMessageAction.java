package modules.actions;

import modules.IAction;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Maxime on 05/01/2017.
 */
public class PostMessageAction implements IAction {
    static private final String _MessageLink = "https://api.linkedin.com/v1/people/~/shares?format=json";
    private OkHttpClient _client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json");

    /*
    {
  "comment": "Check out developer.linkedin.com!",
  "content": {
    "title": "LinkedIn Developers Resources",
    "description": "Leverage LinkedIn's APIs to maximize engagement",
    "submitted-url": "https://developer.linkedin.com",
    "submitted-image-url": "https://example.com/logo.png"
  },
  "visibility": {
    "code": "anyone"
  }
}
    */

    @Override
    public void Activate(String obj) {
        JSONObject jbody = new JSONObject();
        JSONObject content = new JSONObject();
        JSONObject Obj = new JSONObject(obj);
        String message = Obj.getString("data");
        jbody.put("comment", "Post depuis AREA PPAPI");
        content.put("title", "--- PPAPI ---");
        content.put("description", message);
        jbody.put("content", content);
        JSONObject modules = Obj.getJSONObject("User")
                .getJSONObject("Services");
        JSONObject linkedin = modules.getJSONObject("Linkedin");
        RequestBody body = RequestBody.create(JSON, jbody.toString());
        Request request = new Request.Builder()
                .url(_MessageLink)
                .addHeader("x-li-format", "json")
                .addHeader("Authorization", "Bearer " + linkedin.getString("access_token"))
                .post(body)
                .build();
        try {
            Response response = _client.newCall(request).execute();
            String ret = response.body().string();
            Obj = new JSONObject(ret);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getType() {
        return "Post un message sur Linkedin";
    }
}