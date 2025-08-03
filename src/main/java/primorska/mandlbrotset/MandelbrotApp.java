

/*package primorska.mandlbrotset;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import primorska.mandlbrotset.renderer.MandelbrotRenderer;
import primorska.mandlbrotset.parallel.ParallelRenderer;
import primorska.mandlbrotset.sequential.SequentialRenderer;
import primorska.mandlbrotset.distributed.DistributedRendererWrapper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MandelbrotApp extends Application {

    private MandelbrotRenderer renderer;

    private double minX = -2.5, maxX = 1.5;
    private double minY = -1.5, maxY = 1.5;
    private double zoomFactor = 1.0;
    private Canvas canvas;
    private GraphicsContext gc;
    private TextField widthField;
    private TextField heightField;
    private int imageWidth = 800;
    private int imageHeight = 600;


    @Override
    public void start(Stage primaryStage) {
        // Determine mode from args
        String mode = "sequential";
        Parameters params = getParameters();
        for (String arg : params.getRaw()) {
            if (arg.startsWith("--mode=")) {
                mode = arg.substring("--mode=".length()).toLowerCase();
            }
        }

        switch (mode) {
            case "distributed":
                renderer = new DistributedRendererWrapper();
                break;
            case "parallel":
                renderer = new ParallelRenderer();
                break;
            case "sequential":
            default:
                renderer = new SequentialRenderer();
                break;
        }

        renderer.logHardwareUsage();

        canvas = new Canvas(imageWidth, imageHeight);
        gc = canvas.getGraphicsContext2D();

        widthField = new TextField(String.valueOf(imageWidth));
        heightField = new TextField(String.valueOf(imageHeight));
        Button resizeButton = new Button("Resize");
        Button saveButton = new Button("Save");

        resizeButton.setOnAction(e -> handleResize());
        saveButton.setOnAction(e -> handleSave(primaryStage));

        HBox controls = new HBox(10, widthField, heightField, resizeButton, saveButton);
        AnchorPane root = new AnchorPane(canvas, controls);
        AnchorPane.setBottomAnchor(controls, 10.0);
        AnchorPane.setLeftAnchor(controls, 10.0);
        AnchorPane.setRightAnchor(controls, 10.0);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> {
            if (canvas.isFocused()) {
                switch (event.getCode()) {
                    case ADD:
                    case PLUS:
                        zoomFactor *= 1.5;
                        break;
                    case SUBTRACT:
                    case MINUS:
                        zoomFactor /= 1.5;
                        break;
                    case UP:
                        minY -= 0.1 * (maxY - minY) / zoomFactor;
                        maxY -= 0.1 * (maxY - minY) / zoomFactor;
                        break;
                    case DOWN:
                        minY += 0.1 * (maxY - minY) / zoomFactor;
                        maxY += 0.1 * (maxY - minY) / zoomFactor;
                        break;
                    case LEFT:
                        minX -= 0.1 * (maxX - minX) / zoomFactor;
                        maxX -= 0.1 * (maxX - minX) / zoomFactor;
                        break;
                    case RIGHT:
                        minX += 0.1 * (maxX - minX) / zoomFactor;
                        maxX += 0.1 * (maxX - minX) / zoomFactor;
                        break;
                    default:
                        break;
                }
                drawMandelbrot();
            }
        });

        primaryStage.setOnShown(event -> Platform.runLater(() -> canvas.requestFocus()));
        primaryStage.setTitle("Mandelbrot Set Explorer - " + mode);
        primaryStage.setScene(scene);
        primaryStage.show();

        drawMandelbrot();
    }

    private void drawMandelbrot() {
        try {
            renderer.render(gc, imageWidth, imageHeight, minX, maxX, minY, maxY, zoomFactor);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleResize() {
        try {
            imageWidth = Integer.parseInt(widthField.getText());
            imageHeight = Integer.parseInt(heightField.getText());
            canvas.setWidth(imageWidth);
            canvas.setHeight(imageHeight);
            drawMandelbrot();
            canvas.requestFocus();
        } catch (NumberFormatException e) {
            System.out.println("Invalid width or height.");
        }
    }

    private void handleSave(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                new FileChooser.ExtensionFilter("JPEG Files", "*.jpg"),
                new FileChooser.ExtensionFilter("JPG Files", "*.jpg")
        );
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            saveImage(file);
        }
        canvas.requestFocus();
    }

    private void saveImage(File file) {
        WritableImage writableImage = new WritableImage(imageWidth, imageHeight);
        canvas.snapshot(null, writableImage);
        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
            String ext = getExtension(file.getName());
            if (ext == null) ext = "png";
            ImageIO.write(bufferedImage, ext, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getExtension(String filename) {
        int i = filename.lastIndexOf('.');
        if (i > 0 && i < filename.length() - 1) {
            return filename.substring(i + 1).toLowerCase();
        }
        return null;
    }
}
*/


/*package primorska.mandlbrotset;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import primorska.mandlbrotset.renderer.MandelbrotRenderer;
import primorska.mandlbrotset.parallel.ParallelRenderer;
import primorska.mandlbrotset.sequential.SequentialRenderer;
import primorska.mandlbrotset.distributed.DistributedRendererWrapper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MandelbrotApp extends Application {

    private MandelbrotRenderer renderer;

    private double minX = -2.5, maxX = 1.5;
    private double minY = -1.5, maxY = 1.5;
    private double zoomFactor = 1.0;
    private Canvas canvas;
    private GraphicsContext gc;
    private TextField widthField;
    private TextField heightField;
    private int imageWidth = 800;
    private int imageHeight = 600;

    @Override
    public void start(Stage primaryStage) {
        // Determine mode from args
        String mode = "sequential";
        Parameters params = getParameters();
        for (String arg : params.getRaw()) {
            if (arg.startsWith("--mode=")) {
                mode = arg.substring("--mode=".length()).toLowerCase();
            }
        }

        switch (mode) {
            case "distributed":
                renderer = new DistributedRendererWrapper();
                break;
            case "parallel":
                renderer = new ParallelRenderer();
                break;
            case "sequential":
            default:
                renderer = new SequentialRenderer();
                break;
        }

        renderer.logHardwareUsage();

        canvas = new Canvas(imageWidth, imageHeight);
        gc = canvas.getGraphicsContext2D();

        widthField = new TextField(String.valueOf(imageWidth));
        heightField = new TextField(String.valueOf(imageHeight));
        Button resizeButton = new Button("Resize");
        Button saveButton = new Button("Save");

        resizeButton.setOnAction(e -> handleResize());
        saveButton.setOnAction(e -> handleSave(primaryStage));

        HBox controls = new HBox(10, widthField, heightField, resizeButton, saveButton);
        AnchorPane root = new AnchorPane(canvas, controls);
        AnchorPane.setBottomAnchor(controls, 10.0);
        AnchorPane.setLeftAnchor(controls, 10.0);
        AnchorPane.setRightAnchor(controls, 10.0);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> {
            if (canvas.isFocused()) {
                switch (event.getCode()) {
                    case ADD:
                    case PLUS:
                        zoomFactor *= 1.5;
                        break;
                    case SUBTRACT:
                    case MINUS:
                        zoomFactor /= 1.5;
                        break;
                    case UP:
                        minY -= 0.1 * (maxY - minY) / zoomFactor;
                        maxY -= 0.1 * (maxY - minY) / zoomFactor;
                        break;
                    case DOWN:
                        minY += 0.1 * (maxY - minY) / zoomFactor;
                        maxY += 0.1 * (maxY - minY) / zoomFactor;
                        break;
                    case LEFT:
                        minX -= 0.1 * (maxX - minX) / zoomFactor;
                        maxX -= 0.1 * (maxX - minX) / zoomFactor;
                        break;
                    case RIGHT:
                        minX += 0.1 * (maxX - minX) / zoomFactor;
                        maxX += 0.1 * (maxX - minX) / zoomFactor;
                        break;
                    default:
                        break;
                }
                drawMandelbrot();
            }
        });

        primaryStage.setOnShown(event -> Platform.runLater(() -> canvas.requestFocus()));
        primaryStage.setTitle("Mandelbrot Set Explorer - " + mode);
        primaryStage.setScene(scene);
        primaryStage.show();

        drawMandelbrot();
    }


    private void drawMandelbrot() {
        new Thread(() -> {
            try {
                BufferedImage img;

                if (renderer instanceof DistributedRendererWrapper) {
                    // Distributed rendering — returns a BufferedImage
                    img = ((DistributedRendererWrapper) renderer).renderToImage(
                            imageWidth, imageHeight, minX, maxX, minY, maxY, 500);
                } else {
                    // Sequential/Parallel rendering — render to BufferedImage manually
                    //img = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
                    //((SequentialRenderer) renderer).renderToImage(img, imageWidth, imageHeight, minX, maxX, minY, maxY, 500);
                    img = renderer.renderToImage(imageWidth, imageHeight, minX, maxX, minY, maxY, 500);

                }

                if (img != null) {
                    WritableImage fxImage = SwingFXUtils.toFXImage(img, null);
                    Platform.runLater(() -> {
                        gc.clearRect(0, 0, imageWidth, imageHeight);
                        gc.drawImage(fxImage, 0, 0);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleResize() {
        try {
            imageWidth = Integer.parseInt(widthField.getText());
            imageHeight = Integer.parseInt(heightField.getText());
            canvas.setWidth(imageWidth);
            canvas.setHeight(imageHeight);
            drawMandelbrot();
            canvas.requestFocus();
        } catch (NumberFormatException e) {
            System.out.println("Invalid width or height.");
        }
    }

    private void handleSave(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                new FileChooser.ExtensionFilter("JPEG Files", "*.jpg"),
                new FileChooser.ExtensionFilter("JPG Files", "*.jpg")
        );
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            saveImage(file);
        }
        canvas.requestFocus();
    }

    private void saveImage(File file) {
        WritableImage writableImage = new WritableImage(imageWidth, imageHeight);
        canvas.snapshot(null, writableImage);
        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
            String ext = getExtension(file.getName());
            if (ext == null) ext = "png";
            ImageIO.write(bufferedImage, ext, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getExtension(String filename) {
        int i = filename.lastIndexOf('.');
        if (i > 0 && i < filename.length() - 1) {
            return filename.substring(i + 1).toLowerCase();
        }
        return null;
    }
}*/

package primorska.mandlbrotset;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import primorska.mandlbrotset.renderer.MandelbrotRenderer;
import primorska.mandlbrotset.parallel.ParallelRenderer;
import primorska.mandlbrotset.sequential.SequentialRenderer;
import primorska.mandlbrotset.distributed.DistributedRendererWrapper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class MandelbrotApp extends Application {

    private MandelbrotRenderer renderer;

    private double minX = -2.5, maxX = 1.5;
    private double minY = -1.5, maxY = 1.5;
    private double zoomFactor = 1.0;

    private Canvas canvas;
    private GraphicsContext gc;
    private TextField widthField;
    private TextField heightField;

    private int imageWidth = 800;
    private int imageHeight = 600;

    // Keep last rendered image for saving
    private volatile BufferedImage lastRenderedImage;
    private static BufferedImage staticRenderedImage = null;

    public static void setRenderedImage(BufferedImage img) {
        staticRenderedImage = img;
    }

    @Override
    public void start(Stage primaryStage) {
        // Determine mode from args
        String mode = "sequential";
        Parameters params = getParameters();
        for (String arg : params.getRaw()) {
            if (arg.startsWith("--mode=")) {
                mode = arg.substring("--mode=".length()).toLowerCase();
            }
        }

        switch (mode) {
            case "distributed":
                renderer = new DistributedRendererWrapper();
                break;
            case "parallel":
                renderer = new ParallelRenderer();
                break;
            case "sequential":
            default:
                renderer = new SequentialRenderer();
                break;
        }

        renderer.logHardwareUsage();

        canvas = new Canvas(imageWidth, imageHeight);
        gc = canvas.getGraphicsContext2D();

        widthField = new TextField(String.valueOf(imageWidth));
        heightField = new TextField(String.valueOf(imageHeight));
        Button resizeButton = new Button("Resize");
        Button saveButton = new Button("Save");

        resizeButton.setOnAction(e -> handleResize());
        saveButton.setOnAction(e -> handleSave(primaryStage));

        HBox controls = new HBox(10, widthField, heightField, resizeButton, saveButton);
        AnchorPane root = new AnchorPane(canvas, controls);
        AnchorPane.setBottomAnchor(controls, 10.0);
        AnchorPane.setLeftAnchor(controls, 10.0);
        AnchorPane.setRightAnchor(controls, 10.0);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> {
            if (canvas.isFocused()) {
                switch (event.getCode()) {
                    case ADD:
                    case PLUS:
                       // zoomFactor *= 1.5;
                        zoom(1 / 1.5);
                        break;
                    case SUBTRACT:
                    case MINUS:
                       // zoomFactor /= 1.5;
                        zoom(1.5);
                        break;
                    case UP:
                        minY -= 0.1 * (maxY - minY) / zoomFactor;
                        maxY -= 0.1 * (maxY - minY) / zoomFactor;
                        break;
                    case DOWN:
                        minY += 0.1 * (maxY - minY) / zoomFactor;
                        maxY += 0.1 * (maxY - minY) / zoomFactor;
                        break;
                    case LEFT:
                        minX -= 0.1 * (maxX - minX) / zoomFactor;
                        maxX -= 0.1 * (maxX - minX) / zoomFactor;
                        break;
                    case RIGHT:
                        minX += 0.1 * (maxX - minX) / zoomFactor;
                        maxX += 0.1 * (maxX - minX) / zoomFactor;
                        break;
                    default:
                        break;
                }
                drawMandelbrot();
            }
        });

        primaryStage.setOnShown(event -> Platform.runLater(() -> canvas.requestFocus()));
        primaryStage.setTitle("Mandelbrot Set Explorer - " + mode);
        primaryStage.setScene(scene);
        primaryStage.show();

        drawMandelbrot();
    }
/*
    private void drawMandelbrot() {
        new Thread(() -> {
            try {
                BufferedImage img = renderer.renderToImage(imageWidth, imageHeight, minX, maxX, minY, maxY, 500);

                if (img != null) {
                    lastRenderedImage = img;  // Save for later use (e.g., saving file)
                    WritableImage fxImage = SwingFXUtils.toFXImage(img, null);
                    Platform.runLater(() -> {
                        gc.clearRect(0, 0, imageWidth, imageHeight);
                        gc.drawImage(fxImage, 0, 0);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
*/
private void zoom(double factor) {
    double centerX = (minX + maxX) / 2;
    double centerY = (minY + maxY) / 2;
    double width = (maxX - minX) * factor;
    double height = (maxY - minY) * factor;

    minX = centerX - width / 2;
    maxX = centerX + width / 2;
    minY = centerY - height / 2;
    maxY = centerY + height / 2;
}

    private void drawMandelbrot() {
    System.out.println("Starting Mandelbrot rendering...");
    if (staticRenderedImage != null) {
        // Show the pre-rendered image (distributed rendering)
        lastRenderedImage = staticRenderedImage;
        WritableImage fxImage = SwingFXUtils.toFXImage(staticRenderedImage, null);
        Platform.runLater(() -> {
            gc.clearRect(0, 0, imageWidth, imageHeight);
            gc.drawImage(fxImage, 0, 0);
        });
        return;  // skip local rendering
    }

    // Otherwise do normal rendering with current renderer
    new Thread(() -> {
        try {
            BufferedImage img = renderer.renderToImage(imageWidth, imageHeight, minX, maxX, minY, maxY, 500);
            System.out.println("Rendering done: " + (img != null));
            if (img != null) {
                lastRenderedImage = img;  // Save for later use (e.g., saving file)
                WritableImage fxImage = SwingFXUtils.toFXImage(img, null);
                Platform.runLater(() -> {
                    gc.clearRect(0, 0, imageWidth, imageHeight);
                    gc.drawImage(fxImage, 0, 0);
                    System.out.println("Image drawn on canvas.");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }).start();
}

    private void handleResize() {
        try {
            int newWidth = Integer.parseInt(widthField.getText());
            int newHeight = Integer.parseInt(heightField.getText());

            if (newWidth <= 0 || newHeight <= 0) {
                System.out.println("Width and height must be positive.");
                return;
            }

            imageWidth = newWidth;
            imageHeight = newHeight;
            canvas.setWidth(imageWidth);
            canvas.setHeight(imageHeight);
            drawMandelbrot();
            canvas.requestFocus();
        } catch (NumberFormatException e) {
            System.out.println("Invalid width or height.");
        }
    }

    private void handleSave(Stage primaryStage) {
        if (lastRenderedImage == null) {
            System.out.println("No image to save yet.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                new FileChooser.ExtensionFilter("JPEG Files", "*.jpg"),
                new FileChooser.ExtensionFilter("JPG Files", "*.jpg")
        );
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            saveImage(file);
        }
        canvas.requestFocus();
    }
/*
    private void saveImage(File file) {
        try {
            String ext = getExtension(file.getName());
            if (ext == null) ext = "png";
            ImageIO.write(lastRenderedImage, ext, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
private void saveImage(File file) {
    try {
        String ext = getExtension(file.getName());
        if (ext == null) ext = "png";

        BufferedImage imgToSave = lastRenderedImage;

        // JPEG does not support alpha channel; convert if needed
        if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg")) {
            imgToSave = removeAlphaChannel(lastRenderedImage);
        }

        boolean result = ImageIO.write(imgToSave, ext, file);
        if (!result) {
            System.out.println("Saving failed: unknown format or ImageIO plugin missing.");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    private BufferedImage removeAlphaChannel(BufferedImage image) {
        BufferedImage rgbImage = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g = rgbImage.createGraphics();
        g.drawImage(image, 0, 0, java.awt.Color.WHITE, null); // Fill transparent areas with white
        g.dispose();
        return rgbImage;
    }

    private String getExtension(String filename) {
        int i = filename.lastIndexOf('.');
        if (i > 0 && i < filename.length() - 1) {
            return filename.substring(i + 1).toLowerCase();
        }
        return null;
    }
}
