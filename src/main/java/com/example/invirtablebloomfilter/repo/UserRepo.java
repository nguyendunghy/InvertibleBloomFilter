package com.example.invirtablebloomfilter.repo;

import com.example.invirtablebloomfilter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {


}
