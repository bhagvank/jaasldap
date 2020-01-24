

import java.io.*;
import java.util.*;
import java.security.Principal;
import java.security.PrivilegedAction;
import javax.security.auth.*;
import javax.security.auth.callback.*;
import javax.security.auth.login.*;
import javax.security.auth.spi.*;
//import com.sun.security.auth.*;

/**
 * <p> This Sample application attempts to authenticate a user
 * and executes a SampleAction as that user.
 *
 * <p> If the user successfully authenticates itself,
 * the username and number of Credentials is displayed.
 */
public class SampleAzn {

    /**
     * Attempt to authenticate the user.
     *
     * <p>
     *
     * @param args input arguments for this application.  These are ignored.
     */
     public static void main(String[] args) {

    // Obtain a LoginContext, needed for authentication. Tell it
    // to use the LoginModule implementation specified by the
    // entry named "Sample" in the JAAS login configuration
    // file and to also use the specified CallbackHandler.
    LoginContext lc = null;
    try {
        lc = new LoginContext("Sample", new MyCallbackHandler());
    } catch (LoginException le) {
        System.err.println("Cannot create LoginContext. "
            + le.getMessage());
        System.exit(-1);
    } catch (SecurityException se) {
        System.err.println("Cannot create LoginContext. "
            + se.getMessage());
        System.exit(-1);
    }

    // the user has 3 attempts to authenticate successfully
    int i;
    for (i = 0; i < 3; i++) {
        try {

        // attempt authentication
        lc.login();

        // if we return with no exception, authentication succeeded
        break;

        } catch (LoginException le) {

          System.err.println("Authentication failed:");
          System.err.println("  " + le.getMessage());
          try {
              Thread.currentThread().sleep(3000);
          } catch (Exception e) {
              // ignore
          }

        }
    }

    // did they fail three times?
    if (i == 3) {
        System.out.println("Sorry");
        System.exit(-1);
    }

    System.out.println("Authentication succeeded!");

    Subject mySubject = lc.getSubject();

    // let's see what Principals we have
    Iterator principalIterator = mySubject.getPrincipals().iterator();
    System.out.println("Authenticated user has the following Principals:");
    while (principalIterator.hasNext()) {
        Principal p = (Principal)principalIterator.next();
        System.out.println("\t" + p.toString());
    }

    System.out.println("User has " +
            mySubject.getPublicCredentials().size() +
            " Public Credential(s)");

    // now try to execute the SampleAction as the authenticated Subject
    PrivilegedAction action = new SampleAction();
    Subject.doAsPrivileged(mySubject, action, null);

    System.exit(0);
    }
}


