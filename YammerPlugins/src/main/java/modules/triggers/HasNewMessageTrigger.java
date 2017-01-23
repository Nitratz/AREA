package modules.triggers;

import modules.ITrigger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class HasNewMessageTrigger implements ITrigger {
    boolean _triggered = false;
    private String data;
    private OkHttpClient _client = new OkHttpClient();
    static private final String _ReceivedLink = "https://www.yammer.com/api/v1/messages/received.json";
    private JSONObject CheckingStatus = new JSONObject();

    private String getMessages(Integer older_than, Integer newer_than, Boolean threaded, Integer limit, String link, String Token) throws IOException, JSONException {
        link += (older_than != null) ? "?older_than=" + older_than : "";
        link += (newer_than != null) ? ((older_than != null) ? "&" : "?") + "newer_than=" + newer_than : "";
        link += (threaded != null) ? ((older_than != null || newer_than != null) ? "&" : "?") + "threaded=" + threaded : "";
        link += (limit != null) ? ((older_than != null || newer_than != null || threaded != null) ? "&" : "?") + "limit=" + limit : "";

        Request request = new Request.Builder()
                .url(link)
                .addHeader("Authorization", "Bearer " + Token)
                .get()
                .build();
        Response response = _client.newCall(request).execute();
        JSONObject obj = new JSONObject(response.body().string());
        return obj.getJSONArray("messages").toString();
    }

    private String CheckStatus(String Token) {
        boolean ret = false;
        Integer id = null;
        JSONArray messages;

        if (CheckingStatus.has(Token))
            id = CheckingStatus.getInt(Token);

        try {
            messages = new JSONArray(getMessages(null, id, null, 10, _ReceivedLink, Token));
            if (messages.length() > 0) {
                if (id == null) {
                    CheckingStatus.put(Token, messages.getJSONObject(0).getInt("id"));
                } else {
                    JSONObject message = messages.getJSONObject(messages.length() - 1);
                    CheckingStatus.put(Token, message.getInt("id"));
                    data = message.getJSONObject("body").getString("parsed");
                    data += "\n\n";
                    data += "Depuis : " + message.getString("web_url");
                    return data;
                }
            }
        } catch (JSONException | IOException e) {
            //e.printStackTrace();
            return "";
        }
        return "";
    }

    @Override
    public String isActionned(String Users) {
        JSONObject user = new JSONObject(Users);

        return CheckStatus(user
                .getJSONObject("Services")
                .getJSONObject("Yammer")
                .getJSONObject("access_token")
                .getString("token"));
    }

    @Override
    public String getType() {
        return "Nouveau message recu";
    }

}
