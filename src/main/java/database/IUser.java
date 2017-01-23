package database;

public interface IUser {
    /**
     * @return Name of the User
     */
    public String getName();

    /**
     * @return JSONObject with all services and them Token
     */
    public String getServices();

    /**
     * @return User Token
     */
    public String getToken();

    /**
     * @return User email
     */

    public String getEmail();

    /**
     * @param service JSONObject with the new Service and its token
     * @return Add result
     */
    public boolean setService(String service, String Service_Name);

    public boolean addArea(String Area);

    public String getAreas();

    public void DeleteArea(String Area);
}
