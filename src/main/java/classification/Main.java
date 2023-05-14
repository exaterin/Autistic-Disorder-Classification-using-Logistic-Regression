package classification;

import java.io.InputStream;

import static java.lang.System.exit;

public class Main {

    /**
     * Train and return a logistic regression model
     * @return the trained logistic regression model
     */
    public static LogisticRegression trainClassifier(){

        Data trainData = new Data();

        // Read train data
        InputStream trainDataStream = Main.class.getResourceAsStream("/Autism-Adult-Data.arff");
        if (trainDataStream == null) {
            throw new IllegalArgumentException("File not found!");
        }
        trainData.readData(trainDataStream);

        // Extract labels
        trainData.extractLabels();

        /*
        Remove features:
        12: "Ethnicity", 15: "Country of residence", 19: "Relation" because they are incomplete
        16: "Who completed the app test", 17: "Screening score", 18: "Age description",  because it is not relevant
        20: "Class" are labels
        */

        trainData.removeColumns(new int[] {12, 15, 16, 17, 18, 19, 20});

        /*
        Transform the data into categorical features
        Features to be transformed:
        11: Gender (m: Male, f: Female)
        12: Jaundice (Yes, No)
        13: PDD (Yes, No)
        */
        trainData.transformColumns(new int[] {11, 12, 13});

        // Convert the data into doubles
        trainData.convertToDouble();

        double[][] X = trainData.transformedData;
        int[] y = trainData.classLabels;

        System.out.println("Model is training...");

        // Train the model
        LogisticRegression lr = new LogisticRegression(X[0].length, 0.1, 1000);
        lr.fit(X, y);

        System.out.println("Training finished.");

        // Save the model
        lr.saveModel();

        System.out.println("Model is saved into weights.txt.");

        return lr;
    }

    /**
     * Test the model and print the accuracy
     * @param lr    the logistic regression model to test
     */
    public static void testClassifier(LogisticRegression lr){
        Data testData = new Data();

        // Read test data
        InputStream testDataStream = Main.class.getResourceAsStream("/Test-Data.arff");
        if (testDataStream == null) {
            throw new IllegalArgumentException("File not found!");
        }
        testData.readData(testDataStream);

        testData.extractLabels();
        testData.removeColumns(new int[] {12, 15, 16, 17, 18, 19, 20});
        testData.transformColumns(new int[] {11, 12, 13});
        testData.convertToDouble();

        // Calculate false positive, false negative and true positive
        int truePositive = 0;
        int falsePositive = 0;
        int falseNegative = 0;
        for(int i = 0; i < testData.transformedData.length; i++){
            int prediction = lr.predict(testData.transformedData[i]);

            if(prediction == testData.classLabels[i])
                truePositive++;

            if(prediction == 1 && testData.classLabels[i] == 0)
                falsePositive++;
            else if(prediction == 0 && testData.classLabels[i] == 1)
                falseNegative++;
        }

        // Print accuracy, precision, recall and F1 score
        System.out.println("Accuracy: " + String.format("%.4f", (double) truePositive / testData.transformedData.length * 100) + "%");
        System.out.println("Precision: " + String.format("%.4f", (double) truePositive / (truePositive + falsePositive) * 100) + "%");
        System.out.println("Recall: " + String.format("%.4f", (double) truePositive / (truePositive + falseNegative) * 100) + "%");
        System.out.println("F1 score: " + String.format("%.4f", (double) 2 * truePositive / (2 * truePositive + falsePositive + falseNegative) * 100) + "%");

    }


    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("ERROR: Provide an argument: train, test or predict.");
            exit(0);
        }

        LogisticRegression lr;

        switch (args[0]) {
            case "train" -> {
                System.out.println("Mode: train the model.");
                lr = trainClassifier();
                lr.saveModel();
            }
            case "test" -> {
                System.out.println("Mode: testing the model.");
                lr = new LogisticRegression(14, 0.1, 1000);
                lr.loadModel();
                testClassifier(lr);
            }
            case "predict" -> {
                System.out.println("Mode: predict, taking a survey.");
                lr = new LogisticRegression(14, 0.1, 1000);
                lr.loadModel();
                Questions questions = new Questions("/Questions.txt");
                questions.askQuestions();
                questions.processAnswers();
                int prediction = lr.predict(questions.answersDouble);
                if (prediction == 1)
                    System.out.println("Prediction: Yes, it is very probable that you have autism. It would be better to go to a specialist for further evaluation and diagnosis.");
                else
                    System.out.println("Prediction: No, it is not very probable that you have autism. However, if you are still concerned about your symptoms, you may want to seek an evaluation from a specialist to rule out the possibility.");
            }
            default -> System.out.println("ERROR: Provide a correct argument: train, test or predict.");
        }

    }

}
