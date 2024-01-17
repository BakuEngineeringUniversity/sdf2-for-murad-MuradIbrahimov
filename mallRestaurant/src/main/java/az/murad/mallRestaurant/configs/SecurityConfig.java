    package az.murad.mallRestaurant.configs;

    import az.murad.mallRestaurant.services.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.context.annotation.Lazy;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    @Configuration
    @EnableWebSecurity
    public class SecurityConfig extends WebSecurityConfigurerAdapter {

        private final UserService userService;

        @Autowired
        public SecurityConfig(@Lazy UserService userService) {
            this.userService = userService;
        }

        // Configuring authentication manager to use UserDetailsService and PasswordEncoder
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        }

        // Configuring HTTP security, defining URL patterns and authentication mechanisms
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/api/users/guest").permitAll() // Allow unauthenticated access to create a guest user
                    .antMatchers("/api/login").permitAll() // Allow unauthenticated access to the login endpoint
                    .antMatchers("/api/**").authenticated() // Secure other API endpoints
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .and()
                    .httpBasic();
        }


        // Bean for providing UserDetailsService
        @Override
        @Bean
        public UserDetailsService userDetailsService() {
            return userService;
        }

        // Bean for providing PasswordEncoder
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
