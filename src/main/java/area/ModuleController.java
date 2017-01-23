package area;

import database.IUser;
import modules.IModules;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tools.ModulesTools;

@RestController
@RequestMapping("/Module")
public class ModuleController {

    @RequestMapping("/Loader")
    public String Loader(@RequestParam(value="name") String name,@RequestParam(value="path") String path) {
        java.net.URL url = ModulesTools.getURL(path);
        IModules ret = ModulesTools.LoadModule(url, name);
        if (ret != null) {
            Application.lModules.add(ret);
            return "{success: true}";
        }
        return "{success: false}";
    }

    @RequestMapping("/List")
    public String List() {
        JSONArray ret = new JSONArray();

        for (int i = 0; i < Application.lModules.size(); i++) {
            ret.put(Application.lModules.get(i).getModuleName());
        }
        return (ret.toString());
    }



    @RequestMapping(value = "/{ModuleName}/Auth")
    public String Auth(@PathVariable String ModuleName, @RequestParam(value = "code")String AuthCode, @RequestParam(value = "ID")String UserID) {
        for (int i = 0; i < Application.lModules.size(); i++) {
            if (Application.lModules.get(i).getModuleName().equals(ModuleName)) {
                for (IUser user : Application.LoggedUser) {
                    if (user.getToken().equals(UserID)) {
                        user.setService(Application.lModules.get(i).Auth(AuthCode, UserID), ModuleName);
                        return ("{success: true}");
                    }
                }
            }
        }
        return ("{success: false}");
    }
}
