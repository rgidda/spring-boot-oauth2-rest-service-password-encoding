package io.github.pivopil;

import io.github.pivopil.share.entities.impl.Client;
import io.github.pivopil.share.entities.impl.Role;
import io.github.pivopil.share.entities.impl.User;
import io.github.pivopil.share.persistence.ClientRepository;
import io.github.pivopil.share.persistence.RoleRepository;
import io.github.pivopil.share.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created on 17.06.16.
 */

@SpringBootApplication
@ComponentScan("io.github.pivopil")
public class Application extends SpringBootServletInitializer implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String... args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    @Override
    public void run(String... args) throws Exception {
        PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
        UserRepository userRepository = applicationContext.getBean(UserRepository.class);
        if (((List<User>) userRepository.findAll()).size() == 0) {
            RoleRepository roleRepository = applicationContext.getBean(RoleRepository.class);
            ClientRepository clientRepository  = applicationContext.getBean(ClientRepository.class);

            Role roleAdmin = new Role();
            roleAdmin.setName("ROLE_ADMIN");
            roleAdmin = roleRepository.save(roleAdmin);

            Role roleOrgNeoUser = new Role();
            roleOrgNeoUser.setName("ROLE_NEO_LOCAL_USER");
            roleOrgNeoUser = roleRepository.save(roleOrgNeoUser);

            Role roleOrgTrinityUser = new Role();
            roleOrgTrinityUser.setName("ROLE_TRINITY_LOCAL_USER");
            roleOrgTrinityUser = roleRepository.save(roleOrgTrinityUser);


            Role roleOrgNeoAdmin = new Role();
            roleOrgNeoAdmin.setName("ROLE_NEO_LOCAL_ADMIN");
            roleOrgNeoAdmin = roleRepository.save(roleOrgNeoAdmin);

            Role roleOrgTrinityAdmin = new Role();
            roleOrgTrinityAdmin.setName("ROLE_TRINITY_LOCAL_ADMIN");
            roleOrgTrinityAdmin = roleRepository.save(roleOrgTrinityAdmin);


            // admin with roles for all
            User admin = new User();
            admin.setName("adminName");
            admin.setLogin("adminLogin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEnabled(Boolean.TRUE);
            admin.setRoles(new HashSet<>(Arrays.asList(roleOrgNeoUser, roleAdmin, roleOrgNeoAdmin, roleOrgTrinityAdmin, roleOrgTrinityUser)));

            // admin for first client
            User adminNeo = new User();
            adminNeo.setName("neoAdminName");
            adminNeo.setLogin("neoAdminLogin");
            adminNeo.setPassword(passwordEncoder.encode("neoAdmin"));
            adminNeo.setEnabled(Boolean.TRUE);
            adminNeo.setRoles(new HashSet<>(Collections.singletonList(roleOrgNeoAdmin)));

            // admin for second client
            User adminTrinity = new User();
            adminTrinity.setName("trinityAdminName");
            adminTrinity.setLogin("trinityAdminLogin");
            adminTrinity.setPassword(passwordEncoder.encode("trinityAdmin"));
            adminTrinity.setEnabled(Boolean.TRUE);
            adminTrinity.setRoles(new HashSet<>(Collections.singletonList(roleOrgTrinityAdmin)));


            // user for first client
            User userNeo = new User();
            userNeo.setName("neoUserName");
            userNeo.setLogin("neoUserLogin");
            userNeo.setPassword(passwordEncoder.encode("neoUser"));
            userNeo.setEnabled(Boolean.TRUE);
            userNeo.setRoles(new HashSet<>(Collections.singletonList(roleOrgNeoUser)));

            // user for client second
            User userTrinity = new User();
            userTrinity.setName("trinityUserName");
            userTrinity.setLogin("trinityUserLogin");
            userTrinity.setPassword(passwordEncoder.encode("trinityUser"));
            userTrinity.setEnabled(Boolean.TRUE);
            userTrinity.setRoles(new HashSet<>(Collections.singletonList(roleOrgTrinityUser)));


            userRepository.save(Arrays.asList(admin, userNeo, userTrinity, adminNeo, adminTrinity));

            // clients
            Client client1 = new Client("apiOne", passwordEncoder.encode("apiOne"), "read,write", "ROLE_API_ONE");
            Client client2 = new Client("apiTwo", passwordEncoder.encode("apiTwo"), "read,write", "ROLE_API_TWO");
            clientRepository.save(Arrays.asList(client1, client2));

            // acl and token tables creation

//            try {
//                DataSource dataSource = applicationContext.getBean(DataSource.class);
//                Connection connection = dataSource.getConnection();
//                ScriptUtils.executeSqlScript(connection, new EncodedResource(new ClassPathResource("/sql/acl-token-tables.sql"), "utf8"), false, false, "--", "commit", "______", "__________=");
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }

        }
    }

}
