package ru.karelin.project.utility;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PageConfig {
    private int currentPage;

    private int totalPages;

    private List<Integer> pages;

    public PageConfig(int currentPage, int totalPages){
        this.currentPage = currentPage;
        this.totalPages = totalPages;

        buildPagesList();
    }

    public void buildPagesList(){
        List<Integer> pages = new ArrayList<>();

        if(totalPages <= 9){
            for (int i = 1; i <= totalPages; i++) {
                pages.add(i);
            }
            this.pages = pages;
            return;
        }
        if(currentPage == totalPages){
            for (int i = totalPages - 8; i <= totalPages; i++) {
                pages.add(i);
            }
            this.pages = pages;
            return;
        }

        int start = currentPage - 4;
        int end = currentPage + 4;

        if(currentPage - 4 < 1){
            start = 1;
            end = 9;
        }
        if(currentPage + 4 > totalPages){
            end = totalPages;
            start = start - (totalPages - currentPage);
        }

        for (int i = start; i <= end; i++) {
            pages.add(i);
        }

        this.pages = pages;
    }
}
