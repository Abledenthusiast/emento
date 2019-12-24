package com.abledenthusiast.emento.dao;

import com.abledenthusiast.emento.dao.connectionfactory.CassandraConnectionFactory;
import com.abledenthusiast.emento.dao.connectionfactory.ConnectionFactory;
import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public abstract class CassandraDao<T> implements Dao<T> {

    private CassandraConnectionFactory connectionFactory;
    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected Session session;

    public CassandraDao(ConnectionFactory<Session> connectionFactory) {
        session = connectionFactory.connect();
        session.execute("CREATE KEYSPACE IF NOT EXISTS emento WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};");
        initPreparedStatements();
    }

    protected abstract void initPreparedStatements();

    public ResultSet execute(Statement statement) {
        return session.execute(statement);
    }

    protected Optional<String> toJson(Object object) {
        if (object != null) {
            try {
                Optional.ofNullable(objectMapper.writeValueAsString(object));
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return Optional.empty();
    }

    protected <T> Optional<T> toValue(String json, Class<T> clazz) {
        if (json != null && clazz != null) {
            try {
                return Optional.ofNullable(objectMapper.readValue(json, clazz));
            } catch(Exception e) {
                System.out.println(e);
            }
        }
        return Optional.empty();
    }

}
