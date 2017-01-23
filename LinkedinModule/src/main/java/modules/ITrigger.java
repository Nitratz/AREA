package modules;

public interface ITrigger {
    /**
     * @param Users JSONObject with Token or Other needed by the Trigger
     * @return Check the state of the Event
     */
    public String isActionned(String Users);

    /**
     * @return Get the type of the Trigger
     */
    public String getType();
}
