
##maven构建SpringBoot项目
根据需要配置pom.xml<br>
在 dependencies 添加构建 Web 项目相关的依赖
```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.6.RELEASE</version>
</parent>
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-framework-bom</artifactId>
            <version>${spring.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```
`spring-boot-starter-parent`是当前项目的父级依赖<br>
`spring-boot-starter`是SpringBoot场景启动器,SpringBoot将所有的功能场景抽取出来,做成一个个的
starters(启动器),只需项目里引入相关场景的starter, 就会将它所有依赖导入进来。要用什么功能
就导入什么场景的启动器。(各种启动器可参见官方文档 starter)<br>
`spring-boot-dependencies` 是管理了SpringBoot项目中所有依赖的版本<br>

###引导类
`@SpringBootApplication`用于标识一个引导类,说明当前是Spring Boot项目<br>
```@SpringBootApplication
public class HelloMailAppliation {
    public static void main(String[] args) {
        SpringApplication.run(HelloMailAppliation.class, args);
  }
```
通常有一个名为`*Application`的入口类,里面定义一个main方法,使用
`SpringApplication.run(HelloMailAppliation.class, args);` 来启动 SpringBoot 应用项目<br>
`@SpringBootApplication`
标注在某个类上, 说明这个类是Spring Boot的引导类,Spring Boot 就应该运行这个类的main方法来启动 SpringBoot 应用;
`@SpringBootApplication`注解主要组合了`@SpringBootConfiguration`、`@EnableAutoConfiguration`、
`@ComponentScan`<br>
##SpringBoot核心配置
SpringBoot使用一个全局配置文件,放置在`src/main/resources`目录或类路径的`/config`下;
`application.properties`<br>
`application.yml`<br>
yaml 配置文件注入值
`@ConfigurationProperties`告诉SpringBoot将配置文件中对应属性的值,映射到这个组件类中,进行一一绑定。<br>
`prefix = "emp"`:配置文件中的前缀名,哪个前缀与下面的所有属性进行一一映射<br>
`@Component`必须将当前组件作为SpringBoot中的一个组件,才能使用容器提供的`@ConfigurationProperties`功能;
```
@Component
@ConfigurationProperties(prefix = "emp")
public class Emp {
    private String lastName;
    rivate Integer age;
    private Double salary;
    private Boolean boss;
    private Date birthday;
    private Map map;
    private List list;
    //特长
    private Forte forte;
    getter/setter......
    }
    public class Forte {
    private String name;
    private Integer time;
    
    getter/setter......
}
```
使用SpringBoot单元测试类进行测试
SpringBoot单元测试:
在测试时,可以直接将对象注入到容器中使用
使用 SpringBoot 单元测试类进行测试
```
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBoot02ConfigApplicationTests {
    @Autowired
    Emp emp;

    @Test
    public void contextLoads() {
        System.out.println(emp);
    }
}
```

##SpringBoot的web开发
Web开发是项目实战中至关重要的一部分,Web开发的核心内容主要包括嵌入的Servlet容器和SpringMVC<br>
Web开发官方文档:
https://docs.spring.io/spring-boot/docs/2.0.6.RELEASE/reference/htmlsingle/#boot-features-spring-mvc<br>

配置`pom.xml`,增加SpringBoot的打包插件:<br>
```
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

###Controller层
`@RestController`相当于`@Controller`+`@ResponseBody`一起使用，表示整个Controller的方法返回值都是json或json对象

###Dao层
可以使用三种注解来引入DAO层的接口到spring容器中。<br>
1.`@Mapper`，写在每一个DAO层接口上，如下：<br>
```
@Mapper
public interface UserDAO {
  public User find(@Param("name") String name, @Param("password") String password);
}
```
2.`@MapperScan`和`@ComponentScan`两者之一。前者的意义是将指定包中的所有接口都标注为DAO层接口，相当于在每一个接口上写`@Mapper`。<br>
后者则是代替所有`@Component`，包括`@Service`、`@Repository`、`@Controller`等。如下：<br>
```
@SpringBootApplication
/*@ComponentScan(basePackages = "com.example.demo.dao")*/
@MapperScan(basePackages = "com.example.demo.dao")
public class UserManagerApplication {
  public static void main(String[] args) {
    SpringApplication.run(UserManagerApplication.class, args);
  }
}
```
###SpringData JPA
1）编写一个实体类（bean）和数据表进行映射，并且配置好映射关系；
```
//使用JPA注解配置映射关系
@Entity //告诉JPA这是一个实体类（和数据表映射的类）
@Table(name = "tbl_user") //@Table来指定和哪个数据表对应;如果省略默认表名就是user；
public class User {
@Id //这是一个主键
@GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
private Integer id;
@Column(name = "last_name",length = 50) //这是和数据表对应的一个列
private String lastName;
@Column //省略默认列名就是属性名
private String email;
```
编写一个Dao接口来操作实体类对应的数据表（Repository）
```
    /**
     * 减库存。
     * 对于Mapper映射接口方法中存在多个参数的要加@Param()注解标识字段名称，不然Mybatis不能识别出来哪个字段相互对应
     *
     * @param seckillId 秒杀商品ID
     * @param killTime  秒杀时间
     * @return 返回此SQL更新的记录数，如果>=1表示更新成功
     */
    int reduceStock(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 插入购买订单明细
     *
     * @param seckillId 秒杀到的商品ID
     * @param money     秒杀的金额
     * @param userPhone 秒杀的用户
     * @return 返回该SQL更新的记录数，如果>=1则更新成功
     */
    int insertOrder(@Param("seckillId") long seckillId, @Param("money") BigDecimal money, @Param("userPhone") long userPhone);
```


###Test测试类
加入`pom.xml`文件<br>
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```
建立测试基类`BlogRestructureApplicationTests.java`，其他测试类继承它，避免写过多注解和代码<br>
```
package com.blog_restructure;

import org.junit.runner.RunWith;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class BlogRestructureApplicationTests {

        @Before
        public void init() {
            System.out.println("开始测试-----------------");
        }

        @After
        public void after() {
            System.out.println("测试结束-----------------");
        }
}
```
添加具体测试类，测试方法上面要加@Test注解
