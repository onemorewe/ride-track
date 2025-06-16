package com.pluralsight.springapp.repository;

import com.pluralsight.springapp.model.TripEventByTripId;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TripEventsByTripIdRepository extends CassandraRepository<TripEventByTripId, UUID> {

    TripEventByTripId findByTripId(UUID tripId);
}
