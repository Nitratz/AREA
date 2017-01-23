package modules.actions;

import modules.IAction;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import okhttp3.*;
import org.json.JSONObject;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by quent on 06/01/2017.
 */
public class PostTweet implements IAction {
    private static final String consumerKey = "kretOUA6I9UppOL5bNfstBu1g";
    private static final String consumerSecret = "DoZfOIMwU6iqnG4xCYu1jtr9ye0GGXPVvS6HqyN08eLY5a5kWq";
    OkHttpClient client = new OkHttpClient();

    @Override
    public void Activate(String obj) {
        OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(consumerKey, consumerSecret);
        JSONObject jobj = new JSONObject(obj);
        JSONObject TwitterObj = jobj.getJSONObject("User")
                .getJSONObject("Services")
                .getJSONObject("Twitter");
        consumer.setTokenWithSecret(TwitterObj.getString("oauth_token"),TwitterObj.getString("oauth_token_secret"));
        String Data = jobj.getString("data");
        Data = (Data.length() > 140) ? Data.substring(0,137) + "..." : Data;
        RequestBody formBody = new FormBody.Builder()
                .add("status", Data)
                .build();
        Request request = new Request.Builder().post(formBody).url("https://api.twitter.com/1.1/statuses/update.json").build();
        try {
            Request signedRequest = (Request) consumer.sign(request).unwrap();
            Response resp = client.newCall(signedRequest).execute();
        } catch (OAuthMessageSignerException | OAuthExpectationFailedException | OAuthCommunicationException | IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getType() {
        return "Poster un Tweet sur son compte Twitter";
    }
}
