package com.kimgyeong.ticketingsimulation.queue.integration;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kimgyeong.ticketingsimulation.config.IntegrationTestContainers;
import com.kimgyeong.ticketingsimulation.event.application.port.in.CreateEventCommand;
import com.kimgyeong.ticketingsimulation.event.application.port.in.CreateEventUseCase;
import com.kimgyeong.ticketingsimulation.queue.application.port.in.ReadQueueRankUseCase;
import com.kimgyeong.ticketingsimulation.queue.messaging.message.EnterQueueMessage;
import com.kimgyeong.ticketingsimulation.queue.messaging.producer.EnterQueueProducer;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EnterQueueIntegrationTest extends IntegrationTestContainers {

	@Autowired
	private CreateEventUseCase createEventUseCase;

	@Autowired
	private ReadQueueRankUseCase readQueueRankUseCase;

	@Autowired
	private EnterQueueProducer enterQueueProducer;

	@Test
	void enterQueueMessage_ConsumeAndStore() throws Exception {
		// given
		Long userId = 1L;
		LocalDateTime now = LocalDateTime.now();

		CreateEventCommand command = new CreateEventCommand(
			"테스트 이벤트",
			"설명",
			"https://image.com/test.jpg",
			LocalDateTime.now().minusMinutes(10),
			LocalDateTime.now().plusMinutes(10),
			1000,
			5
		);

		Long eventId = createEventUseCase.createEvent(userId, command);// UseCase를 통해 저장: 여기서 트랜잭션 커밋됨

		// 메시지 전송
		EnterQueueMessage message = new EnterQueueMessage(userId, eventId, now);
		enterQueueProducer.send(message);

		Thread.sleep(5000);

		assertThat(readQueueRankUseCase.getRank(userId, eventId)).isNotNull();
	}
}