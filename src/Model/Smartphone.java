package Model;

import org.bson.Document;

public class Smartphone {
    private String brand;
    private String model;
    private double priceInCHF;
    private int ramInGB;
    private String screenSizeInInches;
    private int storageInGB;
    private String os;
    private String OSVersion;
    private String pixelresolution;
    private int numberOfCores;
    private String batteryCapacity;
    private String connectivity;
    private String mobiledataStandard;


    public Smartphone(String brand, String model, double priceInCHF,int ramInGB, String screenSizeInInches , int storageInGB, String os, String OSVersion, String pixelresolution, int numberOfCores, String batteryCapacity, String connectivity, String mobiledataStandard) {
        this.brand = brand;
        this.model = model;
        this.priceInCHF = priceInCHF;
        this.ramInGB = ramInGB;
        this.screenSizeInInches = screenSizeInInches;
        this.storageInGB = storageInGB;
        this.os = os;
        this.OSVersion = OSVersion;
        this.pixelresolution = pixelresolution;
        this.numberOfCores = numberOfCores;
        this.batteryCapacity = batteryCapacity;
        this.connectivity = connectivity;
        this.mobiledataStandard = mobiledataStandard;

    }

    public Document toDocument() {
        return new Document("brand", brand)
                .append("model", model)
                .append("price", priceInCHF)
                .append("ram", ramInGB)
                .append("screenSizeInInches", screenSizeInInches)
                .append("storage", storageInGB)
                .append("os", os)
                .append("OSVersion", OSVersion)
                .append("pixelresolution", pixelresolution)
                .append("numberOfCores", numberOfCores)
                .append("batteryCapacity", batteryCapacity)
                .append("connectivity", connectivity)
                .append("mobiledataStandard", mobiledataStandard);
    }

    public static Smartphone fromDocument(Document doc) {
        String brand = doc.getString("brand");
        String model = doc.getString("model");

        double price = (doc.get("price") != null) ? doc.getDouble("price") : 0.0;
        String screenSizeInInches = doc.getString("screenSizeInInches");
        String os = doc.getString("os");
        String OSVersion = doc.getString("OSVersion");
        String pixelresolution = doc.getString("pixelresolution");
        String batteryCapacity = doc.getString("batteryCapacity");
        String connectivity = doc.getString("connectivity");
        String mobiledataStandard = doc.getString("mobiledataStandard");
        int numberOfCores = 0;
        if (doc.get("numberOfCores") instanceof String) {
            numberOfCores = Integer.parseInt(doc.getString("numberOfCores"));
        } else if (doc.get("numberOfCores") instanceof Integer) {
            numberOfCores = doc.getInteger("numberOfCores");
        }


        int storage = 0;
        if (doc.get("storage") instanceof String) {
            storage = Integer.parseInt(doc.getString("storage"));
        } else if (doc.get("storage") instanceof Integer) {
            storage = doc.getInteger("storage");
        }

        int ram = 0;
        if (doc.get("ram") instanceof String) {
            ram = Integer.parseInt(doc.getString("ram"));
        } else if (doc.get("ram") instanceof Integer) {
            ram = doc.getInteger("ram");
        }



        return new Smartphone(brand, model, price, ram, screenSizeInInches, storage , os, OSVersion, pixelresolution, numberOfCores, batteryCapacity, connectivity, mobiledataStandard);
    }

    public String getBrand() {
        return brand;
    }


    public String getModel() {
        return model;
    }



    public double getPriceInCHF() {
        return priceInCHF;
    }


    public int getStorageInGB() {
        return storageInGB;
    }


    public int getRamInGB() {
        return ramInGB;
    }


    public String getOs() {
        return os;
    }

    public String getScreenSizeInInches() {
        return screenSizeInInches;
    }

    public String getOSVersion() {
        return OSVersion;
    }

    public String getPixelresolution() {
        return pixelresolution;
    }

    public int getNumberOfCores() {
        return numberOfCores;
    }

    public String getBatteryCapacity() {
        return batteryCapacity;
    }

    public String getConnectivity() {
        return connectivity;
    }

    public String getMobiledataStandard() {
        return mobiledataStandard;
    }
}
