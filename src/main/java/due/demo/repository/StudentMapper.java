package due.demo.repository;

import due.demo.model.Student;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Component("StudentMapper")
public interface StudentMapper {
    @Select("SELECT * from student where name = #{name}")
    Student findByName(@Param("name") String name);
    @Select("SELECT * FROM student WHERE name LIKE #{name}")
    List<Student> findByNameLike(@Param("name") String name);

    @Insert("INSERT INTO student(name, age, t_id) VALUES(#{name}, #{age}, #{tId})")
    int insert(@Param("name") String name, @Param("age") Integer age, @Param("tId") Integer tId);

    @Update("UPDATE student SET age = #{age} WHERE name = #{name}")
    int update(@Param("name") String name, @Param("age") Integer age);

    @Delete("DELETE FROM student WHERE name = #{name}")
    int delete(@Param("name") String name);

    @Select("SELECT COUNT(*) FROM student")
    int countAll();
    @Select("select s.name,s.age,u.name as teacher,u.age as teacher_age from student s " +
            "left join user u on u.id=s.t_id " +
            "where s.id in ${uId}" +
            "order by s.id desc limit 10")
    List<HashMap<String, Object>> getUserList(Map params);
}
