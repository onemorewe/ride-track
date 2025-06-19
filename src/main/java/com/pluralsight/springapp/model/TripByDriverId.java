package com.pluralsight.springapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("trip_by_driver_id")
@Data
@NoArgsConstructor
public class TripByDriverId {
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
    private UUID tripId;
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private UUID driverId;
    private UUID riderId;
    private Instant tripStartTime;
}
