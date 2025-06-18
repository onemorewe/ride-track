package com.pluralsight.springapp.repository;

import com.pluralsight.springapp.model.TripEventByDriverId;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TripEventByDriverIdRepository extends CassandraRepository<TripEventByDriverId, String> {
    TripEventByDriverId findByDriverId(UUID driverId);
}
