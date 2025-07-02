package com.kimgyeong.ticketingsimulation.event.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.kimgyeong.ticketingsimulation.event.adapter.out.persistence.entity.SeatEntity;
import com.kimgyeong.ticketingsimulation.event.domain.model.SeatStatus;

@DataJpaTest
class JpaSeatRepositoryTest {

	@Autowired
	private JpaSeatRepository repository;

	@Test
	void findAllExpiredHeldSeats() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expiredTime = now.minusSeconds(130);

		SeatEntity valid1 = new SeatEntity(null, 1L, SeatStatus.TEMPORARY_HOLD, 1, expiredTime, null);
		SeatEntity valid2 = new SeatEntity(null, 1L, SeatStatus.TEMPORARY_HOLD, 2, expiredTime.minusMinutes(1), null);
		SeatEntity invalid = new SeatEntity(null, 1L, SeatStatus.AVAILABLE, 3, null, null);

		repository.saveAll(List.of(valid1, valid2, invalid));

		List<SeatEntity> result = repository.findAllExpiredHeldSeats(now.minusSeconds(120));

		// then
		assertThat(result).hasSize(2);
		assertThat(result).extracting("status").containsOnly(SeatStatus.TEMPORARY_HOLD);
	}
}