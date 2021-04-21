package dk.easv.gui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dk.easv.bll.CallablePixelCounter;
import dk.easv.be.PixelPhoto;
import dk.easv.bll.SlideshowTask;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ImageViewerWindowController implements Initializable
{
    private final List<Image> images = new ArrayList<>();
    @FXML private Label redLbl;
    @FXML private Label blueLbl;
    @FXML private Label greenLbl;
    @FXML private Label mixedLbl;

    @FXML private Label lblImageName;
    @FXML private Button btnLoad;
    @FXML private Button btnStart;
    @FXML private ComboBox boxTime;
    @FXML private Button btnStop;
    @FXML private Button btnPrevious;
    @FXML private Button btnNext;
    @FXML private ImageView imageView;
    private int currentImageIndex = 0;
    private boolean isTimeSelected = false;

    SlideshowTask task;

    @FXML Parent root;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillTimeOptions();

        btnNext.setDisable(true);
        btnPrevious.setDisable(true);
        btnStop.setDisable(true);
        btnStart.setDisable(true);
        boxTime.setDisable(true);

    }

    @FXML
    private void handleBtnLoadAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Images",
                "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());

        if (!files.isEmpty()) {
            files.forEach((File f) -> {
                images.add(new Image(f.toURI().toString()));
            });
            displayImage();
        }

        btnLoad.setDisable(false);
        btnNext.setDisable(false);
        btnPrevious.setDisable(false);
        btnStop.setDisable(true);
        btnStart.setDisable(true);
        boxTime.setDisable(false);
    }

    //starts the slideshow
    @FXML
    private void start() {
        btnLoad.setDisable(true);
        btnNext.setDisable(true);
        btnPrevious.setDisable(true);
        btnStop.setDisable(false);
        btnStart.setDisable(true);
        boxTime.setDisable(true);

        int time = Integer.parseInt(String.valueOf(boxTime.getValue()));

        task = new SlideshowTask();
        task.setNumberOfImages(images.size());
        task.setTime(time);
        task.setStartIndex(currentImageIndex);

        task.valueProperty().addListener((obs, o, n) -> {
            currentImageIndex = (int) n;
            displayImage();
            lblImageName.setText(images.get(currentImageIndex).getUrl());
        });

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(task);
        executorService.shutdown();

    }

    //stops the slideshow
    @FXML
    private void stop() {
        btnLoad.setDisable(false);
        btnNext.setDisable(false);
        btnPrevious.setDisable(false);
        btnStop.setDisable(true);
        btnStart.setDisable(false);
        boxTime.setDisable(false);

        if(task != null) {
            task.cancel();
        }
    }

    @FXML
    private void handleBtnPreviousAction() {
        if (!images.isEmpty()) {
            currentImageIndex = (currentImageIndex - 1 + images.size()) % images.size();
            displayImage();
        }
    }

    @FXML
    private void handleBtnNextAction() {
        if (!images.isEmpty()) {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            displayImage();
        }
    }

    private void displayImage() {
        if (!images.isEmpty()) {
            imageView.setImage(images.get(currentImageIndex));
            lblImageName.setText(images.get(currentImageIndex).getUrl());
            pixelCount();
        }
    }

    private void pixelCount() {
        CallablePixelCounter pixelCounter = new CallablePixelCounter(images.get(currentImageIndex));
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<PixelPhoto> future = executorService.submit(pixelCounter);

        try {
            PixelPhoto photo = future.get();
            redLbl.setText("Red: " + photo.getRed());
            blueLbl.setText("Blue: " + photo.getBlue());
            greenLbl.setText("Green " + photo.getGreen());
            mixedLbl.setText("Mixed: " + photo.getMixed());
        } catch (Exception e) {
            e.printStackTrace();
        }

        executorService.shutdown();
    }

    private void fillTimeOptions() {
        List<String> options = new ArrayList<>();
        for(int i = 1; i <6; i++) {
            options.add(String.valueOf(i));
        }
        boxTime.setItems(FXCollections.observableArrayList(options));
    }

    public void timeSelected(ActionEvent actionEvent) {
        if(!isTimeSelected && !boxTime.getValue().equals(null)) {
            isTimeSelected = true;
            btnStart.setDisable(false);
        }
    }
}