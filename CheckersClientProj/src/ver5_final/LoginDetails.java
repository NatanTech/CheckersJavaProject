package ver5_final;

import java.io.Serializable;

/**
 * LoginDetails- represents details of player hat does login to server Document
 * : LoginDetails Created on : 16/11/2021, 15:22:21 Author : beita
 */
public class LoginDetails implements Serializable
{
    private String userName; // userName of client
    private String password; // password of client
    private boolean withAI; // if client want to play VS AI

    /**
     * Constructor to create new LoginDetails
     *
     * @param userName
     * @param password
     * @param withAI
     */
    public LoginDetails(String userName, String password, boolean withAI)
    {
        this.userName = userName;
        this.password = password;
        this.withAI = withAI;
    }

    /**
     * Constructor to create new LoginDetails to guest player
     *
     * @param withAI
     */
    public LoginDetails(boolean withAI)
    {
        this(null, null, withAI);
    }

    public LoginDetails(LoginDetails login)
    {
        this.userName = login.userName;
        this.password = login.password;
        this.withAI = login.withAI;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public boolean isWithAI()
    {
        return withAI;
    }

    public void setWithAI(boolean withAI)
    {
        this.withAI = withAI;
    }

    @Override
    public String toString()
    {
        return "LoginDetails{" + "userName=" + userName + ", password=" + password + ", withAI=" + withAI + '}';
    }

}
