package scheduler;

import area.Application;
import database.MongoDBUser;
import modules.IAction;
import modules.IModules;
import modules.ITrigger;
import org.bson.Document;
import org.json.JSONObject;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.print.Doc;
import java.util.List;
import java.util.StringJoiner;

public class JobChecker implements org.quartz.Job {

    public JobChecker() {}

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<Document> usersList = MongoDBUser.getUsers();
        List<IModules> modules = Application.lModules;

        for (Document user : usersList) {
            List<Document> areas = user.get("Areas", List.class);
            for (Document area : areas) {
                String tModuleName = area.get("triggerModuleName", String.class);
                for (IModules module : modules) {
                    handleTrigger(module, tModuleName, area, user);
                }
            }
        }
    }

    private void handleTrigger(IModules module, String tModuleName, Document area, Document user) {
        if (module.getModuleName().equals(tModuleName)) {
            ITrigger trigger = module.getTrigger().get(Integer.valueOf(area.get("triggerID", String.class)));
            String ret = trigger.isActionned(user.toJson());
            if (ret != null && !ret.equals("")) {
                IAction action = module.getActions().get(Integer.valueOf(area.get("actionID", String.class)));
                JSONObject obj = new JSONObject();
                obj.put("User", new JSONObject(user.toJson()));
                obj.put("data", ret);
                action.Activate(obj.toString());
            }
        }
    }
}
