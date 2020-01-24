import java.util.Hashtable;
import java.util.Properties;
import java.io.FileInputStream;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

/**
 * This class is a singleton LDAP Authenticator
 * The LDAP server properties are configured using
 * system property ldap.properties.config which points
 * to the properties file location.
 *
 */
public class LDAPAuthenticator
{

  private static LDAPAuthenticator sAuthenticator = new LDAPAuthenticator();

  private static final String FACTORY_CLASS = "FactoryClass";

  private static final String PROVIDER_URL = "ProviderURL";

  private static final String PRINCIPAL = "Principal";

  private static final String CREDENTIALS = "Credentials";

  private static final String LDAP_PROPERTIES = "ldap.properties.config";

  private static final String USER_ID = "uid=";

  private static final String PASSWORD = "userPassword";

  private static Properties sProperties;

  /**
   * Constructor
   * intializes the static server properties
   *
   */
  private LDAPAuthenticator()
  {
      try
      {
    sProperties = new Properties();
    System.out.println(System.getProperty(LDAP_PROPERTIES));
    sProperties.load(new FileInputStream(System.getProperty(LDAP_PROPERTIES)));
      }
      catch(Exception exception)
      {
          exception.printStackTrace();
      }
  }

  /**
   * returns the instance of the LDAP Authenticator
   *
   * @return LDAPAuthenticator
   */
  public static LDAPAuthenticator getInstance()
  {
     return sAuthenticator;
  }

  /**
   * gets the initialcontext based on the ldap server properties
   *
   * @return the initialcontext
   * @exception exception
   */

  private InitialContext getInitialContext() throws Exception
  {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, sProperties.getProperty(FACTORY_CLASS));
        env.put(Context.PROVIDER_URL, sProperties.getProperty(PROVIDER_URL));
        env.put(Context.SECURITY_PRINCIPAL, sProperties.getProperty(PRINCIPAL));
        env.put(Context.SECURITY_CREDENTIALS, sProperties.get(CREDENTIALS));

        InitialContext initialContext = new InitialContext(env);

        return initialContext;
  }

  /**
   * authenticates with the LDAP server
   *
   * @return the authenticated or not
   */
  public boolean authenticate(String userName, char[] password)
  {

     try
     {
      InitialContext context = getInitialContext();

      DirContext dirCtx = (DirContext) context.lookup(USER_ID+userName);

      Attributes attributes = ((Attributes) dirCtx.getAttributes(""));

      String ldapPassword   = (String) (((Attribute)attributes.get(PASSWORD)).get());

      if(ldapPassword.equals(new String(password)))
      {

       return true;
      }
      else
      {
          return false;
      }
     }
     catch(Exception exception)
     {
         exception.printStackTrace();
         return false;
     }


  }


}