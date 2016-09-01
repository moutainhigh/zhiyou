package com.zy.service.impl;

import com.zy.entity.usr.Tag;
import com.zy.mapper.TagMapper;
import com.zy.service.TagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import java.util.List;

import static com.zy.common.util.ValidateUtils.validate;

/**
 * Created by freeman on 16/8/11.
 */
@Service
@Validated
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<Tag> findAll() {
        return tagMapper.findAll();
    }

    @Override
    public Tag findById(@NotNull Long id) {
        return this.tagMapper.findOne(id);
    }

    @Override
    public Tag create(@NotNull Tag tag) {
        validate(tag);
        Tag one = this.tagMapper.findOne(tag.getId());
        if (one != null) return one;
        this.tagMapper.insert(tag);
        return tag;
    }

    @Override
    public void delete(@NotNull Long id) {
        this.tagMapper.delete(id);
    }
}
