package com.pluralsight.springapp.repository;

import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.pluralsight.springapp.model.TripEventByTripId;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Consistency;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TripEventByTripIdRepository extends CassandraRepository<TripEventByTripId, UUID> {

    @Consistency(DefaultConsistencyLevel.LOCAL_QUORUM)
    TripEventByTripId findFirstByTripId(UUID tripId);
}
