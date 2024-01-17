package az.murad.mallRestaurant.services;

import az.murad.mallRestaurant.Entity.User;
import az.murad.mallRestaurant.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User createUser(User newUser) {
        // Encode the password before saving to the database
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userRepository.save(newUser);
    }

    public User createGuestUser() {
        User guestUser = new User();
        guestUser.setUsername("guest_" + UUID.randomUUID().toString());
        guestUser.setPassword(passwordEncoder.encode("guest_password")); // You should handle password more securely
        guestUser.setRole("ROLE_GUEST");
        guestUser.setIsGuest(true);

        // Save the user to the database
        return userRepository.save(guestUser);
    }

    // Additional methods...

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
