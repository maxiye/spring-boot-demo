package due.demo.model;

/**
 * @author due
 */
public class Student {
    private Long id;
    private String name;
    private Integer age;
    private Integer tId;

    public Integer gettId() {
        return tId;
    }

    public void settId(Integer tId) {
        this.tId = tId;
    }

    public Student() {
    }

    public Student(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public Student(String name, Integer age, Integer tId) {
        this.name = name;
        this.age = age;
        this.tId = tId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
