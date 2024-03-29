package com.life.hz.dto;

import com.life.hz.model.Question;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {
  //  private List<Question> questions;
    private List<T> data;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page; //当前页面
    private  Integer totalPage ;
    private  Integer totalCount ;

    private List<Integer> pages = new ArrayList<>();

    public void setPagination(Integer totalPage, Integer page,Integer totalCount) {
        this.totalCount = totalCount;
        this.totalPage = totalPage;
        this.page = page;

        pages.add(page);
        for (int i = 1 ; i <= 2 ; i++ ){
            if(page - i > 0){
                pages.add(0,page - i );
            }

            if(page + i <= totalPage){
                pages.add(page + i );
            }
        }


//      是否展示上一页
        if( page == 1 ){
            showPrevious = false;
        }else {
            showPrevious = true;
        }
//      是否展示下一页
        if( page == totalPage ){
            showNext = false;
        }else {
            showNext = true;
        }

//        是否展示第一页
        if(pages.contains(1)){
            showFirstPage = false;
        }else {
            showFirstPage = true;
        }
//        是否展示最后一页
        if(pages.contains(totalPage) ){
            showEndPage = false;
        }else {
            showEndPage = true;
        }


    }
}
