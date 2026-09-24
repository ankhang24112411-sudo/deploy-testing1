package com.tttn.webthitracnghiem.service.impl;

import com.tttn.webthitracnghiem.model.Document;
import com.tttn.webthitracnghiem.model.DocumentRequest;
import com.tttn.webthitracnghiem.model.News;
import com.tttn.webthitracnghiem.repository.DocumentRepository;
import com.tttn.webthitracnghiem.service.IDocumentService;
import com.tttn.webthitracnghiem.service.UploadStorageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements IDocumentService {

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private UploadStorageService uploadStorageService;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public Page<Document> findAll(Pageable pageable) {
        return documentRepository.findAll(pageable);
    }

    @Override
    public Document findById(int id) {
        return documentRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Document> findByTitle(String title, Pageable pageable) {
        return documentRepository.findByTitleContaining(title,pageable);
    }

    @Override
    public Document save(DocumentRequest documentRequest) {
        String avatarPath;
        String oldUrl;
        Document document;
        if (documentRequest.getId() == null) {
            document = modelMapper.map(documentRequest, Document.class);
            oldUrl = "/img/default-document.png";
        } else {
            Optional<Document> findDocument = documentRepository.findById(documentRequest.getId());
            document = modelMapper.map(documentRequest, Document.class);
            oldUrl = findDocument.get().getAvatar();
        }
        if (documentRequest.getAvatar() != null) {
            uploadStorageService.deleteUploadedFile(oldUrl, "/img/default-document.png", "img");
            File fileSaved = uploadStorageService.upload(documentRequest.getAvatar(), "img");
            avatarPath = "/img/" + fileSaved.getName();
        } else {
            avatarPath = oldUrl;
        }
        document.setAvatar(avatarPath);
        document = documentRepository.save(document);
        return document;
    }

    @Override
    public void remove(Document document) {
        documentRepository.delete(document);
    }

    @Override
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    @Override
    public void save(Document document) {
        documentRepository.save(document);
    }
}
