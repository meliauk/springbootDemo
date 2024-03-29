package com.life.hz.controller;

import com.life.hz.dto.CommentDTO;
import com.life.hz.dto.QuestionDTO;
import com.life.hz.enums.CommentTypeEnum;
import com.life.hz.service.CommentService;
import com.life.hz.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name="id")Long id, Model model){
        QuestionDTO questionDTO = questionService.getById(id);
//      查询相关问题  按标签查询
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments =  commentService.ListByTargetId(id, CommentTypeEnum.QUESTION);

//      累加阅读数
        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }


}
