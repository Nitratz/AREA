package area;

import database.IUser;
import database.MongoDBUser;
import modules.IModules;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/User")
public class UserController {
    @RequestMapping("/CurrentLoggedList")
    public static String CurrentLoggedList() {
        JSONArray ret = new JSONArray();

        for (IUser user : Application.LoggedUser) {
            ret.put(user.getName());
        }

        return ret.toString();
    }

    @RequestMapping("/Login")
    public static String Login(@RequestParam(value = "email") String email,
                               @RequestParam(value = "password") String password) {
        JSONObject obj = new JSONObject();

        try {
            IUser user = MongoDBUser.getUser(email, password);
            for (int i = 0; i < Application.LoggedUser.size(); i++) {
                if (Application.LoggedUser.get(i).getEmail().equals(user.getEmail())) {
                    Application.LoggedUser.remove(i);
                    i--;
                }
            }
            Application.LoggedUser.add(user);
            obj.put("success", true).put("Token", user.getToken());
        } catch (Exception e) {
            obj.put("success", false).put("Error", e.getMessage());
        }
        return obj.toString();
    }

    @RequestMapping("/CreateUser")
    public static String CreateUser(@RequestParam(value = "email") String email,
                                    @RequestParam(value = "password") String password,
                                    @RequestParam(value = "fullname") String fullname) {
        JSONObject obj = new JSONObject();

        try {
            IUser user = MongoDBUser.createUser(email, password, fullname);
            obj.put("success", true);
        } catch (Exception e) {
            obj.put("success", false).put("Error", e.getMessage());
        }
        return obj.toString();
    }

    @RequestMapping("/AddArea")
    public static String addArea(@RequestParam(value = "token") String token,
                                 @RequestParam(value = "actionID") String actionID,
                                 @RequestParam(value = "triggerID") String triggerID,
                                 @RequestParam(value = "actionModuleName") String actionModuleName,
                                 @RequestParam(value = "triggerModuleName") String triggerModuleName) {
        for (IUser user : Application.LoggedUser) {
            if (user.getToken().equals(token)) {
                JSONObject jService = new JSONObject(user.getServices());
                if (jService.has(actionModuleName) && jService.has(triggerModuleName)) {
                    JSONObject jobj = new JSONObject();
                    jobj.put("actionID", actionID);
                    jobj.put("triggerID", triggerID);
                    jobj.put("actionModuleName", actionModuleName);
                    jobj.put("triggerModuleName", triggerModuleName);
                    jobj.put("ID", new ObjectId().toString());
                    user.addArea(jobj.toString());
                    return "{\"success\": true}";
                }
                return "{\"success\": false}";
            }
        }
        return "{\"success\": false}";
    }

    @RequestMapping("/ListArea")
    public static String listArea(@RequestParam(value = "token") String token) {
        for (IUser user : Application.LoggedUser) {
            if (user.getToken().contentEquals(token)) {
                JSONObject jobj = new JSONObject().put("success", true).put("Areas", user.getAreas());
                return jobj.toString();
            }
        }
        return "{\"success\": false}";
    }

    @RequestMapping("/DeleteArea")
    public static String deleteArea(@RequestParam(value = "token") String token,
                                    @RequestParam(value = "AreaID") String AreaID) {
        for (IUser user : Application.LoggedUser) {
            if (user.getToken().contentEquals(token)) {
                user.DeleteArea(AreaID);
                return "{\"success\": true}";
            }
        }
        return "{\"success\": false}";
    }

    @RequestMapping("/Token")
    public static String tokenChecker(@RequestParam(value = "token") String token) {
        for (IUser user : Application.LoggedUser) {
            if (user.getToken().contentEquals(token)) {
                return "{\"success\": true}";
            }
        }
        return "{\"success\": false}";
    }

    @RequestMapping("/ListService")
    public static String listService(@RequestParam(value = "token") String token) {
        for (IUser user : Application.LoggedUser) {
            if (user.getToken().contentEquals(token)) {
                JSONObject jService = new JSONObject(user.getServices());
                return new JSONObject().put("success", true).put("services", jService.names()).toString();
            }
        }
        return "{\"success\": false}";
    }

    @RequestMapping("/GetAuthLink")
    public static String getAuthLink(@RequestParam(value = "token") String token,
                                     @RequestParam(value = "moduleName") String moduleName) {
        for (IUser user : Application.LoggedUser) {
            if (user.getToken().equals(token)) {
                JSONObject jService = new JSONObject(user.getServices());
                for (IModules module : Application.lModules) {
                    if (module.getModuleName().equals(moduleName))
                        try {
                            return module.getAuthLink(URLEncoder.encode(Application.Addr + "Module/" + moduleName + "/Auth" + "?ID=" + token, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                }
                return "";
            }
        }
        return "";
    }
}
