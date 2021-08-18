package com.es.elsaticsearch.controller;

import com.es.elsaticsearch.entity.Content;
import com.es.elsaticsearch.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author : Latent
 * @createdDate : 2020/6/1
 * @updatedDate
 */
@RestController
public class ContentController {

    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String view(){
        return "index";
    }

    @RequestMapping(value = "/index2", method = RequestMethod.GET)
    public ModelAndView getIndex(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value = "/list/{keyWords}/{pageNum}/{pageSize}", method = RequestMethod.GET)
    public List<Map<String,Object>> listContent(@PathVariable("keyWords")String keyWords, @PathVariable("pageNum")Integer pageNum, @PathVariable("pageSize")Integer pageSize) throws IOException {
        System.out.println(keyWords);
        List<Map<String, Object>> maps = contentService.searchPage(keyWords, pageNum, pageSize);
        return maps;
    }

    @RequestMapping(value = "/lists/{keyWords}", method = RequestMethod.GET)
    public List<Content> list(@PathVariable("keyWords")String keyWords) throws IOException {
        System.out.println(keyWords);
        List<Content> list = contentService.list(keyWords);
        return list;
    }
}
