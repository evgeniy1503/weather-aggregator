CREATE SCHEMA IF NOT EXISTS weather_aggregator;

CREATE TABLE raw_weather_data (
    id BIGSERIAL PRIMARY KEY,
    source_id INTEGER NOT NULL,
    payload jsonb NOT NULL,
    timestamp TIMESTAMP NOT NULL
);

CREATE TABLE normalized_weather_data (
    id BIGSERIAL PRIMARY KEY,
    source_id INTEGER NOT NULL,
    temperature DOUBLE PRECISION NOT NULL,
    humidity DOUBLE PRECISION NOT NULL,
    timestamp TIMESTAMP NOT NULL
);