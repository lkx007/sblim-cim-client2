/**
 * SimpleTestClient.java
 *
 * (C) Copyright IBM Corp. 2005, 2009
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE 
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE 
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * @author: Roberto Pineiro, IBM, roberto.pineiro@us.ibm.com  
 * @author: Chung-hao Tan, IBM ,chungtan@us.ibm.com
 * 
 * 
 * Change History
 * Flag       Date            Prog         Description
 *------------------------------------------------------------------------------- 
 *   10423    2004-08-28      thschaef     Inital Creation
 * 2219646    2008-11-17      blaschke-oss Fix / clean code to remove compiler warnings
 * 2807325    2009-06-22      blaschke-oss Change licensing from CPL to EPL
 */

package org.sblim.wbem.cimclient.sample;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.cim.CIMInstance;
import javax.cim.CIMObjectPath;
import javax.security.auth.Subject;
import javax.wbem.CloseableIterator;
import javax.wbem.client.PasswordCredential;
import javax.wbem.client.UserPrincipal;
import javax.wbem.client.WBEMClient;
import javax.wbem.client.WBEMClientFactory;

/**
 * @author lkx007
 */
public class SimpleTestClient {

    /**
     * Initializes a cim client connection to a given CIMOM. Note that the
     * initialization will not lead to client<->CIMOM communication, the first
     * request will be sent to the CIMOM when the first operation is executed.
     * 
     * @param pWbemUrl
     *            The URL of the WBEM service (e.g.
     *            <code>https://myhost.mydomain.com:5989</code>)
     * @param pUser
     *            The user name for authenticating with the WBEM service
     * @param pPassword
     *            The corresponding password
     * @return A <code>WBEMClient</code> instance if connect was successful,
     *         <code>null</code> otherwise
     */
    public static WBEMClient connect(final URL pWbemUrl, final String pUser, final String pPassword) {
        try {
            final WBEMClient client = WBEMClientFactory.getClient("CIM-XML");
            final CIMObjectPath path = new CIMObjectPath(pWbemUrl.getProtocol(), pWbemUrl.getHost(),
                    String.valueOf(pWbemUrl.getPort()), null, null, null);
            final Subject subject = new Subject();
            subject.getPrincipals().add(new UserPrincipal(pUser));
            subject.getPrivateCredentials().add(new PasswordCredential(pPassword));
            client.initialize(path, subject, new Locale[] { Locale.US });

            return client;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Object> get(WBEMClient client, String nameSpace, String CIMClass) {
        Vector<Object> result = new Vector<Object>();
        CloseableIterator iter = null;
        try {
            iter = client.enumerateInstances(new CIMObjectPath(null, null, null, nameSpace, CIMClass, null), true,
                    false, false, null);
            if (iter != null) {
                while (iter.hasNext()) {
                    // Take your action to the element from next().
                    Object next = iter.next();
                    if (next instanceof CIMInstance) {
                        result.add(next);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (iter != null) {
                iter.close();
            }
        }
        return result;
    }

    private void test() {
        try {

            // String cimAgentAddress = "http://192.168.2.85:5985";
            // // String nameSpace = "root/emc";
            // String nameSpace = "interop";
            // String user = "admin";
            // String pw = "#1Password";

            String cimAgentAddress = "https://192.168.2.36:5989";
            // String nameSpace = "cimv2";
            String nameSpace = "interop";
            String user = "admin";
            String pw = "password123";

            System.out.println("===============================================");
            System.out.println("= Starting: " + this.getClass().getName());
            System.out.println("= CIM Agent: " + cimAgentAddress);
            System.out.println("= Namespace: " + nameSpace);
            System.out.println("===============================================");
            System.out.println();

            URL url = new URL(cimAgentAddress);

            WBEMClient client = SimpleTestClient.connect(url, user, pw);
            // System.out.println(get(client, nameSpace,
            // "CIM_RegisteredProfile"));
            System.out.println(get(client, nameSpace, "CIM_AccountManagementService"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        SimpleTestClient stc = new SimpleTestClient();
        stc.test();

    }
}
