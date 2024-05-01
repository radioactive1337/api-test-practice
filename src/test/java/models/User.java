package models;

public record User(Integer id, String username, String firstName, String lastName, String email, String password,
                   String phone, Integer userStatus) {
}