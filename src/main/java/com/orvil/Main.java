package com.orvil;

import java.io.*;
import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.orvil.JSONResponseObjects.*;

import java.time.*;
import java.time.temporal.ChronoUnit;

public class Main {

    // Custom Image Icons
    public static ImageIcon alertIcon = new ImageIcon("src/main/Assets/alert.png");
    public static ImageIcon menuIcon = new ImageIcon("src/main/Assets/menu.png");
    public static ImageIcon passwordIcon = new ImageIcon("src/main/Assets/password.png");
    public static ImageIcon questionIcon = new ImageIcon("src/main/Assets/question.png");
    public static ImageIcon questionAltIcon = new ImageIcon("src/main/Assets/questionAlt.png");
    public static ImageIcon statsIcon = new ImageIcon("src/main/Assets/stats.png");
    public static ImageIcon successIcon = new ImageIcon("src/main/Assets/success.png");
    public static ImageIcon userIcon = new ImageIcon("src/main/Assets/user.png");

    public static void main(String[] args) {

        // Start login prompt
        User currentUser = login();
        boolean loggedIn = currentUser != null;

        if (loggedIn) { // If logged in display the correct view based on role
            String role = currentUser.getRole();
            if (role.equalsIgnoreCase("Instructor")) {
                instructorView(currentUser);
            } else {
                Exam(currentUser);
            }
        } else { // The user could not login, exit the program
            JOptionPane.showMessageDialog(null, "Too many failed attempts", "Error", JOptionPane.ERROR_MESSAGE,
                    alertIcon);
            System.exit(0);
        }

        // Program was completed, terminate
        System.exit(0);
    }

    // Method for prompting user login. Returns Null or the currentUser.
    public static User login() {

        ArrayList<User> users = obtainUsers();
        User currentUser;
        int loginAttempts = 0;
        boolean loggedIn;
        do {

            Object userNameObj = JOptionPane.showInputDialog(null, "Username", "Username",
                    JOptionPane.QUESTION_MESSAGE, userIcon, null, "");
            // Close the program if user selects cancel
            if (userNameObj == null) {
                System.exit(0);
            }

            String inputUserName = userNameObj.toString();

            Object passwordObj = JOptionPane.showInputDialog(null, "Password", "Password",
                    JOptionPane.QUESTION_MESSAGE, passwordIcon, null, "");

            // Close the program if user selects cancel
            if (passwordObj == null) {
                System.exit(0);
            }

            String inputPassword = passwordObj.toString();

            currentUser = validateCredentials(inputUserName, inputPassword, users);
            loggedIn = currentUser != null;
            loginAttempts++;

            if (loggedIn) {
                JOptionPane.showMessageDialog(null, "Login Sucessful", "Sucess", JOptionPane.INFORMATION_MESSAGE,
                        successIcon);
            } else {
                JOptionPane.showMessageDialog(null, "Login Failed", "Error", JOptionPane.ERROR_MESSAGE, alertIcon);
            }

        } while (loginAttempts < 3 && !loggedIn);

        return currentUser;
    }

    // Method that obtains the Users from UserInfo.txt and stores them in an
    // ArrayList of type User
    public static ArrayList<User> obtainUsers() {

        ArrayList<User> users = new ArrayList<>(40);
        File userInfoFile = new File("src/main/resources/UsersInfo.txt");
        try (Scanner inputFile = new Scanner(userInfoFile)) {

            while (inputFile.hasNext()) {
                String fileRow = inputFile.nextLine();
                String[] userInfo = fileRow.split("\t");
                User currentUser = new User(userInfo);
                users.add(currentUser);
            }

            inputFile.close();

            users.remove(0); // Remove the first user object that contains the headers from the UserInfo.txt
                             // file
        } catch (FileNotFoundException e) {
            System.out.println("The file UsersInfo.txt could not be found. " + e.getMessage());
        }

        return users;
    }

    // Helper method that stores all the Questions from the TestBank.txt file into
    // an ArrayList<String>
    public static ArrayList<String> obtainAllQuestions() {

        ArrayList<String> questions = new ArrayList<>();

        File testBankFile = new File("src/main/resources/TestBank.txt");
        try (Scanner inputFile = new Scanner(testBankFile)) {
            while (inputFile.hasNext()) {
                String question = inputFile.nextLine();
                questions.add(question);
            }

            inputFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("TestBank.txt file could not be found.\n" + e.getMessage());
        }

        return questions;
    }

    // Helper method that stores all the Answers from the Answers.txt file into an
    // ArrayList<String>
    public static ArrayList<String> obtainAllAnswers() {

        ArrayList<String> answers = new ArrayList<>();
        File testBankFile = new File("src/main/resources/Answers.txt");

        try (Scanner inputFile = new Scanner(testBankFile)) {
            while (inputFile.hasNext()) {
                String answer = inputFile.nextLine();
                answers.add(answer);
            }
            inputFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("Answers.txt file could not be found.\n" + e.getMessage());
        }

        return answers;
    }

    // Helper method that generates an array of size count with random indexes from
    // startBound to endBound
    public static int[] getRandomIndexes(int startBound, int endBound, int count) {

        Random random = new Random();
        int[] randomIndexes = new int[count];

        for (int i = 0; i < randomIndexes.length; i++) {
            int randomIndex = random.nextInt(startBound, endBound);
            randomIndexes[i] = randomIndex;
        }

        return randomIndexes;
    }

    // Returns a string array of size count with the questions from the
    // randomIndexes
    public static String[] getRandomQuestions(ArrayList<String> questions, int[] randomIndexes, int count) {

        String[] chosenQuestions = new String[count];
        int i = 0;
        for (int questionIndex : randomIndexes) {
            chosenQuestions[i++] = questions.get(questionIndex);
        }

        return chosenQuestions;
    }

    // Returns a string array of size count with the answers from the randomIndexes
    public static String[] getRandomAnswers(ArrayList<String> answers, int[] randomIndexes, int count) {

        String[] chosenAnswers = new String[count];
        int i = 0;
        for (int answerIndex : randomIndexes) {
            chosenAnswers[i++] = answers.get(answerIndex);
        }

        return chosenAnswers;
    }

    // Helper method displaying the test, one question at a time
    public static ArrayList<String> displayTest(String[] questions, String[] answers, int QUESTIONCOUNT) {

        ArrayList<String> questionsInfo = new ArrayList<>(10);

        for (int i = 0; i < QUESTIONCOUNT; i++) {
            String question = questions[i];
            String answer = answers[i];
            int questionNumber = i + 1;

            String[] options = { "TRUE", "FALSE" };
            int choice = JOptionPane.showOptionDialog(null, question, "Question " + questionNumber,
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, questionIcon, options, options[0]);
            String userInput = options[choice];

            System.out.println(userInput);

            String questionInfo = String.format("%d)\t%s\t%s\t%s", questionNumber, question, userInput, answer);
            questionsInfo.add(questionInfo);

            System.out.println("User Input: " + userInput + "\t Answer: " + answer);

        }

        return questionsInfo;
    }

    // Method creates report file and displays the end report with user info
    public static void endReport(ArrayList<String> questionsInfo, User user) {

        try {

            final String FILENAME = "src/main/resources/" + user.getUserName() + "_CSCI_2493_Quiz";
            int testScore = 0;

            // Print Exam report to the file UserName_CSCI_2493_Quiz
            PrintWriter pw = new PrintWriter(FILENAME);
            pw.println("Name:\t" + user.getFullName());
            pw.println("Date time:\t" + getTime());
            pw.println("Question\tUser Answer\tCorrect Answer");

            for (String questionInfo : questionsInfo) {
                String[] data = questionInfo.split("\t");
                pw.println(questionInfo);

                String userAnswer = data[2];
                String correctAnswer = data[3];

                if (userAnswer.equals(correctAnswer)) {
                    testScore++;
                }
            }

            pw.println("Score: " + testScore);
            pw.close();

            // Append user stats to Stats.txt
            FileWriter fw = new FileWriter("src/main/resources/Stats.txt", true);
            PrintWriter statsWriter = new PrintWriter(fw);

            // First Name Last Name Score
            String statsLine = String.format("%s\t%s\t%d", user.getFirstName(), user.getLastName(), testScore);
            statsWriter.println(statsLine);
            statsWriter.close();

            // Display Exam Report
            String score = String.format("First Name: %s\nLast Name: %s\nScore: %d/10",
                    user.getFirstName(), user.getLastName(), testScore);
            JOptionPane.showMessageDialog(null, score, "Report", JOptionPane.INFORMATION_MESSAGE, statsIcon);

            System.exit(0);

        } catch (Exception e) {
            System.out.println("There was an error accessing files. " + e.getMessage());
        }

    }

    // Method for obtaining the Data and Time, used to log the time stamp of a
    // submitted exam
    public static String getTime() {
        LocalTime currentTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDate currentDate = LocalDate.now();

        String currentDateTime = currentDate + " " + currentTime;
        return currentDateTime;
    }

    // Method that checks if a user's credentials match the UsersInfo.txt file for
    // login
    public static User validateCredentials(String inputUserName, String inputPassword, ArrayList<User> users) {

        for (User user : users) {
            boolean validUserName = user.getUserName().equals(inputUserName);
            boolean matchingPassword = user.getPassword().equals(inputPassword);
            if (validUserName && matchingPassword) {
                return user; // Login was sucessful
            }
        }

        return null; // Login failed
    }

    // Method that displays the Instructor view and Instructor options
    public static void instructorView(User instructor) {
        String[] selectionValues = { "Add new student", "Add a new question", "Print the class statistics" };
        Object instructorSelection;

        do { // Continue displaying the instructor view until the instructor exits
            instructorSelection = JOptionPane
                    .showInputDialog(null, "What would you like to do?", "Instructor Menu", 0, menuIcon,
                            selectionValues, JOptionPane.INFORMATION_MESSAGE);

            if (instructorSelection == null) {
                System.exit(0);
            }

            String selection = instructorSelection.toString();

            if (selection.equals(selectionValues[0])) {
                Instructor.addNewStudent();

            } else if (selection.equals(selectionValues[1])) {
                Instructor.addNewQuestions();
            } else {
                Instructor.getClassStats();
            }
        } while (instructorSelection != null);

    }

    // Method is called after a student logs in launching the exam
    public static void Exam(User user) {

        final int QUESTIONCOUNT = 10;
        ArrayList<String> questions = obtainAllQuestions();
        ArrayList<String> answers = obtainAllAnswers();

        int[] randomIndexes = getRandomIndexes(0, questions.size(), QUESTIONCOUNT);
        String[] randomQuestions = getRandomQuestions(questions, randomIndexes, QUESTIONCOUNT);
        String[] randomAnswers = getRandomAnswers(answers, randomIndexes, QUESTIONCOUNT);

        ArrayList<String> testInformation = displayTest(randomQuestions, randomAnswers, QUESTIONCOUNT);
        endReport(testInformation, user);
    }

}
