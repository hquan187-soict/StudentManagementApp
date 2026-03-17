package model;

// LOP CHA: Person - dai dien cho 1 nguoi bat ky

public class Person {

    private String id;
    private String name;

    public Person() { //constructor
        this.id = "";
        this.name = "";
    }

    public Person(String id, String name) { //constructor co tham so truyen vao
        this.id = id;
        this.name = name;
    }

    //ham getter va setter cua id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //ham getter va setter cua name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return "ID: " + id + ", Ten: " + name; //tra ve xau gom id va ten nguoi dung
    }
}