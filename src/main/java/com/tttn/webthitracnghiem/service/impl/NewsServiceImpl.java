package com.tttn.webthitracnghiem.service.impl;

import com.tttn.webthitracnghiem.model.News;
import com.tttn.webthitracnghiem.model.NewsRequest;
import com.tttn.webthitracnghiem.model.Subject;
import com.tttn.webthitracnghiem.repository.NewsRepository;
import com.tttn.webthitracnghiem.service.UploadStorageService;
import com.tttn.webthitracnghiem.service.INewsService;
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
public class NewsServiceImpl implements INewsService {
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UploadStorageService uploadStorageService;

    @Override
    public Page<News> findAll(Pageable pageable) {
        return newsRepository.findAll(pageable);
    }

    @Override
    public Page<News> findByTitle(String name, Pageable pageable) {
        return newsRepository.findAllByTitleContaining(name, pageable);
    }

    @Override
    public News save(NewsRequest newsRequest) {
        String avatarPath;
        String oldUrl;
        News news;
        if (newsRequest.getId() == null) {
            news = modelMapper.map(newsRequest, News.class);
            oldUrl = "/img/default-news.png";
        } else {
            Optional<News> findNews = newsRepository.findById(newsRequest.getId());
            news = modelMapper.map(newsRequest, News.class);
            oldUrl = findNews.get().getImage();
        }
        if (newsRequest.getImage() != null) {
            uploadStorageService.deleteUploadedFile(oldUrl, "/img/default-news.png", "img");
            File fileSaved = uploadStorageService.upload(newsRequest.getImage(), "img");
            avatarPath = "/img/" + fileSaved.getName();
        } else {
            avatarPath = oldUrl;
        }
        news.setImage(avatarPath);
        news = newsRepository.save(news);
        return news;
    }

    @Override
    public News findById(int id) {
        return newsRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(News news) {
        newsRepository.delete(news);
    }

    @Override
    public List<News> findAll() {
        return newsRepository.findAll();
    }

    @Override
    public List<News> getNewsInWeek() {
        Date date = new Date(System.currentTimeMillis());
        return newsRepository.newsInWeek(date);
    }

    @Override
    public void save(News news) {
        newsRepository.save(news);
    }
}
