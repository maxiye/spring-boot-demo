package due.demo;

import due.demo.repository.StudentMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional //必须有，rollback才生效
public class MybatisTests {
    @Autowired
    StudentMapper studentMapper;
    @Test
    @Rollback
    public void test() {
        int count = studentMapper.countAll();
        studentMapper.insert("QQQ",1, 1);
        studentMapper.insert("WWW",2, 2);
        studentMapper.insert("EEE",3, 3);
        studentMapper.insert("AAA",4, 4);
        studentMapper.insert("SSS",5, 5);
        studentMapper.insert("DDD",6, 6);
        studentMapper.insert("ZZZ",7, 7);
        studentMapper.insert("XXX",8, 8);
        studentMapper.insert("CCC",9, 9);
        studentMapper.insert("SSS213",10, 10);

        Assert.assertEquals(10 + count, studentMapper.countAll());

        Assert.assertEquals(5, studentMapper.findByName("SSS").getAge().longValue());

        studentMapper.update("CCC", 10);

        Assert.assertEquals(10, studentMapper.findByName("CCC").getAge().longValue());

        studentMapper.delete("CCC");

        Assert.assertEquals(9 + count, studentMapper.countAll());

        Assert.assertEquals(2, studentMapper.findByNameLike("SSS%").size());
    }
}
