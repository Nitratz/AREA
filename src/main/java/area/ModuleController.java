package area;

import database.IUser;
import database.MongoDBModule;
import modules.IAction;
import modules.IModules;
import modules.ITrigger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import tools.ModulesTools;

import static area.Application.lModules;

@RestController
@RequestMapping("/Module")
public class ModuleController {

    @RequestMapping("/Loader")
    public String Loader(@RequestParam(value = "name") String name,
                         @RequestParam(value = "path") String path) {
        java.net.URL url = ModulesTools.getURL(path);
        IModules ret = ModulesTools.LoadModule(url, name);
        if (ret != null) {

            for (int i = 0; i < lModules.size(); i++) {
                if (lModules.get(i).getModuleName().equals(ret.getModuleName())) {
                    lModules.remove(i);
                    lModules.add(ret);
                    return "{\"success\": true}";
                }
            }
            lModules.add(ret);
            MongoDBModule.setModules(name, path);
            return "{\"success\": true}";
        }
        return "{\"success\": false}";
    }

    @RequestMapping("/List")
    public String List() {
        JSONArray ret = new JSONArray();

        for (int i = 0; i < lModules.size(); i++) {
            ret.put(lModules.get(i).getModuleName());
        }
        return (ret.toString());
    }

    @RequestMapping(value = "/{ModuleName}/Triggers")
    public String TriggerList(@PathVariable String ModuleName) {
        for (IModules mod : lModules) {
            if (mod.getModuleName().equals(ModuleName)) {
                JSONArray jTrigger = new JSONArray();
                for (ITrigger iT : mod.getTrigger()) {
                    jTrigger.put(iT.getType());
                }
                return new JSONObject().put("success", true).put("triggers", jTrigger).toString();
            }
        }
        return ("{\"success\": false}");
    }

    @RequestMapping(value = "/{ModuleName}/Trigger")
    public String getTriggerName(@PathVariable String ModuleName, @RequestParam(value = "id") int id) {
        for (IModules mod : lModules) {
            if (mod.getModuleName().equals(ModuleName)) {
                if (id < mod.getTrigger().size()) {
                    return mod.getTrigger().get(id).getType();
                }
            }
        }
        return "";
    }

    @RequestMapping(value = "/{ModuleName}/Action")
    public String getActionName(@PathVariable String ModuleName, @RequestParam(value = "id") int id) {
        for (IModules mod : lModules) {
            if (mod.getModuleName().equals(ModuleName)) {
                if (id < mod.getActions().size()) {
                    return mod.getActions().get(id).getType();
                }
            }
        }
        return "";
    }

    @RequestMapping(value = "/{ModuleName}/Actions")
    public String ActionList(@PathVariable String ModuleName) {
        for (IModules mod : lModules) {
            if (mod.getModuleName().equals(ModuleName)) {
                JSONArray jAction = new JSONArray();
                for (IAction iA : mod.getActions()) {
                    jAction.put(iA.getType());
                }
                return new JSONObject().put("success", true).put("Actions", jAction).toString();
            }
        }
        return ("{\"success\": false}");
    }

    @RequestMapping(value = "/{ModuleName}/Auth")
    public RedirectView Auth(@PathVariable String ModuleName,
                             @RequestParam(value = "oauth_verifier", defaultValue = "", required = false) String oauth_token,
                             @RequestParam(value = "code", defaultValue = "", required = false) String AuthCode,
                             @RequestParam(value = "ID") String UserID) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(Application.Addr);
        for (int i = 0; i < lModules.size(); i++) {
            if (lModules.get(i).getModuleName().equals(ModuleName)) {
                for (IUser user : Application.LoggedUser) {
                    if (user.getToken().equals(UserID)) {
                        user.setService(lModules.get(i).Auth(oauth_token.equals("") ? AuthCode : oauth_token, UserID), ModuleName);
                        return redirectView;
                    }
                }
            }
        }
        redirectView.setUrl(Application.Addr);
        return redirectView;
    }
}
