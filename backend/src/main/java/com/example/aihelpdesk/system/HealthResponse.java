package com.example.aihelpdesk.system;

import java.time.Instant;

public record HealthResponse(String status, Instant checkedAt) {
}
