package com.abledenthusiast.emento.dao.connectionfactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.Session;

public class CassandraConnectionFactory implements ConnectionFactory<Session> {
    private String host;
    private int port;
    private String username;
    private String password;

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
            cluster = Cluster.builder()
                             .withProtocolVersion(ProtocolVersion.V4)
                             .addContactPoint(host)
                             .withPort(port)
                             .withCredentials(username, password)
                             .build();
        }

        return cluster.connect();
    }
}
