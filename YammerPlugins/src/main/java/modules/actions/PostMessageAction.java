package modules.actions;

import modules.IAction;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class PostMessageAction implements IAction {
    static private final String _MessageLink = "https://www.yammer.com/api/v1/messages.json";
    private OkHttpClient _client = new OkHttpClient();

    @Override
    public void Activate(String obj) {
        JSONObject Obj = new JSONObject(obj);
        JSONObject modules = Obj.getJSONObject("User")
                .getJSONObject("Services");
        JSONObject yammer = modules.getJSONObject("Yammer");
        String message = Obj.getString("data");
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("body", message);
        FormBody form = builder.build();
        Request request = new Request.Builder()
                .url(_MessageLink)
                .addHeader("Authorization", "Bearer " + yammer.getJSONObject("access_token").getString("token"))
                .post(form)
                .build();
        try {
            Response response = _client.newCall(request).execute();
            String ret = response.body().string();
            Obj = new JSONObject(ret);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}
