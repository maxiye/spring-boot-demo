package due.demo.repository;

import due.demo.model.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author due
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 姓名精确查询User
     * @param name String
     * @return User
     */
    //@Cacheable(value = "user", key = "#p0 + '000' + caches[0].name + targetClass + target + method.name + methodName")//不加value 只有key ：java.lang.IllegalStateException: No cache could be resolved for 'Builder
    //@Cacheable(value = "user")
    //定义了@bean cacheManager ，必须value 存在于 cacheManager.cachecNames 中
    User findByName(String name);

    /**
     * 姓名和年龄查询User
     * @param name String
     * @param age int
     * @return User
     */
    User findByNameAndAge(String name, int age);

    /**
     * 精确查询用户列表
     * @param name String
     * @return List
     */
    List<User> findAllByName(String name);

    /**
     * 姓名模糊查询用户列表
     * @param name String
     * @return List
     */
    // 通过名称模糊查询
    @Cacheable("spring-demo-users")//默认key = #p0
    List<User> findByNameLike(String name);

    /**
     * 使用hql查询
     * @param name String
     * @return User
     */
    @Query("from User u where u.name =:name")
    User findByHQL(@Param("name") String name);

    /**
     * 通过姓名和年龄获取User
     * @param name String
     * @param age int
     * @return User
     */
    // 使用sql查询
    @Query(value = "select * from user where name = ?1 and age = ?2", nativeQuery = true)//?1表示第一个参数，?2表示第二个参数
    User findBySQL(String name, Integer age);

    /*@Query("select s.id,s.name,s.age,u.name as teacher,u.age as teacher_age from student s left join user u on u.id=t.t_id")
    Page<HashMap<String, Object>> findUserInfoList(Pageable pageable);*/
}
