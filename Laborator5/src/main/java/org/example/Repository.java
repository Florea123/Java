package org.example;
import java.io.File;
import java.io.IOException;
import java.awt.Desktop;
import java.util.ArrayList;
import java.util.List;
public class Repository {
    private List<Image> images = new ArrayList<>();

    public Repository() {

    }
    public void add(Image image) {
        images.add(image);
    }
    public void remove(String name) {
        images.removeIf(image -> image.name().equals(name));
    }

    public void update(String name, String newName, String filePath) {
        images.stream()
                .filter(image -> image.name().equals(name))
                .findFirst()
                .ifPresentOrElse(
                        image -> {
                            images.remove(image);
                            images.add(new Image(newName, filePath));
                            System.out.println("Image updated successfully.");
                        },
                        () -> System.out.println("Image not found.")
                );
    }

    public List<Image> getImages()
    {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }


    public void displayImage(String name) {
        images.stream()
                .filter(image -> image.name().equals(name))
                .findFirst()
                .ifPresentOrElse(
                        image -> {
                            try {
                                Desktop.getDesktop().open(new File(image.filePath()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        },
                        () -> System.out.println("Image not found")
                );
    }

}
