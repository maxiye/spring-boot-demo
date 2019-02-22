package due.demo.repository;

import due.demo.model.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashMap;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    //@Cacheable(value = "user", key = "#p0 + '000' + caches[0].name + targetClass + target + method.name + methodName")//不加value 只有key ：java.lang.IllegalStateException: No cache could be resolved for 'Builder
    //@Cacheable(value = "user")
    //定义了@bean cacheManager ，必须value 存在于 cacheManager.cachecNames 中
    User findByName(String name);

    User findByNameAndAge(String name, int age);

    List<User> findAllByName(String name);

    // 通过名称模糊查询
    @Cacheable("spring-demo-users")//默认key = #p0
    List<User> findByNameLike(String name);

    @Query("from User u where u.name =:name")
    User findByHQL(@Param("name") String name);

    // 使用sql查询
    @Query(value = "select * from user where name = ?1 and age = ?2", nativeQuery = true)//?1表示第一个参数，?2表示第二个参数
    User findBySQL(String name, Integer age);

    /*@Query("select s.id,s.name,s.age,u.name as teacher,u.age as teacher_age from student s left join user u on u.id=t.t_id")
    Page<HashMap<String, Object>> findUserInfoList(Pageable pageable);*/
}
