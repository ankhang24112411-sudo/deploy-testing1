package com.tttn.webthitracnghiem.api;

import com.tttn.webthitracnghiem.model.Exam;
import com.tttn.webthitracnghiem.model.ExamRequest;
import com.tttn.webthitracnghiem.model.Lesson;
import com.tttn.webthitracnghiem.model.Question;
import com.tttn.webthitracnghiem.model.User;
import com.tttn.webthitracnghiem.repository.LessonRepository;
import com.tttn.webthitracnghiem.repository.QuestionRepository;
import com.tttn.webthitracnghiem.repository.UserRepository;
import com.tttn.webthitracnghiem.service.IExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/exam")
public class ExamApiController {
    @Autowired
    private IExamService examService;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping
    public ResponseEntity<List<Exam>> get(){
        return ResponseEntity.ok(examService.getAll());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(@RequestParam String idName,
                                  @RequestParam boolean free,
                                  @RequestParam(required = false, defaultValue = "0") int time,
                                  @RequestParam Date createDate,
                                  @RequestParam String nameExam,
                                  @RequestParam("lesson") Integer lessonId,
                                  @RequestParam("users") String usersId,
                                  @RequestParam(value = "questions[]", required = false) List<Integer> questionIds) {
        Lesson lesson = lessonRepository.findById(lessonId).orElse(null);
        User user = userRepository.findById(usersId).orElse(null);
        if (lesson == null || user == null || questionIds == null || questionIds.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing lesson, user, or questions");
        }

        List<Question> questions = new ArrayList<>();
        for (Integer questionId : questionIds) {
            questionRepository.findById(questionId).ifPresent(questions::add);
        }
        if (questions.isEmpty()) {
            return ResponseEntity.badRequest().body("No valid questions selected");
        }

        Exam exam = new Exam();
        exam.setIdName(idName);
        exam.setFree(free);
        exam.setTime(free ? 0 : time);
        exam.setCreateDate(createDate);
        exam.setNameExam(nameExam);
        exam.setLesson(lesson);
        exam.setUsers(user);
        exam.setQuestions(questions);
        examService.save(exam);
        return ResponseEntity.ok(exam);
    }
    @PostMapping(value = "/excel",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody ExamRequest examRequest){
        System.out.println(examRequest);
        return ResponseEntity.ok(examService.save(examRequest));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        Exam exam = examService.findById(id);
        examService.delete(exam);
        return ResponseEntity.ok(exam);
    }
}
