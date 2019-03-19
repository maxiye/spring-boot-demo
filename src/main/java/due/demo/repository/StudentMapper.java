package due.demo.repository;

import due.demo.model.Student;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author due
 */
@Mapper
@Component("StudentMapper")
public interface StudentMapper {
    /**
     * 通过姓名获取student
     * @param name String
     * @return Student
     */
    @Select("SELECT * from student where name = #{name}")
    Student findByName(@Param("name") String name);

    /**
     * 通过姓名like获取
     * @param name string
     * @return List
     */
    @Select("SELECT * FROM student WHERE name LIKE #{name}")
    List<Student> findByNameLike(@Param("name") String name);

    /**
     * 新增
     * @param name string
     * @param age int
     * @param tId int
     * @return int id
     */
    @Insert("INSERT INTO student(name, age, t_id) VALUES(#{name}, #{age}, #{tId})")
    int insert(@Param("name") String name, @Param("age") Integer age, @Param("tId") Integer tId);

    /**
     * 修改
     * @param name string
     * @param age int
     * @return int
     */
    @Update("UPDATE student SET age = #{age} WHERE name = #{name}")
    int update(@Param("name") String name, @Param("age") Integer age);

    /**
     * delete
     * @param name String
     * @return int
     */
    @Delete("DELETE FROM student WHERE name = #{name}")
    int delete(@Param("name") String name);

    /**
     * 统计总数
     * @return int
     */
    @Select("SELECT COUNT(*) FROM student")
    int countAll();

    /**
     * 通过id集合获取列表
     * @param params Map
     * @return List
     */
    @Select("select s.name,s.age,u.name as teacher,u.age as teacher_age from student s " +
            "left join user u on u.id=s.t_id " +
            "where s.id in ${uId}" +
            "order by s.id desc limit 10")
    List<HashMap<String, Object>> getUserList(Map params);
}
