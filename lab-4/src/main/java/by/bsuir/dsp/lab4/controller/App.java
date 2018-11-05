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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class App extends Application {
    // learning params
    @FXML
    private TextField hiddenLayerSizeField;
    @FXML
    private TextField aField;
    @FXML
    private TextField bField;
    @FXML
    private TextField errorField;
    @FXML
    private Button learnButton;
    @FXML
    private Label iterationsLabel;

    // noise generator params
    @FXML
    private ComboBox<String> fileBox;
    @FXML
    private Button recognizeButton;
    @FXML
    private ImageView inputImage;
    @FXML
    private ImageView resultImage;

    // class labels
    @FXML
    private Label firstClassPercentLabel;
    @FXML
    private Label secondClassPercentLabel;
    @FXML
    private Label thirdClassPercentLabel;
    @FXML
    private Label fourthClassPercentLabel;
    @FXML
    private Label fifthClassPercentLabel;
    @FXML
    private ImageView firstImage;
    @FXML
    private ImageView secondImage;
    @FXML
    private ImageView thirdImage;
    @FXML
    private ImageView fourthImage;
    @FXML
    private ImageView fifthImage;

    // non-UI fields
    private int[][][] patterns;
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

        firstImage.setImage(SwingFXUtils.toFXImage(ImageService.maximize(ImageIO.read(App.class.getResourceAsStream("/pattern/1.png")), 20), null));
        secondImage.setImage(SwingFXUtils.toFXImage(ImageService.maximize(ImageIO.read(App.class.getResourceAsStream("/pattern/2.png")), 20), null));
        thirdImage.setImage(SwingFXUtils.toFXImage(ImageService.maximize(ImageIO.read(App.class.getResourceAsStream("/pattern/3.png")), 20), null));
        fourthImage.setImage(SwingFXUtils.toFXImage(ImageService.maximize(ImageIO.read(App.class.getResourceAsStream("/pattern/4.png")), 20), null));
        fifthImage.setImage(SwingFXUtils.toFXImage(ImageService.maximize(ImageIO.read(App.class.getResourceAsStream("/pattern/5.png")), 20), null));

        int[][][] patterns = new int[5][][];
        for (int k = 0; k < 5; k++) {
            patterns[k] = ImageService.imageToMap(ImageIO.read(App.class.getResourceAsStream("/pattern/" + (k + 1) + ".png")));

        }

        learnButton.setOnAction(e -> {
            long iterationsCount = 0;
            this.network = new Network(36, Integer.parseInt(hiddenLayerSizeField.getText()), 5);
            for (int j = 0; j < 5; j++) {
                for (int i = 0; i < 5; i++) {
                    double[] r = {0, 0, 0, 0, 0};
                    r[i] = 1;
                    iterationsCount += this.network.learn(patterns[i], r, Double.parseDouble(aField.getText()), Double.parseDouble(bField
                            .getText()), Double.parseDouble(errorField.getText()));
                }
            }
            iterationsLabel.setText(String.valueOf(iterationsCount));
        });


        File images = new File(getClass().getResource("/noise").getPath());
        fileBox.getItems().addAll(Arrays.stream(images.listFiles()).map(File::getName).sorted().toArray(String[]::new));
        fileBox.setOnAction(event -> {
            try {
                this.input = ImageIO.read(getClass().getResourceAsStream("/noise/" + fileBox.getValue()));
                Image value = SwingFXUtils.toFXImage(ImageService.maximize(input, 20), null);
                inputImage.setImage(value);
            } catch (IOException ignored) {
            }
        });

        recognizeButton.setOnAction(event -> {
            int result = this.network.recognize(ImageService.imageToMap(input));
            resultImage.setImage(SwingFXUtils.toFXImage(ImageService.maximize(ImageService.mapToImage(patterns[result]), 20), null));
            double[] output = network.getOutput();
            firstClassPercentLabel.setText(String.format("%.2f", (output[0] * 100)));
            secondClassPercentLabel.setText(String.format("%.2f", (output[1] * 100)));
            thirdClassPercentLabel.setText(String.format("%.2f", (output[2] * 100)));
            fourthClassPercentLabel.setText(String.format("%.2f", (output[3] * 100)));
            fifthClassPercentLabel.setText(String.format("%.2f", (output[4] * 100)));
        });
    }
}
