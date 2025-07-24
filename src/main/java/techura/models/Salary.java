package techura.models;

import techura.utils.EmployeDataUtil;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class Salary extends Employe {
    private final String day; // e.g., "2025-07-23"
    private  final LocalTime inTime;
    private  final LocalTime outTime;

    public Salary(String id, String name, double hourlyRate, String day, LocalTime inTime, LocalTime outTime) {
        super(id, name, "", "", 0, "", hourlyRate, 0);
        this.day = day;
        this.inTime = inTime;
        this.outTime = outTime;
    }

    public double getTotalHoursWorked() {
        Duration duration = Duration.between(inTime, outTime);
        long minutes = duration.toMinutes();
        return minutes / 60.0;
    }

    public double getTotalTime(double totalminut,int days){
        double total = totalminut*days;
        return total;
    }

    public double getSalary() {
        return getTotalHoursWorked() * getHourlyRate();
    }

    public String getDay() {
        return day;
    }

    public  LocalTime getInTime() {
        return inTime;
    }

    public  LocalTime getOutTime() {
        return outTime;
    }

    @Override
    public String toCSV() {
        return String.join(",",
                getId(),
                getName(),
                String.valueOf(getHourlyRate()),
                day,
                inTime.toString(),
                outTime.toString(),
                String.valueOf(getTotalHoursWorked()),
                String.valueOf(getSalary())
        );
    }

    @Override
    public String toString() {
        return "Salary{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", day='" + day + '\'' +
                ", inTime=" + inTime +
                ", outTime=" + outTime +
                ", totalHours=" + getTotalHoursWorked() +
                ", salary=" + getSalary() +
                '}';
    }
}