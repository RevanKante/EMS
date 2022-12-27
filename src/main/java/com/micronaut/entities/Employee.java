package com.micronaut.entities;

import com.aerospike.mapper.annotations.AerospikeEmbed;
import com.aerospike.mapper.annotations.AerospikeKey;
import com.aerospike.mapper.annotations.AerospikeRecord;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

@AerospikeRecord(namespace = "test", set = "Employee")
public class Employee {

    @AerospikeKey
    private int id;

    private String name;

    private int age;

    private double salary;

    @JsonFormat(pattern = "dd-mm-yyyy")
    private Date joiningDate;

    private String email;

    private List<String> contactNum;

    @AerospikeEmbed
    private Department department;

    public Employee() {
    }

    public Employee(String name, int age, double salary, Date joiningDate, String email, List<String> contactNum, Department department) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.joiningDate = joiningDate;
        this.email = email;
        this.contactNum = contactNum;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getContactNum() {
        return contactNum;
    }

    public void setContactNum(List<String> contactNum) {
        this.contactNum = contactNum;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                ", joiningDate=" + joiningDate +
                ", email='" + email + '\'' +
                ", contactNum=" + contactNum +
                ", department=" + department +
                '}';
    }
}
