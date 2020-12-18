package com.shop.order.service;

import com.github.pagehelper.PageInfo;
import com.shop.order.pojo.UndoLog;

import java.util.List;

public interface UndoLogService {

    PageInfo<UndoLog> findPage(UndoLog undoLog, int page, int size);

    PageInfo<UndoLog> findPage(int page, int size);

    List<UndoLog> findList(UndoLog undoLog);

    void delete(Long id);

    void update(UndoLog undoLog);

    void add(UndoLog undoLog);

     UndoLog findById(Long id);

    List<UndoLog> findAll();
}
