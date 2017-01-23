package modules.triggers;

import modules.ITrigger;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;

import java.io.IOException;

/**
 * Created by quent on 06/01/2017.
 */
public class GetLastFav implements ITrigger {
    JSONObject LastID = new JSONObject();
    private static final String consumerKey = "kretOUA6I9UppOL5bNfstBu1g";
    private static final String consumerSecret = "DoZfOIMwU6iqnG4xCYu1jtr9ye0GGXPVvS6HqyN08eLY5a5kWq";
    OkHttpClient client = new OkHttpClient();

    @Override
    public String isActionned(String Users) {
        JSONObject User = new JSONObject(Users);
        JSONObject Twitter = User.getJSONObject("Services").getJSONObject("Twitter");
        String Link = "https://api.twitter.com/1.1/favorites/list.json";
        String Token = Twitter.getString("oauth_token");
        String TokenSecret = Twitter.getString("oauth_token_secret");
        OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(consumerKey, consumerSecret);
        consumer.setTokenWithSecret(Token, TokenSecret);
        if (LastID.has(Token)) {
            String last = LastID.getString(Token);
            Request request = new Request.Builder().get().url(Link + "?since_id=" + last).build();
            try {
                Request signedRequest = (Request) consumer.sign(request).unwrap();
                Response resp = client.newCall(signedRequest).execute();
                if (resp.isSuccessful()) {
                    JSONArray jresp = new JSONArray(resp.body().string());
                    if (jresp.length() > 0) {
                        LastID.put(Token, jresp.getJSONObject(jresp.length() - 1).getString("id_str"));
                        String data = "";
                        data += "Tweet fav :\n";
                        data += "\thttps://twitter.com/" + jresp.getJSONObject(jresp.length() - 1).getJSONObject("user").getString("id_str") + "/status/" + jresp.getJSONObject(jresp.length() - 1).getString("id_str") + "\n";
                        data += "by PPAPI";
                        return data;
                    }
                }
            } catch (OAuthMessageSignerException | OAuthExpectationFailedException | OAuthCommunicationException | IOException e) {
                e.printStackTrace();
            }
        } else {
            Request request = new Request.Builder().get().url(Link + "?count=1").build();
            try {
                Request signedRequest = (Request) consumer.sign(request).unwrap();
                Response resp = client.newCall(signedRequest).execute();
                if (resp.isSuccessful()) {
                    JSONArray jresp = new JSONArray(resp.body().string());
                    if (jresp.length() > 0) {
                        LastID.put(Token, jresp.getJSONObject(0).getString("id_str"));
                    }
                }
            } catch (OAuthMessageSignerException | OAuthExpectationFailedException | OAuthCommunicationException | IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @Override
    public String getType() {
        return "Recois le DERNIER tweet fav (ref tweet)";
    }
}
