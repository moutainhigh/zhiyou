package com.zy.service;

import com.zy.entity.usr.Tag;

/**
 * Created by freeman on 16/8/11.
 */
public interface TagService {
    java.util.List<Tag> findAll();

    Tag findById(Long id);


    Tag create(Tag tag);

    void delete(Long id);

}
