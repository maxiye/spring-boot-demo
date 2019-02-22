package due.demo;

import due.demo.model.User;
import due.demo.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional //必须有，rollback才生效
public class JpaUserTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    @Rollback
    public void test() {
        int size = userRepository.findAll().size();
        userRepository.save(new User("duee1", 1));
        userRepository.save(new User("duee2", 2));
        userRepository.save(new User("duee3", 3));
        userRepository.save(new User("duee4", 4));
        userRepository.save(new User("duee5", 5));
        userRepository.save(new User("duee6", 6));
        userRepository.save(new User("duee7", 7));
        userRepository.save(new User("duee8", 8));
        userRepository.save(new User("duee9", 9));
        userRepository.save(new User("duee10", 10));
        Assert.assertEquals(10 + size, userRepository.findAll().size());

        Assert.assertEquals(5, userRepository.findByName("duee5").getAge());

        Assert.assertEquals("duee10", userRepository.findByNameAndAge("duee10", 10).getName());// 测试删除姓名为AAA的User

        userRepository.delete(userRepository.findByName("duee2"));

        // 测试findAll, 查询所有记录, 验证上面的删除是否成功
        Assert.assertEquals(size + 9, userRepository.findAll().size());

        // 测试findAll, 查询名字有Q的有几个
        Assert.assertEquals(9,userRepository.findByNameLike("%duee%").size());
        //test failed
        //System.out.println(userRepository.findUserInfoList(PageRequest.of(0, 5, Sort.Direction.ASC, "s.id")).toString());
    }
}
