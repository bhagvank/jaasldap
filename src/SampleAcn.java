/*
 * @(#)SampleAcn.java
 *
 * Copyright 2001-2002 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * -Redistributions of source code must retain the above copyright
 * notice, this  list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduct the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES  SUFFERED BY LICENSEE AS A RESULT OF  OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THE SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that Software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */


import java.io.*;
import java.util.*;
import javax.security.auth.login.*;
import javax.security.auth.*;
import javax.security.auth.callback.*;

/**
 * <p> This Sample application attempts to authenticate a user
 * and reports whether or not the authentication was successful.
 */
public class SampleAcn {

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

    }
}



