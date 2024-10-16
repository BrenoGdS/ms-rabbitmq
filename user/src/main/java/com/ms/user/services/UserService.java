package com.ms.user.services;

import com.ms.user.exceptions.UserNotFoundException;
import com.ms.user.models.UserModel;
import com.ms.user.dtos.UserRecordDto;
import com.ms.user.producers.UserProducer;
import com.ms.user.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    UserRepository userRepository;
    UserProducer userProducer;

    public UserService(UserRepository userRepository, UserProducer userProducer) {
        this.userRepository = userRepository;
        this.userProducer = userProducer;
    }

    public List<UserRecordDto> getAllUsers(){
        return userRepository.findAll().stream()
                .map(u -> new UserRecordDto(u.getName(), u.getEmail(), u.getCreatedDate(), u.getLastModifiedDate()))
                .collect(Collectors.toList());
    }

    public Optional<UserModel> getUser(String email){
        Optional<UserModel> userO = userRepository.findByEmail(email);
        if(userO.isEmpty()){
            throw new UserNotFoundException();
        }
        return userO;
    }

    @Transactional
    public UserModel saveUser(UserRecordDto userRecordDto){
        var userModel = userRepository.save(getUserModel(userRecordDto));
        userProducer.publishMessageEmail(userModel);
        return userModel;
    }

    private static UserModel getUserModel(UserRecordDto userRecordDto) {
        var userModel = new UserModel();
        BeanUtils.copyProperties(userRecordDto, userModel);
        return userModel;
    }
}
