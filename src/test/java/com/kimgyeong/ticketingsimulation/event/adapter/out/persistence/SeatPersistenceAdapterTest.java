package com.kimgyeong.ticketingsimulation.event.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.com.google.common.collect.Iterables;

import com.kimgyeong.ticketingsimulation.event.adapter.out.persistence.entity.SeatEntity;
import com.kimgyeong.ticketingsimulation.event.adapter.out.persistence.mapper.SeatEntityMapper;
import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;
import com.kimgyeong.ticketingsimulation.event.domain.model.SeatStatus;

@ExtendWith(MockitoExtension.class)
class SeatPersistenceAdapterTest {
	@InjectMocks
	private SeatPersistenceAdapter adapter;

	@Mock
	private JpaSeatRepository repository;

	@Test
	void findAllByEventId() {
		SeatEntity seatEntity = new SeatEntity(1L, 1L, SeatStatus.AVAILABLE, 1, LocalDateTime.now(), 1L);
		List<SeatEntity> entities = List.of(seatEntity);
		given(repository.findAllByEventId(anyLong())).willReturn(entities);

		List<Seat> result = adapter.findAllByEventId(1L);

		assertThat(result).isNotEmpty();
		assertThat(result.get(0).eventId()).isEqualTo(1L);
	}

	@Test
	void save() {
		Seat seat = new Seat(1L, 1L, SeatStatus.AVAILABLE, 1, LocalDateTime.now(), 1L);
		SeatEntity entity = SeatEntityMapper.toEntity(seat);

		given(repository.save(any(SeatEntity.class))).willReturn(entity);

		adapter.save(seat);

		then(repository).should().save(any(SeatEntity.class));
	}

	@Test
	void findAllExpiredHeldSeats() {
		LocalDateTime threshold = LocalDateTime.now().minusSeconds(120);

		SeatEntity expiredSeat1 = new SeatEntity(1L, 1L, SeatStatus.TEMPORARY_HOLD, 1, threshold.minusSeconds(10),
			null);
		SeatEntity expiredSeat2 = new SeatEntity(2L, 1L, SeatStatus.TEMPORARY_HOLD, 2, threshold.minusSeconds(30),
			null);

		given(repository.findAllExpiredHeldSeats(threshold))
			.willReturn(List.of(expiredSeat1, expiredSeat2));

		List<Seat> result = adapter.findAllExpiredHeldSeats(threshold);

		assertThat(result).hasSize(2);
		assertThat(result.get(0).id()).isEqualTo(1L);
		assertThat(result.get(1).number()).isEqualTo(2);
		assertThat(result.get(0).status()).isEqualTo(SeatStatus.TEMPORARY_HOLD);
	}

	@Test
	void saveAll() {
		Seat domainSeat = new Seat(1L, 1L, SeatStatus.TEMPORARY_HOLD, 1, LocalDateTime.now(), 1L);

		adapter.saveAll(List.of(domainSeat));

		then(repository).should().saveAll(
			argThat(entities -> Iterables.size(entities) == 1 &&
				Iterables.get(entities, 0).getId().equals(domainSeat.id()))
		);
	}

	@Test
	void countAvailableByEvnetId() {
		Long eventId = 1L;
		given(repository.countAvailableByEventId(eventId)).willReturn(5L);

		long result = adapter.countAvailableByEventId(eventId);

		assertThat(result).isEqualTo(5L);
	}
}