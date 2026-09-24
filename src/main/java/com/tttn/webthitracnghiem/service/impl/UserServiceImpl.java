package com.tttn.webthitracnghiem.service.impl;

import com.tttn.webthitracnghiem.model.Subject;
import com.tttn.webthitracnghiem.model.User;
import com.tttn.webthitracnghiem.model.UserRequest;
import com.tttn.webthitracnghiem.repository.UserRepository;
import com.tttn.webthitracnghiem.service.UploadStorageService;
import com.tttn.webthitracnghiem.service.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UploadStorageService uploadStorageService;

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> search(String name, Pageable pageable) {
        return userRepository.search(name,pageable);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id).orElse(null);
    }
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }
    @Override
    public  Integer findByTotalUser(){return userRepository.findByTotalUser();}

    @Override
    public String findByNewUser(){return userRepository.findByNewUser();}

    @Override
    public boolean userExists(String username){
        return userRepository.findById(username).isPresent();
    }

    @Override
    public boolean userExistss(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public String findByPass(String id) {
        return userRepository.findByPass(id);
    }

    @Override
    public List<User> getNewMember() {
        Date date = new Date(System.currentTimeMillis());
        return userRepository.newMember(date);
    }

    @Override
    public int getTotalMember() {
        return userRepository.getMember().size();
    }

    @Override
    public User save(UserRequest userRequest) {
        String avatarPath;
        String oldUrl;
        User user;
        if(userRequest.getId() == null){
            user = modelMapper.map(userRequest,User.class);
            oldUrl = "/img/avatar/default.jpg";
        } else {
            Optional<User> findUser = userRepository.findById(userRequest.getId());
            user = modelMapper.map(userRequest,User.class);
            oldUrl = findUser.get().getImg();
        }
        if(userRequest.getImg() != null){
            uploadStorageService.deleteUploadedFile(oldUrl, "/img/avatar/default.jpg", "img/avatar");
            File fileSaved = uploadStorageService.upload(userRequest.getImg(), "img/avatar");
            avatarPath = "/img/avatar/" + fileSaved.getName();
        } else {
            avatarPath = oldUrl;
        }
        user.setImg(avatarPath);
        user = userRepository.save(user);
        return user;
    }

}

