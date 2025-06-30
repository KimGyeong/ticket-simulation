package com.kimgyeong.ticketingsimulation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

import com.kimgyeong.ticketingsimulation.config.RedisTestContainerConfig;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TicketingSimulationApplicationTests extends RedisTestContainerConfig {

	@Test
	void contextLoads() {
	}

}
