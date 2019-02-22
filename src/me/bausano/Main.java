package me.bausano;

import me.bausano.algorithms.nearestneighbour.NearestNeighbour;
import me.bausano.algorithms.neuralnetwork.NeuralNetwork;
import me.bausano.algorithms.neuralnetwork.Trainer;

import java.nio.file.Paths;

public class Main {

    /**
     * The program starts at this point. We can throw an exception straight to the standard output as it's easier to
     * debug and it's acceptable for this use case.
     *
     * @param args Console arguments
     * @throws Exception Exceptions are thrown into stdout
     */
    public static void main(String[] args) throws Exception {
        // Loads the training data input.
        DataSet inputData = DataSet.from(Paths.get(Settings.TRAINING_FILE_PATH), -1);
        // Loads the testing data input. We don't calibrate the network with this data, we use cross-fold validation
        // instead. I have changed this to the testing data set for the reviewers convenience, so that they don't have to
        // figure out how the DataSet API works.
        DataSet testingData = DataSet.from(Paths.get(Settings.TESTING_FILE_PATH), 0);

        // Instantiates a neural network with random weights and and trains it.
        NeuralNetwork mlp = NeuralNetwork.fromBlueprint(new int[] { 64, 37, 10 });
        new Trainer(mlp, inputData.setForTraining).train();

        // Creates new nearest neighbour instance.
        NearestNeighbour nn = new NearestNeighbour(inputData.setForTraining);

        // Reports on the algorithms.
        Reporter.assess("Neural Network", mlp, testingData.setForValidation);
        Reporter.assess("Nearest neighbour", nn, testingData.setForValidation);
    }
}
