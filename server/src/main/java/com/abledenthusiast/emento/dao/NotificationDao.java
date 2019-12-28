package com.abledenthusiast.emento.dao;

import com.abledenthusiast.emento.dao.connectionfactory.ConnectionFactory;
import com.abledenthusiast.emento.scheduling.notifications.Notification;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;

import java.util.Optional;
import java.util.UUID;

public class NotificationDao extends CassandraDao<Notification> {

    private PreparedStatement insertStatement;
    private PreparedStatement selectStatement;

    public NotificationDao(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    public void insert(Notification notification) {
        BoundStatement statement = insertStatement.bind(notification.getId(), toJson(notification));
        execute(statement);
    }

    public Notification selectNotification(UUID id) {
        BoundStatement statement = selectStatement.bind(id);
        ResultSet results = execute(statement);

        return Optional.ofNullable(results)
                .map(ResultSet::one)
                .flatMap(result -> toValue(result.getString("notification_props"), Notification.class))
                .orElse(null);
    }



    @Override
    protected void initPreparedStatements() {
        session.execute("CREATE TABLE IF NOT EXISTS emento.notification (id TEXT, notification_props TEXT, PRIMARY KEY(id))");
        insertStatement = session.prepare("INSERT INTO  emento.notification (id, notification_props) VALUES (?,?)");
        selectStatement = session.prepare("SELECT * from emento.notification WHERE id = ?");
    }


}
