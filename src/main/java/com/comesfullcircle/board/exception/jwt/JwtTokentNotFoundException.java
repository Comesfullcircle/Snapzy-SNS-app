package com.comesfullcircle.board.exception.jwt;

import io.jsonwebtoken.JwtException;

public class JwtTokentNotFoundException extends JwtException {
    public JwtTokentNotFoundException() {
        super("JWT token not found");
    }
}
