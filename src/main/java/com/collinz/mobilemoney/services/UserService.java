package com.collinz.mobilemoney.services;

import com.collinz.mobilemoney.exceptions.ObjectNotFoundException;
import com.collinz.mobilemoney.models.User;
import com.collinz.mobilemoney.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User findUserById(Long id) throws ObjectNotFoundException{
        return userRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("User not found!"));
    }

}
