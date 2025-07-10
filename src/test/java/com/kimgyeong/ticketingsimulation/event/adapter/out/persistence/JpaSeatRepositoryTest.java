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

	@Test
	void saveAll() {
		Long eventId = 1L;
		List<SeatEntity> seats = List.of(
			new SeatEntity(null, eventId, SeatStatus.AVAILABLE, 1, null, null),
			new SeatEntity(null, eventId, SeatStatus.AVAILABLE, 2, null, null),
			new SeatEntity(null, eventId, SeatStatus.AVAILABLE, 3, null, null)
		);

		List<SeatEntity> savedEntities = repository.saveAll(seats);

		assertThat(savedEntities).hasSize(3);
		assertThat(savedEntities.get(0).getId()).isNotNull();
		assertThat(savedEntities.get(0).getEventId()).isEqualTo(eventId);
		assertThat(savedEntities.get(0).getStatus()).isEqualTo(SeatStatus.AVAILABLE);
	}

	@Test
	void countAvailableByEventId() {
		Long eventId = 1L;

		List<SeatEntity> seats = List.of(
			new SeatEntity(null, eventId, SeatStatus.AVAILABLE, 1, null, null),
			new SeatEntity(null, eventId, SeatStatus.AVAILABLE, 2, null, null),
			new SeatEntity(null, eventId, SeatStatus.TEMPORARY_HOLD, 3, null, null),
			new SeatEntity(null, eventId, SeatStatus.BOOKED, 4, null, null),
			new SeatEntity(null, 2L, SeatStatus.AVAILABLE, 1, null, null) // 다른 이벤트
		);

		repository.saveAll(seats);

		long availableCount = repository.countAvailableByEventId(eventId);

		assertThat(availableCount).isEqualTo(2);
	}
}