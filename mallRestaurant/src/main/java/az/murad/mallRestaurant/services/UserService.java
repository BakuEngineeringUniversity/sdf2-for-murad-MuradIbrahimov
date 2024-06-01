package az.murad.mallRestaurant.services;

import az.murad.mallRestaurant.Entity.User;
import az.murad.mallRestaurant.Util.JwtUtil;
import az.murad.mallRestaurant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Load user by username for authentication
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new CustomUserDetails(user);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by username
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    // Get user by ID
    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Create a new user
    public User createUser(User newUser) {

        return userRepository.save(newUser);
    }

    @Autowired
    private JwtUtil jwtUtil;

    public User createGuestUser() {
        User guestUser = new User();
        guestUser.setUsername("guest_" + UUID.randomUUID().toString());
        guestUser.setPassword(passwordEncoder.encode("guest_password")); // You should handle the password more securely
        guestUser.setRole("ROLE_GUEST");
        guestUser.setIsGuest(true);

        // Generate a random email for the guest user
        String randomEmail = generateRandomEmail();
        guestUser.setEmail(randomEmail);



        return userRepository.save(guestUser);
    }

    private String generateRandomEmail() {
        // Logic to generate a random email (replace it with your own logic)
        return "guest_" + UUID.randomUUID().toString() + "@example.com";
    }

    private String generateTokenForGuestUser(User guestUser) {
        // Generate a token for the guest user similar to the regular login
        return jwtUtil.generateToken(guestUser.getUsername(), guestUser.getRole());
    }
    public User updateUser(User updatedUser) {
        // Perform validation or additional logic if needed
        return userRepository.save(updatedUser);
    }

    // Delete a user by ID
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}