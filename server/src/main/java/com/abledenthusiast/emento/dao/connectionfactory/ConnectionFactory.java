package com.abledenthusiast.emento.dao.connectionfactory;

public interface ConnectionFactory <T> {
    T connect();
}
