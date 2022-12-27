package com.micronaut.repositories;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.BatchResults;
import com.aerospike.client.Key;
import com.aerospike.client.Value;
import com.aerospike.client.cdt.ListOperation;
import com.aerospike.client.cdt.ListReturnType;
import com.github.javafaker.Faker;
import com.micronaut.configuration.AeroMapperConfiguration;
import com.micronaut.entities.Department;
import com.micronaut.entities.Employee;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Singleton
public class EmployeeRepository {

    @Inject
    AeroMapperConfiguration mapper;

    //AeroMapper mapper1 = mapper.getMapper();
    public String addEmployee(Employee employee) {
        String result;
        try {
            Random random = new Random();

            Employee newEmployee = new Employee();
            newEmployee.setId(random.nextInt(1000));
            newEmployee.setAge(employee.getAge());
            newEmployee.setName(employee.getName());
            newEmployee.setContactNum(employee.getContactNum());
            newEmployee.setEmail(employee.getEmail());
            newEmployee.setSalary(employee.getSalary());
            newEmployee.setJoiningDate(employee.getJoiningDate());
            newEmployee.setDepartment(employee.getDepartment());

            mapper.getMapper().save(newEmployee);
            result = "Employee created successfully id is "+newEmployee.getId();
        }
        catch (Exception e) {
            e.printStackTrace();
            result = "Failed to register";
        }
        return  result;
    }

    public List<Employee> getEmployees() {
        return mapper.getMapper().scan(Employee.class);
    }

    public String updateDetails(int id, Employee employee) {
        Employee emp = getEmpById(id);
        if (emp != null) {
            emp.setId(id);
            emp.setAge(employee.getAge());
            emp.setName(employee.getName());
            emp.setContactNum(employee.getContactNum());
            emp.setEmail(employee.getEmail());
            emp.setSalary(employee.getSalary());
            emp.setJoiningDate(employee.getJoiningDate());
            emp.setDepartment(employee.getDepartment());

            mapper.getMapper().save(emp);

            return "Employee information updated successfully ";
        } else {
            return "Please enter correct id, update failed";
        }
    }

    public String deleteById(int id) {
        String result = "";
        try {
            Employee employee = this.getEmpById(id);
            if (employee != null) {
                mapper.getMapper().delete(Employee.class,id);
                result = "Employee having ID : " +id+" deleted successfully";
            }
            else {
                result = "Failed to delete employee, Please enter valid ID ";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            result = "Failed to delete employee, Please enter valid ID ";
        }
        return result;
    }

    public Employee getEmpById(int id) {
        return mapper.getMapper().read(Employee.class, id);
    }

    public String bulkInsert() {
        String result = "";
        try {
            String nameSpace = "test";
            String set = "Employee";
            Faker faker = new Faker();

            List<Employee> employees = new ArrayList<>();
            for (int i=1; i <= 100; i++) {
                String departmentName = faker.job().field();
                String name = faker.name().firstName();
                String email = name+"@gmail.com";
                List<String> contactNos = Arrays.asList(faker.phoneNumber().subscriberNumber(10), faker.phoneNumber().subscriberNumber(10), faker.phoneNumber().subscriberNumber(10));

                Employee employee = new Employee(name, ThreadLocalRandom.current().nextInt(19, 48), ThreadLocalRandom.current().nextDouble(27000, 95000) , faker.date().birthday(1,15), email, contactNos, new Department(1, departmentName));
                employee.setId(i);
                employees.add(employee);
            }

            for (Employee employee : employees) {
                mapper.getMapper().save(employee);
            }
            result = "Added records";
        }
        catch (Exception e) {
            result = "Failed to add records";
        }
        return result;
    }

//    public String bulkDelete() {
//        AerospikeClient client = mapper.getClient();
//        String result;
//        try {
//            Key [] keys = new Key[101];
//            for (int i=1; i <= 100; i++) {
//                keys[i] = new Key("test", "mapset", i);
//            }
//            List<BatchRecord> records = new ArrayList<BatchRecord>();
//            for (int j=1; j <= 100; j++) {
//                records.add(new BatchDelete(new Key("test", "Employee",j)));
//            }
//            client.operate(null, records);
//            result = "Records deleted successfully";
//        }
//        catch (Exception e) {
//            result = "Failed to delete records";
//        }
//        return result;
//    }
    public String bulkDelete() {
        String result = "";
        try {
            String namespace = "test";
            String set = "Employee";

            List<Employee> employees = mapper.getMapper().scan(Employee.class);
            Key [] keys = new Key[employees.size()];
            List<Key> keyList = new ArrayList<>();

            employees.forEach(employee -> {
                keyList.add(new Key(namespace, set, employee.getId()));
            });
            keyList.toArray(keys);

            BatchResults br = mapper.getClient().delete(null, null, keys);
            result = "Deleted records";
        }
        catch (Exception e) {
            result = "Failed to delete records";
        }
        return result;
    }

    public String addContact(int id ,String contact) {
        String result = "";
        try {
            AerospikeClient client = mapper.getClient();
            Key key = new Key("test", "Employee", id);
            client.operate(client.writePolicyDefault, key,
                    ListOperation.append("contactNum", Value.get(contact)
                    ));
            result = "Contact number added sucessfully";
        }
        catch (Exception e) {
            result = "Failed to add contact number";
        }
        return result;
    }

    public String removeContact(int id ,String contact) {
        String result = "";
        try {
            AerospikeClient client = mapper.getClient();
            Key key = new Key("test", "Employee", id);
            List<Value> contacts = Arrays.asList(Value.get(contact));
            client.operate(client.writePolicyDefault, key,
                    ListOperation.removeByValueList("contactNum", contacts, ListReturnType.NONE
                    ));
            result = "Contact number removed successfully";
        }
        catch (Exception e) {
            result = "Failed to remove contact number";
        }
        return result;
    }
}
