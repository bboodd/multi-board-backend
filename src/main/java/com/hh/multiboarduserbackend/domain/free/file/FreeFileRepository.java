package com.hh.multiboarduserbackend.domain.free.file;

import com.hh.multiboarduserbackend.common.vo.FileVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface FreeFileRepository {

    void saveAll(List<FileVo> fileList);

    FileVo findById(Long freeFileId);

    List<FileVo> findAllByPostId(Long freePostId);

    List<FileVo> findAllByIds(List<Long> idList);

    void deleteAllByIds(List<Long> idList);
}
