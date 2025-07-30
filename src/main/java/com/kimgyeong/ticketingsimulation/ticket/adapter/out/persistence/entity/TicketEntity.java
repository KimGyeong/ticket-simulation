package com.kimgyeong.ticketingsimulation.ticket.adapter.out.persistence.entity;

import java.time.LocalDateTime;

import com.kimgyeong.ticketingsimulation.global.jpa.BaseTimeEntity;
import com.kimgyeong.ticketingsimulation.ticket.domain.model.TicketStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Ticket")
@Table(name = "ticket")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class TicketEntity extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;

	private Long eventId;

	private Long seatId;

	private LocalDateTime purchasedAt;

	private LocalDateTime refundedAt;

	@Enumerated
	private TicketStatus status;

	public void markRefunded(LocalDateTime refundedAt) {
		this.refundedAt = refundedAt;
		this.status = TicketStatus.REFUNDED;
	}
}
