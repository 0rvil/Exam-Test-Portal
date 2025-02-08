package com.orvil;

import java.util.Random;

// Class for creating Users, that is students and instructor objects
public class User {

    private String fName;
    private String lName;
    private String userName;
    private String password;
    private String role;

    // A constructor that generates a User from an array
    User(String[] args) {
        lName = args[0];
        fName = args[1];
        userName = args[2];
        password = args[3];
        role = args[4];
    }

    public String getFirstName() {
        return fName;
    }

    public String getLastName() {
        return lName;
    }

    public String getFullName() {
        return fName + " " + lName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public static String generatePassword() {
        Random rand = new Random();
        String[] letters = "QWERTYUIOPLKJHGFDSAZXCVBNM".split("");
        String password = "";

        for (int i = 0; i < 6; i++) {
            int index = rand.nextInt(letters.length);
            String letter = letters[index];
            password += letter;
        }

        return password;
    }

}
