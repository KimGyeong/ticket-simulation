package com.kimgyeong.ticketingsimulation.event.adapter.out.persistence.entity;

import java.time.LocalDateTime;

import com.kimgyeong.ticketingsimulation.global.jpa.BaseTimeEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Event")
@Table(name = "event")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class EventEntity extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	private String description;

	private String imageUrl;

	private LocalDateTime ticketingStartAt;

	private LocalDateTime eventStartAt;

	private Integer maxAttendees;

	private Long userId;
}
