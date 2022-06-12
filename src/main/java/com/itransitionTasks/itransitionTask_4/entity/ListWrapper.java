package com.itransitionTasks.itransitionTask_4.entity;

import java.util.List;

public class ListWrapper {

    private List<Long> list;

    public ListWrapper(List<Long> list) {
        this.list = list;
    }

    public ListWrapper() {
    }

    public List<Long> getList() {
        return list;
    }

    public void setList(List<Long> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ListWrapper{" +
                "list=" + list +
                '}';
    }
}
