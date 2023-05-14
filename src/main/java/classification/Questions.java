package classification;

import java.io.*;
import java.util.ArrayList;

/**
 * A class that represents a set of questions, performs Autismus test and collects answers.
 */
public class Questions {
    private final ArrayList<String> questions;
    private final String[] answers;
    public double[] answersDouble;

    /**
     * Constructs a Questions object by reading the questions from a resource file.
     * @param resourcePath  the path of the resource file containing the questions
     */
    public Questions(String resourcePath) {
        questions = new ArrayList<>();

        try {
            InputStream is = getClass().getResourceAsStream(resourcePath);
            if (is == null) {
                throw new IllegalArgumentException("File not found!");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                questions.add(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the questions.");
        }
        answers = new String[questions.size()];
    }


    /**
     * Asks the questions and stores the answers in the answers array.
     */
    public void askQuestions() {
        System.out.println("Please answer the following questions: ");
        for (int i = 0; i < questions.size(); i++) {
            System.out.println(questions.get(i));
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                while (true) {  // Loop until the answer is in the correct format
                    String answer = reader.readLine();
                    boolean validFormat = switch (i) {
                        case 10 ->
                            // Question 11 expects a number
                                answer.matches("\\d+");
                        case 11 ->
                            // Question 12 expects "m" or "f"
                                answer.matches("[mf]");
                        default ->
                            // Questions 1-10 and 13-14 expect "yes" or "no"
                                answer.matches("yes|no");
                    };
                    if (validFormat) {
                        answers[i] = answer;
                        break;
                    } else {
                        System.out.println("Incorrect format. Please try again.");
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading your answer.");
            }
        }
    }




    /**
     * Processes the answers and converts them to doubles using information about test evaluation.
     */
    public void processAnswers(){
        answersDouble = new double[answers.length];
        for (int i = 0; i < answers.length; i++) {
            switch (answers[i]) {
                case "yes":
                case "no":
                    if (i == 0 || i == 6 || i == 7 || i == 9 || i == 12 || i == 13) {
                        answersDouble[i] = answers[i].equals("yes") ? 1 : 0;
                    } else if (i == 1 || i == 2 || i == 3 || i == 4 || i == 5 || i == 8) {
                        answersDouble[i] = answers[i].equals("yes") ? 0 : 1;
                    }
                    break;
                case "m":
                    answersDouble[i] = 1;
                    break;
                case "f":
                    answersDouble[i] = 0;
                    break;
                default:
                    answersDouble[i] = Double.parseDouble(answers[i]);
                    break;
            }
        }
    }
}
