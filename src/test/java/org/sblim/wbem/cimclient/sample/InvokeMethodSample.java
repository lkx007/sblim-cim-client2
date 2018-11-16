/**
 * InvokeMethodSample.java
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
import java.util.Enumeration;
import java.util.Vector;

import javax.cim.CIMArgument;
import javax.cim.CIMDataType;
import javax.cim.CIMObjectPath;
import javax.wbem.client.WBEMClient;

/**
 * @author thschaef
 */
public class InvokeMethodSample {

    private void test() {
        try {

            String cimAgentAddress = "https://192.168.2.36:5989";
            String nameSpace = "cimv2";
            String user = "admin";
            String pw = "password123";

            System.out.println("===============================================");
            System.out.println("= Starting: " + this.getClass().getName());
            System.out.println("= CIM Agent: " + cimAgentAddress);
            System.out.println("= Namespace: " + nameSpace);
            System.out.println("===============================================");
            System.out.println();

            URL pWbemUrl = new URL(cimAgentAddress);
            WBEMClient cimClient = SimpleTestClient.connect(pWbemUrl, user, pw);

            // SendTestIndication does not use any in/out parameters
            CIMArgument<?>[] input = new CIMArgument[1];
            CIMArgument<?>[] output = new CIMArgument[0];

            input[0] = new CIMArgument<String>("CLASSNAME", CIMDataType.STRING_T, "sss");

            // This will trigger a TestIndication that is caught by our
            // listener
            Object obj = cimClient.invokeMethod(
                    new CIMObjectPath(null, null, null, nameSpace, "CIM_AccountManagementService", null),
                    "CreateAccount", input, output);

            if (obj.toString().equals("0")) {
                System.out.println("Indication generated successfully, waiting for delivery...");
                Thread.sleep(5000);
            } else {
                System.out.println("Indication not generated successfully!");
            }

            System.out.println("Sample completed.");

            cimClient.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        InvokeMethodSample iss = new InvokeMethodSample();
        iss.test();

    }
}
