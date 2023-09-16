package com.bkav.aiotcloud.entity.widget;

import com.bkav.aiotcloud.entity.notify.NotifyItem;

public class WidgetItem {
    private static long nextId = 0L;
    private String indetify;
    private int totalEvent;
    private String imagePath, firstImg, secondImg, firstContent, secondContent;
    private String content;
    private String startAt;
    private boolean dragAble;
    private long id;
    private int viewType;
    private boolean spanned;

    private String objectGuide = "";


    public WidgetItem(String indetify, int totalEvent, String imagePath, String content, String startAt, boolean dragAble, int viewType, boolean spanned, String firstImg, String secondImg, String firstContent, String secondContent) {
        this.id = nextId++;
        this.indetify = indetify;
        this.totalEvent = totalEvent;
        this.imagePath = imagePath;
        this.content = content;
        this.startAt = startAt;
        this.dragAble = dragAble;
        this.viewType = viewType;
        this.spanned = spanned;
        this.firstImg = firstImg;
        this.secondImg = secondImg;
        this.firstContent = firstContent;
        this.secondContent = secondContent;
    }

    public String getObjectGuide() {
        return objectGuide;
    }

    public void setObjectGuide(String objectGuide) {
        this.objectGuide = objectGuide;
    }
    public String getFirstContent() {
        return firstContent;
    }

    public void setFirstContent(String firstContent) {
        this.firstContent = firstContent;
    }

    public String getSecondContent() {
        return secondContent;
    }

    public void setSecondContent(String secondContent) {
        this.secondContent = secondContent;
    }

    public boolean isSpanned() {
        return spanned;
    }

    public void setSpanned(boolean spanned) {
        this.spanned = spanned;
    }

    public String getFirstImg() {
        return firstImg;
    }

    public void setFirstImg(String firstImg) {
        this.firstImg = firstImg;
    }

    public String getSecondImg() {
        return secondImg;
    }

    public void setSecondImg(String secondImg) {
        this.secondImg = secondImg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStartAt() {
        return startAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getIndetify() {
        return indetify;
    }

    public void setIndetify(String indetify) {
        this.indetify = indetify;
    }

    public int getTotalEvent() {
        return totalEvent;
    }

    public void setTotalEvent(int totalEvent) {
        this.totalEvent = totalEvent;
    }

    public boolean isDragAble() {
        return dragAble;
    }

    public void setDragAble(boolean dragAble) {
        this.dragAble = dragAble;
    }
    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

}
