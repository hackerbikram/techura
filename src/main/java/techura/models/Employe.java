package techura.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Employe {
   private String id;
   private String name;
   private String password;
    private Integer age;
   private String address;
   private String role;
   private double hourlyRate;
   private double hourlyworked;
    public Employe(String id,String name,String password,String address,int age,String role,double hourlyRate,double hourlyWorked){
        this.id=id;
        this.name =name;
       this.password = password;
        this.age =age;
        this.address=address;
        this.role =role;
        this.hourlyRate = hourlyRate;
        this.hourlyworked = hourlyWorked;
    }
    public Employe(String id,String name,double hourlyRate){
        this.id= id;
        this.name = name;
        this.password = "";
        this.age = 0;
        this.address= "";
        this.role = "";
        this.hourlyRate =hourlyRate;
        this.hourlyworked = 0;
    }


    public String getId(){return id;}
    public void setId(String id){this.id=id;}

    public String getName(){return name;}
    public void setName(String name){this.name=name;}

    public String getPassword(){return password;}
    public void setPassword(String password){this.password=password;}

    public String getAddress(){return address;}
    public void setAddress(String address){this.address=address;}

    public int getAge(){return age;}
    public void setAge(int age){this.age=age;}

    public String getRole(){return role;}
    public void  setRole(String role){this.role=role;}

    public double getHourlyRate(){return hourlyRate;}
    public void setHourlyRate(double rate){this.hourlyRate=rate;}

    public double getHourlyWorked(){return hourlyworked;}
    public void setHourlyworked(double hourlyworked){this.hourlyworked=hourlyworked;}

    public String getEmploy(){return "id: "+id+"name: "+name+"address: "+address+"age: "+age;}

    public String toCSV() {
        return id + "," +
                name + "," +
                password + "," +
                address + "," +
                getAge() + "," +          // 👈 Extract int from property
                role + "," +
                getHourlyRate() + "," +   // 👈 Extract double from property
                getHourlyWorked();        // 👈 Extract double from property
    }
}
