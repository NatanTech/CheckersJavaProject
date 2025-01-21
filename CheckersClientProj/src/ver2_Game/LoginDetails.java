package ver2_Game;

import java.io.Serializable;

/**
 * Document : LoginDetails Created on : 16/11/2021, 15:22:21 Author : beita
 */
public class LoginDetails implements Serializable
{
    private String userName;
    private String password;
    private boolean withAI;

    public LoginDetails(String userName, String password, boolean withAI)
    {
        this.userName = userName;
        this.password = password;
        this.withAI = withAI;
    }

    public LoginDetails(boolean withAI)
    {
        this(null, null, withAI);
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
