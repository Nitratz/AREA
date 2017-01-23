package modules;

public interface IAction {
    /**
     * @param obj Arguments as JSONObject
     */
    public void Activate(String obj);
    /**
     * @return Get the type of the Action
     */
    public String getType();
}
