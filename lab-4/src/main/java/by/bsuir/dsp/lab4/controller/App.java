package by.bsuir.dsp.lab4.controller;

import by.bsuir.dsp.lab4.image.ImageService;
import by.bsuir.dsp.lab4.perceptron.Network;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class App extends Application {
    @FXML
    private ComboBox<String> fileBox;
    @FXML
    private Button recognizeButton;
    @FXML
    private ImageView inputImage;
    @FXML
    private ImageView resultImage;
    private BufferedImage input;
    private Network network;

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);

        String fxmlFile = "/fxml/app.fxml";
        Parent root = loader.load(getClass().getResourceAsStream(fxmlFile));

        primaryStage.setTitle("lab-4");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        this.network = new Network(36, 18, 5);
        int[][][] patterns = new int[5][][];
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 5; i++) {
                int[][] p = ImageService.imageToMap(ImageIO.read(App.class.getResourceAsStream("/pattern/" + (i + 1) + ".png")));
                patterns[i] = p;
                double[] r = {0, 0, 0, 0, 0};
                r[i] = 1;
                this.network.learn(p, r, 0.5);
            }
        }

        File images = new File(getClass().getResource("/pattern").getPath());
        fileBox.getItems().addAll(Arrays.stream(images.listFiles()).map(File::getName).sorted().toArray(String[]::new));
        fileBox.setOnAction(event -> {
            try {
                this.input = ImageIO.read(getClass().getResourceAsStream("/pattern/" + fileBox.getValue()));
                int[][] map = ImageService.imageToMap(input);
                this.input = ImageService.mapToImage(map);
                Image value = SwingFXUtils.toFXImage(ImageService.maximize(input, 10), null);
                inputImage.setImage(value);
            } catch (IOException ignored) {
            }
        });

        recognizeButton.setOnAction(event -> {
            int result = this.network.recognize(ImageService.imageToMap(input));
            resultImage.setImage(SwingFXUtils.toFXImage(ImageService.maximize(ImageService.mapToImage(patterns[result]), 10), null));
        });
    }
}
