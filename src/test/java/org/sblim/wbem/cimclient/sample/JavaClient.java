package org.sblim.wbem.cimclient.sample;

import javax.cim.CIMArgument;
import javax.cim.CIMDataType;
import javax.cim.CIMObjectPath;
import javax.cim.UnsignedInteger32;

//Copyright (C) 2012  Javi Roman <javiroman@kernel-labs.org>
//
//This program is free software; you can redistribute it and/or
//modify it under the terms of the GNU General Public License
//as published by the Free Software Foundation; either version 2
//of the License, or (at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program; if not, see <http://www.gnu.org/licenses/>.

import javax.security.auth.Subject;
import javax.wbem.CloseableIterator;
import javax.wbem.WBEMException;
import javax.wbem.client.PasswordCredential;
import javax.wbem.client.UserPrincipal;
import javax.wbem.client.WBEMClient;
import javax.wbem.client.WBEMClientFactory;

class JavaClient {
    public static void invokeCIMMethod() {
        try {
            WBEMClient cli = WBEMClientFactory.getClient("CIM-XML");
            Subject subject = new Subject();
            subject.getPrincipals().add(new UserPrincipal("admin"));
            subject.getPrivateCredentials().add(new PasswordCredential("password123"));
            CIMObjectPath cop = new CIMObjectPath("https", "192.168.2.36", "5989", "/root/cimv2", null, null);

            System.out.println(cop.getHost());
            System.out.println(cop.getNamespace());
            System.out.println(cop.getPort());

            cli.initialize(cop, subject, null);

            CloseableIterator itr = cli.execQuery(cop, "select * from KC_Widget", "WQL");

            while (itr.hasNext()) {
                Object element = itr.next();
                System.out.print(element + "\n");
            }

            // Two inputs arguments to pass to method Add()
            CIMArgument<?>[] input = new CIMArgument[2];
            // Two output value returns, the addition result and the error code
            CIMArgument<?>[] output = new CIMArgument[1];

            CIMDataType d = new CIMDataType(CIMDataType.UINT32, 1);
            CIMDataType dd = new CIMDataType(CIMDataType.UINT32, 1);

            input[0] = new CIMArgument("X", d, new UnsignedInteger32(7));
            input[1] = new CIMArgument("Y", dd, new UnsignedInteger32(7));

            Object obj = cli.invokeMethod(new CIMObjectPath("/root/cimv2:KC_Widget"), "Add", input, output);

            try {
                Thread.sleep(1000);

                if (obj.toString().equals("0")) {
                    System.out.println("Method not invoked successfully!");
                } else {
                    System.out.println("Method invoked successfully!");
                    System.out.println("result: " + obj);
                }

            } catch (InterruptedException ie) {
               ie.printStackTrace();
            }

        } catch (WBEMException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        System.out.println("CIM Java client");
        invokeCIMMethod();
    }
}
