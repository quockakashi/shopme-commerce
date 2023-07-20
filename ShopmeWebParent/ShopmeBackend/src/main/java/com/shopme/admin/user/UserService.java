package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {

    public static final int NUM_USERS_PER_PAGE = 4;
    private UserRepository repo;
    private RoleRepository roleRepo;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repo, RoleRepository roleRepo, PasswordEncoder encoder) {
        this.repo = repo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = encoder;
    }

    public User findById(Integer id) throws UserNotFoundException {
        try {
            return repo.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }
    }

    public User getByEmail(String email) {
        return repo.findUserByEmail(email);
    }
    public List<User> listAll() {
        return repo.findAll();

    }

    public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        var pageable = PageRequest.of(pageNum - 1, NUM_USERS_PER_PAGE, sort);
        if(keyword == null) {
            return repo.findAll(pageable);
        }
        return repo.findAll(keyword, pageable);
    }

    public List<Role> listRoles() {
        return roleRepo.findAll();
    }

    public User save(User user) {
        var isUpdatingUser = (user.getId() != null);
        if(isUpdatingUser) {
            var existingUser = repo.findById(user.getId()).get();
            if(user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                encoderPassWord(user);
            }
        } else {
            encoderPassWord(user);
        }
        return repo.save(user);
    }

    public void encoderPassWord(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
    
    public boolean isUniqueEmail(Integer id, String email) {
        var user = repo.findUserByEmail(email);
        if(user == null)
            return true; // this email has never used before
        boolean isCreating = (id == null); // if this is a creating action, the variable will be true

        if(isCreating) {
            return false;
        } else {
            return user.getId().equals(id); // the new email was not used by another user
        }
    }

    void deleteUser(Integer id) {
        repo.deleteById(id);
    }

    void updateEnabledStatus(Integer id, boolean enabled) {
        repo.updateEnabledStatus(id, enabled);
    }
}
