package modules;

import com.google.common.base.Splitter;
import modules.actions.PostTweet;
import modules.triggers.GetLastFav;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import okhttp3.*;
import org.json.JSONObject;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;

import java.io.IOException;
import java.util.*;

/**
 * Created by quent on 05/01/2017.
 */
public class TwitterModule implements IModules {
    private static final String consumerKey = "kretOUA6I9UppOL5bNfstBu1g";
    private static final String consumerSecret = "DoZfOIMwU6iqnG4xCYu1jtr9ye0GGXPVvS6HqyN08eLY5a5kWq";
    private static final String authLink = "https://api.twitter.com/oauth/request_token";
    List<IAction> lAction = new ArrayList<IAction>();
    List<ITrigger> lTrigger = new ArrayList<ITrigger>();
    OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(consumerKey, consumerSecret);
    OkHttpClient client = new OkHttpClient();
    JSONObject Jobj = new JSONObject();

    public TwitterModule() {
        lAction.add(new PostTweet());
        lTrigger.add(new GetLastFav());
    }

    @Override
    public List<IAction> getActions() {
        return lAction;
    }

    @Override
    public List<ITrigger> getTrigger() {
        return lTrigger;
    }

    @Override
    public String getAuthLink(String RedirectLink) {
        Request request = new Request.Builder().get().url(authLink + "?oauth_callback=" + RedirectLink).build();
            try {
            Request signedRequest = (Request) consumer.sign(request).unwrap();
            String head = signedRequest.headers().get("Authorization").concat(",oauth_callback=" + RedirectLink);
            signedRequest = new Request.Builder().get().url(authLink).addHeader("Authorization", head).build();
            Response resp = client.newCall(signedRequest).execute();
            if (resp.isSuccessful()) {
                String body = resp.body().string();
                Jobj.put(RedirectLink.substring(RedirectLink.length() - 40),body );
                return "https://api.twitter.com/oauth/authenticate?oauth_token=" + body.substring(12, 39);
            }
        } catch (OAuthMessageSignerException | OAuthExpectationFailedException | OAuthCommunicationException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String getModuleName() {
        return "Twitter";
    }


    @Override
    public String Auth(String AuthCode, String ID) {
        JSONObject ret = new JSONObject();

        RequestBody formBody = new FormBody.Builder()
                .add("oauth_verifier", AuthCode)
                .add("oauth_token", Jobj.getString(ID).substring(12, 39))
                .build();
        Request request = new Request.Builder().post(formBody).url("https://api.twitter.com/oauth/access_token").build();
        try {
            Response resp = client.newCall(request).execute();

            if (resp.isSuccessful()){
                Map<String, String> map = Splitter.on('&').trimResults().withKeyValueSeparator("=").split(resp.body().string());
                ret.put("oauth_token", map.get("oauth_token"));
                ret.put("oauth_token_secret", map.get("oauth_token_secret"));
                ret.put("user_id", map.get("user_id"));
                ret.put("screen_name", map.get("screen_name"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret.toString();
    }
}
