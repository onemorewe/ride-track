package com.pluralsight.springapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("trip_by_rider_id")
@Data
@NoArgsConstructor
public class TripByRiderId {
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
    private UUID tripId;
    private UUID driverId;
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private UUID riderId;
    private Instant tripStartTime;
}
