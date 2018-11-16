package org.sblim.wbem.cimclient.sample;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.cim.CIMObjectPath;
import javax.security.auth.Subject;
import javax.wbem.client.PasswordCredential;
import javax.wbem.client.UserPrincipal;
import javax.wbem.client.WBEMClient;
import javax.wbem.client.WBEMClientConstants;
import javax.wbem.client.WBEMClientFactory;

public class WbemClientCreater {
    private static Logger logger = Logger.getLogger(WbemClientCreater.class.getName());
    private static WBEMClient client;
    private static String errorMsg;
    private static String namespace;
    private static String protocol;
    private static String host;
    private static int port = -1;
    private static boolean ssl = false;
  

    static {
        init();
    }

    /**
     * 获取WBEMClient的实例
     * 
     * @author sxp
     * @since Nov 15, 2018
     */
    public static WBEMClient getInstance() {
        if (client == null) {
            throw new RuntimeException(errorMsg);
        }
        return client;
    }

    public static String getProtocol() {
        return protocol;
    }

    public static String getHost() {
        return host;
    }

    /**
     * 获取命名空间
     * 
     * @author sxp
     * @since Nov 15, 2018
     */
    public static String getNamespace() {
        return namespace;
    }

    /**
     * 获取端口
     * 
     * @author sxp
     * @since Nov 15, 2018
     */
    public static int getPort() {
        return port;
    }
    
    /**
     * 是否采用https通讯协议
     * 
     * @author sxp
     * @since Nov 15, 2018
     */
    public static boolean isSsl() {
        return ssl;
    }

    /**
     * 读取配置文件并创建Client
     * 
     * @author sxp
     * @since Nov 15, 2018
     */
    private static void init() {
        URL cimomUrl = null;
        try {
            cimomUrl = new URL("https://192.168.2.36:5989");
        } catch (MalformedURLException e) {
            errorMsg = "cimomUrl配置出错, " + e.getMessage();
            logger.log(Level.SEVERE, errorMsg, e);
            return;
        }

        String user = "admin";
        String pw = "password123";
        namespace = "cimv2";
        protocol = cimomUrl.getProtocol();
        host = cimomUrl.getHost();
        port = cimomUrl.getPort();
        
        if ("https".equalsIgnoreCase(cimomUrl.getProtocol())) {
            ssl = true;
        }

        CIMObjectPath path = new CIMObjectPath(protocol, host, String.valueOf(port), null,
                null, null);

        Subject subject = new Subject();
        subject.getPrincipals().add(new UserPrincipal(user));
        subject.getPrivateCredentials().add(new PasswordCredential(pw));

        try {
            client = WBEMClientFactory.getClient(WBEMClientConstants.PROTOCOL_CIMXML);
            client.initialize(path, subject, Locale.getAvailableLocales());
        } catch (Exception e) {
            errorMsg = "创建客户端出错, " + e.getMessage();
            logger.log(Level.SEVERE, errorMsg, e);
        }

    }

}
