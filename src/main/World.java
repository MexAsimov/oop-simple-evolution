package main;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class World {
    public static void main(String[] args) throws Exception {
        try (FileReader fileReader = new FileReader("parameters.json")) {
            Object obj = new JSONParser().parse(fileReader);
            JSONObject jo = (JSONObject) obj;
            int width = JSONGetInt(jo, "width");
            int height = JSONGetInt(jo, "height");
            int startEnergy = JSONGetInt(jo, "startEnergy");
            int moveEnergy = JSONGetInt(jo, "moveEnergy");
            int plantEnergy = JSONGetInt(jo, "plantEnergy");
            int numberOfAnimals = JSONGetInt(jo, "initialNumberOfAnimals");
            double jungleRatio = (double) jo.get("jungleRatio");
            System.out.println("Simulation size: " + width + "x" + height);

            Field firstField = new Field(width, height, startEnergy, moveEnergy, plantEnergy, jungleRatio, numberOfAnimals);
            Field secondField = new Field(width, height, startEnergy, moveEnergy, plantEnergy, jungleRatio, numberOfAnimals);
            VisualAnimation.runInWindow(firstField, secondField);
            firstField.initializeMap();
            secondField.initializeMap();
        } catch (FileNotFoundException e) {
            e.getMessage();
        }
    }

    private static int JSONGetInt(JSONObject o, String data) {
        return Math.toIntExact((long) o.get(data));
    }
}
