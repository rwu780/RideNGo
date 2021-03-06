package assignment1.ridengo;

/**
 * The type Vehicle.
 * Keep track of all vehicle info, such as plate number, year, make, etc.
 */

public class Vehicle {

    private String plateNum;
    private int vYear;
    private String vMake;
    private String vModel;
    private String vColor;

    /**
     * Instantiates a new vehicle
     * @param pNum plate number
     * @param year year of vehicle
     * @param make make of vehicle
     * @param model model of vehicle
     * @param color color of vehicle
     */
    public Vehicle(String pNum,int year,String make,String model,String color){
        this.plateNum = pNum;
        this.vYear = year;
        this.vMake = make;
        this.vModel = model;
        this.vColor = color;
    }

    /**
     * Set plate number of the vehicle
     * @param pNum plate number
     */
    public void setPlateNum(String pNum){
        this.plateNum = pNum;
    }

    /**
     * Set year of the vehicle
     * @param year year of vehicle
     */
    public void setYear(int year){
        this.vYear = year;
    }

    /**
     * Set make of the vehicle
     * @param make make of the vehicle
     */
    public void setMake(String make){
        this.vMake = make;
    }

    /**
     * Set model of the vehicle
     * @param model model of the vehicle
     */
    public void setModel(String model){
        this.vModel = model;
    }

    /**
     * Set color of the vehicle
     * @param color color of the vehicle
     */
    public void setColor(String color){
        this.vColor = color;
    }

    /**
     * Get plate number of the vehicle
     * @return String of plate number
     */
    public String getPlateNum(){
        return this.plateNum;
    }

    /**
     * Get year of the vehicle
     * @return int type year
     */
    public int getYear(){
        return this.vYear;
    }

    /**
     * Get make of the vehicle
     * @return String of make
     */
    public String getMake(){
        return this.vMake;
    }

    /**
     * Get model of the vehicle
     * @return String of model
     */
    public String getModel(){
        return this.vModel;
    }

    /**
     * Get color of the vehicle
     * @return String of color
     */
    public String getColor(){
        return this.vColor;
    }

    @Override
    public String toString() {
        return this.vColor +" "+ this.vYear+" "+this.vMake+" "+this.vModel;
    }
}
