package com.abledenthusiast.emento.dao;

import com.abledenthusiast.emento.dao.connectionfactory.CassandraConnectionFactory;
import com.abledenthusiast.emento.dto.Schedule;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;

public class ScheduleDao extends CassandraDao<Schedule> {

    private PreparedStatement insertStatement;

    public ScheduleDao(CassandraConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    public void prepareInsertStatement() {
        insertStatement = session.prepare("INSERT INTO  emento.schedule (id, scheduling_properties) VALUES (?,?)");
    }

    public void insertSchedule(Schedule schedule) {
        BoundStatement boundStatement = new BoundStatement(insertStatement);
        session.execute(boundStatement.bind(schedule.id(), toJson(schedule)));
    }


    @Override
    protected void initPreparedStatements() {
        prepareInsertStatement();
    }

}
