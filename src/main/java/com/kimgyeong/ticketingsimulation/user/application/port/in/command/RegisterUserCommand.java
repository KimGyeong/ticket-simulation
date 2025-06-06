package com.kimgyeong.ticketingsimulation.user.application.port.in.command;

public record RegisterUserCommand(String name, String email, String password, String phoneNumber) {
}
