package com.abledenthusiast.emento.dao.connectionfactory;

import com.datastax.driver.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

public class CassandraConnectionFactory implements ConnectionFactory<Session> {

    private final Logger log = LoggerFactory.getLogger(CassandraConnectionFactory.class);
    private String host;
    private int port;
    private String username;
    private String password;

    private String sslKeyStorePassword = "changeit";

    private File sslKeyStoreFile;

    private Cluster cluster;

    public CassandraConnectionFactory(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    @Override
    public Session connect() {
        if (cluster == null) {

            JdkSSLOptions sslOptions = getJdkSSLOptions();
            cluster = Cluster.builder()
                             .addContactPoint(host)
                             .withPort(port)
                             .withCredentials(username, password)
                             .withSSL(sslOptions)
                             .build();
        }

        return cluster.connect();
    }

    private JdkSSLOptions getJdkSSLOptions() {
        JdkSSLOptions sslOptions = null;
        try {
            loadCassandraConnectionDetails();

            final KeyStore keyStore = KeyStore.getInstance("JKS");
            try (final InputStream is = new FileInputStream(sslKeyStoreFile)) {
                keyStore.load(is, sslKeyStorePassword.toCharArray());
            }

            final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
                                                                            .getDefaultAlgorithm());
            kmf.init(keyStore, sslKeyStorePassword.toCharArray());
            final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory
                                                                                .getDefaultAlgorithm());
            tmf.init(keyStore);

            // Creates a socket factory for HttpsURLConnection using JKS contents.
            final SSLContext sc = SSLContext.getInstance("TLSv1.2");
            sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new java.security.SecureRandom());

            sslOptions = RemoteEndpointAwareJdkSSLOptions.builder()
                                                         .withSSLContext(sc)
                                                         .build();
        } catch(Exception e) {
            log.warn("exception while attempting to setup ssl {}", e.getMessage());
        }
        return sslOptions;
    }

    /**
     * Loads Cassandra end-point details from config.properties.
     * @throws Exception
     */
    private void loadCassandraConnectionDetails() throws Exception {
        String ssl_keystore_file_path;
        String ssl_keystore_password = null;

        // If ssl_keystore_file_path, build the path using JAVA_HOME directory.

        String javaHomeDirectory = System.getenv("JAVA_HOME");
        if (javaHomeDirectory == null || javaHomeDirectory.isEmpty()) {
            throw new Exception("JAVA_HOME not set");
        }
        ssl_keystore_file_path = new StringBuilder(javaHomeDirectory).append("/lib/security/cacerts").toString();

        sslKeyStorePassword = (ssl_keystore_password != null && !ssl_keystore_password.isEmpty()) ?
                              ssl_keystore_password : sslKeyStorePassword;

        sslKeyStoreFile = new File(ssl_keystore_file_path);

        if (!sslKeyStoreFile.exists() || !sslKeyStoreFile.canRead()) {
            throw new Exception(String.format("Unable to access the SSL Key Store file from %s", ssl_keystore_file_path));
        }
    }
}
