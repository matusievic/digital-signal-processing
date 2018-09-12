package by.bsuir.dsp.lab1.controller;

import by.bsuir.dsp.lab1.service.ImageService;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class App extends Application {
    @FXML
    private ImageView sourceImage;
    @FXML
    private ImageView grayImage;
    @FXML
    private BarChart<Integer, Integer> redChart;
    @FXML
    private BarChart<Integer, Integer> greenChart;
    @FXML
    private BarChart<Integer, Integer> blueChart;
    @FXML
    private BarChart<Integer, Integer> grayChart;
    @FXML
    private ImageView sourceDissectedImage;
    @FXML
    private ImageView grayDissectedImage;
    @FXML
    private BarChart<Integer, Integer> redDissectedChart;
    @FXML
    private BarChart<Integer, Integer> greenDissectedChart;
    @FXML
    private BarChart<Integer, Integer> blueDissectedChart;
    @FXML
    private BarChart<Integer, Integer> grayDissectedChart;
    @FXML
    private ImageView sourcePrewittImage;
    @FXML
    private ImageView grayPrewittImage;
    @FXML
    private BarChart<Integer, Integer> redPrewittChart;
    @FXML
    private BarChart<Integer, Integer> greenPrewittChart;
    @FXML
    private BarChart<Integer, Integer> bluePrewittChart;
    @FXML
    private BarChart<Integer, Integer> grayPrewittChart;
    @FXML
    private TextField fField;
    @FXML
    private TextField gField;
    @FXML
    private TextField modeField;
    @FXML
    private Button okButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);

        String fxmlFile = "/fxml/app.fxml";
        Parent root = loader.load(getClass().getResourceAsStream(fxmlFile));

        primaryStage.setTitle("lab-1");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


        // set up source image
        BufferedImage source = ImageIO.read(getClass().getResourceAsStream("/images/source.jpg"));
        sourceImage.setImage(SwingFXUtils.toFXImage(source, null));

        // set up gray image
        BufferedImage gray = ImageService.toGrayscaled(source);
        grayImage.setImage(SwingFXUtils.toFXImage(gray, null));

        // set up source histograms
        setUpSourceHistograms(source, gray);

        // set up dissected output
        setUpDissectedOutput(source, gray, Integer.parseInt(fField.getText()), Integer.parseInt(gField.getText()), Integer.parseInt(modeField.getText()));

        //set up Prewitt output
        setUpPrewittOutput(source, gray);


        // set up handler
        okButton.setOnAction(event -> setUpDissectedOutput(source, gray, Integer.parseInt(fField.getText()), Integer.parseInt(gField.getText()), Integer.parseInt(modeField.getText())));
    }

    private void setUpSourceHistograms(BufferedImage source, BufferedImage gray) {
        //set up reg histogram
        int[] redHistogram = ImageService.getHistogram(source, 'r');

        XYChart.Series redSeries = new XYChart.Series();
        for (int i = 0; i < redHistogram.length; i++) {
            redSeries.getData().add(new XYChart.Data(String.valueOf(i), redHistogram[i]));
        }
        redChart.getData().add(redSeries);

        //set up green histogram
        int[] greenHistogram = ImageService.getHistogram(source, 'g');

        XYChart.Series greenSeries = new XYChart.Series();
        for (int i = 0; i < greenHistogram.length; i++) {
            greenSeries.getData().add(new XYChart.Data(String.valueOf(i), greenHistogram[i]));
        }
        greenChart.getData().add(greenSeries);

        //set up blue histogram
        int[] blueHistogram = ImageService.getHistogram(source, 'b');

        XYChart.Series blueSeries = new XYChart.Series();
        for (int i = 0; i < blueHistogram.length; i++) {
            blueSeries.getData().add(new XYChart.Data(String.valueOf(i), blueHistogram[i]));
        }
        blueChart.getData().add(blueSeries);

        //set up gray histogram
        int[] grayHistogram = ImageService.getHistogram(gray);

        XYChart.Series graySeries = new XYChart.Series();
        for (int i = 0; i < grayHistogram.length; i++) {
            graySeries.getData().add(new XYChart.Data(String.valueOf(i), grayHistogram[i]));
        }
        grayChart.getData().add(graySeries);
    }

    private void setUpDissectedOutput(BufferedImage source, BufferedImage gray, int f, int g, int mode) {
        // set up source image
        BufferedImage sourceDissected = ImageService.dissect(source, f, g, mode);
        sourceDissectedImage.setImage(SwingFXUtils.toFXImage(sourceDissected, null));

        // set up gray image
        BufferedImage grayDissected = ImageService.dissect(gray, f, g, mode);
        grayDissectedImage.setImage(SwingFXUtils.toFXImage(grayDissected, null));

        // set up reg histogram
        int[] redHistogram = ImageService.getHistogram(sourceDissected, 'r');

        XYChart.Series redSeries = new XYChart.Series();
        for (int i = 0; i < redHistogram.length; i++) {
            redSeries.getData().add(new XYChart.Data(String.valueOf(i), redHistogram[i]));
        }
        redDissectedChart.getData().clear();
        redDissectedChart.getData().add(redSeries);

        //set up green histogram
        int[] greenHistogram = ImageService.getHistogram(sourceDissected, 'g');

        XYChart.Series greenSeries = new XYChart.Series();
        for (int i = 0; i < greenHistogram.length; i++) {
            greenSeries.getData().add(new XYChart.Data(String.valueOf(i), greenHistogram[i]));
        }
        greenDissectedChart.getData().clear();
        greenDissectedChart.getData().add(greenSeries);

        //set up blue histogram
        int[] blueHistogram = ImageService.getHistogram(sourceDissected, 'b');

        XYChart.Series blueSeries = new XYChart.Series();
        for (int i = 0; i < blueHistogram.length; i++) {
            blueSeries.getData().add(new XYChart.Data(String.valueOf(i), blueHistogram[i]));
        }
        blueDissectedChart.getData().clear();
        blueDissectedChart.getData().add(blueSeries);

        //set up gray histogram
        int[] grayHistogram = ImageService.getHistogram(grayDissected);

        XYChart.Series graySeries = new XYChart.Series();
        for (int i = 0; i < grayHistogram.length; i++) {
            graySeries.getData().add(new XYChart.Data(String.valueOf(i), grayHistogram[i]));
        }
        grayDissectedChart.getData().clear();
        grayDissectedChart.getData().add(graySeries);
    }

    private void setUpPrewittOutput(BufferedImage source, BufferedImage gray) {
        // set up source image
        BufferedImage sourcePrewitt = ImageService.prewitt(source);
        sourcePrewittImage.setImage(SwingFXUtils.toFXImage(sourcePrewitt, null));

        // set up gray image
        BufferedImage grayPrewitt = ImageService.prewitt(gray);
        grayPrewittImage.setImage(SwingFXUtils.toFXImage(grayPrewitt, null));

        // set up reg histogram
        int[] redHistogram = ImageService.getHistogram(sourcePrewitt, 'r');

        XYChart.Series redSeries = new XYChart.Series();
        for (int i = 0; i < redHistogram.length; i++) {
            redSeries.getData().add(new XYChart.Data(String.valueOf(i), redHistogram[i]));
        }
        redPrewittChart.getData().add(redSeries);

        //set up green histogram
        int[] greenHistogram = ImageService.getHistogram(sourcePrewitt, 'g');

        XYChart.Series greenSeries = new XYChart.Series();
        for (int i = 0; i < greenHistogram.length; i++) {
            greenSeries.getData().add(new XYChart.Data(String.valueOf(i), greenHistogram[i]));
        }
        greenPrewittChart.getData().add(greenSeries);

        //set up blue histogram
        int[] blueHistogram = ImageService.getHistogram(sourcePrewitt, 'b');

        XYChart.Series blueSeries = new XYChart.Series();
        for (int i = 0; i < blueHistogram.length; i++) {
            blueSeries.getData().add(new XYChart.Data(String.valueOf(i), blueHistogram[i]));
        }
        bluePrewittChart.getData().add(blueSeries);

        //set up gray histogram
        int[] grayHistogram = ImageService.getHistogram(grayPrewitt);

        XYChart.Series graySeries = new XYChart.Series();
        for (int i = 0; i < grayHistogram.length; i++) {
            graySeries.getData().add(new XYChart.Data(String.valueOf(i), grayHistogram[i]));
        }
        grayPrewittChart.getData().add(graySeries);
    }
}
