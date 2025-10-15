package com.app.dao;

import com.app.config.DataSourceConfig;
import com.app.model.User;
import com.app.security.CryptoUtil;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

public class UserDAO {
    private final DataSource ds = DataSourceConfig.get();

    public Optional<User> findByUsername(String username) throws Exception {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    public Optional<User> findById(long id) throws Exception {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    public void create(User u) throws Exception {
        String sql = "INSERT INTO users(username,email,password_hash,first_name,last_name,role,phone_enc,prefs_enc) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPasswordHash());
            ps.setString(4, u.getFirstName());
            ps.setString(5, u.getLastName());
            ps.setString(6, u.getRole());
            ps.setString(7, safeEncrypt(u.getPhone()));
            ps.setString(8, safeEncrypt(u.getPreferencesJson()));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) u.setId(keys.getLong(1));
            }
        } catch (Exception ex) {
            throw new Exception("UserDAO.create failed for username=" + u.getUsername(), ex);
        }
    }

    public void updateProfile(User u) throws Exception {
        String sql = "UPDATE users SET email=?, first_name=?, last_name=?, phone_enc=?, prefs_enc=? WHERE id=?";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getEmail());
            ps.setString(2, u.getFirstName());
            ps.setString(3, u.getLastName());
            ps.setString(4, safeEncrypt(u.getPhone()));
            ps.setString(5, safeEncrypt(u.getPreferencesJson()));
            ps.setLong(6, u.getId());
            ps.executeUpdate();
        } catch (Exception ex) {
            throw new Exception("UserDAO.updateProfile failed for id=" + u.getId(), ex);
        }
    }

    private User map(ResultSet rs) throws Exception {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setUsername(rs.getString("username"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setFirstName(rs.getString("first_name"));
        u.setLastName(rs.getString("last_name"));
        u.setRole(rs.getString("role"));
        try {
            u.setPhone(safeDecrypt(rs.getString("phone_enc")));
            u.setPreferencesJson(safeDecrypt(rs.getString("prefs_enc")));
        } catch (Exception ex) {
            throw new Exception("UserDAO.map failed while decrypting user id=" + u.getId(), ex);
        }
        return u;
    }

    private String safeEncrypt(String plain) throws Exception {
        if (plain == null || plain.isBlank()) return null;
        try {
            return CryptoUtil.encrypt(plain);
        } catch (Exception ex) {
            throw new Exception("Encryption failed (redacted): " + ex.getMessage(), ex);
        }
    }

    private String safeDecrypt(String b64) throws Exception {
        if (b64 == null || b64.isBlank()) return null;
        try {
            return CryptoUtil.decrypt(b64);
        } catch (Exception ex) {
            throw new Exception("Decryption failed (redacted): " + ex.getMessage(), ex);
        }
    }
}