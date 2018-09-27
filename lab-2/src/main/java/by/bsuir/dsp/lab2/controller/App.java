package by.bsuir.dsp.lab2.controller;

import by.bsuir.dsp.lab2.service.ImageService;
import by.bsuir.dsp.lab2.service.Property;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class App extends Application {
    @FXML
    private ImageView sourceImageView;
    @FXML
    private ImageView binaryImageView;
    @FXML
    private ImageView markedImageView;
    @FXML
    private ImageView clusterizedImageView;
    @FXML
    private TextField sourceTextField;
    @FXML
    private TextField numberTextField;
    @FXML
    private Button okButton;
    @FXML
    private TextArea clustersTextArea;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);

        String fxmlFile = "/fxml/app.fxml";
        Parent root = loader.load(getClass().getResourceAsStream(fxmlFile));

        primaryStage.setMaximized(true);
        primaryStage.setTitle("lab-2");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        okButton.setOnAction(event -> {
            BufferedImage source = null;

            try {
                source = ImageIO.read(getClass().getResourceAsStream(sourceTextField.getText()));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            sourceImageView.setImage(SwingFXUtils.toFXImage(source, null));

            BufferedImage binary = ImageService.toBinary(source);
            binaryImageView.setImage(SwingFXUtils.toFXImage(binary, null));

            int[][] map = ImageService.mark(binary);
            BufferedImage marked = ImageService.mapToImage(map);
            markedImageView.setImage(SwingFXUtils.toFXImage(marked, null));

            int k = Integer.parseInt(numberTextField.getText());
            List<List<Map.Entry<Integer, Property>>> clusters = ImageService.kMeans(ImageService.getProperties(map), k);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < clusters.size(); i++) {
                sb.append("CLUSTER #").append(i).append('\n');
                for (Map.Entry<Integer, Property> obj : clusters.get(i)) {
                    sb.append(obj.getValue()).append('\n');
                }
            }
            clustersTextArea.setText(sb.toString());

            clusterizedImageView.setImage(SwingFXUtils.toFXImage(ImageService.mapToClusteredImage(map, clusters), null));
        });

        okButton.getOnAction().handle(new ActionEvent());
    }
}
