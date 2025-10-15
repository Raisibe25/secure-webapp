package com.app.tools;

import com.app.security.PasswordUtil;

public class HashGenerator {
    public static void main(String[] args) {
        String password = "admin"; // choose your admin password
        String hash = PasswordUtil.hash(password);
        System.out.println("Bcrypt hash for admin: " + hash);
    }
}
