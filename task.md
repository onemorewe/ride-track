### Project Brief — “Ride-Track” micro-service

You’re building the **trip-event service** for a ride-sharing platform (think minimalist Uber clone). This service’s single job is to ingest trip events in real time and expose queries that other services (billing, customer-support, driver-analytics) will call.

---

## 1 . High-Level Functionality

| Capability              | Short description                                                                                                                                |
| ----------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------ |
| **Event ingest**        | Collect every state change for a ride: *RIDE\_REQUESTED → DRIVER\_ACCEPTED → DRIVER\_ARRIVED → RIDE\_STARTED → RIDE\_ENDED → PAYMENT\_CAPTURED*. |
| **Current trip lookup** | Given a `riderId` or `driverId`, return the in-progress trip, if any.                                                                            |
| **Trip history**        | Paginated list of a rider’s (or driver’s) past trips, newest first, filterable by date range.                                                    |
| **Geo–time heatmap**    | Down-sampled query that counts completed trips within a city grid + 15-min time buckets (used by surge-pricing service).                         |
| **Dispute snapshot**    | Fetch the *immutable* JSON blob of a single trip (all its events plus metadata) that existed at `RIDE_ENDED`, for CS reps.                       |

*Hard requirement*: writes are **very** heavy (\~10 k events/sec system-wide), reads are mostly keyed by rider/driver but the heatmap query is an aggregate scan that should still finish < 2 s at p99.

---

## 2 . Main User Stories

### Epic A — Trip lifecycle ingestion

> **As a** mobile-gateway service
> **I want** to POST each state change (`TripEvent`) as soon as it happens
> **So that** other downstream services can react in near real-time.

Acceptance: event is acknowledged in < 50 ms and is queryable immediately at `CONSISTENCY LOCAL_QUORUM`.
For the exercise we’ve scoped, your Spring “Trip-Event” service owns exactly three things:

### 1  •  Your slice of the pie

For the exercise we’ve scoped, **your Spring “Trip-Event” service owns exactly three things**:

| Responsibility          | Concrete tasks                                                                                                                                             |
| ----------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **API surface**         | • Expose `POST /trip-events` (or `/events`) that accepts a JSON body.                                                                                      |
| **Validation / guards** | • Check auth token (mock JWT is fine).<br>• Ensure state-machine order (e.g., `STARTED` can’t follow `ENDED`).<br>• Enforce idempotency (skip duplicates). |
| **Persistence**         | • Insert the event into the Cassandra table(s) you design, using the consistency level you choose.                                                         |

Everything downstream (web-socket push, Kafka fan-out, billing service, support dashboard) **is outside your deliverable** unless you decide to play with it later.

So: **yes, your only “done” criteria** are *accept valid POST → write durable row* (plus unit/integration tests proving it).

---

### 2  •  How “real” ride-sharing systems decide trip states

| State                 | Who usually triggers it                                                     | Typical signal(s)                                                                                                                      |
| --------------------- | --------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------- |
| **REQUESTED**         | Rider app                                                                   | Rider taps “Request ride”.                                                                                                             |
| **DRIVER\_ACCEPTED**  | Driver app                                                                  | Driver taps “Accept”.                                                                                                                  |
| **DRIVER\_ARRIVED**   | Driver app OR backend auto-detect (GPS within 50 m & velocity ≈ 0 for N s). |                                                                                                                                        |
| **RIDE\_STARTED**     | Driver app                                                                  | Tap “Start trip” when passenger on board.                                                                                              |
| **RIDE\_ENDED**       | Driver app                                                                  | Tap “End trip” at drop-off **plus** backend verifies: <br>• distance traveled ≥ X <br>• GPS near destination<br>• payment method valid |
| **PAYMENT\_CAPTURED** | Backend payments service                                                    | Stripe/Adyen webhook confirms charge.                                                                                                  |

So the **phone is the first source of truth**, but the **backend still verifies** with business rules (GPS sanity, fraud checks). That’s why client apps send *explicit* state-change events instead of “just streaming location until you figure it out.” It keeps:

1. **Bandwidth predictable** – one small JSON vs. 1 Hz GPS pings forever.
2. **Logic deterministic** – the driver explicitly says “I ended” → billing can close the meter at that timestamp.
3. **Disputes traceable** – support reps can see that at 12:03 the driver issued `RIDE_ENDED` and was 10 m from drop-off point.

---

---

### Epic B — Active-trip lookup

> **As a** dispatcher UI
> **I want** to GET the current active trip for a given `driverId` or `riderId`
> **So that** the ops team can manually intervene if something stalls.

Edge cases: driver on two trips simultaneously = data-integrity bug → must not ever happen.

---

### Epic C — Rider & driver history

> **As a** rider app
> **I want** to scroll my last 90 days of trips, 20 per page, newest first
> **So that** I can find receipts.

Performance: first page p95 < 120 ms.

---

### Epic D — Surge heatmap

> **As a** pricing engine
> **I want** to query “number of completed trips by 1 km × 1 km grid square and by 15-min bucket for the last 2 hours in city = ‘Charlotte’”
> **So that** I can calculate surge coefficients.

This runs every minute; okay if data is 1–2 min eventually consistent.

---

### Epic E — Immutable dispute snapshot

> **As a** customer-support rep
> **I want** a read-only JSON of the full trip (all events + metadata) exactly as it looked when the ride ended
> **So that** I can resolve fare disputes.

Must not mutate after `RIDE_ENDED`; TTL = 18 months.

---

## 3 . Constraints & Hints (but **no schema**)

1. **Partition-key design matters.**

   * Think rider-centric and driver-centric access as separate tables or materialized views.
   * Time-series data often needs bucketing (date-bucket in the PK) to avoid hot partitions.

2. **Heatmap query** is an *aggregation*, so denormalise counts during ingest or leverage a table that’s append-only per `(cityId, gridId, timeBucket)`.

3. **Immutable snapshot** can be a blob column (JSON) written once at `RIDE_ENDED` time; dispute service always fetches by `tripId`.

4. **Consistency levels**: ingest → `LOCAL_QUORUM`, reads can be `ONE` or `LOCAL_ONE` except where stronger guarantees are spelled out.

5. **TTL strategy**: events raw = keep 30 days; snapshot = 18 months; aggregates maybe 7 days (pricing only uses recent data).

6. Expect **multi-region replication** down the road. Stick to data-center-aware strategy classes (`NetworkTopologyStrategy`).

---

## 4 . Deliverable for You

* Create whatever tables / keyspaces you think satisfy the stories and constraints.
* Use Spring Data Cassandra (or CassandraTemplate) the way you like; feel free to sprinkle Hibernate-style annotations if it helps you reason.
* Put a brief comment above each table explaining which user story & query pattern it targets.

When you’re ready, drop your schema + a couple of sample repository/query snippets, and we’ll review trade-offs (partition size, clustering order, consistency, secondary indexes, etc.).

Happy modelling—ping me when you have a first cut!
