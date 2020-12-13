package com.shop.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.user.dao.UndoLogMapper;
import com.shop.user.pojo.UndoLog;
import com.shop.user.service.UndoLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UndoLogServiceImpl implements UndoLogService {

    @Autowired
    private UndoLogMapper undoLogMapper;


    @Override
    public PageInfo<UndoLog> findPage(UndoLog undoLog, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(undoLog);
        return new PageInfo<UndoLog>(undoLogMapper.selectByExample(example));
    }

    @Override
    public PageInfo<UndoLog> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return new PageInfo<UndoLog>(undoLogMapper.selectAll());
    }

    @Override
    public List<UndoLog> findList(UndoLog undoLog){
        Example example = createExample(undoLog);
        return undoLogMapper.selectByExample(example);
    }

    public Example createExample(UndoLog undoLog){
        Example example=new Example(UndoLog.class);
        Example.Criteria criteria = example.createCriteria();
        if(undoLog!=null){
            if(!StringUtils.isEmpty(undoLog.getId())){
                    criteria.andEqualTo("id",undoLog.getId());
            }
            if(!StringUtils.isEmpty(undoLog.getBranchId())){
                    criteria.andEqualTo("branchId",undoLog.getBranchId());
            }
            if(!StringUtils.isEmpty(undoLog.getXid())){
                    criteria.andEqualTo("xid",undoLog.getXid());
            }
            if(!StringUtils.isEmpty(undoLog.getRollbackInfo())){
                    criteria.andEqualTo("rollbackInfo",undoLog.getRollbackInfo());
            }
            if(!StringUtils.isEmpty(undoLog.getLogStatus())){
                    criteria.andEqualTo("logStatus",undoLog.getLogStatus());
            }
            if(!StringUtils.isEmpty(undoLog.getLogCreated())){
                    criteria.andEqualTo("logCreated",undoLog.getLogCreated());
            }
            if(!StringUtils.isEmpty(undoLog.getLogModified())){
                    criteria.andEqualTo("logModified",undoLog.getLogModified());
            }
            if(!StringUtils.isEmpty(undoLog.getExt())){
                    criteria.andEqualTo("ext",undoLog.getExt());
            }
        }
        return example;
    }

    @Override
    public void delete(Long id){
        undoLogMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(UndoLog undoLog){
        undoLogMapper.updateByPrimaryKeySelective(undoLog);
    }

    @Override
    public void add(UndoLog undoLog){
        undoLogMapper.insertSelective(undoLog);
    }

    @Override
    public UndoLog findById(Long id){
        return  undoLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<UndoLog> findAll() {
        return undoLogMapper.selectAll();
    }
}
