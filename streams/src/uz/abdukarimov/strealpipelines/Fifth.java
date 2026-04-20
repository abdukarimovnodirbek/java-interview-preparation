package uz.abdukarimov.strealpipelines;

import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class Fifth {
    static void main() {
        record Employee(String name, double salary, boolean active) {
        }
        record Department(String name, List<Employee> employees) {
        }

        List<Department> company = List.of(
                new Department("IT", List.of(
                        new Employee("Ali", 3500, true),
                        new Employee("Vali", 2800, false),
                        new Employee("Sali", 4200, true)
                )),
                new Department("HR", List.of(
                        new Employee("Dilnoza", 3100, true),
                        new Employee("Malika", 2500, true)
                )),
                new Department("Finance", List.of(
                        new Employee("Jamshid", 5000, true),
                        new Employee("Bobur", 3800, false)
                ))
        );

        // Barcha aktiv xodimlarning o'rtacha maoshi
        OptionalDouble avgSalary = company.stream()
                .flatMap(dept -> dept.employees().stream())  // yassilash
                .filter(Employee::active)                    // faqat aktiv
                .mapToDouble(Employee::salary)               // double stream
                .average();

        avgSalary.ifPresent(avg ->
                System.out.printf("O'rtacha maosh: $%.1f%n", avg)
        );
        // O'rtacha maosh: $3960.0

        // Departament bo'yicha eng yuqori maosh
        company.stream()
                .collect(Collectors.toMap(
                        Department::name,
                        dept -> dept.employees().stream()
                                .filter(Employee::active)
                                .mapToDouble(Employee::salary)
                                .max()
                                .orElse(0)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(e -> System.out.printf("%-10s → $%.0f%n",
                        e.getKey(), e.getValue()));
        // Finance    → $5000
        // IT         → $4200
        // HR         → $3100
    }
}
