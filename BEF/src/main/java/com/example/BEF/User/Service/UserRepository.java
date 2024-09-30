package com.example.BEF.User.Service;

import com.example.BEF.User.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUserNumber(Long userNumber);
}
