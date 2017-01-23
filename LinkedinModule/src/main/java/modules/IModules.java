package modules;

import java.util.List;

public interface IModules {
    /**
     *
     * @return List of Actions available
     */
    public List<IAction> getActions();

    /**
     *
     * @return List of Trigger available
     */
    public List<ITrigger> getTrigger();


    /**
     * @param RedirectLink Link to the Auth controller
     * @return link for client
     */

    public String getAuthLink(String RedirectLink);

    /**
     *
     * @return The name of the Module
     */
    public String getModuleName();

    /**
     *
     * @param AuthCode JSONObject or String for authentication
     * @return Authentication
     */
    public String Auth(String AuthCode, String ID);
}
