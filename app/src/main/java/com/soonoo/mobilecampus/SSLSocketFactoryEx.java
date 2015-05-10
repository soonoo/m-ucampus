package com.soonoo.mobilecampus;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * Created by soonoo on 2015-05-08.
 */
class SSLSocketFactoryEx extends SSLSocketFactory {
    String args[] = {
//            "SSL_CK_RC4_128_EXPORT40_WITH_MD5",
//            "SSL_CK_RC2_128_CBC_EXPORT40_WITH_MD5",
//            "TLS_RSA_EXPORT_WITH_RC4_40_MD5",
//            "TLS_RSA_EXPORT_WITH_RC2_CBC_40_MD5",
//            "SSL_CK_DES_64_CBC_WITH_MD5",
//            "SSL_CK_RC4_128_WITH_MD5",
//            "SSL_CK_RC2_128_CBC_WITH_MD5",
//            "SSL_CK_IDEA_128_CBC_WITH_MD5",
              "SSL_RSA_WITH_RC4_128_MD5"
//            "SSL_CK_DES_192_EDE3_CBC_WITH_MD5"
    };
    public SSLSocketFactoryEx() throws NoSuchAlgorithmException, KeyManagementException {
        initSSLSocketFactoryEx(null, null, null);
    }

    public SSLSocketFactoryEx(KeyManager[] km, TrustManager[] tm, SecureRandom random) throws NoSuchAlgorithmException, KeyManagementException {
        initSSLSocketFactoryEx(km, tm, random);
    }

    public SSLSocketFactoryEx(SSLContext ctx) throws NoSuchAlgorithmException, KeyManagementException {
        initSSLSocketFactoryEx(ctx);
    }

    public String[] getDefaultCipherSuites() {
        return args;
    }

    public String[] getSupportedCipherSuites() {
        return args;
    }

    public String[] getDefaultProtocols() {
        return m_protocols;
    }

    public String[] getSupportedProtocols() {
        return m_protocols;
    }

    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        SSLSocketFactory factory = m_ctx.getSocketFactory();
        SSLSocket ss = (SSLSocket) factory.createSocket(s, host, port, autoClose);

        ss.setEnabledProtocols(m_protocols);
        //ss.setEnabledCipherSuites(m_ciphers);
        ss.setEnabledCipherSuites(args);
        return ss;
    }

    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        SSLSocketFactory factory = m_ctx.getSocketFactory();
        SSLSocket ss = (SSLSocket) factory.createSocket(address, port, localAddress, localPort);

        ss.setEnabledProtocols(m_protocols);
        //ss.setEnabledCipherSuites(m_ciphers);
        ss.setEnabledCipherSuites(args);

        return ss;
    }

    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        SSLSocketFactory factory = m_ctx.getSocketFactory();
        SSLSocket ss = (SSLSocket) factory.createSocket(host, port, localHost, localPort);

        ss.setEnabledProtocols(m_protocols);
        //ss.setEnabledCipherSuites(m_ciphers);
        ss.setEnabledCipherSuites(args);

        return ss;
    }

    public Socket createSocket(InetAddress host, int port) throws IOException {
        SSLSocketFactory factory = m_ctx.getSocketFactory();
        SSLSocket ss = (SSLSocket) factory.createSocket(host, port);

        ss.setEnabledProtocols(m_protocols);
        //ss.setEnabledCipherSuites(m_ciphers);
        ss.setEnabledCipherSuites(args);

        return ss;
    }

    public Socket createSocket(String host, int port) throws IOException {
        SSLSocketFactory factory = m_ctx.getSocketFactory();
        SSLSocket ss = (SSLSocket) factory.createSocket(host, port);

        ss.setEnabledProtocols(m_protocols);
        //ss.setEnabledCipherSuites(m_ciphers);
        ss.setEnabledCipherSuites(args);

        return ss;
    }

    private void initSSLSocketFactoryEx(KeyManager[] km, TrustManager[] tm, SecureRandom random)
            throws NoSuchAlgorithmException, KeyManagementException {
        m_ctx = SSLContext.getInstance("TLS");
        m_ctx.init(km, tm, random);

        m_protocols = GetProtocolList();
        m_ciphers = GetCipherList();
    }

    private void initSSLSocketFactoryEx(SSLContext ctx)
            throws NoSuchAlgorithmException, KeyManagementException {
        m_ctx = ctx;

        m_protocols = GetProtocolList();
        m_ciphers = GetCipherList();
    }

    protected String[] GetProtocolList() {
        String[] preferredProtocols = { "TLSv1.1"};
        String[] availableProtocols = null;

        SSLSocket socket = null;

        try {
            SSLSocketFactory factory = m_ctx.getSocketFactory();
            socket = (SSLSocket) factory.createSocket();

            availableProtocols = socket.getSupportedProtocols();
            Arrays.sort(availableProtocols);
        } catch (Exception e) {
            return new String[]{"TLSv1"};
        } finally {
            try {
                if (socket != null)
                    socket.close();
            } catch (Exception e) {
            }
        }

        List<String> aa = new ArrayList<String>();
        for (int i = 0; i < preferredProtocols.length; i++) {
            int idx = Arrays.binarySearch(availableProtocols, preferredProtocols[i]);
            if (idx >= 0)
                aa.add(preferredProtocols[i]);
        }

        return aa.toArray(new String[0]);
    }

    protected String[] GetCipherList() {
        String[] preferredCiphers = {

                "SSL_CK_RC4_128_EXPORT40_WITH_MD5",
                "SSL_CK_RC2_128_CBC_EXPORT40_WITH_MD5",
                "TLS_RSA_EXPORT_WITH_RC4_40_MD5",
                "TLS_RSA_EXPORT_WITH_RC2_CBC_40_MD5",
                "SSL_CK_DES_64_CBC_WITH_MD5",
                "SSL_CK_RC4_128_WITH_MD5",
                "SSL_CK_RC2_128_CBC_WITH_MD5",
                "SSL_CK_IDEA_128_CBC_WITH_MD5",
                "TLS_RSA_WITH_RC4_128_MD5",
                "SSL_CK_DES_192_EDE3_CBC_WITH_MD5",

                "TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256",
                "TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256",
                "TLS_DHE_RSA_WITH_CHACHA20_POLY1305_SHA256",
                "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA",
                "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA",
                "TLS_DHE_RSA_WITH_AES_256_CBC_SHA",
                "TLS_DHE_DSS_WITH_AES_256_CBC_SHA",
                "TLS_RSA_WITH_AES_256_CBC_SHA",
                "TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA",
                "TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA",
                "TLS_DHE_RSA_WITH_3DES_EDE_CBC_SHA",
                "TLS_DHE_DSS_WITH_3DES_EDE_CBC_SHA",
                "TLS_RSA_WITH_3DES_EDE_CBC_SHA",
                "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
                "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
                "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA",
                "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
                "TLS_DHE_DSS_WITH_AES_128_GCM_SHA256",
                "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256",
                "TLS_DHE_RSA_WITH_AES_128_CBC_SHA",
                "TLS_DHE_DSS_WITH_AES_128_CBC_SHA",
                "TLS_RSA_WITH_AES_128_GCM_SHA256",
                "TLS_RSA_WITH_AES_128_CBC_SHA",
                "TLS_ECDHE_RSA_WITH_RC4_128_SHA",
                "TLS_ECDHE_ECDSA_WITH_RC4_128_SHA",
                "TLS_RSA_WITH_RC4_128_SHA",
                "TLS_RSA_WITH_RC4_128_MD5",
                "TLS_EMPTY_RENEGOTIATION_INFO_SCSV"
        };

        String[] availableCiphers = null;

        try {
            SSLSocketFactory factory = m_ctx.getSocketFactory();
            availableCiphers = factory.getSupportedCipherSuites();
            Arrays.sort(availableCiphers);
        } catch (Exception e) {
            return new String[]{
                    "SSL_CK_RC4_128_EXPORT40_WITH_MD5",
                    "SSL_CK_RC2_128_CBC_EXPORT40_WITH_MD5",
                    "TLS_RSA_EXPORT_WITH_RC4_40_MD5",
                    "TLS_RSA_EXPORT_WITH_RC2_CBC_40_MD5",
                    "SSL_CK_DES_64_CBC_WITH_MD5",
                    "SSL_CK_RC4_128_WITH_MD5",
                    "SSL_CK_RC2_128_CBC_WITH_MD5",
                    "SSL_CK_IDEA_128_CBC_WITH_MD5",
                    "TLS_RSA_WITH_RC4_128_MD5",
                    "SSL_CK_DES_192_EDE3_CBC_WITH_MD5",

                    "TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256",
                    "TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256",
                    "TLS_DHE_RSA_WITH_CHACHA20_POLY1305_SHA256",
                    "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA",
                    "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA",
                    "TLS_DHE_RSA_WITH_AES_256_CBC_SHA",
                    "TLS_DHE_DSS_WITH_AES_256_CBC_SHA",
                    "TLS_RSA_WITH_AES_256_CBC_SHA",
                    "TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA",
                    "TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA",
                    "TLS_DHE_RSA_WITH_3DES_EDE_CBC_SHA",
                    "TLS_DHE_DSS_WITH_3DES_EDE_CBC_SHA",
                    "TLS_RSA_WITH_3DES_EDE_CBC_SHA",
                    "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
                    "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
                    "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA",
                    "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
                    "TLS_DHE_DSS_WITH_AES_128_GCM_SHA256",
                    "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256",
                    "TLS_DHE_RSA_WITH_AES_128_CBC_SHA",
                    "TLS_DHE_DSS_WITH_AES_128_CBC_SHA",
                    "TLS_RSA_WITH_AES_128_GCM_SHA256",
                    "TLS_RSA_WITH_AES_128_CBC_SHA",
                    "TLS_ECDHE_RSA_WITH_RC4_128_SHA",
                    "TLS_ECDHE_ECDSA_WITH_RC4_128_SHA",
                    "TLS_RSA_WITH_RC4_128_SHA",
                    "TLS_RSA_WITH_RC4_128_MD5",
                    "TLS_EMPTY_RENEGOTIATION_INFO_SCSV"
            };
        }

        List<String> aa = new ArrayList<String>();
        for (int i = 0; i < preferredCiphers.length; i++) {
            int idx = Arrays.binarySearch(availableCiphers, preferredCiphers[i]);
            if (idx >= 0)
                aa.add(preferredCiphers[i]);
        }

        aa.add("TLS_EMPTY_RENEGOTIATION_INFO_SCSV");

        return aa.toArray(new String[0]);
    }

    private SSLContext m_ctx;

    private String[] m_ciphers;
    private String[] m_protocols;
}