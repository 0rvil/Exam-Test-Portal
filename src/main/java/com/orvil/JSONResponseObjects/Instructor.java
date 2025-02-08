package com.orvil.JSONResponseObjects;

import javax.swing.JOptionPane;
import java.io.*;

import com.orvil.Response;
import com.orvil.User;
import javax.swing.ImageIcon;
import java.util.*;
import java.net.URI;
import java.net.http.*;
import com.google.gson.Gson;

// A class with static methods that support the different capabilities offered to instructors
public class Instructor {

    public static ImageIcon alertIcon = new ImageIcon("src/main/Assets/alert.png");
    public static ImageIcon successIcon = new ImageIcon("src/main/Assets/success.png");
    public static ImageIcon statsIcon = new ImageIcon("src/main/Assets/stats.png");
    public static ImageIcon questionIcon = new ImageIcon("src/main/Assets/question.png");
    public static ImageIcon questionAltIcon = new ImageIcon("src/main/Assets/questionAlt.png");

    public static void addNewStudent() {
        // add a new student
        String newStudent = createNewStudent();

        // Append Student to UserInfo.txt
        addStudentToClass(newStudent);
    }

    // Helper method for creating a new student from manual user input
    public static String createNewStudent() {
        String fName = JOptionPane.showInputDialog(null, "Enter the student's first name: ", "New Student First Name",
                JOptionPane.QUESTION_MESSAGE, questionAltIcon, null, "").toString();
        String lName = JOptionPane.showInputDialog(null, "Enter the student's last name: ", "New Student Last Name",
                JOptionPane.QUESTION_MESSAGE, questionAltIcon, null, "").toString();
        String userName = JOptionPane.showInputDialog(null, "Enter the student's username: ", "New Student UserName",
                JOptionPane.QUESTION_MESSAGE, questionAltIcon, null, "").toString();
        String password = User.generatePassword();
        final String role = "Student";

        String newStudent = String.format("\n%s\t%s\t%s\t%s\t%s", lName, fName, userName, password, role);

        return newStudent;
    }

    // Helper method for appending a student into the class
    public static void addStudentToClass(String student) {
        try {

            FileWriter fw = new FileWriter("src/main/resources/UsersInfo.txt", true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(student);
            fw.close();
            pw.close();

            String[] data = student.split("\t");

            String msg = String.format(
                    "New Student Added\nFirst Name: %s\nLast Name: %s\nUserName: %s\nPassword: %s\nRole: %s",
                    data[1], data[0], data[2], data[3], data[4]);

            JOptionPane.showMessageDialog(null, msg, "User Successfully Added", JOptionPane.INFORMATION_MESSAGE,
                    successIcon);

        } catch (IOException e) {
            System.out.println("There was an error accessing the File UsersInfo.txt " + e.getMessage());
        }

    }

    public static void getClassStats() {
        // Obtain Stats.txt data
        ArrayList<String> studentsData = readStatsFile();

        // Print the student max, min and class average
        obtainClassStats(studentsData);
    }

    // Helper method for reading the entries in the Stats.txt file
    public static ArrayList<String> readStatsFile() {
        ArrayList<String> studentsData = new ArrayList<>();

        try {
            File classStatsFile = new File("src/main/resources/Stats.txt");
            Scanner statsReader = new Scanner(classStatsFile);

            while (statsReader.hasNext()) {
                String studentData = statsReader.nextLine();
                studentsData.add(studentData);
            }

            statsReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("The file Stats.txt could not be accessed. " + e.getMessage());
        }

        return studentsData;
    }

    // Method for obtaining the class statistics (Average, Min, and Max) from
    // Stats.txt
    public static void obtainClassStats(ArrayList<String> data) {

        int count = 0;
        double sum = 0.0;
        int defaultVal = Integer.parseInt(data.get(0).split("\t")[2]);
        int min = defaultVal;
        int max = defaultVal;

        for (String entry : data) {
            String[] studentData = entry.split("\t");
            int score = Integer.parseInt(studentData[2]);

            if (score < min) {
                min = score;
            } else if (score > max) {
                max = score;
            }

            sum += score;
            count++;
        }
        double avg = sum / count;

        String classStats = String.format("Class Average: %.2f\nClass Max: %d\nClass Min: %d\n", avg, max, min);
        JOptionPane.showMessageDialog(null, classStats, "Class Statistics", JOptionPane.INFORMATION_MESSAGE, statsIcon);

    }

    public static void addNewQuestions() {
        // Prompt user to select Manual entry of AI generated entry
        String[] options = { "Manual", "AI Generated" };
        int type = JOptionPane.showOptionDialog(null, "Select your input method:", "Question Type Selection",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, questionIcon, options, options[0]);
        System.out.println(options[type]);

        // Variable for storing the number of question to add
        int numOfQ = -1;

        if (type == 0) {
            // User selected manual input, prompt for positive number of questions to add
            // and loop manual entry
            while (numOfQ < 1) {
                String amount = JOptionPane.showInputDialog(null, "How many questions would you like to add?",
                        "Number of Questions", JOptionPane.QUESTION_MESSAGE, questionIcon, null, "").toString();
                numOfQ = Integer.parseInt(amount);
            }

            addManualQuestion(numOfQ);
        } else {
            // User selected AI Generated Question ask for the number (1-5) to add and
            // append to Question and Answer Bank files
            Integer[] nums = { 1, 2, 3, 4, 5 };
            String userSelection = JOptionPane.showInputDialog(null,
                    "How many questions, between 1 and 5, would you like to add?", "Number of Questions", 0,
                    questionAltIcon,
                    nums, JOptionPane.INFORMATION_MESSAGE).toString();

            numOfQ = Integer.parseInt(userSelection);
            addAIQuestions(numOfQ);
        }
    }

    // Method for adding a manual method
    public static void addManualQuestion(int numOfQ) {

        // Loop from 0 to #ofQuestionsToAsk (No limit)
        // Ask User for Question
        // Ask User for Input
        // Apend Question to TestBank.txt
        // Apend Answer to Answers.txt

        try {

            String displayMsg = "The following questions were added:\n";

            FileWriter questionsFile = new FileWriter("src/main/resources/TestBank.txt", true);
            FileWriter answersFile = new FileWriter("src/main/resources/Answers.txt", true);

            PrintWriter questionsWriter = new PrintWriter(questionsFile);
            PrintWriter answersWriter = new PrintWriter(answersFile);

            for (int i = 0; i < numOfQ; i++) {
                String question = JOptionPane.showInputDialog(null, "Enter the Question", "Question " + (i + 1),
                        JOptionPane.QUESTION_MESSAGE, questionIcon, null, "").toString();
                String[] options = { "TRUE", "FALSE" };
                int choice = JOptionPane.showOptionDialog(null, "Select the Answer", "Answer " + (i + 1),
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, questionIcon, options, options[0]);
                String answer = options[choice];

                questionsWriter.println(question);
                answersWriter.println(answer);
                displayMsg += "Question: " + question + "\t" + "Answer: " + answer + "\n";
            }

            questionsFile.close();
            questionsWriter.close();

            answersFile.close();
            answersWriter.close();

            JOptionPane.showMessageDialog(null, displayMsg, "Success", JOptionPane.INFORMATION_MESSAGE, successIcon);
        } catch (IOException e) {
            System.out
                    .println("There was an error accessing the Answers.txt and TestBank.txt files. " + e.getMessage());
        }

    }

    // Method for adding an AI generated quesiton
    public static void addAIQuestions(int numOfQuestions) {
        String outputFromLLM = AIGeneratedQuestions(numOfQuestions);
        getJSONFromLLMResponse(outputFromLLM);
    }

    // Call to generate the AI question from Gemini LLM
    public static String AIGeneratedQuestions(int numberOfQuestions) {

        HttpClient client = HttpClient.newHttpClient();

        final String API_KEY = "<API KEY>";

        String questionsWordType = numberOfQuestions > 1 ? "questions" : "question";

        String prompt = "{\"contents\": [{\"parts\": [{\"text\": \"You are a Computer Science professor creating an exam for an undergraduate class taught in Java.\\n\\nDesign "
                + numberOfQuestions + " TRUE/FALSE " + questionsWordType
                + " that are concise and unambiguous about general computer science topics or the Java programming language. Do not number the questions and follow the given format for output:\\n{\\\"question\\\" : \\\"Java is a object-oriented programming language.\\\", \\\"answer\\\" : \\\"TRUE\\\"} \\n{\\\"question\\\" : \\\"All elements of an array must be of the same data type.\\\", \\\"answer\\\" : \\\"TRUE\\\"} \\n{\\\"question\\\" : \\\"RAM stands for Random Access Memories.\\\", \\\"answer\\\" : \\\"FALSE\\\"} \"}]}],"
                +
                "\"generationConfig\": {\"temperature\": 0.7,\"topK\": 1,\"topP\": 1,\"maxOutputTokens\": 2048,\"stopSequences\": []},\"safetySettings\": "
                +
                "[{\"category\": \"HARM_CATEGORY_HARASSMENT\",\"threshold\": \"BLOCK_MEDIUM_AND_ABOVE\"}," +
                "{\"category\": \"HARM_CATEGORY_HATE_SPEECH\",\"threshold\": \"BLOCK_MEDIUM_AND_ABOVE\"}," +
                "{\"category\": \"HARM_CATEGORY_SEXUALLY_EXPLICIT\",\"threshold\": \"BLOCK_MEDIUM_AND_ABOVE\"}," +
                "{\"category\": \"HARM_CATEGORY_DANGEROUS_CONTENT\",\"threshold\": \"BLOCK_MEDIUM_AND_ABOVE\"}]}";

        URI geminiUrl = URI
                .create("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.0-pro:generateContent?key="
                        + API_KEY);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(geminiUrl)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(prompt))
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.statusCode());

            if (response.statusCode() == 200) {
                System.out.println("Response Body: " + response.body());
                return response.body().toString();
            }

            System.out.println("The following error occurred");
            System.out.println(response.statusCode());

            return "The following error occurred: " + response.statusCode();

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        return "Unexpected error occurred. Please try again.";

    }

    // Method for converting Gemini JSON String ouptut into a Java Object
    public static void getJSONFromLLMResponse(String output) {

        if (!output.contains("The following error")) { // Make sure there wasn't an error in the Call response
            Gson gson = new Gson();

            Response responseObj = gson.fromJson(output, Response.class);

            Candidate responseCandidate = responseObj.getCandidate(0);
            Content responseContent = responseCandidate.getContent();
            Part responsePart = responseContent.getPart(0);
            String responseText = responsePart.getText();

            String[] responseArray = responseText.split("\n");
            ArrayList<QuestionAnswerPair> qaPairs = new ArrayList<>();
            String displayMsg = "The following questions and answers were added:\n";
            // Loop through text containing the questions and answers to convert string JSON
            // into Java object
            for (String jsonString : responseArray) {
                QuestionAnswerPair qaPair = gson.fromJson(jsonString, QuestionAnswerPair.class);
                qaPairs.add(qaPair);
            }

            // Append Question and Answer Pairs into their corresponding files
            try {

                FileWriter questionsFile = new FileWriter("src/main/resources/TestBank.txt", true);
                FileWriter answersFile = new FileWriter("src/main/resources/Answers.txt", true);

                PrintWriter questionsWriter = new PrintWriter(questionsFile);
                PrintWriter answersWriter = new PrintWriter(answersFile);

                for (QuestionAnswerPair qaPair : qaPairs) {
                    displayMsg += "Question: " + qaPair.getQuestion() + "\t" + "Answer: " + qaPair.getAnswer() + "\n";
                    questionsWriter.println(qaPair.getQuestion());
                    answersWriter.println(qaPair.getAnswer());
                }

                questionsFile.close();
                questionsWriter.close();

                answersFile.close();
                answersWriter.close();

                // Questions were added, show the questions added to the Instructor
                JOptionPane.showMessageDialog(null, displayMsg, "Success", JOptionPane.INFORMATION_MESSAGE,
                        successIcon);

            } catch (IOException e) {
                System.out.println(e.getMessage()); // There was an error acessing the TestBank.txt or Answers.txt files
            }

        } else {
            // Questions were not able to be added, display error to the Instructor
            JOptionPane.showMessageDialog(null, output, "Error", JOptionPane.ERROR_MESSAGE, alertIcon);
        }

    }

}
