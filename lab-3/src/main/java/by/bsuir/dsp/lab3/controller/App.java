package by.bsuir.dsp.lab3.controller;

import by.bsuir.dsp.lab3.image.ImageService;
import by.bsuir.dsp.lab3.kohonen.Network;
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

        primaryStage.setTitle("lab-3");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        int[][] d = ImageService.imageToMap(ImageIO.read(App.class.getResourceAsStream("/source/D.png")));
        int[][] x = ImageService.imageToMap(ImageIO.read(App.class.getResourceAsStream("/source/X.png")));
        int[][] h = ImageService.imageToMap(ImageIO.read(App.class.getResourceAsStream("/source/H.png")));
        this.network = new Network(100).learn(d).learn(x).learn(h);

        File images = new File(getClass().getResource("/noize").getPath());
        fileBox.getItems().addAll(Arrays.stream(images.listFiles()).map(File::getName).sorted().toArray(String[]::new));
        fileBox.setOnAction(event -> {
            try {
                this.input = ImageIO.read(getClass().getResourceAsStream("/noize/" + fileBox.getValue()));
                Image value = SwingFXUtils.toFXImage(ImageService.maximize(input, 10), null);
                inputImage.setImage(value);
            } catch (IOException ignored) {
            }
        });

        recognizeButton.setOnAction(event -> {
            int[][] result = this.network.recognize(ImageService.imageToMap(input));
            resultImage.setImage(SwingFXUtils.toFXImage(ImageService.maximize(ImageService.mapToImage(result), 10), null));
        });
    }
}
