package com.example.aihelpdesk.system;

import com.example.aihelpdesk.common.Result;
import java.time.Instant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public Result<HealthResponse> health() {
        return Result.success(new HealthResponse("UP", Instant.now()));
    }
}
