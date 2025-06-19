package com.pluralsight.springapp.repository;

import com.pluralsight.springapp.model.TripByRiderId;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface TripByRiderIdRepository extends CassandraRepository<TripByRiderId, UUID> {

}
